package ao.co.isptec.aplm.anunciosloc.ui.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import ao.co.isptec.aplm.anunciosloc.R;
import ao.co.isptec.aplm.anunciosloc.ui.viewmodel.AuthViewModel;
import ao.co.isptec.aplm.anunciosloc.utils.ValidationUtils;

public class RegisterActivity extends AppCompatActivity {
    
    private EditText editUsername, editPassword, editConfirmPassword;
    private Button btnRegister;
    private TextView txtLogin;
    private ProgressBar progressBar;
    private AuthViewModel authViewModel;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        
        initializeViews();
        initializeViewModel();
        setupListeners();
        observeViewModel();
    }
    
    private void initializeViews() {
        editUsername = findViewById(R.id.editUsername);
        editPassword = findViewById(R.id.editPassword);
        editConfirmPassword = findViewById(R.id.editConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        txtLogin = findViewById(R.id.txtLogin);
        progressBar = findViewById(R.id.progressBar);
    }
    
    private void initializeViewModel() {
        authViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(AuthViewModel.class);
    }
    
    private void setupListeners() {
        btnRegister.setOnClickListener(v -> attemptRegister());
        txtLogin.setOnClickListener(v -> finish());
    }
    
    private void observeViewModel() {
        authViewModel.getIsLoading().observe(this, isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            btnRegister.setEnabled(!isLoading);
        });
        
        authViewModel.getRegisterSuccess().observe(this, success -> {
            if (success != null && success) {
                Toast.makeText(this, "Registo efetuado com sucesso!", Toast.LENGTH_LONG).show();
                finish(); 
            }
        });
        
        authViewModel.getErrorMessage().observe(this, error -> {
            if (error != null && !error.isEmpty()) {
                Toast.makeText(this, error, Toast.LENGTH_LONG).show();
            }
        });
    }
    
    private void attemptRegister() {
        String username = editUsername.getText().toString().trim();
        String password = editPassword.getText().toString();
        String confirmPassword = editConfirmPassword.getText().toString();
        
        if (ValidationUtils.isNotEmpty(username) && ValidationUtils.isValidPassword(password) && password.equals(confirmPassword)) {
            authViewModel.register(username, password);
        } else {
            if (!ValidationUtils.isNotEmpty(username)) editUsername.setError("Campo obrigatório");
            if (!ValidationUtils.isValidPassword(password)) editPassword.setError("Senha inválida (mínimo 6 caracteres)");
            if (!password.equals(confirmPassword)) editConfirmPassword.setError("As senhas não correspondem");
        }
    }
}
