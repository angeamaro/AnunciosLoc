package ao.co.isptec.aplm.anunciosloc.ui.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.Locale;

import ao.co.isptec.aplm.anunciosloc.R;
import ao.co.isptec.aplm.anunciosloc.data.model.P2PAnnouncement;
import ao.co.isptec.aplm.anunciosloc.utils.P2PAnnouncementManager;

/**
 * Tela de Detalhes Completos de Anúncio P2P
 * Mostra todas informações do anúncio, mapa, autenticidade, etc.
 */
public class P2PAnnouncementDetailActivity extends AppCompatActivity {

    public static final String EXTRA_ANNOUNCEMENT_ID = "announcement_id";

    private TextView tvTitle, tvDescription, tvLocationName, tvReceivedTime;
    private TextView tvSenderUsername, tvDeviceId, tvReceivedType, tvRetransmitCount;
    private TextView tvWindowStart, tvWindowEnd, tvStatus;
    private ImageView ivAuthentic, ivMapPreview;
    private CardView cardRetransmitInfo, cardAuthentic;
    private MaterialButton btnRetransmit, btnViewOnMap;
    
    private P2PAnnouncement announcement;
    private P2PAnnouncementManager announcementManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p2p_announcement_detail);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        announcementManager = P2PAnnouncementManager.getInstance(this);
        
        String announcementId = getIntent().getStringExtra(EXTRA_ANNOUNCEMENT_ID);
        if (announcementId == null) {
            finish();
            return;
        }

        announcement = announcementManager.getAnnouncementById(announcementId);
        if (announcement == null) {
            finish();
            return;
        }

        initializeViews();
        displayAnnouncementDetails();
    }

    private void initializeViews() {
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        tvTitle = findViewById(R.id.tvTitle);
        tvDescription = findViewById(R.id.tvDescription);
        tvLocationName = findViewById(R.id.tvLocationName);
        tvReceivedTime = findViewById(R.id.tvReceivedTime);
        tvSenderUsername = findViewById(R.id.tvSenderUsername);
        tvDeviceId = findViewById(R.id.tvDeviceId);
        tvReceivedType = findViewById(R.id.tvReceivedType);
        tvRetransmitCount = findViewById(R.id.tvRetransmitCount);
        tvWindowStart = findViewById(R.id.tvWindowStart);
        tvWindowEnd = findViewById(R.id.tvWindowEnd);
        tvStatus = findViewById(R.id.tvStatus);
        
        ivAuthentic = findViewById(R.id.ivAuthentic);
        ivMapPreview = findViewById(R.id.ivMapPreview);
        
        cardRetransmitInfo = findViewById(R.id.cardRetransmitInfo);
        cardAuthentic = findViewById(R.id.cardAuthentic);
        
        btnRetransmit = findViewById(R.id.btnRetransmit);
        btnViewOnMap = findViewById(R.id.btnViewOnMap);

        btnRetransmit.setOnClickListener(v -> startRetransmission());
        btnViewOnMap.setOnClickListener(v -> openMapView());
    }

    private void displayAnnouncementDetails() {
        // Informações Principais
        tvTitle.setText(announcement.getTitle());
        tvDescription.setText(announcement.getDescription());
        tvLocationName.setText(announcement.getLocationName());

        // Tempo Relativo
        CharSequence relativeTime = DateUtils.getRelativeTimeSpanString(
            announcement.getReceivedAt().getTime(),
            System.currentTimeMillis(),
            DateUtils.MINUTE_IN_MILLIS
        );
        tvReceivedTime.setText("Recebido " + relativeTime);

        // Informações do Remetente
        tvSenderUsername.setText(announcement.getSenderUsername());
        tvDeviceId.setText("ID: " + announcement.getSenderDeviceId());

        // Tipo de Recebimento
        if (announcement.getReceivedType() == P2PAnnouncement.ReceivedType.DIRECT) {
            tvReceivedType.setText("📡 Recebido Diretamente");
            tvReceivedType.setTextColor(getResources().getColor(R.color.blue_600, null));
            cardRetransmitInfo.setVisibility(View.GONE);
            btnRetransmit.setVisibility(View.GONE);
        } else {
            tvReceivedType.setText("🐴 Via Mula (Retransmissão)");
            tvReceivedType.setTextColor(getResources().getColor(R.color.orange_600, null));
            tvRetransmitCount.setText("Retransmissões: " + announcement.getRetransmissionCount());
            cardRetransmitInfo.setVisibility(View.VISIBLE);
            
            // Mostra botão apenas se pendente de retransmissão
            if (announcement.isPendingRetransmission()) {
                btnRetransmit.setVisibility(View.VISIBLE);
                btnRetransmit.setText("Retransmitir Agora");
            } else {
                btnRetransmit.setVisibility(View.GONE);
            }
        }

        // Autenticidade
        if (announcement.isAuthentic()) {
            cardAuthentic.setVisibility(View.VISIBLE);
            ivAuthentic.setImageResource(R.drawable.ic_verified);
        } else {
            cardAuthentic.setVisibility(View.GONE);
        }

        // Janela Temporal
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        tvWindowStart.setText("Início: " + dateFormat.format(announcement.getWindowStart()));
        tvWindowEnd.setText("Fim: " + dateFormat.format(announcement.getWindowEnd()));

        // Status
        if (announcement.isActive()) {
            tvStatus.setText("🟢 Ativo");
            tvStatus.setTextColor(getResources().getColor(R.color.green_600, null));
        } else {
            tvStatus.setText("🔴 Expirado");
            tvStatus.setTextColor(getResources().getColor(R.color.red_600, null));
        }

        // Mapa Preview (placeholder - pode implementar com Google Maps Static API)
        // ivMapPreview.setImageResource(R.drawable.placeholder_map);
    }

    private void startRetransmission() {
        Intent intent = new Intent(this, P2PDiscoveryActivity.class);
        intent.putExtra(P2PDiscoveryActivity.EXTRA_ANNOUNCEMENT_ID, announcement.getId());
        startActivity(intent);
    }

    private void openMapView() {
        // Abre mapa com localização do anúncio
        // Pode usar Google Maps Intent ou Activity própria
        Intent intent = new Intent(this, LocationDetailsActivity.class);
        intent.putExtra("latitude", announcement.getLatitude());
        intent.putExtra("longitude", announcement.getLongitude());
        intent.putExtra("locationName", announcement.getLocationName());
        startActivity(intent);
    }
}
