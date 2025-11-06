package ao.co.isptec.aplm.anunciosloc.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import ao.co.isptec.aplm.anunciosloc.R;

public class ProfileActivity extends AppCompatActivity {

    private RelativeLayout securitySection;
    private ImageView imageViewBack;
    private TextView textViewBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Inicializa as views
        securitySection = findViewById(R.id.security_section);
        imageViewBack = findViewById(R.id.imageView_back);
        textViewBack = findViewById(R.id.textView_back);

        // Configura o listener de clique para a seção de segurança
        securitySection.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, ChangePasswordActivity.class);
            startActivity(intent);
        });

        // Listener para fechar a atividade
        View.OnClickListener backClickListener = v -> finish();

        // Aplica o mesmo listener a ambos os botões de voltar
        imageViewBack.setOnClickListener(backClickListener);
        textViewBack.setOnClickListener(backClickListener);
    }
}
