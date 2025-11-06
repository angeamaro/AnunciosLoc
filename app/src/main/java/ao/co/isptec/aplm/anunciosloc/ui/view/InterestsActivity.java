package ao.co.isptec.aplm.anunciosloc.ui.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import java.util.LinkedHashMap;

import ao.co.isptec.aplm.anunciosloc.R;

public class InterestsActivity extends AppCompatActivity {

    private EditText editChave, editValor;
    private LinearLayout listaPares;
    private LinkedHashMap<String, String> pares = new LinkedHashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interests);

        editChave = findViewById(R.id.editChave);
        editValor = findViewById(R.id.editValor);
        listaPares = findViewById(R.id.listaPares);
        Button btnSalvar = findViewById(R.id.btnSalvar);

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chave = editChave.getText().toString().trim();
                String valor = editValor.getText().toString().trim();

                if (!chave.isEmpty() && !valor.isEmpty()) {
                    pares.put(chave, valor);
                    atualizarLista();
                    editChave.setText("");
                    editValor.setText("");
                }
            }
        });
    }

    private void atualizarLista() {
        listaPares.removeAllViews();

        for (String chave : pares.keySet()) {
            TextView chip = new TextView(this);
            chip.setText(chave + " : " + pares.get(chave));
            chip.setBackgroundResource(R.drawable.edittext_bg);
            chip.setPadding(24, 16, 24, 16);
            chip.setTextColor(getColor(android.R.color.black));
            chip.setTextSize(14);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(8, 8, 8, 8);
            chip.setLayoutParams(params);
            listaPares.addView(chip);
        }
    }
}
