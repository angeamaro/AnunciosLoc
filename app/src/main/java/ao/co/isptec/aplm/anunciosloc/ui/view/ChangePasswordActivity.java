package ao.co.isptec.aplm.anunciosloc.ui.view;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import ao.co.isptec.aplm.anunciosloc.R;

public class ChangePasswordActivity extends AppCompatActivity {

    private ImageView imageViewBack;
    private TextView textViewBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        // Inicializa as views
        imageViewBack = findViewById(R.id.imageView_back);
        textViewBack = findViewById(R.id.textView_back);

        // Listener para fechar a atividade
        View.OnClickListener backClickListener = v -> finish();

        // Aplica o mesmo listener a ambos os botões
        imageViewBack.setOnClickListener(backClickListener);
        textViewBack.setOnClickListener(backClickListener);
    }
}
