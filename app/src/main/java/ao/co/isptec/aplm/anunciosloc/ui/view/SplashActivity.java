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
import ao.co.isptec.aplm.anunciosloc.data.model.User;
import ao.co.isptec.aplm.anunciosloc.data.repository.UserRepository;
import ao.co.isptec.aplm.anunciosloc.ui.view.LoginActivity;
import ao.co.isptec.aplm.anunciosloc.utils.Constants;
import ao.co.isptec.aplm.anunciosloc.utils.PreferencesHelper;

/**
 * Splash Screen - Tela de apresentação do aplicativo
 */
public class SplashActivity extends AppCompatActivity {
    
    private PreferencesHelper preferencesHelper;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        
        // Esconde a ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        
        preferencesHelper = new PreferencesHelper(this);

        TextView title = findViewById(R.id.textTitle);
        TextView subtitle = findViewById(R.id.textSubtitle);

        // Animações
        Animation fadeIn = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        fadeIn.setDuration(1000);
        title.startAnimation(fadeIn);
        subtitle.startAnimation(fadeIn);

        // Aguarda o tempo do splash e navega para a próxima tela
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            navigateToNextScreen();
        }, Constants.SPLASH_DELAY);
    }
    
    /**
     * Navega para a tela apropriada baseado no estado de login
     */
    private void navigateToNextScreen() {
        Intent intent;
        
        try {
            // Verifica se há sessão salva
            if (preferencesHelper.isUserLoggedIn()) {
                String userEmail = preferencesHelper.getUserEmail();
                
                // Valida se tem email
                if (userEmail != null && !userEmail.isEmpty()) {
                    // Reconstitui o usuário no repository
                    UserRepository userRepository = UserRepository.getInstance();
                    User user = userRepository.getUserByEmail(userEmail);
                    
                    if (user != null) {
                        userRepository.setCurrentUser(user);
                        // Vai para MainActivity
                        intent = new Intent(SplashActivity.this, MainActivity.class);
                    } else {
                        // Usuário não encontrado, limpa sessão e vai para Login
                        preferencesHelper.clearUserSession();
                        intent = new Intent(SplashActivity.this, LoginActivity.class);
                    }
                } else {
                    // Email inválido, limpa sessão e vai para Login
                    preferencesHelper.clearUserSession();
                    intent = new Intent(SplashActivity.this, LoginActivity.class);
                }
            } else {
                // Não há sessão, vai para Login
                intent = new Intent(SplashActivity.this, LoginActivity.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Em caso de erro, limpa sessão e vai para login
            try {
                preferencesHelper.clearUserSession();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            intent = new Intent(SplashActivity.this, LoginActivity.class);
        }
        
        startActivity(intent);
        finish();
    }
}
