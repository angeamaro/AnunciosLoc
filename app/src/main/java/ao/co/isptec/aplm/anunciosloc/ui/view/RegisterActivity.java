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

import ao.co.isptec.aplm.anunciosloc.MainActivity;
import ao.co.isptec.aplm.anunciosloc.R;
import ao.co.isptec.aplm.anunciosloc.data.model.User;
import ao.co.isptec.aplm.anunciosloc.ui.viewmodel.AuthViewModel;
import ao.co.isptec.aplm.anunciosloc.utils.PreferencesHelper;
import ao.co.isptec.aplm.anunciosloc.utils.ValidationUtils;

/**
 * Tela de Registro
 */
public class RegisterActivity extends AppCompatActivity {
    
    private EditText editUsername, editName, editEmail, editPassword, editConfirmPassword;
    private Button btnRegister;
    private TextView txtLogin;
    private ProgressBar progressBar;
    
    private AuthViewModel authViewModel;
    private PreferencesHelper preferencesHelper;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        
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
        editUsername = findViewById(R.id.editUsername);
        editName = findViewById(R.id.editName);
        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        editConfirmPassword = findViewById(R.id.editConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        txtLogin = findViewById(R.id.txtLogin);
        progressBar = findViewById(R.id.progressBar);
        
        preferencesHelper = new PreferencesHelper(this);
    }
    
    private void initializeViewModel() {
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
    }
    
    private void setupListeners() {
        btnRegister.setOnClickListener(v -> attemptRegister());
        
        txtLogin.setOnClickListener(v -> {
            finish(); // Volta para LoginActivity
        });
    }
    
    private void observeViewModel() {
        // Observa estado de carregamento
        authViewModel.getIsLoading().observe(this, isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            btnRegister.setEnabled(!isLoading);
            setFieldsEnabled(!isLoading);
        });
        
        // Observa sucesso de registro
        authViewModel.getRegisterSuccess().observe(this, success -> {
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
                    
                    Toast.makeText(this, R.string.success_register, Toast.LENGTH_SHORT).show();
                    
                    // Navega para MainActivity
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        });
        
        // Observa mensagens de erro
        authViewModel.getErrorMessage().observe(this, error -> {
            if (error != null && !error.isEmpty()) {
                Toast.makeText(this, error, Toast.LENGTH_LONG).show();
            }
        });
    }
    
    private void attemptRegister() {
        // Limpa erros anteriores
        clearErrors();
        
        String username = editUsername.getText().toString().trim();
        String name = editName.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString();
        String confirmPassword = editConfirmPassword.getText().toString();
        
        // Validações
        boolean isValid = true;
        
        if (!ValidationUtils.isNotEmpty(username)) {
            editUsername.setError(getString(R.string.error_empty_field));
            isValid = false;
        } else if (!ValidationUtils.isValidUsername(username)) {
            editUsername.setError("Nome de utilizador inválido (3-20 caracteres alfanuméricos)");
            isValid = false;
        }
        
        if (!ValidationUtils.isNotEmpty(name)) {
            editName.setError(getString(R.string.error_empty_field));
            isValid = false;
        }
        
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
        
        if (!ValidationUtils.isNotEmpty(confirmPassword)) {
            editConfirmPassword.setError(getString(R.string.error_empty_field));
            isValid = false;
        } else if (!password.equals(confirmPassword)) {
            editConfirmPassword.setError(getString(R.string.error_passwords_dont_match));
            isValid = false;
        }
        
        if (isValid) {
            authViewModel.register(username, email, password, name);
        }
    }
    
    private void clearErrors() {
        editUsername.setError(null);
        editName.setError(null);
        editEmail.setError(null);
        editPassword.setError(null);
        editConfirmPassword.setError(null);
    }
    
    private void setFieldsEnabled(boolean enabled) {
        editUsername.setEnabled(enabled);
        editName.setEnabled(enabled);
        editEmail.setEnabled(enabled);
        editPassword.setEnabled(enabled);
        editConfirmPassword.setEnabled(enabled);
    }
}
