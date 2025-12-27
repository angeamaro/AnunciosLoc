package ao.co.isptec.aplm.anunciosloc.ui.view;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import ao.co.isptec.aplm.anunciosloc.R;
import ao.co.isptec.aplm.anunciosloc.data.model.P2PAnnouncement;
import ao.co.isptec.aplm.anunciosloc.ui.adapter.P2PDeviceAdapter;
import ao.co.isptec.aplm.anunciosloc.utils.P2PAnnouncementManager;

/**
 * Tela de Descoberta de Dispositivos WiFi Direct
 * Permite selecionar dispositivo para retransmitir anúncio
 */
public class P2PDiscoveryActivity extends AppCompatActivity implements P2PDeviceAdapter.OnDeviceSelectListener {

    public static final String EXTRA_ANNOUNCEMENT_ID = "announcement_id";
    private static final int PERMISSION_REQUEST_CODE = 1001;

    private RecyclerView rvDevices;
    private TextView tvEmptyState, tvScanningStatus;
    private MaterialButton btnStartScan, btnConfirmTransmit;
    
    private P2PDeviceAdapter deviceAdapter;
    private List<P2PDevice> discoveredDevices;
    private P2PDevice selectedDevice;
    
    private P2PAnnouncement announcement;
    private P2PAnnouncementManager announcementManager;
    
    private boolean isScanning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p2p_discovery);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        announcementManager = P2PAnnouncementManager.getInstance(this);
        
        String announcementId = getIntent().getStringExtra(EXTRA_ANNOUNCEMENT_ID);
        if (announcementId == null) {
            Toast.makeText(this, "Erro: Anúncio não encontrado", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        announcement = announcementManager.getAnnouncementById(announcementId);
        if (announcement == null) {
            Toast.makeText(this, "Erro: Anúncio não encontrado", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        discoveredDevices = new ArrayList<>();
        
        initializeViews();
        checkPermissions();
    }

    private void initializeViews() {
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        rvDevices = findViewById(R.id.rvDevices);
        tvEmptyState = findViewById(R.id.tvEmptyState);
        tvScanningStatus = findViewById(R.id.tvScanningStatus);
        btnStartScan = findViewById(R.id.btnStartScan);
        btnConfirmTransmit = findViewById(R.id.btnConfirmTransmit);

        // RecyclerView
        deviceAdapter = new P2PDeviceAdapter(discoveredDevices, this);
        rvDevices.setLayoutManager(new LinearLayoutManager(this));
        rvDevices.setAdapter(deviceAdapter);

        btnStartScan.setOnClickListener(v -> startWiFiDirectScan());
        btnConfirmTransmit.setOnClickListener(v -> confirmTransmission());
        btnConfirmTransmit.setEnabled(false);
    }

    private void checkPermissions() {
        // Permissões necessárias para WiFi Direct
        String[] permissions = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.CHANGE_WIFI_STATE,
            Manifest.permission.NEARBY_WIFI_DEVICES // Android 13+
        };

        List<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(permission);
            }
        }

        if (!permissionsToRequest.isEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                permissionsToRequest.toArray(new String[0]),
                PERMISSION_REQUEST_CODE
            );
        }
    }

    private void startWiFiDirectScan() {
        if (isScanning) {
            stopWiFiDirectScan();
            return;
        }

        isScanning = true;
        btnStartScan.setText("Parar Busca");
        tvScanningStatus.setText("🔍 Procurando dispositivos próximos...");
        tvScanningStatus.setVisibility(TextView.VISIBLE);
        
        // Simula descoberta de dispositivos (implementação real usaria WiFiP2pManager)
        simulateDeviceDiscovery();
    }

    private void stopWiFiDirectScan() {
        isScanning = false;
        btnStartScan.setText("Iniciar Busca");
        tvScanningStatus.setVisibility(TextView.GONE);
    }

    /**
     * SIMULAÇÃO: Em produção, usar WiFiP2pManager.discoverPeers()
     * e registrar BroadcastReceiver para WIFI_P2P_PEERS_CHANGED_ACTION
     */
    private void simulateDeviceDiscovery() {
        // Limpa lista antiga
        discoveredDevices.clear();
        
        // Simula encontrar 3 dispositivos
        btnStartScan.postDelayed(() -> {
            if (isScanning) {
                P2PDevice device1 = new P2PDevice("device_abc123", "Smartphone João", "Available");
                discoveredDevices.add(device1);
                deviceAdapter.notifyItemInserted(0);
                updateEmptyState();
            }
        }, 1000);

        btnStartScan.postDelayed(() -> {
            if (isScanning) {
                P2PDevice device2 = new P2PDevice("device_def456", "Tablet Maria", "Available");
                discoveredDevices.add(device2);
                deviceAdapter.notifyItemInserted(1);
            }
        }, 2000);

        btnStartScan.postDelayed(() -> {
            if (isScanning) {
                P2PDevice device3 = new P2PDevice("device_ghi789", "Laptop Pedro", "Connected");
                discoveredDevices.add(device3);
                deviceAdapter.notifyItemInserted(2);
                
                stopWiFiDirectScan();
                tvScanningStatus.setText("✅ Busca concluída - 3 dispositivos encontrados");
            }
        }, 3000);
    }

    private void updateEmptyState() {
        if (discoveredDevices.isEmpty()) {
            tvEmptyState.setVisibility(TextView.VISIBLE);
            rvDevices.setVisibility(RecyclerView.GONE);
        } else {
            tvEmptyState.setVisibility(TextView.GONE);
            rvDevices.setVisibility(RecyclerView.VISIBLE);
        }
    }

    @Override
    public void onDeviceSelected(P2PDevice device) {
        selectedDevice = device;
        btnConfirmTransmit.setEnabled(true);
        btnConfirmTransmit.setText("Transmitir para " + device.getDeviceName());
    }

    private void confirmTransmission() {
        if (selectedDevice == null) {
            Toast.makeText(this, "Selecione um dispositivo", Toast.LENGTH_SHORT).show();
            return;
        }

        // Simula transmissão bem-sucedida
        simulateTransmission();
    }

    /**
     * SIMULAÇÃO: Em produção, usar WiFiP2pManager.connect() 
     * e transferir dados via Socket após conexão estabelecida
     */
    private void simulateTransmission() {
        btnConfirmTransmit.setEnabled(false);
        btnConfirmTransmit.setText("Transmitindo...");

        btnConfirmTransmit.postDelayed(() -> {
            // Incrementa contador de retransmissões
            announcementManager.incrementRetransmissionCount(announcement.getId());
            
            // Marca como não pendente (já foi transmitido)
            announcement.setPendingRetransmission(false);
            announcementManager.saveAnnouncement(announcement);

            Toast.makeText(this, 
                "✅ Anúncio retransmitido com sucesso para " + selectedDevice.getDeviceName(), 
                Toast.LENGTH_LONG).show();
            
            // Fecha activity e volta para lista
            finish();
        }, 2000);
    }

    /**
     * Classe interna representando um dispositivo WiFi Direct
     * Em produção, usar WifiP2pDevice da API Android
     */
    public static class P2PDevice {
        private String deviceId;
        private String deviceName;
        private String status;
        private boolean isSelected;

        public P2PDevice(String deviceId, String deviceName, String status) {
            this.deviceId = deviceId;
            this.deviceName = deviceName;
            this.status = status;
            this.isSelected = false;
        }

        public String getDeviceId() { return deviceId; }
        public String getDeviceName() { return deviceName; }
        public String getStatus() { return status; }
        public boolean isSelected() { return isSelected; }
        public void setSelected(boolean selected) { isSelected = selected; }
    }
}
