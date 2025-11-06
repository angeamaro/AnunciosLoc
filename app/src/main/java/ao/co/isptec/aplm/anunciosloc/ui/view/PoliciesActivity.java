package ao.co.isptec.aplm.anunciosloc.ui.view;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import ao.co.isptec.aplm.anunciosloc.R;

/**
 * Activity para exibir as políticas de utilização do aplicativo
 * Inclui: Termos de Uso, Privacidade, Responsabilidade e explicação sobre Mulas
 */
public class PoliciesActivity extends AppCompatActivity {

    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policies);

        initViews();
        setupListeners();
    }

    /**
     * Inicializa as views
     */
    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
    }

    /**
     * Configura os listeners
     */
    private void setupListeners() {
        // Botão voltar
        btnBack.setOnClickListener(v -> finish());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
