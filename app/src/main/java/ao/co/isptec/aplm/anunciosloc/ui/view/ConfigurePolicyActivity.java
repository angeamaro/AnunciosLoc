package ao.co.isptec.aplm.anunciosloc.ui.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import ao.co.isptec.aplm.anunciosloc.R;
import ao.co.isptec.aplm.anunciosloc.data.model.User;
import ao.co.isptec.aplm.anunciosloc.ui.adapter.UserSelectionAdapter;
import ao.co.isptec.aplm.anunciosloc.ui.viewmodel.UserViewModel;
import ao.co.isptec.aplm.anunciosloc.utils.Constants;

/**
 * Activity para configurar regras de whitelist/blacklist
 */
public class ConfigurePolicyActivity extends AppCompatActivity {
    
    public static final String EXTRA_POLICY_TYPE = "policy_type";
    public static final String EXTRA_SELECTED_KEYS = "selected_keys";
    
    private MaterialToolbar toolbar;
    private RecyclerView recyclerViewUsers;
    private ProgressBar progressBar;
    private MaterialButton btnConfirm;
    
    private UserViewModel userViewModel;
    private UserSelectionAdapter adapter;
    private String policyType;
    private List<String> selectedKeys = new ArrayList<>();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure_policy);
        
        // Pega o tipo de política (whitelist ou blacklist)
        policyType = getIntent().getStringExtra(EXTRA_POLICY_TYPE);
        if (policyType == null) {
            policyType = Constants.DELIVERY_POLICY_WHITELIST;
        }
        
        // Recupera chaves previamente selecionadas (se houver)
        ArrayList<String> previousKeys = getIntent().getStringArrayListExtra(EXTRA_SELECTED_KEYS);
        if (previousKeys != null) {
            selectedKeys = new ArrayList<>(previousKeys);
        }
        
        initializeViews();
        initializeViewModel();
        setupToolbar();
        setupRecyclerView();
        setupListeners();
        observeViewModel();
        
        // Carrega os usuários
        userViewModel.loadAllUsers();
    }
    
    private void initializeViews() {
        toolbar = findViewById(R.id.toolbar);
        recyclerViewUsers = findViewById(R.id.recyclerViewUsers);
        progressBar = findViewById(R.id.progressBar);
        btnConfirm = findViewById(R.id.btnConfirm);
    }
    
    private void initializeViewModel() {
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
    }
    
    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            
            String title = policyType.equals(Constants.DELIVERY_POLICY_WHITELIST) 
                ? "Configurar Whitelist" 
                : "Configurar Blacklist";
            getSupportActionBar().setTitle(title);
        }
        
        toolbar.setNavigationOnClickListener(v -> finish());
    }
    
    private void setupRecyclerView() {
        adapter = new UserSelectionAdapter(
            new ArrayList<>(), 
            selectedKeys,
            policyType.equals(Constants.DELIVERY_POLICY_WHITELIST)
        );
        
        recyclerViewUsers.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewUsers.setAdapter(adapter);
    }
    
    private void setupListeners() {
        btnConfirm.setOnClickListener(v -> {
            selectedKeys = adapter.getSelectedKeys();
            
            if (selectedKeys.isEmpty()) {
                String message = policyType.equals(Constants.DELIVERY_POLICY_WHITELIST)
                    ? "Selecione pelo menos um usuário para permitir"
                    : "Selecione pelo menos um usuário para bloquear";
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                return;
            }
            
            // Retorna as chaves selecionadas
            Intent resultIntent = new Intent();
            resultIntent.putStringArrayListExtra(EXTRA_SELECTED_KEYS, new ArrayList<>(selectedKeys));
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }
    
    private void observeViewModel() {
        // Observa lista de usuários
        userViewModel.getAllUsers().observe(this, users -> {
            if (users != null && !users.isEmpty()) {
                adapter.updateUsers(users);
            }
        });
        
        // Observa estado de carregamento
        userViewModel.getIsLoading().observe(this, isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            recyclerViewUsers.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        });
        
        // Observa erros
        userViewModel.getErrorMessage().observe(this, error -> {
            if (error != null && !error.isEmpty()) {
                Toast.makeText(this, error, Toast.LENGTH_LONG).show();
            }
        });
    }
}
