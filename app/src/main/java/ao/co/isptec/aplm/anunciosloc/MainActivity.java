package ao.co.isptec.aplm.anunciosloc;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

import ao.co.isptec.aplm.anunciosloc.adapters.AnnouncementAdapter;
import ao.co.isptec.aplm.anunciosloc.models.Announcement;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AnnouncementAdapter adapter;
    private List<Announcement> announcementList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerAnnouncements);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 🔹 Criar lista de anúncios (dados de teste)
        announcementList = new ArrayList<>();
        announcementList.add(new Announcement(
                "Casa para alugar",
                "Casa T3 no Benfica com quintal amplo e garagem.",
                "Luanda",
                "📅 30/10/2025"
        ));
        announcementList.add(new Announcement(
                "Vendo laptop HP",
                "HP Pavilion, 16GB RAM, SSD 512GB, em ótimo estado.",
                "Talatona",
                "📅 29/10/2025"
        ));
        announcementList.add(new Announcement(
                "Procura-se Emprego",
                "Técnico de informática com 2 anos de experiência em suporte técnico.",
                "Kilamba",
                "📅 28/10/2025"
        ));

        // 🔹 Ligar o adapter
        adapter = new AnnouncementAdapter(announcementList);
        recyclerView.setAdapter(adapter);
    }
}
