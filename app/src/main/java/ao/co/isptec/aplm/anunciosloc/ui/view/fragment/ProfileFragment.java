package ao.co.isptec.aplm.anunciosloc.ui.view.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import ao.co.isptec.aplm.anunciosloc.R;
import ao.co.isptec.aplm.anunciosloc.data.model.User;
import ao.co.isptec.aplm.anunciosloc.ui.view.LoginActivity;
import ao.co.isptec.aplm.anunciosloc.ui.adapter.AttributeAdapter;
import ao.co.isptec.aplm.anunciosloc.ui.viewmodel.ProfileViewModel;
import ao.co.isptec.aplm.anunciosloc.utils.PreferencesHelper;

public class ProfileFragment extends Fragment {
    
    private TextView txtUsername;
    private RecyclerView recyclerAttributes;
    private AttributeAdapter adapter;
    private FloatingActionButton fabAddAttribute;
    private MaterialButton btnLogout;
    private ProfileViewModel viewModel;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        
        initializeViews(view);
        initializeViewModel();
        setupRecyclerView();
        setupListeners();
        loadUserData();
        
        return view;
    }
    
    private void initializeViews(View view) {
        txtUsername = view.findViewById(R.id.txtUsername);
        recyclerAttributes = view.findViewById(R.id.recyclerAttributes);
        fabAddAttribute = view.findViewById(R.id.fabAddAttribute);
        btnLogout = view.findViewById(R.id.btnLogout);
    }
    
    private void initializeViewModel() {
        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
    }
    
    private void setupRecyclerView() {
        adapter = new AttributeAdapter(new ArrayList<>());
        recyclerAttributes.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerAttributes.setAdapter(adapter);
    }
    
    private void setupListeners() {
        fabAddAttribute.setOnClickListener(v -> showAddAttributeDialog());
        btnLogout.setOnClickListener(v -> logout());
    }
    
    private void loadUserData() {
        String username = PreferencesHelper.getUserName(getContext());
        if (username != null) {
            txtUsername.setText("@" + username);
        }
        // A lógica para carregar os atributos virá do ViewModel quando a API estiver pronta
    }
    
    private void showAddAttributeDialog() {
        // Lógica para adicionar atributo (precisará do ViewModel)
    }
    
    private void logout() {
        if (getContext() != null) {
            PreferencesHelper.clearSession(getContext());
            Intent intent = new Intent(getContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            if (getActivity() != null) {
                getActivity().finish();
            }
        }
    }
}
