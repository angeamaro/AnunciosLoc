package ao.co.isptec.aplm.anunciosloc.ui.view;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.switchmaterial.SwitchMaterial;

import ao.co.isptec.aplm.anunciosloc.R;

public class SettingsActivity extends AppCompatActivity {

    private SwitchMaterial switchLocation, switchNotifications, switchMulaMode;
    private TextView tvLocationStatus, tvNotificationStatus;
    private SharedPreferences prefs;
    
    private static final int REQUEST_CODE_LOCATION = 100;
    private static final int REQUEST_CODE_NOTIFICATION = 101;
    private static final String PREFS_NAME = "AppSettings";
    private static final String KEY_LOCATION = "location_enabled";
    private static final String KEY_NOTIFICATIONS = "notifications_enabled";
    private static final String KEY_MULA_MODE = "mula_mode_enabled";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        initializeViews();
        loadSettings();
        setupPermissionSwitches();
    }

    private void initializeViews() {
        switchLocation = findViewById(R.id.switchLocation);
        switchNotifications = findViewById(R.id.switchNotifications);
        switchMulaMode = findViewById(R.id.switchMulaMode);
        tvLocationStatus = findViewById(R.id.tvLocationStatus);
        tvNotificationStatus = findViewById(R.id.tvNotificationStatus);

        MaterialCardView cardPolicies = findViewById(R.id.cardPolicies);
        cardPolicies.setOnClickListener(v -> 
            Toast.makeText(this, "Políticas de Privacidade - Em desenvolvimento", Toast.LENGTH_SHORT).show()
        );
    }

    private void loadSettings() {
        boolean mulaMode = prefs.getBoolean(KEY_MULA_MODE, false);
        switchMulaMode.setChecked(mulaMode);
    }

    private void setupPermissionSwitches() {
        setupLocationSwitch();
        setupNotificationSwitch();
        setupMulaModeSwitch();
    }

    private void setupLocationSwitch() {
        final boolean hasLocationPermission = ContextCompat.checkSelfPermission(this, 
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        
        switchLocation.setChecked(hasLocationPermission);
        updateLocationStatus(hasLocationPermission);

        switchLocation.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (!hasLocationPermission) {
                    requestLocationPermission();
                } else {
                    saveLocationSetting(true);
                }
            } else {
                if (hasLocationPermission) {
                    showPermissionDisableDialog("Localização", 
                        "Para desativar a permissão de localização, vá às Definições do sistema.");
                    switchLocation.setChecked(true);
                }
                saveLocationSetting(false);
            }
        });
    }

    private void setupNotificationSwitch() {
        boolean hasNotifPerm = true;
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            hasNotifPerm = ContextCompat.checkSelfPermission(this, 
                    Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED;
        }
        
        final boolean hasNotificationPermission = hasNotifPerm;
        
        switchNotifications.setChecked(hasNotificationPermission);
        updateNotificationStatus(hasNotificationPermission);

        switchNotifications.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (!hasNotificationPermission && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    requestNotificationPermission();
                } else {
                    saveNotificationSetting(true);
                }
            } else {
                if (hasNotificationPermission) {
                    showPermissionDisableDialog("Notificações", 
                        "Para desativar as notificações, vá às Definições do sistema.");
                    switchNotifications.setChecked(true);
                }
                saveNotificationSetting(false);
            }
        });
    }

    private void setupMulaModeSwitch() {
        switchMulaMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            saveMulaModeSetting(isChecked);
            String message = isChecked ? 
                "Modo MULA ativado - Sistema de entrega baseado em localização" : 
                "Modo MULA desativado";
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        });
    }

    private void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, 
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            
            new AlertDialog.Builder(this)
                .setTitle("Permissão de Localização")
                .setMessage("Esta aplicação precisa de acesso à sua localização para mostrar anúncios relevantes próximos de si.")
                .setPositiveButton("Permitir", (dialog, which) -> 
                    ActivityCompat.requestPermissions(this, 
                        new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        }, REQUEST_CODE_LOCATION)
                )
                .setNegativeButton("Cancelar", (dialog, which) -> {
                    switchLocation.setChecked(false);
                    dialog.dismiss();
                })
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

    private void showPermissionDisableDialog(String permissionName, String message) {
        new AlertDialog.Builder(this)
            .setTitle("Desativar " + permissionName)
            .setMessage(message)
            .setPositiveButton("Ir para Definições", (dialog, which) -> {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            })
            .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss())
            .create()
            .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, 
                                         @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                switchLocation.setChecked(true);
                updateLocationStatus(true);
                saveLocationSetting(true);
                Toast.makeText(this, "Permissão de localização concedida", Toast.LENGTH_SHORT).show();
            } else {
                switchLocation.setChecked(false);
                updateLocationStatus(false);
                saveLocationSetting(false);
                Toast.makeText(this, "Permissão de localização negada", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_CODE_NOTIFICATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                switchNotifications.setChecked(true);
                updateNotificationStatus(true);
                saveNotificationSetting(true);
                Toast.makeText(this, "Permissão de notificações concedida", Toast.LENGTH_SHORT).show();
            } else {
                switchNotifications.setChecked(false);
                updateNotificationStatus(false);
                saveNotificationSetting(false);
                Toast.makeText(this, "Permissão de notificações negada", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupLocationSwitch();
        setupNotificationSwitch();
    }

    private void updateLocationStatus(boolean enabled) {
        tvLocationStatus.setText(enabled ? 
            "Localização ativada" : "Localização desativada");
    }

    private void updateNotificationStatus(boolean enabled) {
        tvNotificationStatus.setText(enabled ? 
            "Notificações ativadas" : "Notificações desativadas");
    }

    private void saveLocationSetting(boolean enabled) {
        prefs.edit().putBoolean(KEY_LOCATION, enabled).apply();
    }

    private void saveNotificationSetting(boolean enabled) {
        prefs.edit().putBoolean(KEY_NOTIFICATIONS, enabled).apply();
    }

    private void saveMulaModeSetting(boolean enabled) {
        prefs.edit().putBoolean(KEY_MULA_MODE, enabled).apply();
    }
}
