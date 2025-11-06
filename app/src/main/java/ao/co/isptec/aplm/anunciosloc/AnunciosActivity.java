package ao.co.isptec.aplm.anunciosloc;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;



public class AnunciosActivity extends AppCompatActivity {

    private TextView buttonGuardados;
    private TextView buttonMeusAnuncios;
    private LinearLayout emptyStateSaved;
    private LinearLayout emptyStateMyAds;
    private ImageView buttonMoreOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anuncios);

        // Encontra as views do layout pelo seu ID
        buttonGuardados = findViewById(R.id.button_guardados);
        buttonMeusAnuncios = findViewById(R.id.button_meus_anuncios);
        emptyStateSaved = findViewById(R.id.empty_state_saved);
        emptyStateMyAds = findViewById(R.id.empty_state_my_ads);
        buttonMoreOptions = findViewById(R.id.button_more_options);

        // Define o estado inicial para "Meus Anúncios" como padrão
        selectMeusAnuncios();

        // Configura os listeners de clique para as abas
        buttonGuardados.setOnClickListener(v -> selectGuardados());
        buttonMeusAnuncios.setOnClickListener(v -> selectMeusAnuncios());

        // Configura o listener para o botão de mais opções
        buttonMoreOptions.setOnClickListener(this::showPopupMenu);
    }

    private void showPopupMenu(View view) {
        PopupMenu popup = new PopupMenu(this, view);
        popup.getMenuInflater().inflate(R.menu.options_menu, popup.getMenu());

        // Configura o que acontece ao clicar num item do menu
        popup.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.menu_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
                return true;
            } else if (itemId == R.id.menu_settings) {
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            } else if (itemId == R.id.menu_interests) {
                Toast.makeText(this, "Interesses clicado", Toast.LENGTH_SHORT).show();
                return true;
            } else if (itemId == R.id.menu_policy) {
                Toast.makeText(this, "Políticas clicado", Toast.LENGTH_SHORT).show();
                return true;
            } else if (itemId == R.id.menu_logout) {
                // Lógica para sair, por exemplo, voltar para a tela de login
                Intent intent = new Intent(this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                return true;
            }
            return false;
        });

        popup.show();
    }

    private void selectGuardados() {
        // Atualiza a aparência dos botões
        buttonGuardados.setBackground(ContextCompat.getDrawable(this, R.drawable.segmented_button_selected));
        buttonGuardados.setTextColor(ContextCompat.getColor(this, R.color.blue));
        buttonMeusAnuncios.setBackground(null);
        buttonMeusAnuncios.setTextColor(Color.WHITE);

        // Atualiza a visibilidade dos estados vazios
        emptyStateSaved.setVisibility(View.VISIBLE);
        emptyStateMyAds.setVisibility(View.GONE);
    }

    private void selectMeusAnuncios() {
        // Atualiza a aparência dos botões
        buttonMeusAnuncios.setBackground(ContextCompat.getDrawable(this, R.drawable.segmented_button_selected));
        buttonMeusAnuncios.setTextColor(ContextCompat.getColor(this, R.color.blue));
        buttonGuardados.setBackground(null);
        buttonGuardados.setTextColor(Color.WHITE);

        // Atualiza a visibilidade dos estados vazios
        emptyStateMyAds.setVisibility(View.VISIBLE);
        emptyStateSaved.setVisibility(View.GONE);
    }
}
