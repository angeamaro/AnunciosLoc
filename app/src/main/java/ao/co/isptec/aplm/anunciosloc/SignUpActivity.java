package ao.co.isptec.aplm.anunciosloc;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        ImageView imageViewBack = findViewById(R.id.imageView_back);
        Button buttonSignUp = findViewById(R.id.button_signup);

        // Listener para o botão de voltar
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Fecha a activity atual e volta para a anterior (Login)
                finish();
            }
        });

        // Listener para o botão de criar conta
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aqui, futuramente, você adicionaria a lógica para salvar os dados do usuário.
                // Por enquanto, apenas fecha a tela e volta para o Login, como solicitado.
                finish();
            }
        });
    }
}
