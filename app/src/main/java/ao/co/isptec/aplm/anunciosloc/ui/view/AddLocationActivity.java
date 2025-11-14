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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import ao.co.isptec.aplm.anunciosloc.R;
import ao.co.isptec.aplm.anunciosloc.data.model.Location;
import ao.co.isptec.aplm.anunciosloc.data.model.User;
import ao.co.isptec.aplm.anunciosloc.data.repository.UserRepository;
import ao.co.isptec.aplm.anunciosloc.ui.viewmodel.LocationViewModel;
import ao.co.isptec.aplm.anunciosloc.utils.ValidationUtils;

public class AddLocationActivity extends AppCompatActivity {
    
    public static final String EXTRA_EDIT_MODE = "EDIT_MODE";
    public static final String EXTRA_LOCATION_ID = "LOCATION_ID";
    
    private static final String TAG = "AddLocationActivity";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private static final int WIFI_PERMISSION_REQUEST_CODE = 1002;
    private static final double FIXED_RADIUS = 20.0;
    
    private Toolbar toolbar;
    private EditText editName, editLatitude, editLongitude, editSsid;
    private RadioGroup radioGroupMethod;
    private RadioButton radioGPS, radioWiFi;
    private View containerGPS, containerWiFi;
    private Button btnSave, btnDelete, btnGetLocation, btnDetectWiFi;
    private ProgressBar progressBar;
    
    private LocationViewModel viewModel;
    private FusedLocationProviderClient fusedLocationClient;
    private UserRepository userRepository;
    
    private boolean isEditMode = false;
    private String locationId;
    private boolean isGPSMode = true;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);
        
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
            String title = isEditMode ? getString(R.string.edit_location) : getString(R.string.add_location);
            getSupportActionBar().setTitle(title);
        }
        toolbar.setNavigationOnClickListener(v -> finish());
    }
    
    private void setupListeners() {
        radioGroupMethod.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioGPS) {
                isGPSMode = true;
                containerGPS.setVisibility(View.VISIBLE);
                containerWiFi.setVisibility(View.GONE);
            } else if (checkedId == R.id.radioWiFi) {
                isGPSMode = false;
                containerGPS.setVisibility(View.GONE);
                containerWiFi.setVisibility(View.VISIBLE);
            }
        });
        
        btnGetLocation.setOnClickListener(v -> getCurrentLocation());
        btnDetectWiFi.setOnClickListener(v -> detectCurrentWiFi());
        btnSave.setOnClickListener(v -> saveLocation());
        btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                .setTitle(R.string.delete_location)
                .setMessage("Tem certeza que deseja eliminar esta localização?")
                .setPositiveButton(R.string.yes, (dialog, which) -> deleteLocation())
                .setNegativeButton(R.string.no, null)
                .show();
        });
    }
    
    private void observeViewModel() {
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
        
        viewModel.getIsLoading().observe(this, isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            btnSave.setEnabled(!isLoading);
            btnDelete.setEnabled(!isLoading);
        });
        
        viewModel.getErrorMessage().observe(this, error -> {
            if (error != null && !error.isEmpty()) {
                Toast.makeText(this, error, Toast.LENGTH_LONG).show();
            }
        });
        
        viewModel.getLocationCreated().observe(this, success -> {
            if (success != null && success) {
                Toast.makeText(this, R.string.success_location_created, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        
        viewModel.getLocationUpdated().observe(this, success -> {
            if (success != null && success) {
                Toast.makeText(this, R.string.success_location_updated, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        
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
        if (location.getSsid() != null && !location.getSsid().isEmpty()) {
            radioWiFi.setChecked(true);
            isGPSMode = false;
            containerGPS.setVisibility(View.GONE);
            containerWiFi.setVisibility(View.VISIBLE);
            editSsid.setText(location.getSsid());
        } else {
            radioGPS.setChecked(true);
            isGPSMode = true;
            containerGPS.setVisibility(View.VISIBLE);
            containerWiFi.setVisibility(View.GONE);
            editLatitude.setText(String.valueOf(location.getLatitude()));
            editLongitude.setText(String.valueOf(location.getLongitude()));
        }
    }
    
    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "Por favor, ative o GPS", Toast.LENGTH_LONG).show();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        btnGetLocation.setEnabled(false);
        fusedLocationClient.getLastLocation()
            .addOnSuccessListener(this, location -> {
                progressBar.setVisibility(View.GONE);
                btnGetLocation.setEnabled(true);
                if (location != null) {
                    editLatitude.setText(String.format(java.util.Locale.US, "%.6f", location.getLatitude()));
                    editLongitude.setText(String.format(java.util.Locale.US, "%.6f", location.getLongitude()));
                } else {
                    Toast.makeText(this, "Não foi possível obter a localização.", Toast.LENGTH_LONG).show();
                }
            })
            .addOnFailureListener(this, e -> {
                progressBar.setVisibility(View.GONE);
                btnGetLocation.setEnabled(true);
                Toast.makeText(this, "Erro ao obter localização: " + e.getMessage(), Toast.LENGTH_LONG).show();
            });
    }
    
    private void detectCurrentWiFi() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, WIFI_PERMISSION_REQUEST_CODE);
            return;
        }
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiManager != null && wifiManager.isWifiEnabled()) {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            if (wifiInfo != null) {
                String ssid = wifiInfo.getSSID();
                if (ssid != null && !ssid.equals("<unknown ssid>")) {
                    ssid = ssid.replace("\"", "");
                    editSsid.setText(ssid);
                } else {
                    Toast.makeText(this, "Não conectado a nenhuma rede WiFi", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, "Não conectado a nenhuma rede WiFi", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "WiFi está desativado.", Toast.LENGTH_LONG).show();
        }
    }
    
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(this, "Permissão de localização necessária", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == WIFI_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                detectCurrentWiFi();
            } else {
                Toast.makeText(this, "Permissão necessária para detectar WiFi", Toast.LENGTH_LONG).show();
            }
        }
    }
    
    private void saveLocation() {
        String name = editName.getText().toString().trim();
        if (name.isEmpty()) {
            editName.setError(getString(R.string.error_empty_field));
            editName.requestFocus();
            return;
        }
        
        double latitude = 0;
        double longitude = 0;
        String ssid = null;
        
        if (isGPSMode) {
            String latStr = editLatitude.getText().toString().trim().replace(",", ".");
            String lonStr = editLongitude.getText().toString().trim().replace(",", ".");
            if (latStr.isEmpty() || lonStr.isEmpty()) {
                Toast.makeText(this, "Por favor, obtenha a localização GPS", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                latitude = Double.parseDouble(latStr);
                longitude = Double.parseDouble(lonStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Valores de coordenadas inválidos", Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            ssid = editSsid.getText().toString().trim();
            if (ssid.isEmpty()) {
                editSsid.setError("Por favor, insira o nome da rede WiFi");
                editSsid.requestFocus();
                return;
            }
        }

        User currentUser = userRepository.getCurrentUser();
        String creatorId = (currentUser != null && currentUser.getId() != null) ? String.valueOf(currentUser.getId()) : "unknown";

        Location location = new Location();
        location.setName(name);
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        location.setRadiusInMeters(FIXED_RADIUS);
        location.setSsid(ssid);
        location.setActive(true);

        if (isEditMode && locationId != null) {
            location.setId(locationId);
            viewModel.updateLocation(location, creatorId);
        } else {
            viewModel.createLocation(location, creatorId);
        }
    }
    
    private void deleteLocation() {
        if (locationId != null) {
            viewModel.deleteLocation(locationId);
        }
    }
}
