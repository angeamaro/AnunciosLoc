package ao.co.isptec.aplm.anunciosloc.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import ao.co.isptec.aplm.anunciosloc.R;
import ao.co.isptec.aplm.anunciosloc.ui.view.activities.PoliciesActivity;

public class SettingsActivity extends AppCompatActivity {

    private ImageView imageViewBack;
    private TextView textViewBack;
    private TextView linkKnowMoreMula;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Inicializa as views
        imageViewBack = findViewById(R.id.imageView_back);
        textViewBack = findViewById(R.id.textView_back);
        linkKnowMoreMula = findViewById(R.id.link_know_more_mula);

        // Listener para fechar a atividade
        View.OnClickListener backClickListener = v -> finish();

        // Aplica o mesmo listener a ambos os botões de voltar
        imageViewBack.setOnClickListener(backClickListener);
        textViewBack.setOnClickListener(backClickListener);

        // Listener para o link "Saiba mais"
        linkKnowMoreMula.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, PoliciesActivity.class);
            startActivity(intent);
        });
    }
}
