package ao.co.isptec.aplm.anunciosloc.ui.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;

import java.util.List;

import ao.co.isptec.aplm.anunciosloc.R;
import ao.co.isptec.aplm.anunciosloc.data.model.P2PAnnouncement;
import ao.co.isptec.aplm.anunciosloc.ui.adapter.P2PAnnouncementAdapter;
import ao.co.isptec.aplm.anunciosloc.utils.P2PAnnouncementManager;

/**
 * Tela de Anúncios P2P Recebidos
 * Com tabs: Todos, Recebidos (Diretos), Mula (Para Retransmitir)
 */
public class P2PAnnouncementsActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private RecyclerView recyclerView;
    private LinearLayout emptyStateLayout;
    private TextView tvTotalCount, tvDirectCount, tvMuleCount;
    
    private P2PAnnouncementManager announcementManager;
    private P2PAnnouncementAdapter adapter;
    private List<P2PAnnouncement> currentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p2p_announcements);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        announcementManager = P2PAnnouncementManager.getInstance(this);

        initializeViews();
        setupTabs();
        setupRecyclerView();
        updateStatistics();
        loadAnnouncements(0); // Carrega "Todos" por padrão
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Atualiza quando voltar (pode ter retransmitido algo)
        updateStatistics();
        int currentTab = tabLayout.getSelectedTabPosition();
        loadAnnouncements(currentTab);
    }

    private void initializeViews() {
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        tabLayout = findViewById(R.id.tabLayout);
        recyclerView = findViewById(R.id.recyclerViewAnnouncements);
        emptyStateLayout = findViewById(R.id.emptyStateLayout);
        
        tvTotalCount = findViewById(R.id.tvTotalCount);
        tvDirectCount = findViewById(R.id.tvDirectCount);
        tvMuleCount = findViewById(R.id.tvMuleCount);
    }

    private void setupTabs() {
        tabLayout.addTab(tabLayout.newTab().setText("Todos"));
        tabLayout.addTab(tabLayout.newTab().setText("Recebidos"));
        tabLayout.addTab(tabLayout.newTab().setText("Mula"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                loadAnnouncements(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        
        adapter = new P2PAnnouncementAdapter(this, 
            announcement -> {
                // Click no Ver Detalhes - Mostrar detalhes completos
                showAnnouncementDetails(announcement);
            },
            announcement -> {
                // Click no Retransmitir - Ir para descoberta de dispositivos
                startRetransmission(announcement);
            }
        );
        
        recyclerView.setAdapter(adapter);
    }

    private void loadAnnouncements(int tabPosition) {
        switch (tabPosition) {
            case 0: // Todos
                currentList = announcementManager.getAllAnnouncements();
                break;
            case 1: // Recebidos (Diretos)
                currentList = announcementManager.getDirectAnnouncements();
                break;
            case 2: // Mula
                currentList = announcementManager.getMuleAnnouncements();
                break;
            default:
                currentList = announcementManager.getAllAnnouncements();
        }

        adapter.updateList(currentList);

        // Mostra empty state se lista vazia
        if (currentList.isEmpty()) {
            emptyStateLayout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            emptyStateLayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void updateStatistics() {
        tvTotalCount.setText(String.valueOf(announcementManager.getTotalCount()));
        tvDirectCount.setText(String.valueOf(announcementManager.getDirectCount()));
        tvMuleCount.setText(String.valueOf(announcementManager.getMuleCount()));
    }

    private void showAnnouncementDetails(P2PAnnouncement announcement) {
        Intent intent = new Intent(this, P2PAnnouncementDetailActivity.class);
        intent.putExtra("announcement", announcement);
        startActivity(intent);
    }

    private void startRetransmission(P2PAnnouncement announcement) {
        // Ir para tela de descoberta de dispositivos P2P
        Intent intent = new Intent(this, P2PDiscoveryActivity.class);
        intent.putExtra("announcement_id", announcement.getId());
        intent.putExtra("mode", "retransmit");
        startActivity(intent);
    }
}
