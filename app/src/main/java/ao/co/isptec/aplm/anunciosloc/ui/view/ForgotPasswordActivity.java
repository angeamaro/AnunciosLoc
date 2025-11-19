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
        authViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(AuthViewModel.class);
    }
    
    private void setupListeners() {
        btnSendEmail.setOnClickListener(v -> attemptPasswordRecovery());
        txtBackToLogin.setOnClickListener(v -> finish());
    }
    
    private void observeViewModel() {
        // A lógica de recuperação de senha foi removida do ViewModel, 
        // então os observadores correspondentes são desnecessários por agora.
    }
    
    private void attemptPasswordRecovery() {
        String email = editEmail.getText().toString().trim();
        if (ValidationUtils.isValidEmail(email)) {
            Toast.makeText(this, "Funcionalidade em desenvolvimento", Toast.LENGTH_SHORT).show();
        } else {
            editEmail.setError("Email inválido");
        }
    }
}
