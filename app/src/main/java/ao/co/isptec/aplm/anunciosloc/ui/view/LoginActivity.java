package ao.co.isptec.aplm.anunciosloc.ui.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import ao.co.isptec.aplm.anunciosloc.ui.view.MainActivity;
import ao.co.isptec.aplm.anunciosloc.R;
import ao.co.isptec.aplm.anunciosloc.data.model.User;
import ao.co.isptec.aplm.anunciosloc.ui.viewmodel.AuthViewModel;
import ao.co.isptec.aplm.anunciosloc.utils.FeedbackUtils;
import ao.co.isptec.aplm.anunciosloc.utils.PreferencesHelper;
import ao.co.isptec.aplm.anunciosloc.utils.ValidationUtils;

/**
 * Tela de Login
 */
public class LoginActivity extends AppCompatActivity {
    
    private EditText editEmail, editPassword;
    private Button btnLogin;
    private TextView txtRegister, txtForgotPassword;
    private ProgressBar progressBar;
    
    private AuthViewModel authViewModel;
    private PreferencesHelper preferencesHelper;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        // Esconde ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        
        initializeViews();
        initializeViewModel();
        setupListeners();
        observeViewModel();
    }
    
    private void initializeViews() {
        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        btnLogin = findViewById(R.id.btnLogin);
        txtRegister = findViewById(R.id.txtRegister);
        txtForgotPassword = findViewById(R.id.txtForgotPassword);
        progressBar = findViewById(R.id.progressBar);
        
        preferencesHelper = new PreferencesHelper(this);
    }
    
    private void initializeViewModel() {
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
    }
    
    private void setupListeners() {
        btnLogin.setOnClickListener(v -> attemptLogin());
        
        txtRegister.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
        
        txtForgotPassword.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
        });
    }
    
    private void observeViewModel() {
        // Observa estado de carregamento
        authViewModel.getIsLoading().observe(this, isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            btnLogin.setEnabled(!isLoading);
            editEmail.setEnabled(!isLoading);
            editPassword.setEnabled(!isLoading);
        });
        
        // Observa sucesso de login
        authViewModel.getLoginSuccess().observe(this, success -> {
            if (success != null && success) {
                User user = authViewModel.getCurrentUser();
                if (user != null) {
                    // Salva sessão
                    preferencesHelper.saveUserSession(
                        user.getId(),
                        user.getName(),  // Corrigido: usa getName() ao invés de getUsername()
                        user.getEmail(),
                        "mock_token_" + user.getId()
                    );
                    
                    FeedbackUtils.showSuccess(findViewById(android.R.id.content), getString(R.string.success_login));
                    
                    // Navega para MainActivity
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        });
        
        // Observa mensagens de erro
        authViewModel.getErrorMessage().observe(this, error -> {
            if (error != null && !error.isEmpty()) {
                FeedbackUtils.showError(findViewById(android.R.id.content), error);
            }
        });
    }
    
    private void attemptLogin() {
        // Limpa erros anteriores
        editEmail.setError(null);
        editPassword.setError(null);
        
        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString();
        
        // Validações
        boolean isValid = true;
        
        if (!ValidationUtils.isNotEmpty(email)) {
            editEmail.setError(getString(R.string.error_empty_field));
            isValid = false;
        } else if (!ValidationUtils.isValidEmail(email)) {
            editEmail.setError(getString(R.string.error_invalid_email));
            isValid = false;
        }
        
        if (!ValidationUtils.isNotEmpty(password)) {
            editPassword.setError(getString(R.string.error_empty_field));
            isValid = false;
        } else if (!ValidationUtils.isValidPassword(password)) {
            editPassword.setError(getString(R.string.error_invalid_password));
            isValid = false;
        }
        
        if (isValid) {
            authViewModel.login(email, password);
        }
    }
}
