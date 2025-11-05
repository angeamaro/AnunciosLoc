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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import ao.co.isptec.aplm.anunciosloc.R;
import ao.co.isptec.aplm.anunciosloc.ui.view.AddLocationActivity;
import ao.co.isptec.aplm.anunciosloc.ui.view.adapters.LocationAdapter;
import ao.co.isptec.aplm.anunciosloc.ui.viewmodel.LocationViewModel;

/**
 * Fragment para listar localizações
 */
public class LocationsFragment extends Fragment {
    
    private RecyclerView recyclerView;
    private LocationAdapter adapter;
    private ProgressBar progressBar;
    private TextView txtEmpty;
    private FloatingActionButton fabAdd;
    private ImageButton btnSettings;
    
    private LocationViewModel viewModel;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        try {
            View view = inflater.inflate(R.layout.fragment_locations, container, false);
            
            initializeViews(view);
            initializeViewModel();
            setupRecyclerView();
            setupListeners();
            observeViewModel();
            
            return view;
        } catch (Exception e) {
            e.printStackTrace();
            // Retorna uma view vazia em caso de erro
            return new View(getContext());
        }
    }
    
    private void initializeViews(View view) {
        try {
            recyclerView = view.findViewById(R.id.recyclerLocations);
            progressBar = view.findViewById(R.id.progressBar);
            txtEmpty = view.findViewById(R.id.txtEmpty);
            fabAdd = view.findViewById(R.id.fabAdd);
            btnSettings = view.findViewById(R.id.btnSettings);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void initializeViewModel() {
        try {
            viewModel = new ViewModelProvider(this).get(LocationViewModel.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void setupRecyclerView() {
        try {
            if (recyclerView == null) return;
            
            adapter = new LocationAdapter(new ArrayList<>(), location -> {
                // Abrir modo edição
                Intent intent = new Intent(getContext(), AddLocationActivity.class);
                intent.putExtra(AddLocationActivity.EXTRA_LOCATION_ID, location.getId());
                intent.putExtra(AddLocationActivity.EXTRA_EDIT_MODE, true);
                startActivity(intent);
            });
            
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void setupListeners() {
        try {
            if (fabAdd != null) {
                fabAdd.setOnClickListener(v -> {
                    // Abrir AddLocationActivity em modo criação
                    Intent intent = new Intent(getContext(), AddLocationActivity.class);
                    startActivity(intent);
                });
            }
            
            if (btnSettings != null) {
                btnSettings.setOnClickListener(v -> {
                    Toast.makeText(getContext(), "Definições - Em desenvolvimento", Toast.LENGTH_SHORT).show();
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void observeViewModel() {
        try {
            if (viewModel == null) return;
            
            // Observa lista de localizações
            viewModel.getLocations().observe(getViewLifecycleOwner(), locations -> {
                try {
                    if (adapter == null || recyclerView == null || txtEmpty == null) return;
                    
                    if (locations != null && !locations.isEmpty()) {
                        adapter.updateLocations(locations);
                        txtEmpty.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    } else {
                        txtEmpty.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            
            // Observa estado de carregamento
            viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
                try {
                    if (progressBar != null) {
                        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            
            // Observa mensagens de erro
            viewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
                try {
                    if (error != null && !error.isEmpty() && getContext() != null) {
                        Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void onResume() {
        super.onResume();
        try {
            // Recarrega localizações ao voltar para o fragment
            if (viewModel != null) {
                viewModel.loadLocations();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
