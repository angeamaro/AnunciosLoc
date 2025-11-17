package ao.co.isptec.aplm.anunciosloc.ui.view;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

import java.util.LinkedHashMap;

import ao.co.isptec.aplm.anunciosloc.R;

public class InterestsActivity extends AppCompatActivity {

    private EditText editChave, editValor;
    private GridLayout listaPares;
    private LinkedHashMap<String, String> pares = new LinkedHashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interests);

        // Inicializar views
        editChave = findViewById(R.id.editChave);
        editValor = findViewById(R.id.editValor);
        listaPares = findViewById(R.id.listaPares);
        Button btnSalvar = findViewById(R.id.btnSalvar);
        ImageView btnVoltar = findViewById(R.id.btnVoltar);

        // Botão voltar
        btnVoltar.setOnClickListener(v -> finish());

        // Botão salvar par chave-valor
        btnSalvar.setOnClickListener(v -> {
            String chave = editChave.getText().toString().trim();
            String valor = editValor.getText().toString().trim();

            if (!chave.isEmpty() && !valor.isEmpty()) {
                pares.put(chave, valor);
                atualizarLista();
                editChave.setText("");
                editValor.setText("");
            }
        });
    }

    private void atualizarLista() {
        listaPares.removeAllViews();

        for (String chave : pares.keySet()) {
            TextView chip = new TextView(this);
            chip.setText(chave + " : " + pares.get(chave));
            chip.setBackgroundResource(R.drawable.bg_item_par);
            chip.setPadding(24, 16, 24, 16);
            chip.setTextColor(getColor(android.R.color.black));
            chip.setTextSize(14);

            // GridLayout.LayoutParams correto
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 0; // distribuir igualmente
            params.height = GridLayout.LayoutParams.WRAP_CONTENT;
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            params.setMargins(8, 8, 8, 8);

            chip.setLayoutParams(params);

            listaPares.addView(chip);
        }
    }

    // Fecha o teclado quando clicas fora do EditText
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) ev.getRawX(), (int) ev.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }
}
