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

        // Animação de entrada suave

        // Verifica modo edição
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
            String title = isEditMode ? "Editar Localização" : "Adicionar Localização";
            getSupportActionBar().setTitle(title);
        }

        // Seta de voltar (tanto toolbar quanto botão físico)
        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });
    }



    // Garante animação ao sair com finish()
    @Override
    public void finish() {
        super.finish();
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
            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Eliminar Localização")
                    .setMessage("Tem certeza que deseja eliminar esta localização?")
                    .setPositiveButton("Sim", (dialog, which) -> deleteLocation())
                    .setNegativeButton("Não", null)
                    .show();
        });
    }

    private void observeViewModel() {
        viewModel.getLocations().observe(this, locations -> {
            if (isEditMode && locationId != null && locations != null) {
                for (Location loc : locations) {
                    if (loc.getId().equals(locationId)) {
                        fillFormWithLocation(loc);
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
                Toast.makeText(this, "Localização criada com sucesso!", Toast.LENGTH_LONG).show();
                finish();
            }
        });

        viewModel.getLocationUpdated().observe(this, success -> {
            if (success != null && success) {
                Toast.makeText(this, "Localização atualizada!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        viewModel.getLocationDeleted().observe(this, success -> {
            if (success != null && success) {
                Toast.makeText(this, "Localização eliminada!", Toast.LENGTH_SHORT).show();
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
            editLatitude.setText(String.format(java.util.Locale.US, "%.6f", location.getLatitude()));
            editLongitude.setText(String.format(java.util.Locale.US, "%.6f", location.getLongitude()));
        }
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }

        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
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
                        Toast.makeText(this, "Localização obtida!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Não foi possível obter localização", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    btnGetLocation.setEnabled(true);
                    Toast.makeText(this, "Erro: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

    private void detectCurrentWiFi() {
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
            WifiInfo info = wifiManager.getConnectionInfo();
            if (info != null) {
                String ssid = info.getSSID();
                if (ssid != null && !ssid.equals("<unknown ssid>") && !ssid.equals("0x")) {
                    ssid = ssid.replace("\"", "");
                    editSsid.setText(ssid);
                    Toast.makeText(this, "WiFi detectado: " + ssid, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Não conectado a WiFi", Toast.LENGTH_LONG).show();
                }
            }
        } else {
            Toast.makeText(this, "WiFi desativado", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE || requestCode == WIFI_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) getCurrentLocation();
                else detectCurrentWiFi();
            } else {
                Toast.makeText(this, "Permissão necessária", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void saveLocation() {
        String name = editName.getText().toString().trim();
        if (name.isEmpty()) {
            editName.setError("Nome obrigatório");
            editName.requestFocus();
            return;
        }

        double lat = 0, lon = 0;
        String ssid = null;

        if (isGPSMode) {
            String latStr = editLatitude.getText().toString().trim().replace(",", ".");
            String lonStr = editLongitude.getText().toString().trim().replace(",", ".");

            if (latStr.isEmpty() || lonStr.isEmpty()) {
                Toast.makeText(this, "Obtenha a localização GPS primeiro", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                lat = Double.parseDouble(latStr);
                lon = Double.parseDouble(lonStr);
            } catch (Exception e) {
                Toast.makeText(this, "Coordenadas inválidas", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!ValidationUtils.isValidLatitude(lat) || !ValidationUtils.isValidLongitude(lon)) {
                Toast.makeText(this, "Coordenadas fora do alcance", Toast.LENGTH_LONG).show();
                return;
            }
        } else {
            ssid = editSsid.getText().toString().trim();
            if (ssid.isEmpty()) {
                editSsid.setError("SSID obrigatório");
                editSsid.requestFocus();
                return;
            }
        }

        Location location = isEditMode && locationId != null
                ? new Location(locationId, name, lat, lon, FIXED_RADIUS)
                : new Location();

        location.setName(name);
        location.setLatitude(lat);
        location.setLongitude(lon);
        location.setRadiusInMeters(FIXED_RADIUS);
        location.setSsid(ssid);
        location.setCreatedBy(userRepository.getCurrentUser() != null ? userRepository.getCurrentUser().getId() : "unknown");
        location.setActive(true);

        if (isEditMode && locationId != null) {
            viewModel.updateLocation(location);
        } else {
            viewModel.createLocation(location);
        }
    }

    private void deleteLocation() {
        if (locationId != null) {
            viewModel.deleteLocation(locationId);
        }
    }
}