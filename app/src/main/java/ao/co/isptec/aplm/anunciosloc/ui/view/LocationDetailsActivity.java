package ao.co.isptec.aplm.anunciosloc.ui.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

import ao.co.isptec.aplm.anunciosloc.R;
import ao.co.isptec.aplm.anunciosloc.data.model.Location;
import ao.co.isptec.aplm.anunciosloc.ui.viewmodel.LocationViewModel;
import ao.co.isptec.aplm.anunciosloc.utils.PreferencesHelper;

public class LocationDetailsActivity extends AppCompatActivity implements OnMapReadyCallback {
    
    private MaterialToolbar toolbar;
    private TextView txtLocationName, txtLocationType, txtLatitude, txtLongitude, txtSsid, txtCreatedBy;
    private ProgressBar progressBar;
    private GoogleMap googleMap;
    private LocationViewModel locationViewModel;
    private String locationId, currentUserId;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_details);
        
        currentUserId = PreferencesHelper.getUserId(this);
        locationId = getIntent().getStringExtra("LOCATION_ID");
        
        if (locationId == null) {
            Toast.makeText(this, "Erro: Localização não encontrada", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        
        initializeViews();
        setupToolbar();
        setupViewModel();
        loadLocationDetails();
    }
    
    private void initializeViews() {
        toolbar = findViewById(R.id.toolbar);
        txtLocationName = findViewById(R.id.txtLocationName);
        // ... outras inicializações
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }
    
    private void setupToolbar() { /* ... */ }
    private void setupViewModel() { /* ... */ }
    private void loadLocationDetails() { /* ... */ }
    
    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        // Lógica do mapa
    }
}
