package ao.co.isptec.aplm.anunciosloc.ui.view;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.switchmaterial.SwitchMaterial;

import ao.co.isptec.aplm.anunciosloc.R;

public class SettingsActivity extends AppCompatActivity {

    private SwitchMaterial switchLocation, switchWiFi, switchNotifications, switchMulaMode;
    private TextView tvLocationStatus, tvNotificationStatus, tvSaibaMais;
    private Button btnSalvar;
    private LinearLayout btnBack;
    private SharedPreferences prefs;
    
    private static final int REQUEST_CODE_LOCATION = 100;
    private static final int REQUEST_CODE_NOTIFICATION = 101;
    private static final String PREFS_NAME = "AppSettings";
    private static final String KEY_LOCATION = "location_enabled";
    private static final String KEY_WIFI = "wifi_enabled";
    private static final String KEY_NOTIFICATIONS = "notifications_enabled";
    private static final String KEY_MULA_MODE = "mula_mode_enabled";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        initializeViews();
        loadSettings();
        setupListeners();
    }

    private void initializeViews() {
        btnBack = findViewById(R.id.btnBack);
        switchLocation = findViewById(R.id.switchLocation);
        switchWiFi = findViewById(R.id.switchWiFi);
        switchNotifications = findViewById(R.id.switchNotifications);
        switchMulaMode = findViewById(R.id.switchMulaMode);
        tvLocationStatus = findViewById(R.id.tvLocationStatus);
        tvNotificationStatus = findViewById(R.id.tvNotificationStatus);
        tvSaibaMais = findViewById(R.id.tvSaibaMais);
        btnSalvar = findViewById(R.id.btnSalvar);
    }

    private void loadSettings() {
        boolean locationEnabled = prefs.getBoolean(KEY_LOCATION, false);
        boolean wifiEnabled = prefs.getBoolean(KEY_WIFI, false);
        boolean notificationsEnabled = prefs.getBoolean(KEY_NOTIFICATIONS, false);
        boolean mulaMode = prefs.getBoolean(KEY_MULA_MODE, false);

        boolean hasLocationPermission = ContextCompat.checkSelfPermission(this, 
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        
        boolean hasNotificationPermission = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            hasNotificationPermission = ContextCompat.checkSelfPermission(this, 
                    Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED;
        }

        switchLocation.setChecked(hasLocationPermission && locationEnabled);
        switchWiFi.setChecked(wifiEnabled);
        switchNotifications.setChecked(hasNotificationPermission && notificationsEnabled);
        switchMulaMode.setChecked(mulaMode);

        updateLocationStatus(hasLocationPermission && locationEnabled);
        updateNotificationStatus(hasNotificationPermission && notificationsEnabled);
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        switchLocation.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                boolean hasPermission = ContextCompat.checkSelfPermission(SettingsActivity.this, 
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
                if (!hasPermission) {
                    requestLocationPermission();
                    switchLocation.setChecked(false);
                } else {
                    updateLocationStatus(true);
                }
            } else {
                updateLocationStatus(false);
            }
        });

        switchWiFi.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Toast.makeText(SettingsActivity.this, isChecked ? "Detecção Wi-Fi ativada" : "Detecção Wi-Fi desativada", 
                    Toast.LENGTH_SHORT).show();
        });

        switchNotifications.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                boolean hasPermission = true;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    hasPermission = ContextCompat.checkSelfPermission(SettingsActivity.this, 
                            Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED;
                }
                if (!hasPermission) {
                    requestNotificationPermission();
                    switchNotifications.setChecked(false);
                } else {
                    updateNotificationStatus(true);
                }
            } else {
                updateNotificationStatus(false);
            }
        });

        switchMulaMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            String message = isChecked ? 
                "Modo MULA ativado - Sistema de entrega baseado em localização" : 
                "Modo MULA desativado";
            Toast.makeText(SettingsActivity.this, message, Toast.LENGTH_SHORT).show();
        });

        tvSaibaMais.setOnClickListener(v -> {
            new AlertDialog.Builder(SettingsActivity.this)
                .setTitle("Sobre o Modo MULA")
                .setMessage("O modo MULA permite que você auxilie na distribuição de anúncios, " +
                        "atuando como um intermediário no sistema de comunicação baseado em localização. " +
                        "Você ajuda a retransmitir anúncios para outros usuários próximos.")
                .setPositiveButton("Entendi", null)
                .show();
        });

        btnSalvar.setOnClickListener(v -> saveAllSettings());
    }

    private void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, 
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            new AlertDialog.Builder(this)
                .setTitle("Permissão de Localização")
                .setMessage("Esta aplicação precisa de acesso à sua localização para mostrar anúncios relevantes próximos de si.")
                .setPositiveButton("Permitir", (dialog, which) -> {
                    ActivityCompat.requestPermissions(SettingsActivity.this, 
                        new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        }, REQUEST_CODE_LOCATION);
                })
                .setNegativeButton("Cancelar", null)
                .create()
                .show();
        } else {
            ActivityCompat.requestPermissions(this, 
                new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                }, REQUEST_CODE_LOCATION);
        }
    }

    private void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(this, 
                new String[]{Manifest.permission.POST_NOTIFICATIONS}, 
                REQUEST_CODE_NOTIFICATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, 
                                         @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                switchLocation.setChecked(true);
                updateLocationStatus(true);
                Toast.makeText(this, "Permissão de localização concedida", Toast.LENGTH_SHORT).show();
            } else {
                switchLocation.setChecked(false);
                updateLocationStatus(false);
                Toast.makeText(this, "Permissão de localização negada", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_CODE_NOTIFICATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                switchNotifications.setChecked(true);
                updateNotificationStatus(true);
                Toast.makeText(this, "Permissão de notificações concedida", Toast.LENGTH_SHORT).show();
            } else {
                switchNotifications.setChecked(false);
                updateNotificationStatus(false);
                Toast.makeText(this, "Permissão de notificações negada", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadSettings();
    }

    private void updateLocationStatus(boolean enabled) {
        tvLocationStatus.setText(enabled ? 
            "Usar coordenadas geográficas" : "Localização desativada");
    }

    private void updateNotificationStatus(boolean enabled) {
        tvNotificationStatus.setText(enabled ? 
            "Alertas de anúncios próximos" : "Notificações desativadas");
    }

    private void saveAllSettings() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(KEY_LOCATION, switchLocation.isChecked());
        editor.putBoolean(KEY_WIFI, switchWiFi.isChecked());
        editor.putBoolean(KEY_NOTIFICATIONS, switchNotifications.isChecked());
        editor.putBoolean(KEY_MULA_MODE, switchMulaMode.isChecked());
        editor.apply();

        Toast.makeText(this, "Definições guardadas com sucesso!", Toast.LENGTH_SHORT).show();
    }
}
