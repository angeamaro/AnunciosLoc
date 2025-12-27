package ao.co.isptec.aplm.anunciosloc.ui.view;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.Date;
import java.util.UUID;

import ao.co.isptec.aplm.anunciosloc.R;
import ao.co.isptec.aplm.anunciosloc.data.model.P2PAnnouncement;
import ao.co.isptec.aplm.anunciosloc.utils.P2PAnnouncementManager;

/**
 * Tela para Simular Recebimento de Anúncios P2P
 * Permite testar como Utilizador (direto) ou como Mula (retransmitir)
 */
public class SimulateP2PReceiveActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "p2p_announcements";
    private static final String CHANNEL_NAME = "Anúncios P2P";
    
    private SwitchMaterial switchReceiverMode, switchMuleMode;
    private MaterialButton btnSimulateReceive;
    private TextView tvExplanation;
    
    private P2PAnnouncementManager announcementManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simulate_p2p_receive);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        announcementManager = P2PAnnouncementManager.getInstance(this);
        createNotificationChannel();

        initializeViews();
        setupSwitches();
    }

    private void initializeViews() {
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        switchReceiverMode = findViewById(R.id.switchReceiverMode);
        switchMuleMode = findViewById(R.id.switchMuleMode);
        btnSimulateReceive = findViewById(R.id.btnSimulateReceive);
        tvExplanation = findViewById(R.id.tvExplanation);

        btnSimulateReceive.setOnClickListener(v -> simulateReceive());
    }

    private void setupSwitches() {
        // Garante que só um está ativo por vez
        switchReceiverMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                switchMuleMode.setChecked(false);
                tvExplanation.setText("Como Utilizador: Receberá anúncios destinados diretamente a você. " +
                    "Estes anúncios serão salvos localmente e você poderá visualizá-los a qualquer momento.");
            }
        });

        switchMuleMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                switchReceiverMode.setChecked(false);
                tvExplanation.setText("Como Mula: Receberá anúncios para retransmitir a outros dispositivos próximos. " +
                    "Você ajuda a entregar mensagens que não chegaram diretamente ao destinatário.");
            }
        });
    }

    private void simulateReceive() {
        boolean isReceiverMode = switchReceiverMode.isChecked();
        boolean isMuleMode = switchMuleMode.isChecked();

        if (!isReceiverMode && !isMuleMode) {
            Toast.makeText(this, "Selecione um modo de recebimento", Toast.LENGTH_SHORT).show();
            return;
        }

        if (isReceiverMode) {
            simulateDirectReceive();
        } else {
            simulateMuleReceive();
        }
    }

    private void simulateDirectReceive() {
        // Cria 2 anúncios mock como utilizador direto
        P2PAnnouncement announcement1 = createMockAnnouncement(
            "Oferta Especial de Café",
            "Café expresso por apenas 1€ até 18h! Venha experimentar nossos novos sabores.",
            "Café Central",
            -8.916667, 13.383333,
            "CafeBarista",
            P2PAnnouncement.ReceivedType.DIRECT
        );

        P2PAnnouncement announcement2 = createMockAnnouncement(
            "Meetup de Programadores",
            "Encontro informal de devs às 19h. Traga seu laptop e vamos codar juntos!",
            "Tech Hub Luanda",
            -8.838333, 13.234444,
            "DevCommunity",
            P2PAnnouncement.ReceivedType.DIRECT
        );

        announcementManager.saveAnnouncement(announcement1);
        announcementManager.saveAnnouncement(announcement2);

        showNotification("Novos Anúncios P2P!", 
            "Você recebeu 2 anúncios via WiFi Direct", 
            announcement1.getId());

        Toast.makeText(this, "✅ 2 anúncios recebidos como Utilizador!", Toast.LENGTH_LONG).show();
        
        // Redireciona para lista de anúncios
        navigateToP2PList(500);
    }

    private void simulateMuleReceive() {
        // Cria 2 anúncios mock para retransmitir
        P2PAnnouncement announcement1 = createMockAnnouncement(
            "Aula Aberta de Dança",
            "Aula gratuita de Kizomba sábado às 15h. Todos os níveis são bem-vindos!",
            "Centro Cultural",
            -8.815556, 13.230000,
            "DanceStudio",
            P2PAnnouncement.ReceivedType.VIA_MULE
        );
        announcement1.setPendingRetransmission(true);
        announcement1.setTargetDeviceId("device_abc123");

        P2PAnnouncement announcement2 = createMockAnnouncement(
            "Palestra sobre IA",
            "Discussão sobre o futuro da Inteligência Artificial na África. Entrada livre.",
            "Universidade ISPTEC",
            -8.906944, 13.186111,
            "ISPTEC_Events",
            P2PAnnouncement.ReceivedType.VIA_MULE
        );
        announcement2.setPendingRetransmission(true);
        announcement2.setTargetDeviceId("device_xyz789");

        announcementManager.saveAnnouncement(announcement1);
        announcementManager.saveAnnouncement(announcement2);

        showNotification("Nova Missão de Mula! 🐴", 
            "Você recebeu 2 anúncios para retransmitir", 
            announcement1.getId());

        Toast.makeText(this, "✅ 2 anúncios recebidos para retransmitir!", Toast.LENGTH_LONG).show();
        
        // Redireciona para lista de anúncios (tab Mula)
        navigateToP2PList(500);
    }

    private P2PAnnouncement createMockAnnouncement(String title, String description,
                                                    String locationName, double lat, double lng,
                                                    String sender, P2PAnnouncement.ReceivedType type) {
        P2PAnnouncement announcement = new P2PAnnouncement();
        announcement.setId(UUID.randomUUID().toString());
        announcement.setTitle(title);
        announcement.setDescription(description);
        announcement.setLocationName(locationName);
        announcement.setLatitude(lat);
        announcement.setLongitude(lng);
        announcement.setSenderUsername(sender);
        announcement.setSenderDeviceId("device_" + UUID.randomUUID().toString().substring(0, 8));
        announcement.setReceivedAt(new Date());
        announcement.setAuthentic(true); // Mock sempre autêntico
        announcement.setReceivedType(type);
        
        // Janela temporal: agora até 7 dias
        announcement.setWindowStart(new Date());
        announcement.setWindowEnd(new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000));
        
        return announcement;
    }

    private void showNotification(String title, String message, String announcementId) {
        NotificationManager notificationManager = 
            (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Intent para abrir lista de anúncios P2P ao clicar
        Intent intent = new Intent(this, P2PAnnouncementsActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
            this, 0, intent, 
            PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_wifi)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent);

        notificationManager.notify(System.currentTimeMillis(), (int) System.currentTimeMillis(), builder.build());
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Notificações de anúncios recebidos via P2P");

            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    private void navigateToP2PList(long delayMs) {
        // Pequeno delay para mostrar toast
        btnSimulateReceive.postDelayed(() -> {
            Intent intent = new Intent(this, P2PAnnouncementsActivity.class);
            startActivity(intent);
            finish();
        }, delayMs);
    }
}
