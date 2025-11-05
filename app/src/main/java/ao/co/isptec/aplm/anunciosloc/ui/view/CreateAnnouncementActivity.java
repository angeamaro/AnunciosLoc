package ao.co.isptec.aplm.anunciosloc.ui.view;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import ao.co.isptec.aplm.anunciosloc.R;
import ao.co.isptec.aplm.anunciosloc.data.model.Location;
import ao.co.isptec.aplm.anunciosloc.data.repository.UserRepository;
import ao.co.isptec.aplm.anunciosloc.ui.viewmodel.AnnouncementViewModel;
import ao.co.isptec.aplm.anunciosloc.ui.viewmodel.LocationViewModel;
import ao.co.isptec.aplm.anunciosloc.utils.Constants;

/**
 * Activity para criar um novo anúncio
 */
public class CreateAnnouncementActivity extends AppCompatActivity {
    
    private MaterialToolbar toolbar;
    private TextInputEditText editTitle, editContent, editStartDate, editEndDate;
    private AutoCompleteTextView spinnerLocation;
    private RadioGroup radioGroupPolicy;
    private MaterialButton btnSave, btnConfigurePolicy;
    private ProgressBar progressBar;
    
    private AnnouncementViewModel announcementViewModel;
    private LocationViewModel locationViewModel;
    private UserRepository userRepository;
    
    private List<Location> locations = new ArrayList<>();
    private String selectedLocationId;
    private long startDateTimestamp = 0;
    private long endDateTimestamp = 0;
    private String selectedPolicy = Constants.DELIVERY_POLICY_EVERYONE;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_announcement);
        
        initializeViews();
        initializeViewModels();
        setupToolbar();
        setupListeners();
        observeViewModels();
        loadLocations();
    }
    
    private void initializeViews() {
        toolbar = findViewById(R.id.toolbar);
        editTitle = findViewById(R.id.editTitle);
        editContent = findViewById(R.id.editContent);
        editStartDate = findViewById(R.id.editStartDate);
        editEndDate = findViewById(R.id.editEndDate);
        spinnerLocation = findViewById(R.id.spinnerLocation);
        radioGroupPolicy = findViewById(R.id.radioGroupPolicy);
        btnSave = findViewById(R.id.btnSave);
        btnConfigurePolicy = findViewById(R.id.btnConfigurePolicy);
        progressBar = findViewById(R.id.progressBar);
        
        // Configuração inicial
        btnConfigurePolicy.setVisibility(View.GONE);
    }
    
    private void initializeViewModels() {
        announcementViewModel = new ViewModelProvider(this).get(AnnouncementViewModel.class);
        locationViewModel = new ViewModelProvider(this).get(LocationViewModel.class);
        userRepository = UserRepository.getInstance();
    }
    
    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.create_announcement);
        }
        
        toolbar.setNavigationOnClickListener(v -> finish());
    }
    
    private void setupListeners() {
        // Date pickers
        editStartDate.setOnClickListener(v -> showDateTimePicker(true));
        editEndDate.setOnClickListener(v -> showDateTimePicker(false));
        
        // Policy selection
        radioGroupPolicy.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioEveryone) {
                selectedPolicy = Constants.DELIVERY_POLICY_EVERYONE;
                btnConfigurePolicy.setVisibility(View.GONE);
            } else if (checkedId == R.id.radioWhitelist) {
                selectedPolicy = Constants.DELIVERY_POLICY_WHITELIST;
                btnConfigurePolicy.setVisibility(View.VISIBLE);
            } else if (checkedId == R.id.radioBlacklist) {
                selectedPolicy = Constants.DELIVERY_POLICY_BLACKLIST;
                btnConfigurePolicy.setVisibility(View.VISIBLE);
            }
        });
        
        btnConfigurePolicy.setOnClickListener(v -> {
            // TODO: Abrir activity de configuração de políticas
            Toast.makeText(this, "Configurar " + selectedPolicy, Toast.LENGTH_SHORT).show();
        });
        
        btnSave.setOnClickListener(v -> createAnnouncement());
    }
    
    private void observeViewModels() {
        // Observa localizações
        locationViewModel.getLocations().observe(this, locationList -> {
            if (locationList != null && !locationList.isEmpty()) {
                locations = locationList;
                setupLocationSpinner();
            }
        });
        
        // Observa estado de carregamento
        announcementViewModel.getIsLoading().observe(this, isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            btnSave.setEnabled(!isLoading);
        });
        
        // Observa mensagens de erro
        announcementViewModel.getErrorMessage().observe(this, error -> {
            if (error != null && !error.isEmpty()) {
                Toast.makeText(this, error, Toast.LENGTH_LONG).show();
            }
        });
        
        // Observa sucesso de criação
        announcementViewModel.getAnnouncementCreated().observe(this, success -> {
            if (success != null && success) {
                Toast.makeText(this, R.string.success_announcement_created, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
    
    private void loadLocations() {
        locationViewModel.loadLocations();
    }
    
    private void setupLocationSpinner() {
        List<String> locationNames = new ArrayList<>();
        for (Location location : locations) {
            locationNames.add(location.getName());
        }
        
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
            this,
            android.R.layout.simple_dropdown_item_1line,
            locationNames
        );
        
        spinnerLocation.setAdapter(adapter);
        spinnerLocation.setOnItemClickListener((parent, view, position, id) -> {
            selectedLocationId = locations.get(position).getId();
        });
    }
    
    private void showDateTimePicker(boolean isStartDate) {
        Calendar calendar = Calendar.getInstance();
        
        // Date picker
        DatePickerDialog datePickerDialog = new DatePickerDialog(
            this,
            (view, year, month, dayOfMonth) -> {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                
                // Time picker
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                    this,
                    (timeView, hourOfDay, minute) -> {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        calendar.set(Calendar.SECOND, 0);
                        
                        long timestamp = calendar.getTimeInMillis();
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                        String formattedDate = sdf.format(calendar.getTime());
                        
                        if (isStartDate) {
                            startDateTimestamp = timestamp;
                            editStartDate.setText(formattedDate);
                        } else {
                            endDateTimestamp = timestamp;
                            editEndDate.setText(formattedDate);
                        }
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true
                );
                timePickerDialog.show();
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        );
        
        datePickerDialog.show();
    }
    
    private void createAnnouncement() {
        // Validações
        String title = editTitle.getText().toString().trim();
        String content = editContent.getText().toString().trim();
        
        if (title.isEmpty()) {
            editTitle.setError(getString(R.string.error_empty_field));
            editTitle.requestFocus();
            return;
        }
        
        if (content.isEmpty()) {
            editContent.setError(getString(R.string.error_empty_field));
            editContent.requestFocus();
            return;
        }
        
        if (selectedLocationId == null || selectedLocationId.isEmpty()) {
            Toast.makeText(this, "Selecione uma localização", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (startDateTimestamp == 0) {
            Toast.makeText(this, "Selecione a data de início", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (endDateTimestamp == 0) {
            Toast.makeText(this, "Selecione a data de término", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (endDateTimestamp <= startDateTimestamp) {
            Toast.makeText(this, "A data de término deve ser posterior à data de início", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Cria anúncio
        announcementViewModel.createAnnouncement(
            title,
            content,
            selectedLocationId,
            startDateTimestamp,
            endDateTimestamp,
            selectedPolicy,
            new ArrayList<>() // TODO: Implementar configuração de regras de política
        );
    }
}
