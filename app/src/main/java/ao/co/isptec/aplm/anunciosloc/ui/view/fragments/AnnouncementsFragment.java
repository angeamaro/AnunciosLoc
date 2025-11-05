package ao.co.isptec.aplm.anunciosloc.ui.view.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
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

import ao.co.isptec.aplm.anunciosloc.MainActivity;
import ao.co.isptec.aplm.anunciosloc.R;
import ao.co.isptec.aplm.anunciosloc.adapters.AnnouncementAdapter;
import ao.co.isptec.aplm.anunciosloc.data.model.Announcement;
import ao.co.isptec.aplm.anunciosloc.data.model.User;
import ao.co.isptec.aplm.anunciosloc.data.repository.UserRepository;
import ao.co.isptec.aplm.anunciosloc.ui.view.AnnouncementDetailActivity;
import ao.co.isptec.aplm.anunciosloc.ui.view.CreateAnnouncementActivity;
import ao.co.isptec.aplm.anunciosloc.ui.view.LoginActivity;
import ao.co.isptec.aplm.anunciosloc.ui.viewmodel.AnnouncementViewModel;
import ao.co.isptec.aplm.anunciosloc.utils.PreferencesHelper;

/**
 * Fragment para listar anúncios
 */
public class AnnouncementsFragment extends Fragment {
    
    private RecyclerView recyclerView;
    private AnnouncementAdapter adapter;
    private ProgressBar progressBar;
    private TextView txtEmpty, txtUserName;
    private TextView btnViewAll;
    private ImageButton btnMenu;
    
    private AnnouncementViewModel viewModel;
    private UserRepository userRepository;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        try {
            View view = inflater.inflate(R.layout.fragment_announcements, container, false);
            
            initializeViews(view);
            initializeViewModel();
            setupRecyclerView();
            setupListeners();
            observeViewModel();
            
            // Carrega anúncios mockados
            loadMockAnnouncements();
            
            return view;
        } catch (Exception e) {
            e.printStackTrace();
            // Retorna uma view vazia em caso de erro
            return new View(getContext());
        }
    }
    
    private void initializeViews(View view) {
        try {
            recyclerView = view.findViewById(R.id.recyclerAnnouncements);
            progressBar = view.findViewById(R.id.progressBar);
            txtEmpty = view.findViewById(R.id.txtEmpty);
            btnMenu = view.findViewById(R.id.btnMenu);
            txtUserName = view.findViewById(R.id.txtUserName);
            btnViewAll = view.findViewById(R.id.btnViewAll);
            
            // Carrega e exibe o nome do usuário
            loadUserName();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void loadUserName() {
        try {
            if (getContext() == null || txtUserName == null) return;
            
            userRepository = UserRepository.getInstance();
            User currentUser = userRepository.getCurrentUser();
            
            if (currentUser != null) {
                String userName = currentUser.getName();
                if (userName != null && !userName.isEmpty()) {
                    txtUserName.setText(userName);
                } else {
                    txtUserName.setText("Usuário");
                }
            } else {
                // Se não encontrar o usuário no repositório, busca do SharedPreferences
                PreferencesHelper preferencesHelper = new PreferencesHelper(getContext());
                String userIdStr = preferencesHelper.getUserId();
                
                if (userIdStr != null && !userIdStr.isEmpty()) {
                    try {
                        long userId = Long.parseLong(userIdStr);
                        // Busca o usuário pelo ID
                        viewModel.getUserById(userId).observe(getViewLifecycleOwner(), user -> {
                            if (user != null && txtUserName != null) {
                                String userName = user.getName();
                                if (userName != null && !userName.isEmpty()) {
                                    txtUserName.setText(userName);
                                } else {
                                    txtUserName.setText("Usuário");
                                }
                            } else {
                                if (txtUserName != null) {
                                    txtUserName.setText("Usuário");
                                }
                            }
                        });
                    } catch (NumberFormatException e) {
                        txtUserName.setText("Usuário");
                    }
                } else {
                    txtUserName.setText("Usuário");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (txtUserName != null) {
                txtUserName.setText("Usuário");
            }
        }
    }
    
    private void initializeViewModel() {
        try {
            viewModel = new ViewModelProvider(this).get(AnnouncementViewModel.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void setupRecyclerView() {
        try {
            if (recyclerView == null) return;
            
            adapter = new AnnouncementAdapter(new ArrayList<>());
            adapter.setOnAnnouncementClickListener(announcement -> {
                // Abrir AnnouncementDetailActivity
                Intent intent = new Intent(getContext(), AnnouncementDetailActivity.class);
                intent.putExtra(AnnouncementDetailActivity.EXTRA_ANNOUNCEMENT_ID, announcement.getId());
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
            if (btnMenu != null) {
                Log.d("AnnouncementsFragment", "Configurando listener do btnMenu");
                btnMenu.setOnClickListener(v -> {
                    Log.d("AnnouncementsFragment", "btnMenu clicado!");
                    showPopupMenu(v);
                });
                Log.d("AnnouncementsFragment", "Listener do btnMenu configurado com sucesso");
            } else {
                Log.e("AnnouncementsFragment", "btnMenu é null - não foi possível configurar listener");
            }
            
            if (btnViewAll != null) {
                btnViewAll.setOnClickListener(v -> {
                    Toast.makeText(getContext(), "Ver todos os anúncios", Toast.LENGTH_SHORT).show();
                });
            }
        } catch (Exception e) {
            Log.e("AnnouncementsFragment", "Erro em setupListeners", e);
            e.printStackTrace();
        }
    }
    
    private void showPopupMenu(View anchor) {
        try {
            Log.d("AnnouncementsFragment", "showPopupMenu chamado");
            
            if (getContext() == null) {
                Log.e("AnnouncementsFragment", "Context é null");
                return;
            }
            
            if (anchor == null) {
                Log.e("AnnouncementsFragment", "Anchor é null");
                return;
            }
            
            Log.d("AnnouncementsFragment", "Criando PopupMenu");
            PopupMenu popupMenu = new PopupMenu(getContext(), anchor, Gravity.END);
            popupMenu.getMenuInflater().inflate(R.menu.menu_home_options, popupMenu.getMenu());
            
            Log.d("AnnouncementsFragment", "Menu inflado com " + popupMenu.getMenu().size() + " itens");
            
            popupMenu.setOnMenuItemClickListener(item -> {
                Log.d("AnnouncementsFragment", "Menu item clicado: " + item.getTitle());
                int itemId = item.getItemId();
                
                if (itemId == R.id.menu_perfil) {
                    // Navegar para ProfileFragment
                    if (getActivity() != null) {
                        Toast.makeText(getContext(), "Perfil - Em desenvolvimento", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                } else if (itemId == R.id.menu_definicoes) {
                    Toast.makeText(getContext(), "Definições - Em desenvolvimento", Toast.LENGTH_SHORT).show();
                    return true;
                } else if (itemId == R.id.menu_interesses) {
                    Toast.makeText(getContext(), "Interesses - Em desenvolvimento", Toast.LENGTH_SHORT).show();
                    return true;
                } else if (itemId == R.id.menu_politicas) {
                    Toast.makeText(getContext(), "Políticas - Em desenvolvimento", Toast.LENGTH_SHORT).show();
                    return true;
                } else if (itemId == R.id.menu_sair) {
                    logout();
                    return true;
                }
                
                return false;
            });
            
            Log.d("AnnouncementsFragment", "Mostrando popup menu");
            popupMenu.show();
            Log.d("AnnouncementsFragment", "PopupMenu.show() chamado com sucesso");
            
        } catch (Exception e) {
            Log.e("AnnouncementsFragment", "Erro ao mostrar popup menu", e);
            e.printStackTrace();
            Toast.makeText(getContext(), "Erro ao abrir menu: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    
    private void logout() {
        try {
            if (getContext() == null) return;
            
            // Limpa a sessão
            PreferencesHelper preferencesHelper = new PreferencesHelper(getContext());
            preferencesHelper.clearUserSession();
            
            // Redireciona para login
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
    
    private void observeViewModel() {
        try {
            if (viewModel == null) return;
            
            // Observa lista de anúncios
            viewModel.getAnnouncements().observe(getViewLifecycleOwner(), announcements -> {
                try {
                    if (adapter == null || recyclerView == null || txtEmpty == null) return;
                    
                    if (announcements != null && !announcements.isEmpty()) {
                        adapter.updateAnnouncements(announcements);
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
    
    private void loadMockAnnouncements() {
        try {
            // Cria lista de anúncios mockados
            java.util.List<Announcement> mockAnnouncements = new java.util.ArrayList<>();
            
            // Anúncio 1
            Announcement ann1 = new Announcement();
            ann1.setId("mock_1");
            ann1.setTitle("Promoção de Natal - 50% OFF");
            ann1.setContent("Aproveite nossa mega promoção de Natal com até 50% de desconto em todos os produtos da loja. Válido apenas para esta semana!");
            ann1.setLocationId("loc_1");
            ann1.setLocationName("Shopping Colombo");
            ann1.setAuthorId("user_1");
            ann1.setAuthorName("Loja Tech Store");
            ann1.setCreatedAt(System.currentTimeMillis() - 86400000); // 1 dia atrás
            ann1.setStatus("ACTIVE");
            mockAnnouncements.add(ann1);
            
            // Anúncio 2
            Announcement ann2 = new Announcement();
            ann2.setId("mock_2");
            ann2.setTitle("Novo Café Inaugurado!");
            ann2.setContent("Venha conhecer o novo Café Aroma, agora aberto na universidade. Oferecemos café especial, croissants e ambiente perfeito para estudar.");
            ann2.setLocationId("loc_2");
            ann2.setLocationName("Universidade de Lisboa");
            ann2.setAuthorId("user_2");
            ann2.setAuthorName("Café Aroma");
            ann2.setCreatedAt(System.currentTimeMillis() - 172800000); // 2 dias atrás
            ann2.setStatus("ACTIVE");
            mockAnnouncements.add(ann2);
            
            // Anúncio 3
            Announcement ann3 = new Announcement();
            ann3.setId("mock_3");
            ann3.setTitle("Workshop de Programação Gratuito");
            ann3.setContent("Participe do nosso workshop gratuito sobre desenvolvimento Android. Inscrições abertas! Venha aprender a criar apps incríveis.");
            ann3.setLocationId("loc_3");
            ann3.setLocationName("Centro de Inovação ISPTEC");
            ann3.setAuthorId("user_3");
            ann3.setAuthorName("ISPTEC Tech Lab");
            ann3.setCreatedAt(System.currentTimeMillis() - 259200000); // 3 dias atrás
            ann3.setStatus("ACTIVE");
            mockAnnouncements.add(ann3);
            
            // Atualiza o adapter com os dados mockados
            if (adapter != null) {
                adapter.updateAnnouncements(mockAnnouncements);
                
                // Atualiza visibilidade
                if (recyclerView != null && txtEmpty != null) {
                    recyclerView.setVisibility(View.VISIBLE);
                    txtEmpty.setVisibility(View.GONE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void onResume() {
        super.onResume();
        try {
            // Carrega anúncios filtrados por perfil do usuário
            UserRepository userRepository = UserRepository.getInstance();
            if (userRepository != null && userRepository.getCurrentUser() != null) {
                viewModel.loadAnnouncementsForUser(userRepository.getCurrentUser().getProfileAttributes());
            } else {
                viewModel.loadAnnouncements();
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Em caso de erro, carrega todos os anúncios
            if (viewModel != null) {
                viewModel.loadAnnouncements();
            }
        }
    }
}
