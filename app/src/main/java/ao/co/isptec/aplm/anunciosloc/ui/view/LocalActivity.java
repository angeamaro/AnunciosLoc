package ao.co.isptec.aplm.anunciosloc.ui.view;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import ao.co.isptec.aplm.anunciosloc.R;
import ao.co.isptec.aplm.anunciosloc.ui.view.fragment.LocalFragment;

public class LocalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local);

        // Carrega LocalFragment dentro do container
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, new LocalFragment())
                    .commit();
        }
    }
}
