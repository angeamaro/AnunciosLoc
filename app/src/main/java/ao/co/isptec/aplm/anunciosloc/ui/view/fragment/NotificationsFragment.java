package ao.co.isptec.aplm.anunciosloc.ui.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ao.co.isptec.aplm.anunciosloc.R;
import ao.co.isptec.aplm.anunciosloc.data.model.Notification;
import ao.co.isptec.aplm.anunciosloc.ui.view.AnnouncementDetailActivity;
import ao.co.isptec.aplm.anunciosloc.ui.view.MenuOptionsActivity;
import ao.co.isptec.aplm.anunciosloc.ui.adapter.NotificationAdapter;
import ao.co.isptec.aplm.anunciosloc.ui.viewmodel.NotificationViewModel;

/**
 * Fragment para exibir notificações com funcionalidades completas
 */
public class NotificationsFragment extends Fragment implements NotificationAdapter.OnNotificationActionListener {
    
    private static final String TAG = "NotificationsFragment";
    private static final String PREFS_NAME = "saved_announcements";
    private static final String KEY_SAVED_IDS = "saved_ids";
    
    private RecyclerView recyclerView;
    private NotificationAdapter adapter;
    private ProgressBar progressBar;
    private LinearLayout txtEmpty;
    private MaterialButton btnMarkAllRead;
    private ImageButton btnMenu;
    private LinearLayout containerMarkAllRead;
    
    private NotificationViewModel viewModel;
    private SharedPreferences prefs;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);
        
        initializeViews(view);
        initializePreferences();
        initializeViewModel();
        setupRecyclerView();
        setupListeners();
        observeViewModel();
        
        // Carrega notificações mockadas
        loadMockNotifications();
        
        return view;
    }
    
    private void initializeViews(View view) {
        recyclerView = view.findViewById(R.id.recyclerNotifications);
        progressBar = view.findViewById(R.id.progressBar);
        txtEmpty = view.findViewById(R.id.txtEmpty);
        btnMarkAllRead = view.findViewById(R.id.btnMarkAllRead);
        btnMenu = view.findViewById(R.id.btnMenu);
        containerMarkAllRead = view.findViewById(R.id.containerMarkAllRead);
        
        Log.d(TAG, "Views inicializadas - btnMenu: " + (btnMenu != null));
    }
    
    private void initializePreferences() {
        if (getContext() != null) {
            prefs = getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        }
    }
    
    private void initializeViewModel() {
        viewModel = new ViewModelProvider(this).get(NotificationViewModel.class);
    }
    
    private void setupRecyclerView() {
        adapter = new NotificationAdapter(new ArrayList<>());
        adapter.setOnNotificationActionListener(this);
        
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }
    
    private void setupListeners() {
        // Botão Menu
        if (btnMenu != null) {
            btnMenu.setOnClickListener(v -> {
                Log.d(TAG, "Menu button clicked");
                Intent intent = new Intent(getContext(), MenuOptionsActivity.class);
                startActivity(intent);
            });
            Log.d(TAG, "Menu button listener configurado");
        } else {
            Log.e(TAG, "btnMenu is null!");
        }
        
        // Botão Marcar Todas como Lidas
        if (btnMarkAllRead != null) {
            btnMarkAllRead.setOnClickListener(v -> markAllAsRead());
        }
    }
    
    private void observeViewModel() {
        // Observa lista de notificações
        viewModel.getNotifications().observe(getViewLifecycleOwner(), notifications -> {
            if (notifications != null && !notifications.isEmpty()) {
                adapter.setNotifications(notifications);
                updateSavedStates();
                txtEmpty.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                
                // Mostra botão marcar todas como lidas se houver não lidas
                boolean hasUnread = notifications.stream().anyMatch(n -> !n.isRead());
                containerMarkAllRead.setVisibility(hasUnread ? View.VISIBLE : View.GONE);
            } else {
                txtEmpty.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                containerMarkAllRead.setVisibility(View.GONE);
            }
        });
        
        // Observa estado de carregamento
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading != null) {
                progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            }
        });
        
        // Observa mensagens de erro
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null && !error.isEmpty()) {
                Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
            }
        });
    }
    
    /**
     * Atualiza o estado de "guardado" de todas as notificações
     */
    private void updateSavedStates() {
        Set<String> savedIds = getSavedAnnouncementIds();
        List<Notification> notifications = adapter.getNotifications();
        
        for (Notification notification : notifications) {
            if ("ANNOUNCEMENT".equals(notification.getType()) && notification.getRelatedId() != null) {
                boolean isSaved = savedIds.contains(notification.getRelatedId());
                // Você pode adicionar um campo no Notification para armazenar se está guardado
                // Por agora, vamos gerenciar isso no adapter
            }
        }
        
        adapter.notifyDataSetChanged();
    }
    
    /**
     * Marca todas as notificações como lidas
     */
    private void markAllAsRead() {
        List<Notification> notifications = adapter.getNotifications();
        
        if (notifications.isEmpty()) {
            return;
        }
        
        for (Notification notification : notifications) {
            notification.setRead(true);
        }
        
        adapter.notifyDataSetChanged();
        
        // Atualiza no ViewModel
        viewModel.markAllAsRead();
        
        // Oculta o botão
        containerMarkAllRead.setVisibility(View.GONE);
        
        Toast.makeText(getContext(), "Todas as notificações foram marcadas como lidas", Toast.LENGTH_SHORT).show();
    }
    
    @Override
    public void onViewNotification(Notification notification) {
        Log.d(TAG, "onViewNotification: " + notification.getTitle());
        
        // Marca como lida
        notification.setRead(true);
        adapter.notifyDataSetChanged();
        
        // Abre apenas se for do tipo ANNOUNCEMENT
        if ("ANNOUNCEMENT".equals(notification.getType()) && notification.getRelatedId() != null) {
            Intent intent = new Intent(getContext(), AnnouncementDetailActivity.class);
            intent.putExtra("announcement_id", notification.getRelatedId());
            startActivity(intent);
        } else {
            Toast.makeText(getContext(), "Detalhes da notificação: " + notification.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    
    @Override
    public void onSaveNotification(Notification notification) {
        Log.d(TAG, "onSaveNotification: " + notification.getTitle());
        
        // Apenas salva se for do tipo ANNOUNCEMENT
        if (!"ANNOUNCEMENT".equals(notification.getType()) || notification.getRelatedId() == null) {
            Toast.makeText(getContext(), "Apenas anúncios podem ser guardados", Toast.LENGTH_SHORT).show();
            return;
        }
        
        Set<String> savedIds = getSavedAnnouncementIds();
        String announcementId = notification.getRelatedId();
        
        if (savedIds.contains(announcementId)) {
            // Remove dos guardados
            savedIds.remove(announcementId);
            saveSavedAnnouncementIds(savedIds);
            Toast.makeText(getContext(), "Anúncio removido dos guardados", Toast.LENGTH_SHORT).show();
        } else {
            // Adiciona aos guardados
            savedIds.add(announcementId);
            saveSavedAnnouncementIds(savedIds);
            Toast.makeText(getContext(), "Anúncio guardado com sucesso!", Toast.LENGTH_SHORT).show();
        }
        
        // Atualiza o adapter
        adapter.notifyDataSetChanged();
    }
    
    /**
     * Verifica se um anúncio está guardado
     */
    public boolean isAnnouncementSaved(String announcementId) {
        Set<String> savedIds = getSavedAnnouncementIds();
        return savedIds.contains(announcementId);
    }
    
    /**
     * Obtém os IDs dos anúncios guardados
     */
    private Set<String> getSavedAnnouncementIds() {
        if (prefs == null) return new HashSet<>();
        return new HashSet<>(prefs.getStringSet(KEY_SAVED_IDS, new HashSet<>()));
    }
    
    /**
     * Salva os IDs dos anúncios guardados
     */
    private void saveSavedAnnouncementIds(Set<String> ids) {
        if (prefs != null) {
            prefs.edit().putStringSet(KEY_SAVED_IDS, ids).apply();
        }
    }
    
    /**
     * Carrega notificações mockadas para teste
     */
    private void loadMockNotifications() {
        List<Notification> mockNotifications = new ArrayList<>();
        
        // Notificação 1 - Promoção Restaurante
        Notification n1 = new Notification();
        n1.setId("notif1");
        n1.setTitle("🍕 Promoção Especial - Pizza Margherita");
        n1.setMessage("Desfrute de 30% de desconto em todas as pizzas hoje! Válido apenas na Pizzaria Bella Napoli. Localização: Centro Comercial Colombo. Autor: Restaurante Bella Napoli");
        n1.setType("ANNOUNCEMENT");
        n1.setRelatedId("ann1");
        n1.setRead(false);
        n1.setTimestamp(System.currentTimeMillis());
        mockNotifications.add(n1);
        
        // Notificação 2 - Evento Academia
        Notification n2 = new Notification();
        n2.setId("notif2");
        n2.setTitle("💪 Aula Experimental de Yoga Grátis");
        n2.setMessage("Participe da nossa aula experimental de yoga amanhã às 18h. Inscrições limitadas! Localização: Academia Fitness Plus, Avenida da República. Autor: Academia Fitness Plus");
        n2.setType("ANNOUNCEMENT");
        n2.setRelatedId("ann2");
        n2.setRead(false);
        n2.setTimestamp(System.currentTimeMillis() - 3600000); // 1 hora atrás
        mockNotifications.add(n2);
        
        // Notificação 3 - Livraria
        Notification n3 = new Notification();
        n3.setId("notif3");
        n3.setTitle("📚 Lançamento de Livro com Sessão de Autógrafos");
        n3.setMessage("Não perca o lançamento do novo livro 'Aventuras em Lisboa' com sessão de autógrafos no sábado às 15h. Localização: Livraria Bertrand, Chiado. Autor: Livraria Bertrand");
        n3.setType("ANNOUNCEMENT");
        n3.setRelatedId("ann3");
        n3.setRead(false);
        n3.setTimestamp(System.currentTimeMillis() - 7200000); // 2 horas atrás
        mockNotifications.add(n3);
        
        // Notificação 4 - Café
        Notification n4 = new Notification();
        n4.setId("notif4");
        n4.setTitle("☕ Happy Hour - Café e Bolo por 5€");
        n4.setMessage("De segunda a sexta, das 15h às 17h, desfrute do nosso Happy Hour: café expresso + fatia de bolo por apenas 5€. Localização: Café Central, Rossio. Autor: Café Central");
        n4.setType("ANNOUNCEMENT");
        n4.setRelatedId("ann4");
        n4.setRead(false);
        n4.setTimestamp(System.currentTimeMillis() - 10800000); // 3 horas atrás
        mockNotifications.add(n4);
        
        // Notificação 5 - Loja de Tecnologia
        Notification n5 = new Notification();
        n5.setId("notif5");
        n5.setTitle("🎧 Saldos de Verão - Até 50% OFF");
        n5.setMessage("Aproveite os saldos de verão com descontos até 50% em smartphones, tablets e acessórios. Localização: TechStore, Centro Comercial Vasco da Gama. Autor: TechStore Lisboa");
        n5.setType("ANNOUNCEMENT");
        n5.setRelatedId("ann5");
        n5.setRead(false);
        n5.setTimestamp(System.currentTimeMillis() - 14400000); // 4 horas atrás
        mockNotifications.add(n5);
        
        // Atualiza o adapter
        adapter.setNotifications(mockNotifications);
        updateSavedStates();
        
        txtEmpty.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        containerMarkAllRead.setVisibility(View.VISIBLE);
    }
}
