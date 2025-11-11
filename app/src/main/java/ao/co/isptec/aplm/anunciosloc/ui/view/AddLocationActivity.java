package ao.co.isptec.aplm.anunciosloc.ui.view;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import ao.co.isptec.aplm.anunciosloc.R;
import ao.co.isptec.aplm.anunciosloc.data.model.Location;
import ao.co.isptec.aplm.anunciosloc.data.repository.UserRepository;
import ao.co.isptec.aplm.anunciosloc.ui.viewmodel.LocationViewModel;
import ao.co.isptec.aplm.anunciosloc.utils.ValidationUtils;

/**
 * Activity para adicionar ou editar uma localização
 */
public class AddLocationActivity extends AppCompatActivity {
    
    private static final String TAG = "AddLocationActivity";
    public static final String EXTRA_LOCATION_ID = "location_id";
    public static final String EXTRA_EDIT_MODE = "edit_mode";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private static final int WIFI_PERMISSION_REQUEST_CODE = 1002;
    private static final int FIXED_RADIUS = 20; // Raio fixo de 20 metros
    
    private MaterialToolbar toolbar;
    private TextInputEditText editName, editLatitude, editLongitude, editSsid;
    private RadioGroup radioGroupMethod;
    private RadioButton radioGPS, radioWiFi;
    private LinearLayout containerGPS, containerWiFi;
    private MaterialButton btnSave, btnDelete, btnGetLocation, btnDetectWiFi;
    private ProgressBar progressBar;
    
    private LocationViewModel viewModel;
    private UserRepository userRepository;
    private FusedLocationProviderClient fusedLocationClient;
    
    private boolean isEditMode = false;
    private String locationId;
    private boolean isGPSMode = true;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);
        
        // Verifica se está em modo de edição
        isEditMode = getIntent().getBooleanExtra(EXTRA_EDIT_MODE, false);
        locationId = getIntent().getStringExtra(EXTRA_LOCATION_ID);
        
        initializeViews();
        initializeViewModel();
        initializeLocationClient();
        setupToolbar();
        setupListeners();
        observeViewModel();
        
        if (isEditMode && locationId != null) {
            loadLocationData();
        }
    }
    
    private void initializeViews() {
        toolbar = findViewById(R.id.toolbar);
        editName = findViewById(R.id.editName);
        editLatitude = findViewById(R.id.editLatitude);
        editLongitude = findViewById(R.id.editLongitude);
        editSsid = findViewById(R.id.editSsid);
        radioGroupMethod = findViewById(R.id.radioGroupMethod);
        radioGPS = findViewById(R.id.radioGPS);
        radioWiFi = findViewById(R.id.radioWiFi);
        containerGPS = findViewById(R.id.containerGPS);
        containerWiFi = findViewById(R.id.containerWiFi);
        btnSave = findViewById(R.id.btnSave);
        btnDelete = findViewById(R.id.btnDelete);
        btnGetLocation = findViewById(R.id.btnGetLocation);
        btnDetectWiFi = findViewById(R.id.btnDetectWiFi);
        progressBar = findViewById(R.id.progressBar);
        
        android.util.Log.d("AddLocationActivity", "Views initialized - radioGroupMethod: " + radioGroupMethod);
        android.util.Log.d("AddLocationActivity", "radioGPS: " + radioGPS + ", radioWiFi: " + radioWiFi);
        android.util.Log.d("AddLocationActivity", "containerGPS: " + containerGPS + ", containerWiFi: " + containerWiFi);
        
        // Mostra botão delete apenas em modo edição
        btnDelete.setVisibility(isEditMode ? View.VISIBLE : View.GONE);
    }
    
    private void initializeViewModel() {
        viewModel = new ViewModelProvider(this).get(LocationViewModel.class);
        userRepository = UserRepository.getInstance();
    }
    
    private void initializeLocationClient() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }
    
    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            
            // Define o título baseado no modo
            String title = isEditMode ? getString(R.string.edit_location) : getString(R.string.add_location);
            getSupportActionBar().setTitle(title);
            
            android.util.Log.d(TAG, "setupToolbar - isEditMode: " + isEditMode + ", Title: " + title);
        }
        
        toolbar.setNavigationOnClickListener(v -> finish());
    }
    
    private void setupListeners() {
        // Listener para mudança de método
        radioGroupMethod.setOnCheckedChangeListener((group, checkedId) -> {
            android.util.Log.d("AddLocationActivity", "RadioGroup changed - checkedId: " + checkedId);
            android.util.Log.d("AddLocationActivity", "R.id.radioGPS: " + R.id.radioGPS + ", R.id.radioWiFi: " + R.id.radioWiFi);
            
            if (checkedId == R.id.radioGPS) {
                android.util.Log.d("AddLocationActivity", "GPS mode selected");
                isGPSMode = true;
                containerGPS.setVisibility(View.VISIBLE);
                containerWiFi.setVisibility(View.GONE);
            } else if (checkedId == R.id.radioWiFi) {
                android.util.Log.d("AddLocationActivity", "WiFi mode selected");
                isGPSMode = false;
                containerGPS.setVisibility(View.GONE);
                containerWiFi.setVisibility(View.VISIBLE);
            }
        });
        
        // Botão para obter localização GPS
        btnGetLocation.setOnClickListener(v -> getCurrentLocation());
        
        // Botão para detectar WiFi atual
        btnDetectWiFi.setOnClickListener(v -> detectCurrentWiFi());
        
        // Botão salvar
        btnSave.setOnClickListener(v -> saveLocation());
        
        // Botão deletar
        btnDelete.setOnClickListener(v -> {
            new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle(R.string.delete_location)
                .setMessage("Tem certeza que deseja eliminar esta localização?")
                .setPositiveButton(R.string.yes, (dialog, which) -> deleteLocation())
                .setNegativeButton(R.string.no, null)
                .show();
        });
    }
    
    private void observeViewModel() {
        // Observa lista de localizações para carregar dados
        viewModel.getLocations().observe(this, locations -> {
            if (isEditMode && locationId != null && locations != null) {
                for (Location location : locations) {
                    if (location.getId().equals(locationId)) {
                        fillFormWithLocation(location);
                        break;
                    }
                }
            }
        });
        
        // Observa estado de carregamento
        viewModel.getIsLoading().observe(this, isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            btnSave.setEnabled(!isLoading);
            btnDelete.setEnabled(!isLoading);
        });
        
        // Observa mensagens de erro
        viewModel.getErrorMessage().observe(this, error -> {
            if (error != null && !error.isEmpty()) {
                Toast.makeText(this, error, Toast.LENGTH_LONG).show();
            }
        });
        
        // Observa sucesso de criação
        viewModel.getLocationCreated().observe(this, success -> {
            if (success != null && success) {
                if (isGPSMode) {
                    Toast.makeText(this, "Localização criada com sucesso! Visualize no mapa na aba Localizações.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, R.string.success_location_created, Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        });
        
        // Observa sucesso de atualização
        viewModel.getLocationUpdated().observe(this, success -> {
            if (success != null && success) {
                Toast.makeText(this, R.string.success_location_updated, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        
        // Observa sucesso de eliminação
        viewModel.getLocationDeleted().observe(this, success -> {
            if (success != null && success) {
                Toast.makeText(this, R.string.success_location_deleted, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
    
    private void loadLocationData() {
        viewModel.loadLocations();
    }
    
    private void fillFormWithLocation(Location location) {
        editName.setText(location.getName());
        
        // Se tem SSID, é WiFi mode
        if (location.getSsid() != null && !location.getSsid().isEmpty()) {
            radioWiFi.setChecked(true);
            isGPSMode = false;
            containerGPS.setVisibility(View.GONE);
            containerWiFi.setVisibility(View.VISIBLE);
            editSsid.setText(location.getSsid());
        } else {
            // É GPS mode
            radioGPS.setChecked(true);
            isGPSMode = true;
            containerGPS.setVisibility(View.VISIBLE);
            containerWiFi.setVisibility(View.GONE);
            editLatitude.setText(String.valueOf(location.getLatitude()));
            editLongitude.setText(String.valueOf(location.getLongitude()));
        }
    }
    
    /**
     * Obtém a localização GPS atual do dispositivo
     */
    private void getCurrentLocation() {
        // Verifica permissão
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) 
                != PackageManager.PERMISSION_GRANTED) {
            // Solicita permissão
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }
        
        // Verifica se GPS está ativado
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "Por favor, ative o GPS", Toast.LENGTH_LONG).show();
            return;
        }
        
        // Mostra loading
        progressBar.setVisibility(View.VISIBLE);
        btnGetLocation.setEnabled(false);
        
        // Obtém última localização conhecida
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    progressBar.setVisibility(View.GONE);
                    btnGetLocation.setEnabled(true);
                    
                    if (location != null) {
                        // Preenche os campos usando formato US (ponto como separador decimal)
                        editLatitude.setText(String.format(java.util.Locale.US, "%.6f", location.getLatitude()));
                        editLongitude.setText(String.format(java.util.Locale.US, "%.6f", location.getLongitude()));
                        Toast.makeText(this, "Localização obtida com sucesso!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Não foi possível obter a localização. Tente novamente.", 
                                Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(this, e -> {
                    progressBar.setVisibility(View.GONE);
                    btnGetLocation.setEnabled(true);
                    Toast.makeText(this, "Erro ao obter localização: " + e.getMessage(), 
                            Toast.LENGTH_LONG).show();
                });
    }
    
    /**
     * Detecta o SSID da rede WiFi atual
     */
    private void detectCurrentWiFi() {
        // Verifica permissão (Android 8.0+)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) 
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        WIFI_PERMISSION_REQUEST_CODE);
                return;
            }
        }
        
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiManager != null && wifiManager.isWifiEnabled()) {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            if (wifiInfo != null) {
                String ssid = wifiInfo.getSSID();
                if (ssid != null && !ssid.equals("<unknown ssid>")) {
                    // Remove aspas do SSID
                    ssid = ssid.replace("\"", "");
                    editSsid.setText(ssid);
                    Toast.makeText(this, "WiFi detectado: " + ssid, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Não conectado a nenhuma rede WiFi", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, "Não conectado a nenhuma rede WiFi", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "WiFi está desativado. Por favor, ative o WiFi.", Toast.LENGTH_LONG).show();
        }
    }
    
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, 
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permissão concedida, tenta novamente
                getCurrentLocation();
            } else {
                Toast.makeText(this, "Permissão de localização necessária", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == WIFI_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permissão concedida, tenta novamente
                detectCurrentWiFi();
            } else {
                Toast.makeText(this, "Permissão necessária para detectar WiFi", Toast.LENGTH_LONG).show();
            }
        }
    }
    
    private void saveLocation() {
        // Validações
        String name = editName.getText().toString().trim();
        
        Log.d(TAG, "saveLocation() called - isGPSMode: " + isGPSMode);
        
        if (name.isEmpty()) {
            editName.setError(getString(R.string.error_empty_field));
            editName.requestFocus();
            return;
        }
        
        double latitude = 0;
        double longitude = 0;
        String ssid = null;
        
        if (isGPSMode) {
            // Modo GPS - valida coordenadas
            String latStr = editLatitude.getText().toString().trim();
            String lonStr = editLongitude.getText().toString().trim();
            
            Log.d(TAG, "GPS Mode - Raw Latitude: '" + latStr + "', Raw Longitude: '" + lonStr + "'");
            
            if (latStr.isEmpty() || lonStr.isEmpty()) {
                Toast.makeText(this, "Por favor, obtenha a localização GPS", Toast.LENGTH_SHORT).show();
                return;
            }
            
            try {
                // Remove espaços em branco e caracteres invisíveis
                latStr = latStr.replaceAll("\\s+", "");
                lonStr = lonStr.replaceAll("\\s+", "");
                
                // Substitui vírgula por ponto (para localizações que usam vírgula como separador decimal)
                latStr = latStr.replace(",", ".");
                lonStr = lonStr.replace(",", ".");
                
                latitude = Double.parseDouble(latStr);
                longitude = Double.parseDouble(lonStr);
                
                Log.d(TAG, "Parsed coordinates - Lat: " + latitude + ", Lon: " + longitude);
            } catch (NumberFormatException e) {
                Log.e(TAG, "Error parsing coordinates: " + e.getMessage());
                Toast.makeText(this, "Valores de coordenadas inválidos: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                return;
            }
            
            // Valida coordenadas
            if (!ValidationUtils.isValidLatitude(latitude)) {
                Toast.makeText(this, "Latitude inválida (deve estar entre -90 e 90). Valor: " + latitude, Toast.LENGTH_LONG).show();
                return;
            }
            
            if (!ValidationUtils.isValidLongitude(longitude)) {
                Toast.makeText(this, "Longitude inválida (deve estar entre -180 e 180). Valor: " + longitude, Toast.LENGTH_LONG).show();
                return;
            }
        } else {
            // Modo WiFi - valida SSID
            ssid = editSsid.getText().toString().trim();
            
            if (ssid.isEmpty()) {
                editSsid.setError("Por favor, insira o nome da rede WiFi");
                editSsid.requestFocus();
                return;
            }
            
            // Para WiFi, coordenadas são 0,0
            latitude = 0;
            longitude = 0;
        }
        
        // Cria ou atualiza localização com raio fixo de 20 metros
        if (isEditMode && locationId != null) {
            Location location = new Location(locationId, name, latitude, longitude, FIXED_RADIUS);
            location.setSsid(ssid);
            location.setCreatedBy(userRepository.getCurrentUser() != null ? 
                    userRepository.getCurrentUser().getId() : "unknown");
            location.setActive(true);
            
            viewModel.updateLocation(location);
        } else {
            Location location = new Location();
            location.setName(name);
            location.setLatitude(latitude);
            location.setLongitude(longitude);
            location.setRadiusInMeters(FIXED_RADIUS); // Raio fixo de 20 metros
            location.setSsid(ssid);
            location.setCreatedBy(userRepository.getCurrentUser() != null ? 
                    userRepository.getCurrentUser().getId() : "unknown");
            location.setActive(true);
            
            viewModel.createLocation(location);
        }
    }
    
    private void deleteLocation() {
        if (locationId != null) {
            viewModel.deleteLocation(locationId);
        }
    }
}
