package ao.co.isptec.aplm.anunciosloc.ui.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

import ao.co.isptec.aplm.anunciosloc.R;
import ao.co.isptec.aplm.anunciosloc.data.model.Location;
import ao.co.isptec.aplm.anunciosloc.data.model.User;
import ao.co.isptec.aplm.anunciosloc.ui.viewmodel.LocationViewModel;
import ao.co.isptec.aplm.anunciosloc.utils.PreferencesHelper;

public class LocationDetailsActivity extends AppCompatActivity implements OnMapReadyCallback {
    
    private static final String TAG = "LocationDetailsActivity";
    
    // Views
    private MaterialToolbar toolbar;
    private TextView txtLocationName, txtLocationType, txtLatitude, txtLongitude, txtSsid, txtCreatedBy;
    private LinearLayout containerCoordinates, containerSsid, containerActions;
    private ImageView iconType;
    private MaterialButton btnEdit, btnDelete;
    private ProgressBar progressBar;
    
    // Map
    private GoogleMap googleMap;
    
    // Data
    private LocationViewModel locationViewModel;
    private Location location;
    private String locationId;
    private String currentUserId;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_details);
        
        // CORRIGIDO: Obtém o usuário atual através do método estático
        User currentUser = PreferencesHelper.getCurrentUser(this);
        if (currentUser != null) {
            currentUserId = String.valueOf(currentUser.getId());
        } else {
            Toast.makeText(this, "Erro de sessão. Por favor, faça login novamente.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        
        // Obtém ID da localização
        locationId = getIntent().getStringExtra("LOCATION_ID");
        if (locationId == null) {
            Toast.makeText(this, "Erro: Localização não encontrada", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        
        Log.d(TAG, "LocationDetailsActivity started with locationId: " + locationId);
        
        initializeViews();
        setupToolbar();
        setupViewModel();
        loadLocationDetails();
    }
    
    private void initializeViews() {
        toolbar = findViewById(R.id.toolbar);
        txtLocationName = findViewById(R.id.txtLocationName);
        txtLocationType = findViewById(R.id.txtLocationType);
        txtLatitude = findViewById(R.id.txtLatitude);
        txtLongitude = findViewById(R.id.txtLongitude);
        txtSsid = findViewById(R.id.txtSsid);
        txtCreatedBy = findViewById(R.id.txtCreatedBy);
        containerCoordinates = findViewById(R.id.containerCoordinates);
        containerSsid = findViewById(R.id.containerSsid);
        containerActions = findViewById(R.id.containerActions);
        iconType = findViewById(R.id.iconType);
        btnEdit = findViewById(R.id.btnEdit);
        btnDelete = findViewById(R.id.btnDelete);
        progressBar = findViewById(R.id.progressBar);
        
        // Setup map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapView);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }
    
    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Detalhes da Localização");
        }
        
        toolbar.setNavigationOnClickListener(v -> finish());
    }
    
    private void setupViewModel() {
        locationViewModel = new ViewModelProvider(this).get(LocationViewModel.class);
        
        // Observer para quando localização for deletada
        locationViewModel.getLocationDeleted().observe(this, deleted -> {
            if (deleted != null && deleted) {
                Toast.makeText(this, "Localização removida com sucesso", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            }
        });
        
        locationViewModel.getErrorMessage().observe(this, error -> {
            if (error != null && !error.isEmpty()) {
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            }
        });
        
        locationViewModel.getIsLoading().observe(this, isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });
    }
    
    private void loadLocationDetails() {
        progressBar.setVisibility(View.VISIBLE);
        
        // Observer para obter os detalhes da localização
        locationViewModel.getLocations().observe(this, locations -> {
            if (locations != null) {
                for (Location loc : locations) {
                    if (loc.getId().equals(locationId)) {
                        location = loc;
                        displayLocationDetails(loc);
                        progressBar.setVisibility(View.GONE);
                        return;
                    }
                }
                // Se não encontrou
                Toast.makeText(this, "Localização não encontrada", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        
        // Carrega as localizações
        locationViewModel.loadLocations();
    }
    
    private void displayLocationDetails(Location location) {
        // Nome
        txtLocationName.setText(location.getName());
        
        // Determina o tipo baseado nos dados
        boolean isGPS = isGPSLocation(location);
        
        // Tipo
        if (isGPS) {
            txtLocationType.setText("Localização GPS");
            iconType.setImageResource(R.drawable.ic_location_on);
            
            // Mostra coordenadas
            containerCoordinates.setVisibility(View.VISIBLE);
            containerSsid.setVisibility(View.GONE);
            txtLatitude.setText(String.format(java.util.Locale.US, "%.6f", location.getLatitude()));
            txtLongitude.setText(String.format(java.util.Locale.US, "%.6f", location.getLongitude()));
            
            // Atualiza mapa
            if (googleMap != null) {
                LatLng position = new LatLng(location.getLatitude(), location.getLongitude());
                googleMap.clear();
                googleMap.addMarker(new MarkerOptions()
                        .position(position)
                        .title(location.getName()));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15f));
            }
        } else {
            txtLocationType.setText("Rede WiFi");
            iconType.setImageResource(R.drawable.ic_wifi);
            
            // Mostra SSID
            containerCoordinates.setVisibility(View.GONE);
            containerSsid.setVisibility(View.VISIBLE);
            txtSsid.setText(location.getSsid() != null ? location.getSsid() : "N/A");
        }
        
        // Criador
        txtCreatedBy.setText(location.getCreatedBy() != null ? location.getCreatedBy() : "Desconhecido");
        
        // Verifica se é o criador para mostrar botões de ação
        boolean isOwner = location.getCreatedBy() != null && 
                         location.getCreatedBy().equals(currentUserId);
        
        Log.d(TAG, "Current user: " + currentUserId + ", Location creator: " + location.getCreatedBy() + ", isOwner: " + isOwner);
        
        if (isOwner) {
            containerActions.setVisibility(View.VISIBLE);
            setupActionButtons();
        } else {
            containerActions.setVisibility(View.GONE);
        }
    }
    
    private void setupActionButtons() {
        btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddLocationActivity.class);
            intent.putExtra(AddLocationActivity.EXTRA_LOCATION_ID, locationId);
            intent.putExtra(AddLocationActivity.EXTRA_EDIT_MODE, true);
            startActivity(intent);
            finish();
        });
        
        btnDelete.setOnClickListener(v -> showDeleteConfirmationDialog());
    }
    
    private void showDeleteConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Eliminar Localização")
                .setMessage("Tem certeza que deseja eliminar esta localização? Esta ação não pode ser desfeita.")
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    locationViewModel.deleteLocation(locationId);
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
    
    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        
        // Se já tem localização carregada, atualiza o mapa
        if (location != null && isGPSLocation(location)) {
            LatLng position = new LatLng(location.getLatitude(), location.getLongitude());
            googleMap.addMarker(new MarkerOptions()
                    .position(position)
                    .title(location.getName()));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15f));
        }
    }
    
    /**
     * Determina se a localização é GPS ou WiFi
     * GPS: tem coordenadas não-nulas (lat != 0 ou lon != 0)
     * WiFi: tem SSID definido e coordenadas zeradas
     */
    private boolean isGPSLocation(Location location) {
        if (location == null) return false;
        
        // Se tem SSID, é WiFi
        if (location.getSsid() != null && !location.getSsid().isEmpty()) {
            return false;
        }
        
        // Se tem coordenadas válidas (diferente de 0,0), é GPS
        return location.getLatitude() != 0 || location.getLongitude() != 0;
    }
}
