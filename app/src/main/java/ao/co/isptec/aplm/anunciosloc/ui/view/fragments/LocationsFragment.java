package ao.co.isptec.aplm.anunciosloc.ui.view.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import ao.co.isptec.aplm.anunciosloc.R;
import ao.co.isptec.aplm.anunciosloc.data.model.Location;
import ao.co.isptec.aplm.anunciosloc.ui.view.AddLocationActivity;
import ao.co.isptec.aplm.anunciosloc.ui.viewmodel.LocationViewModel;

/**
 * Fragment para visualizar localizações no mapa
 */
public class LocationsFragment extends Fragment implements OnMapReadyCallback {
    
    private GoogleMap googleMap;
    private ProgressBar progressBar;
    private TextView txtLocationCount;
    private MaterialCardView emptyStateCard;
    private FloatingActionButton fabAdd;
    private ImageButton btnMenu;
    
    private LocationViewModel viewModel;
    private List<Location> currentLocations;
    
    // Coordenadas padrão (Luanda, Angola)
    private static final LatLng DEFAULT_LOCATION = new LatLng(-8.8400, 13.2890);
    private static final float DEFAULT_ZOOM = 12f;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_locations, container, false);
        
        initializeViews(view);
        initializeViewModel();
        initializeMap();
        setupListeners();
        observeViewModel();
        
        return view;
    }
    
    private void initializeViews(View view) {
        progressBar = view.findViewById(R.id.progressBar);
        txtLocationCount = view.findViewById(R.id.txtLocationCount);
        emptyStateCard = view.findViewById(R.id.emptyStateCard);
        fabAdd = view.findViewById(R.id.fabAdd);
        btnMenu = view.findViewById(R.id.btnMenu);
    }
    
    private void initializeViewModel() {
        viewModel = new ViewModelProvider(this).get(LocationViewModel.class);
    }
    
    private void initializeMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.mapFragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }
    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        googleMap = map;
        
        // Configura o mapa
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        
        // Move câmera para posição padrão
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(DEFAULT_LOCATION, DEFAULT_ZOOM));
        
        // Configura click nos marcadores
        googleMap.setOnMarkerClickListener(marker -> {
            if (marker.getTag() instanceof Location) {
                Location location = (Location) marker.getTag();
                openEditLocation(location);
            }
            return true;
        });
        
        // Se já temos localizações, adiciona ao mapa
        if (currentLocations != null && !currentLocations.isEmpty()) {
            addLocationsToMap(currentLocations);
        }
    }
    
    private void addLocationsToMap(List<Location> locations) {
        if (googleMap == null || locations == null || locations.isEmpty()) return;
        
        googleMap.clear();
        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        boolean hasValidLocations = false;
        
        for (Location location : locations) {
            // Se é localização WiFi (lat/long = 0), pula
            if (location.getLatitude() == 0 && location.getLongitude() == 0) {
                continue;
            }
            
            LatLng position = new LatLng(location.getLatitude(), location.getLongitude());
            
            // Adiciona marcador
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(position)
                    .title(location.getName())
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            
            com.google.android.gms.maps.model.Marker marker = googleMap.addMarker(markerOptions);
            if (marker != null) {
                marker.setTag(location);
            }
            
            // Adiciona círculo do raio
            CircleOptions circleOptions = new CircleOptions()
                    .center(position)
                    .radius(location.getRadiusInMeters())
                    .strokeColor(0x880078D4) // Azul semi-transparente
                    .fillColor(0x220078D4)   // Azul muito transparente
                    .strokeWidth(2);
            
            googleMap.addCircle(circleOptions);
            
            boundsBuilder.include(position);
            hasValidLocations = true;
        }
        
        // Ajusta câmera para mostrar todas as localizações
        if (hasValidLocations) {
            try {
                LatLngBounds bounds = boundsBuilder.build();
                googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
            } catch (Exception e) {
                // Se falhar, mantém a posição atual
            }
        }
    }
    
    private void openEditLocation(Location location) {
        Intent intent = new Intent(getContext(), AddLocationActivity.class);
        intent.putExtra(AddLocationActivity.EXTRA_LOCATION_ID, location.getId());
        intent.putExtra(AddLocationActivity.EXTRA_EDIT_MODE, true);
        startActivity(intent);
    }
    
    private void setupListeners() {
        if (fabAdd != null) {
            fabAdd.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), AddLocationActivity.class);
                startActivity(intent);
            });
        }
        
        if (btnMenu != null) {
            btnMenu.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), ao.co.isptec.aplm.anunciosloc.ui.view.MenuOptionsActivity.class);
                startActivity(intent);
            });
        }
    }
    
    private void observeViewModel() {
        // Observa lista de localizações
        viewModel.getLocations().observe(getViewLifecycleOwner(), locations -> {
            currentLocations = locations;
            updateUI(locations);
            
            if (googleMap != null && locations != null && !locations.isEmpty()) {
                addLocationsToMap(locations);
            }
        });
        
        // Observa estado de carregamento
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (progressBar != null) {
                progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            }
        });
        
        // Observa mensagens de erro
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null && !error.isEmpty() && getContext() != null) {
                Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
            }
        });
    }
    
    private void updateUI(List<Location> locations) {
        if (txtLocationCount == null || emptyStateCard == null) return;
        
        if (locations == null || locations.isEmpty()) {
            txtLocationCount.setText("0 localizações");
            emptyStateCard.setVisibility(View.VISIBLE);
        } else {
            int count = locations.size();
            txtLocationCount.setText(count + (count == 1 ? " localização" : " localizações"));
            emptyStateCard.setVisibility(View.GONE);
        }
    }
    
    @Override
    public void onResume() {
        super.onResume();
        // Recarrega localizações ao voltar para o fragment
        if (viewModel != null) {
            viewModel.loadLocations();
        }
    }
}
