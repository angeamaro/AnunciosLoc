package ao.co.isptec.aplm.anunciosloc.ui.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;

import ao.co.isptec.aplm.anunciosloc.R;
import ao.co.isptec.aplm.anunciosloc.utils.PreferencesHelper;

public class MenuOptionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_options);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        ImageButton btnBack = findViewById(R.id.btnBack);
        MaterialCardView cardPerfil = findViewById(R.id.cardPerfil);
        MaterialCardView cardSair = findViewById(R.id.cardSair);

        btnBack.setOnClickListener(v -> finish());
        cardPerfil.setOnClickListener(v -> startActivity(new Intent(this, ProfileActivity.class)));
        cardSair.setOnClickListener(v -> logout());
    }

    private void logout() {
        PreferencesHelper.clearSession(this);
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
