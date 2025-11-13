package ao.co.isptec.aplm.anunciosloc.ui.view;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import ao.co.isptec.aplm.anunciosloc.R;
import ao.co.isptec.aplm.anunciosloc.ui.view.fragment.LocationsFragment;

public class LocalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local);

        // Se ainda não há fragmento carregado, carrega o LocationsFragment
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, new LocationsFragment())
                    .commit();
        }
    }
}

