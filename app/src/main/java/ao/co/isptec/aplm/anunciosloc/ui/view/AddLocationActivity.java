package ao.co.isptec.aplm.anunciosloc.ui.view;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

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
    
    public static final String EXTRA_LOCATION_ID = "location_id";
    public static final String EXTRA_EDIT_MODE = "edit_mode";
    
    private MaterialToolbar toolbar;
    private TextInputEditText editName, editLatitude, editLongitude, editRadius, editSsid, editDescription;
    private MaterialButton btnSave, btnDelete;
    private ProgressBar progressBar;
    
    private LocationViewModel viewModel;
    private UserRepository userRepository;
    
    private boolean isEditMode = false;
    private String locationId;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);
        
        // Verifica se está em modo de edição
        isEditMode = getIntent().getBooleanExtra(EXTRA_EDIT_MODE, false);
        locationId = getIntent().getStringExtra(EXTRA_LOCATION_ID);
        
        initializeViews();
        initializeViewModel();
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
        editRadius = findViewById(R.id.editRadius);
        editSsid = findViewById(R.id.editSsid);
        editDescription = findViewById(R.id.editDescription);
        btnSave = findViewById(R.id.btnSave);
        btnDelete = findViewById(R.id.btnDelete);
        progressBar = findViewById(R.id.progressBar);
        
        // Mostra botão delete apenas em modo edição
        btnDelete.setVisibility(isEditMode ? View.VISIBLE : View.GONE);
    }
    
    private void initializeViewModel() {
        viewModel = new ViewModelProvider(this).get(LocationViewModel.class);
        userRepository = UserRepository.getInstance();
    }
    
    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(isEditMode ? R.string.edit_location : R.string.add_location);
        }
        
        toolbar.setNavigationOnClickListener(v -> finish());
    }
    
    private void setupListeners() {
        btnSave.setOnClickListener(v -> saveLocation());
        
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
                Toast.makeText(this, R.string.success_location_created, Toast.LENGTH_SHORT).show();
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
        editLatitude.setText(String.valueOf(location.getLatitude()));
        editLongitude.setText(String.valueOf(location.getLongitude()));
        editRadius.setText(String.valueOf(location.getRadiusInMeters()));
        if (location.getSsid() != null) {
            editSsid.setText(location.getSsid());
        }
        if (location.getDescription() != null) {
            editDescription.setText(location.getDescription());
        }
    }
    
    private void saveLocation() {
        // Validações
        String name = editName.getText().toString().trim();
        String latStr = editLatitude.getText().toString().trim();
        String lonStr = editLongitude.getText().toString().trim();
        String radiusStr = editRadius.getText().toString().trim();
        String ssid = editSsid.getText().toString().trim();
        String description = editDescription.getText().toString().trim();
        
        if (name.isEmpty()) {
            editName.setError(getString(R.string.error_empty_field));
            editName.requestFocus();
            return;
        }
        
        if (latStr.isEmpty()) {
            editLatitude.setError(getString(R.string.error_empty_field));
            editLatitude.requestFocus();
            return;
        }
        
        if (lonStr.isEmpty()) {
            editLongitude.setError(getString(R.string.error_empty_field));
            editLongitude.requestFocus();
            return;
        }
        
        if (radiusStr.isEmpty()) {
            editRadius.setError(getString(R.string.error_empty_field));
            editRadius.requestFocus();
            return;
        }
        
        double latitude, longitude;
        int radius;
        
        try {
            latitude = Double.parseDouble(latStr);
            longitude = Double.parseDouble(lonStr);
            radius = Integer.parseInt(radiusStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Valores numéricos inválidos", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Valida coordenadas
        if (!ValidationUtils.isValidLatitude(latitude)) {
            editLatitude.setError(getString(R.string.error_invalid_coordinates));
            editLatitude.requestFocus();
            return;
        }
        
        if (!ValidationUtils.isValidLongitude(longitude)) {
            editLongitude.setError(getString(R.string.error_invalid_coordinates));
            editLongitude.requestFocus();
            return;
        }
        
        if (radius <= 0 || radius > 10000) {
            editRadius.setError(getString(R.string.error_invalid_radius));
            editRadius.requestFocus();
            return;
        }
        
        // Cria ou atualiza localização
        if (isEditMode && locationId != null) {
            Location location = new Location(locationId, name, latitude, longitude, radius);
            location.setSsid(ssid.isEmpty() ? null : ssid);
            location.setDescription(description.isEmpty() ? null : description);
            location.setCreatedBy(userRepository.getCurrentUser() != null ? userRepository.getCurrentUser().getId() : "unknown");
            location.setActive(true);
            
            viewModel.updateLocation(location);
        } else {
            Location location = new Location();
            location.setName(name);
            location.setLatitude(latitude);
            location.setLongitude(longitude);
            location.setRadiusInMeters(radius);
            location.setSsid(ssid.isEmpty() ? null : ssid);
            location.setDescription(description.isEmpty() ? null : description);
            location.setCreatedBy(userRepository.getCurrentUser() != null ? userRepository.getCurrentUser().getId() : "unknown");
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
