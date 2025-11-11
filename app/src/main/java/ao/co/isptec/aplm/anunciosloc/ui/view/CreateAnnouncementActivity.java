package ao.co.isptec.aplm.anunciosloc.ui.view;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import ao.co.isptec.aplm.anunciosloc.data.model.PolicyFilter;
import ao.co.isptec.aplm.anunciosloc.data.repository.UserRepository;
import ao.co.isptec.aplm.anunciosloc.ui.viewmodel.AnnouncementViewModel;
import ao.co.isptec.aplm.anunciosloc.ui.viewmodel.LocationViewModel;
import ao.co.isptec.aplm.anunciosloc.utils.Constants;

/**
 * Activity para criar um novo anúncio
 */
public class CreateAnnouncementActivity extends AppCompatActivity {
    
    private static final String KEY_TITLE = "saved_title";
    private static final String KEY_CONTENT = "saved_content";
    private static final String KEY_START_DATE = "saved_start_date";
    private static final String KEY_END_DATE = "saved_end_date";
    private static final String KEY_START_TIME = "saved_start_time";
    private static final String KEY_END_TIME = "saved_end_time";
    private static final String KEY_LOCATION_ID = "saved_location_id";
    private static final String KEY_POLICY = "saved_policy";
    
    private MaterialToolbar toolbar;
    private TextInputEditText editTitle, editContent;
    private TextInputEditText editStartDate, editEndDate, editStartTime, editEndTime;
    private AutoCompleteTextView spinnerLocation;
    private RadioGroup radioGroupPolicy, radioGroupDeliveryMode;
    private MaterialButton btnSave, btnConfigurePolicy, btnCreateLocation;
    private ProgressBar progressBar;
    
    private AnnouncementViewModel announcementViewModel;
    private LocationViewModel locationViewModel;
    private UserRepository userRepository;
    
    private List<Location> locations = new ArrayList<>();
    private String selectedLocationId;
    private long startDateTimestamp = 0;
    private long endDateTimestamp = 0;
    private String selectedPolicy = Constants.DELIVERY_POLICY_EVERYONE;
    private String selectedDeliveryMode = Constants.DELIVERY_MODE_CENTRALIZED;
    private PolicyFilter policyFilter;
    
    private ActivityResultLauncher<Intent> createLocationLauncher;
    private ActivityResultLauncher<Intent> configurePolicyLauncher;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_announcement);
        
        // Configurar o launcher para criar localização
        createLocationLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    // Recarrega as localizações após criar uma nova
                    loadLocations();
                    Toast.makeText(this, "Localização criada com sucesso!", Toast.LENGTH_SHORT).show();
                }
            }
        );
        
        // Configurar o launcher para configurar política
        configurePolicyLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    PolicyFilter filter = (PolicyFilter) result.getData().getSerializableExtra(ConfigurePolicyActivity.EXTRA_POLICY_FILTER);
                    if (filter != null && !filter.isEmpty()) {
                        policyFilter = filter;
                        String attributes = filter.toString();
                        String message = selectedPolicy.equals(Constants.DELIVERY_POLICY_WHITELIST)
                            ? "Whitelist: " + attributes
                            : "Blacklist: " + attributes;
                        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                    }
                }
            }
        );

        
        initializeViews();
        initializeViewModels();
        setupToolbar();
        setupListeners();
        observeViewModels();
        loadLocations();
        
        // Restaura dados salvos se houver
        if (savedInstanceState != null) {
            restoreInstanceState(savedInstanceState);
        }
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        
        // Salva o estado atual dos campos
        if (editTitle.getText() != null) {
            outState.putString(KEY_TITLE, editTitle.getText().toString());
        }
        if (editContent.getText() != null) {
            outState.putString(KEY_CONTENT, editContent.getText().toString());
        }
        if (editStartDate.getText() != null) {
            outState.putString(KEY_START_DATE, editStartDate.getText().toString());
        }
        if (editEndDate.getText() != null) {
            outState.putString(KEY_END_DATE, editEndDate.getText().toString());
        }
        if (editStartTime.getText() != null) {
            outState.putString(KEY_START_TIME, editStartTime.getText().toString());
        }
        if (editEndTime.getText() != null) {
            outState.putString(KEY_END_TIME, editEndTime.getText().toString());
        }
        outState.putString(KEY_LOCATION_ID, selectedLocationId);
        outState.putString(KEY_POLICY, selectedPolicy);
    }
    
    private void restoreInstanceState(Bundle savedInstanceState) {
        String title = savedInstanceState.getString(KEY_TITLE);
        String content = savedInstanceState.getString(KEY_CONTENT);
        String startDate = savedInstanceState.getString(KEY_START_DATE);
        String endDate = savedInstanceState.getString(KEY_END_DATE);
        String startTime = savedInstanceState.getString(KEY_START_TIME);
        String endTime = savedInstanceState.getString(KEY_END_TIME);
        selectedLocationId = savedInstanceState.getString(KEY_LOCATION_ID);
        selectedPolicy = savedInstanceState.getString(KEY_POLICY);
        
        if (title != null) editTitle.setText(title);
        if (content != null) editContent.setText(content);
        if (startDate != null) editStartDate.setText(startDate);
        if (endDate != null) editEndDate.setText(endDate);
        if (startTime != null) editStartTime.setText(startTime);
        if (endTime != null) editEndTime.setText(endTime);
    }
    
    private void initializeViews() {
        toolbar = findViewById(R.id.toolbar);
        editTitle = findViewById(R.id.editTitle);
        editContent = findViewById(R.id.editContent);
        editStartDate = findViewById(R.id.editStartDate);
        editEndDate = findViewById(R.id.editEndDate);
        editStartTime = findViewById(R.id.editStartTime);
        editEndTime = findViewById(R.id.editEndTime);
        spinnerLocation = findViewById(R.id.spinnerLocation);
        radioGroupPolicy = findViewById(R.id.radioGroupPolicy);
        radioGroupDeliveryMode = findViewById(R.id.radioGroupDeliveryMode);
        btnSave = findViewById(R.id.btnSave);
        btnConfigurePolicy = findViewById(R.id.btnConfigurePolicy);
        btnCreateLocation = findViewById(R.id.btnCreateLocation);
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
        editStartDate.setOnClickListener(v -> showDatePicker(true, true));
        editEndDate.setOnClickListener(v -> showDatePicker(false, true));
        
        // Time pickers
        editStartTime.setOnClickListener(v -> showTimePicker(true));
        editEndTime.setOnClickListener(v -> showTimePicker(false));
        
        // Botão criar localização
        btnCreateLocation.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddLocationActivity.class);
            createLocationLauncher.launch(intent);
        });
        
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
        
        // Delivery mode selection
        radioGroupDeliveryMode.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioCentralized) {
                selectedDeliveryMode = Constants.DELIVERY_MODE_CENTRALIZED;
            } else if (checkedId == R.id.radioDecentralized) {
                selectedDeliveryMode = Constants.DELIVERY_MODE_DECENTRALIZED;
            }
        });
        
        btnConfigurePolicy.setOnClickListener(v -> {
            Intent intent = new Intent(this, ConfigurePolicyActivity.class);
            intent.putExtra(ConfigurePolicyActivity.EXTRA_POLICY_TYPE, selectedPolicy);
            if (policyFilter != null) {
                intent.putExtra(ConfigurePolicyActivity.EXTRA_POLICY_FILTER, policyFilter);
            }
            configurePolicyLauncher.launch(intent);
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
    
    private void showDatePicker(boolean isStartDate, boolean showTime) {
        Calendar calendar = Calendar.getInstance();
        
        // Date picker
        DatePickerDialog datePickerDialog = new DatePickerDialog(
            this,
            (view, year, month, dayOfMonth) -> {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                calendar.set(year, month, dayOfMonth);
                String dateStr = sdf.format(calendar.getTime());
                
                if (isStartDate) {
                    editStartDate.setText(dateStr);
                } else {
                    editEndDate.setText(dateStr);
                }
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        );
        
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
    }
    
    private void showTimePicker(boolean isStartTime) {
        Calendar calendar = Calendar.getInstance();
        
        TimePickerDialog timePickerDialog = new TimePickerDialog(
            this,
            (view, hourOfDay, minute) -> {
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                String timeStr = sdf.format(calendar.getTime());
                
                if (isStartTime) {
                    editStartTime.setText(timeStr);
                } else {
                    editEndTime.setText(timeStr);
                }
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        );
        
        timePickerDialog.show();
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
        String startDateStr = editStartDate.getText().toString().trim();
        String endDateStr = editEndDate.getText().toString().trim();
        String startTimeStr = editStartTime.getText().toString().trim();
        String endTimeStr = editEndTime.getText().toString().trim();
        
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
        
        if (startDateStr.isEmpty()) {
            Toast.makeText(this, "Selecione a data de início", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (startTimeStr.isEmpty()) {
            Toast.makeText(this, "Selecione a hora de início", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (endDateStr.isEmpty()) {
            Toast.makeText(this, "Selecione a data de término", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (endTimeStr.isEmpty()) {
            Toast.makeText(this, "Selecione a hora de término", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Converte data e hora para timestamp
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            startDateTimestamp = sdf.parse(startDateStr + " " + startTimeStr).getTime();
            endDateTimestamp = sdf.parse(endDateStr + " " + endTimeStr).getTime();
            
            if (endDateTimestamp <= startDateTimestamp) {
                Toast.makeText(this, "A data de término deve ser posterior à data de início", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (Exception e) {
            Toast.makeText(this, "Erro ao processar as datas", Toast.LENGTH_SHORT).show();
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
            policyFilter,
            selectedDeliveryMode
        );
    }
}
