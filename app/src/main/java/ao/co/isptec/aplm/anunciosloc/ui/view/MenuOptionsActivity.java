package ao.co.isptec.aplm.anunciosloc.ui.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;

import ao.co.isptec.aplm.anunciosloc.MainActivity;
import ao.co.isptec.aplm.anunciosloc.R;
import ao.co.isptec.aplm.anunciosloc.activities.InterestsActivity;
import ao.co.isptec.aplm.anunciosloc.ui.view.activities.PoliciesActivity;
import ao.co.isptec.aplm.anunciosloc.utils.PreferencesHelper;

/**
 * MenuOptionsActivity - Tela de menu com todas as opções do app
 */
public class MenuOptionsActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private MaterialCardView cardPerfil, cardDefinicoes, cardInteresses, cardPoliticas, cardSair;
    private PreferencesHelper preferencesHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_options);

        // Esconde ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        preferencesHelper = new PreferencesHelper(this);

        initializeViews();
        setupListeners();
    }

    private void initializeViews() {
        btnBack = findViewById(R.id.btnBack);
        
        // Conta
        cardPerfil = findViewById(R.id.cardPerfil);
        cardDefinicoes = findViewById(R.id.cardDefinicoes);
        cardInteresses = findViewById(R.id.cardInteresses);
        cardPoliticas = findViewById(R.id.cardPoliticas);
        cardSair = findViewById(R.id.cardSair);
    }

    private void setupListeners() {
        // Botão voltar
        btnBack.setOnClickListener(v -> finish());

        // Conta
        cardPerfil.setOnClickListener(v -> {
            Toast.makeText(this, "Perfil - Em desenvolvimento", Toast.LENGTH_SHORT).show();
        });

        cardDefinicoes.setOnClickListener(v -> {
            Toast.makeText(this, "Definições - Em desenvolvimento", Toast.LENGTH_SHORT).show();
        });

        cardInteresses.setOnClickListener(v -> {
            Intent intent = new Intent(this, InterestsActivity.class);
            startActivity(intent);
        });

        cardPoliticas.setOnClickListener(v -> {
            Intent intent = new Intent(this, PoliciesActivity.class);
            startActivity(intent);
        });

        cardSair.setOnClickListener(v -> {
            logout();
        });
    }

    private void logout() {
        // Limpa dados do usuário
        preferencesHelper.clearUserSession();
        
        // Redireciona para Login
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
