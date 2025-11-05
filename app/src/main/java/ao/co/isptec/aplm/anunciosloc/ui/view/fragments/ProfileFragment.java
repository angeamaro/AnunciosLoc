package ao.co.isptec.aplm.anunciosloc.ui.view.fragments;

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
import ao.co.isptec.aplm.anunciosloc.ui.view.adapters.AttributeAdapter;
import ao.co.isptec.aplm.anunciosloc.ui.viewmodel.ProfileViewModel;
import ao.co.isptec.aplm.anunciosloc.utils.PreferencesHelper;

/**
 * Fragment para exibir e editar perfil do usuário
 */
public class ProfileFragment extends Fragment {
    
    private ImageView imgProfile;
    private TextView txtName, txtEmail, txtUsername, txtPhoneNumber;
    private RecyclerView recyclerAttributes;
    private AttributeAdapter adapter;
    private FloatingActionButton fabAddAttribute;
    private MaterialButton btnLogout;
    private ProgressBar progressBar;
    private LinearLayout layoutProfile;
    
    private ProfileViewModel viewModel;
    private UserRepository userRepository;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        try {
            View view = inflater.inflate(R.layout.fragment_profile, container, false);
            
            initializeViews(view);
            initializeViewModel();
            setupRecyclerView();
            setupListeners();
            observeViewModel();
            loadUserData();
            
            return view;
        } catch (Exception e) {
            e.printStackTrace();
            // Retorna uma view vazia em caso de erro
            return new View(getContext());
        }
    }
    
    private void initializeViews(View view) {
        try {
            imgProfile = view.findViewById(R.id.imgProfile);
            txtName = view.findViewById(R.id.txtName);
            txtEmail = view.findViewById(R.id.txtEmail);
            txtUsername = view.findViewById(R.id.txtUsername);
            txtPhoneNumber = view.findViewById(R.id.txtPhoneNumber);
            recyclerAttributes = view.findViewById(R.id.recyclerAttributes);
            fabAddAttribute = view.findViewById(R.id.fabAddAttribute);
            btnLogout = view.findViewById(R.id.btnLogout);
            progressBar = view.findViewById(R.id.progressBar);
            layoutProfile = view.findViewById(R.id.layoutProfile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void initializeViewModel() {
        try {
            viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
            userRepository = UserRepository.getInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void setupRecyclerView() {
        try {
            if (recyclerAttributes == null) return;
            
            adapter = new AttributeAdapter(new ArrayList<>());
            adapter.setOnAttributeDeleteListener((key, value) -> {
                try {
                    if (viewModel != null) {
                        viewModel.removeProfileAttribute(key);
                        if (getContext() != null) {
                            Toast.makeText(getContext(), "Atributo removido", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            
            recyclerAttributes.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerAttributes.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void setupListeners() {
        try {
            if (fabAddAttribute != null) {
                fabAddAttribute.setOnClickListener(v -> showAddAttributeDialog());
            }
            
            if (btnLogout != null) {
                btnLogout.setOnClickListener(v -> {
                    try {
                        if (getContext() != null) {
                            new AlertDialog.Builder(getContext())
                                .setTitle("Sair")
                                .setMessage("Tem certeza que deseja sair da sua conta?")
                                .setPositiveButton("Sim", (dialog, which) -> logout())
                                .setNegativeButton("Cancelar", null)
                                .show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void observeViewModel() {
        try {
            if (viewModel == null) return;
            
            // Observa usuário atualizado
            viewModel.getUser().observe(getViewLifecycleOwner(), user -> {
                try {
                    if (user != null) {
                        displayUserData(user);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            
            // Observa estado de carregamento
            viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
                try {
                    if (progressBar != null && layoutProfile != null) {
                        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
                        layoutProfile.setVisibility(isLoading ? View.GONE : View.VISIBLE);
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
            
            // Observa sucesso de atualização
            viewModel.getUpdateSuccess().observe(getViewLifecycleOwner(), success -> {
                try {
                    if (success != null && success && getContext() != null) {
                        Toast.makeText(getContext(), "Perfil atualizado", Toast.LENGTH_SHORT).show();
                        loadUserData();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void loadUserData() {
        try {
            if (userRepository == null) return;
            
            User currentUser = userRepository.getCurrentUser();
            if (currentUser != null) {
                displayUserData(currentUser);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void displayUserData(User user) {
        try {
            if (user == null) return;
            
            if (txtName != null) {
                txtName.setText(user.getName());
            }
            if (txtEmail != null) {
                txtEmail.setText(user.getEmail());
            }
            if (txtUsername != null) {
                txtUsername.setText("@" + user.getUsername());
            }
            
            if (txtPhoneNumber != null) {
                if (user.getPhoneNumber() != null && !user.getPhoneNumber().isEmpty()) {
                    txtPhoneNumber.setText(user.getPhoneNumber());
                    txtPhoneNumber.setVisibility(View.VISIBLE);
                } else {
                    txtPhoneNumber.setVisibility(View.GONE);
                }
            }
            
            // Atualiza lista de atributos
            if (adapter != null) {
                Map<String, String> attributes = user.getProfileAttributes();
                adapter.updateAttributes(attributes);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void showAddAttributeDialog() {
        try {
            if (getContext() == null) return;
            
            View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_attribute, null);
            EditText editKey = dialogView.findViewById(R.id.editKey);
            EditText editValue = dialogView.findViewById(R.id.editValue);
            
            new AlertDialog.Builder(getContext())
                .setTitle("Adicionar Atributo")
                .setView(dialogView)
                .setPositiveButton("Adicionar", (dialog, which) -> {
                    try {
                        if (editKey == null || editValue == null) return;
                        
                        String key = editKey.getText().toString().trim();
                        String value = editValue.getText().toString().trim();
                        
                        if (key.isEmpty() || value.isEmpty()) {
                            if (getContext() != null) {
                                Toast.makeText(getContext(), "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                            }
                            return;
                        }
                        
                        if (viewModel != null) {
                            viewModel.addProfileAttribute(key, value);
                            if (getContext() != null) {
                                Toast.makeText(getContext(), "Atributo adicionado", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (getContext() != null) {
                            Toast.makeText(getContext(), "Erro ao adicionar atributo", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
        } catch (Exception e) {
            e.printStackTrace();
            if (getContext() != null) {
                Toast.makeText(getContext(), "Erro ao abrir diálogo", Toast.LENGTH_SHORT).show();
            }
        }
    }
    
    private void logout() {
        try {
            if (getContext() == null) return;
            
            PreferencesHelper preferencesHelper = new PreferencesHelper(getContext());
            preferencesHelper.clearUserSession();
            
            Intent intent = new Intent(getContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            
            if (getActivity() != null) {
                getActivity().finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void onResume() {
        super.onResume();
        try {
            loadUserData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
