package ao.co.isptec.aplm.anunciosloc.ui.view.fragments;

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

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

import ao.co.isptec.aplm.anunciosloc.R;
import ao.co.isptec.aplm.anunciosloc.ui.view.adapters.NotificationAdapter;
import ao.co.isptec.aplm.anunciosloc.ui.viewmodel.NotificationViewModel;

/**
 * Fragment para listar notificações
 */
public class NotificationsFragment extends Fragment {
    
    private RecyclerView recyclerView;
    private NotificationAdapter adapter;
    private ProgressBar progressBar;
    private TextView txtEmpty;
    private MaterialButton btnMarkAllRead;
    private ImageButton btnSettings;
    
    private NotificationViewModel viewModel;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        try {
            View view = inflater.inflate(R.layout.fragment_notifications, container, false);
            
            initializeViews(view);
            initializeViewModel();
            setupRecyclerView();
            setupListeners();
            observeViewModel();
            
            // Carrega notificações mockadas
            loadMockNotifications();
            
            return view;
        } catch (Exception e) {
            e.printStackTrace();
            // Retorna uma view vazia em caso de erro
            return new View(getContext());
        }
    }
    
    private void initializeViews(View view) {
        try {
            recyclerView = view.findViewById(R.id.recyclerNotifications);
            progressBar = view.findViewById(R.id.progressBar);
            txtEmpty = view.findViewById(R.id.txtEmpty);
            btnMarkAllRead = view.findViewById(R.id.btnMarkAllRead);
            btnSettings = view.findViewById(R.id.btnSettings);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void initializeViewModel() {
        try {
            viewModel = new ViewModelProvider(this).get(NotificationViewModel.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void setupRecyclerView() {
        try {
            if (recyclerView == null) return;
            
            adapter = new NotificationAdapter(new ArrayList<>());
            adapter.setOnNotificationClickListener(notification -> {
                if (!notification.isRead()) {
                    viewModel.markAsRead(notification.getId());
                }
                // TODO: Abrir detalhe relacionado (anúncio, localização, etc)
                Toast.makeText(getContext(), notification.getTitle(), Toast.LENGTH_SHORT).show();
            });
            
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void setupListeners() {
        try {
            if (btnMarkAllRead != null) {
                btnMarkAllRead.setOnClickListener(v -> {
                    // Marca todas as notificações mockadas como lidas
                    if (adapter != null) {
                        java.util.List<ao.co.isptec.aplm.anunciosloc.data.model.Notification> currentNotifications = adapter.notifications;
                        for (ao.co.isptec.aplm.anunciosloc.data.model.Notification notification : currentNotifications) {
                            notification.setRead(true);
                        }
                        adapter.notifyDataSetChanged();
                        btnMarkAllRead.setVisibility(View.GONE);
                        
                        if (getContext() != null) {
                            Toast.makeText(getContext(), "Todas as notificações foram marcadas como lidas", Toast.LENGTH_SHORT).show();
                        }
                    }
                    
                    // Se houver viewModel, também marca no repositório
                    if (viewModel != null) {
                        viewModel.markAllAsRead();
                    }
                });
            }
            
            if (btnSettings != null) {
                btnSettings.setOnClickListener(v -> {
                    Toast.makeText(getContext(), "Opções - Em desenvolvimento", Toast.LENGTH_SHORT).show();
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void observeViewModel() {
        try {
            if (viewModel == null) return;
            
            // Observa lista de notificações
            viewModel.getNotifications().observe(getViewLifecycleOwner(), notifications -> {
                try {
                    if (adapter == null || recyclerView == null || txtEmpty == null || btnMarkAllRead == null) return;
                    
                    if (notifications != null && !notifications.isEmpty()) {
                        adapter.updateNotifications(notifications);
                        txtEmpty.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        btnMarkAllRead.setVisibility(hasUnreadNotifications(notifications) ? View.VISIBLE : View.GONE);
                    } else {
                        txtEmpty.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                        btnMarkAllRead.setVisibility(View.GONE);
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
    
    private boolean hasUnreadNotifications(java.util.List<ao.co.isptec.aplm.anunciosloc.data.model.Notification> notifications) {
        for (ao.co.isptec.aplm.anunciosloc.data.model.Notification notification : notifications) {
            if (!notification.isRead()) {
                return true;
            }
        }
        return false;
    }
    
    private void loadMockNotifications() {
        try {
            // Cria lista de notificações mockadas
            java.util.List<ao.co.isptec.aplm.anunciosloc.data.model.Notification> mockNotifications = new java.util.ArrayList<>();
            
            // Notificação 1 - Nova promoção
            ao.co.isptec.aplm.anunciosloc.data.model.Notification notif1 = new ao.co.isptec.aplm.anunciosloc.data.model.Notification();
            notif1.setId("notif_1");
            notif1.setTitle("Nova Promoção Disponível");
            notif1.setMessage("Promoção de Natal - 50% OFF está disponível no Shopping Colombo!");
            notif1.setType("ANNOUNCEMENT");
            notif1.setRelatedId("mock_1");
            notif1.setTimestamp(System.currentTimeMillis() - 3600000); // 1 hora atrás
            notif1.setRead(false);
            mockNotifications.add(notif1);
            
            // Notificação 2 - Novo local próximo
            ao.co.isptec.aplm.anunciosloc.data.model.Notification notif2 = new ao.co.isptec.aplm.anunciosloc.data.model.Notification();
            notif2.setId("notif_2");
            notif2.setTitle("Você está próximo!");
            notif2.setMessage("Novo Café Aroma inaugurado na Universidade de Lisboa. Confira!");
            notif2.setType("LOCATION");
            notif2.setRelatedId("loc_2");
            notif2.setTimestamp(System.currentTimeMillis() - 7200000); // 2 horas atrás
            notif2.setRead(false);
            mockNotifications.add(notif2);
            
            // Notificação 3 - Workshop
            ao.co.isptec.aplm.anunciosloc.data.model.Notification notif3 = new ao.co.isptec.aplm.anunciosloc.data.model.Notification();
            notif3.setId("notif_3");
            notif3.setTitle("Workshop Gratuito Amanhã");
            notif3.setMessage("Não perca! Workshop de Programação Android no Centro de Inovação ISPTEC.");
            notif3.setType("ANNOUNCEMENT");
            notif3.setRelatedId("mock_3");
            notif3.setTimestamp(System.currentTimeMillis() - 86400000); // 1 dia atrás
            notif3.setRead(true);
            mockNotifications.add(notif3);
            
            // Notificação 4 - Sistema
            ao.co.isptec.aplm.anunciosloc.data.model.Notification notif4 = new ao.co.isptec.aplm.anunciosloc.data.model.Notification();
            notif4.setId("notif_4");
            notif4.setTitle("Bem-vindo ao AnunciosLoc!");
            notif4.setMessage("Complete seu perfil para receber anúncios personalizados baseados nos seus interesses.");
            notif4.setType("SYSTEM");
            notif4.setRelatedId(null);
            notif4.setTimestamp(System.currentTimeMillis() - 172800000); // 2 dias atrás
            notif4.setRead(true);
            mockNotifications.add(notif4);
            
            // Notificação 5 - Novo anúncio próximo
            ao.co.isptec.aplm.anunciosloc.data.model.Notification notif5 = new ao.co.isptec.aplm.anunciosloc.data.model.Notification();
            notif5.setId("notif_5");
            notif5.setTitle("Novidade na sua área");
            notif5.setMessage("3 novos anúncios foram publicados próximos à sua localização atual.");
            notif5.setType("SYSTEM");
            notif5.setRelatedId(null);
            notif5.setTimestamp(System.currentTimeMillis() - 259200000); // 3 dias atrás
            notif5.setRead(true);
            mockNotifications.add(notif5);
            
            // Atualiza o adapter com os dados mockados
            if (adapter != null) {
                adapter.updateNotifications(mockNotifications);
                
                // Atualiza visibilidade
                if (recyclerView != null && txtEmpty != null) {
                    recyclerView.setVisibility(View.VISIBLE);
                    txtEmpty.setVisibility(View.GONE);
                }
                
                // Mostra botão de marcar todas como lidas se houver não lidas
                if (btnMarkAllRead != null) {
                    btnMarkAllRead.setVisibility(hasUnreadNotifications(mockNotifications) ? View.VISIBLE : View.GONE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void onResume() {
        super.onResume();
        viewModel.loadNotifications();
    }
}
