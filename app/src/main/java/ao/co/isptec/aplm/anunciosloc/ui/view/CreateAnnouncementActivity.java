package ao.co.isptec.aplm.anunciosloc.ui.view;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import ao.co.isptec.aplm.anunciosloc.R;
import ao.co.isptec.aplm.anunciosloc.data.model.PolicyFilter;
import ao.co.isptec.aplm.anunciosloc.ui.viewmodel.AnnouncementViewModel;

public class CreateAnnouncementActivity extends AppCompatActivity {

    private EditText editTitle, editContent;
    private Button btnSave;
    private AnnouncementViewModel announcementViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_announcement);

        announcementViewModel = new ViewModelProvider(this).get(AnnouncementViewModel.class);

        editTitle = findViewById(R.id.editTitle);
        editContent = findViewById(R.id.editContent);
        btnSave = findViewById(R.id.btnSave);

        btnSave.setOnClickListener(v -> {
            String title = editTitle.getText().toString();
            String content = editContent.getText().toString();
            // Dados de exemplo, a serem substituídos pela lógica real
            announcementViewModel.createAnnouncement(title, content, "location1", System.currentTimeMillis(), System.currentTimeMillis() + 86400000, "EVERYONE", new PolicyFilter(), "STANDARD");
        });
    }
}
