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
import java.util.Map;

import ao.co.isptec.aplm.anunciosloc.R;
import ao.co.isptec.aplm.anunciosloc.data.model.User;
import ao.co.isptec.aplm.anunciosloc.data.repository.UserRepository;
import ao.co.isptec.aplm.anunciosloc.ui.view.LoginActivity;
import ao.co.isptec.aplm.anunciosloc.ui.adapter.AttributeAdapter;
import ao.co.isptec.aplm.anunciosloc.ui.viewmodel.ProfileViewModel;
import ao.co.isptec.aplm.anunciosloc.utils.PreferencesHelper;

/**
 * Fragment para exibir e editar perfil do usuário
 */
public class ProfileFragment extends Fragment {
    
    private ImageView imgProfile;
    private TextView txtUsername;
    private RecyclerView recyclerAttributes;
    private AttributeAdapter adapter;
    private FloatingActionButton fabAddAttribute;
    private MaterialButton btnLogout;
    private ProgressBar progressBar;
    private LinearLayout layoutProfile;
    
    private ProfileViewModel viewModel;
    private UserRepository userRepository; // Mantido para carregar dados iniciais, se necessário
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        
        initializeViews(view);
        initializeViewModel();
        setupRecyclerView();
        setupListeners();
        observeViewModel();
        loadUserData();
        
        return view;
    }
    
    private void initializeViews(View view) {
        imgProfile = view.findViewById(R.id.imgProfile);
        txtUsername = view.findViewById(R.id.txtUsername);
        recyclerAttributes = view.findViewById(R.id.recyclerAttributes);
        fabAddAttribute = view.findViewById(R.id.fabAddAttribute);
        btnLogout = view.findViewById(R.id.btnLogout);
        progressBar = view.findViewById(R.id.progressBar);
        layoutProfile = view.findViewById(R.id.layoutProfile);
    }
    
    private void initializeViewModel() {
        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        userRepository = UserRepository.getInstance();
    }
    
    private void setupRecyclerView() {
        if (recyclerAttributes == null) return;
        
        adapter = new AttributeAdapter(new ArrayList<>());
        adapter.setOnAttributeDeleteListener((key, value) -> {
            if (viewModel != null) {
                viewModel.removeProfileAttribute(key);
                if (getContext() != null) {
                    Toast.makeText(getContext(), "Atributo removido", Toast.LENGTH_SHORT).show();
                }
            }
        });
        
        recyclerAttributes.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerAttributes.setAdapter(adapter);
    }
    
    private void setupListeners() {
        if (fabAddAttribute != null) {
            fabAddAttribute.setOnClickListener(v -> showAddAttributeDialog());
        }
        
        if (btnLogout != null) {
            btnLogout.setOnClickListener(v -> {
                if (getContext() != null) {
                    new AlertDialog.Builder(getContext())
                        .setTitle("Sair")
                        .setMessage("Tem certeza que deseja sair da sua conta?")
                        .setPositiveButton("Sim", (dialog, which) -> logout())
                        .setNegativeButton("Cancelar", null)
                        .show();
                }
            });
        }
    }
    
    private void observeViewModel() {
        if (viewModel == null) return;
        
        viewModel.getUser().observe(getViewLifecycleOwner(), this::displayUserData);
        
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (progressBar != null && layoutProfile != null) {
                progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
                layoutProfile.setVisibility(isLoading ? View.GONE : View.VISIBLE);
            }
        });
        
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null && !error.isEmpty() && getContext() != null) {
                Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
            }
        });
        
        viewModel.getUpdateSuccess().observe(getViewLifecycleOwner(), success -> {
            if (success != null && success && getContext() != null) {
                Toast.makeText(getContext(), "Perfil atualizado", Toast.LENGTH_SHORT).show();
                loadUserData();
            }
        });
    }
    
    private void loadUserData() {
        // A fonte da verdade para o usuário logado é o PreferencesHelper
        User currentUser = PreferencesHelper.getCurrentUser(getContext());
        if (currentUser != null) {
            displayUserData(currentUser);
        }
    }
    
    private void displayUserData(User user) {
        if (user == null) return;
        
        if (txtUsername != null) {
            txtUsername.setText("@" + user.getUsername());
        }
        
        // Os campos de nome, email e telefone foram removidos pois não existem no novo modelo User

        if (adapter != null) {
            Map<String, String> attributes = user.getProfileAttributes();
            adapter.updateAttributes(attributes);
        }
    }
    
    private void showAddAttributeDialog() {
        if (getContext() == null) return;
        
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_attribute, null);
        EditText editKey = dialogView.findViewById(R.id.editKey);
        EditText editValue = dialogView.findViewById(R.id.editValue);
        
        new AlertDialog.Builder(getContext())
            .setTitle("Adicionar Atributo")
            .setView(dialogView)
            .setPositiveButton("Adicionar", (dialog, which) -> {
                String key = editKey.getText().toString().trim();
                String value = editValue.getText().toString().trim();
                
                if (key.isEmpty() || value.isEmpty()) {
                    Toast.makeText(getContext(), "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                    return;
                }
                
                if (viewModel != null) {
                    viewModel.addProfileAttribute(key, value);
                    Toast.makeText(getContext(), "Atributo adicionado", Toast.LENGTH_SHORT).show();
                }
            })
            .setNegativeButton("Cancelar", null)
            .show();
    }
    
    private void logout() {
        if (getContext() == null) return;
        
        // CORRIGIDO: Usa o método estático para limpar a sessão
        PreferencesHelper.clearSession(getContext());
        
        Intent intent = new Intent(getContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        
        if (getActivity() != null) {
            getActivity().finish();
        }
    }
    
    @Override
    public void onResume() {
        super.onResume();
        loadUserData();
    }
}
