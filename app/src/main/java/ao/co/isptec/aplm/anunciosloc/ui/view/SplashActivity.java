package ao.co.isptec.aplm.anunciosloc.ui.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import ao.co.isptec.aplm.anunciosloc.R;
import ao.co.isptec.aplm.anunciosloc.utils.Constants;
import ao.co.isptec.aplm.anunciosloc.utils.PreferencesHelper;

/**
 * Splash Screen - Tela de apresentação do aplicativo
 */
public class SplashActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        
        // Esconde a ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        TextView title = findViewById(R.id.textTitle);
        TextView subtitle = findViewById(R.id.textSubtitle);

        // Animações
        Animation fadeIn = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        fadeIn.setDuration(1000);
        title.startAnimation(fadeIn);
        subtitle.startAnimation(fadeIn);

        // Aguarda o tempo do splash e navega para a próxima tela
        new Handler(Looper.getMainLooper()).postDelayed(this::navigateToNextScreen, Constants.SPLASH_DELAY);
    }
    
    /**
     * Navega para a tela apropriada baseado no estado de login (presença de um token)
     */
    private void navigateToNextScreen() {
        Intent intent;
        
        // Usa o método estático para verificar se o usuário está logado
        if (PreferencesHelper.isLoggedIn(this)) {
            // Se há um token, vai para a tela principal
            intent = new Intent(SplashActivity.this, MainActivity.class);
        } else {
            // Se não há token, vai para a tela de login
            intent = new Intent(SplashActivity.this, LoginActivity.class);
        }
        
        startActivity(intent);
        finish();
    }
}
