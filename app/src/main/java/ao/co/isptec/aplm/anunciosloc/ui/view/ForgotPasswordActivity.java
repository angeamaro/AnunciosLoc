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

/**
 * Tela de Recuperação de Senha
 */
public class ForgotPasswordActivity extends AppCompatActivity {
    
    private EditText editEmail;
    private Button btnSendEmail;
    private TextView txtBackToLogin;
    private ProgressBar progressBar;
    
    private AuthViewModel authViewModel;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        
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
        btnSendEmail = findViewById(R.id.btnSendEmail);
        txtBackToLogin = findViewById(R.id.txtBackToLogin);
        progressBar = findViewById(R.id.progressBar);
    }
    
    private void initializeViewModel() {
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
    }
    
    private void setupListeners() {
        btnSendEmail.setOnClickListener(v -> attemptPasswordRecovery());
        
        txtBackToLogin.setOnClickListener(v -> {
            finish(); // Volta para LoginActivity
        });
    }
    
    private void observeViewModel() {
        // Observa estado de carregamento
        authViewModel.getIsLoading().observe(this, isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            btnSendEmail.setEnabled(!isLoading);
            editEmail.setEnabled(!isLoading);
        });
        
        // Observa sucesso de recuperação
        authViewModel.getPasswordRecoverySuccess().observe(this, success -> {
            if (success != null && success) {
                Toast.makeText(this, R.string.success_password_recovery, Toast.LENGTH_LONG).show();
                
                // Volta para Login após 2 segundos
                editEmail.postDelayed(() -> {
                    finish();
                }, 2000);
            }
        });
        
        // Observa mensagens de erro
        authViewModel.getErrorMessage().observe(this, error -> {
            if (error != null && !error.isEmpty()) {
                Toast.makeText(this, error, Toast.LENGTH_LONG).show();
            }
        });
    }
    
    private void attemptPasswordRecovery() {
        // Limpa erros anteriores
        editEmail.setError(null);
        
        String email = editEmail.getText().toString().trim();
        
        // Validações
        boolean isValid = true;
        
        if (!ValidationUtils.isNotEmpty(email)) {
            editEmail.setError(getString(R.string.error_empty_field));
            isValid = false;
        } else if (!ValidationUtils.isValidEmail(email)) {
            editEmail.setError(getString(R.string.error_invalid_email));
            isValid = false;
        }
        
        if (isValid) {
            authViewModel.recoverPassword(email);
        }
    }
}
