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
    private static final String KEY_NOTIFICATIONS_STATE = "notifications_state";
    private static final String KEY_READ_IDS = "read_notification_ids";
    
    private RecyclerView recyclerView;
    private NotificationAdapter adapter;
    private ProgressBar progressBar;
    private LinearLayout txtEmpty;
    private ImageButton btnMenu;
    
    private NotificationViewModel viewModel;
    private SharedPreferences prefs;
    private List<Notification> currentNotifications = new ArrayList<>();
    
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
        
        // Restaura notificações salvas ou carrega mockadas
        if (savedInstanceState != null) {
            restoreNotificationsState(savedInstanceState);
        } else if (currentNotifications.isEmpty()) {
            loadMockNotifications();
        } else {
            // Já temos notificações (fragment não foi destruído completamente)
            adapter.setNotifications(currentNotifications);
            updateUI();
        }
        
        return view;
    }
    
    private void initializeViews(View view) {
        recyclerView = view.findViewById(R.id.recyclerNotifications);
        progressBar = view.findViewById(R.id.progressBar);
        txtEmpty = view.findViewById(R.id.txtEmpty);
        btnMenu = view.findViewById(R.id.btnMenu);
        
        Log.d(TAG, "Views inicializadas - btnMenu: " + (btnMenu != null));
    }
    
    private void initializePreferences() {
        if (getContext() != null) {
            prefs = getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        }
    }
    
    private void initializeViewModel() {
        viewModel = new ViewModelProvider(this).get(NotificationViewModel.class);
        // NÃO carrega notificações do ViewModel - usamos notificações mockadas do fragment
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
    }
    
    private void observeViewModel() {
        // Observer removido - usamos notificações mockadas do fragment ao invés do Repository
        // Se quiser usar o Repository no futuro, descomente abaixo e remova loadMockNotifications()
        
        /*
        viewModel.getNotifications().observe(getViewLifecycleOwner(), notifications -> {
            if (notifications != null && !notifications.isEmpty()) {
                currentNotifications = notifications;
                adapter.setNotifications(notifications);
                updateSavedStates();
                txtEmpty.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            } else {
                txtEmpty.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
        });
        */
        
        // Observa estado de carregamento (mantido para possível uso futuro)
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading != null) {
                progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            }
        });
        
        // Observa mensagens de erro (mantido para possível uso futuro)
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
    
    @Override
    public void onViewNotification(Notification notification) {
        Log.d(TAG, "onViewNotification: " + notification.getTitle());
        
        // Marca como lida
        notification.setRead(true);
        
        // Atualiza na lista atual
        for (Notification n : currentNotifications) {
            if (n.getId().equals(notification.getId())) {
                n.setRead(true);
                break;
            }
        }
        
        adapter.notifyDataSetChanged();
        updateUI();
        
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
        
        // Notificação 1 - Anúncio 2: Promoção de Verão (Belas Shopping)
        Notification n1 = new Notification();
        n1.setId("notif1");
        n1.setTitle("🍕 Promoção de Verão");
        n1.setMessage("Descontos em todas as lojas! Válido até o fim do mês. Localização: Belas Shopping. Autor: Carol Lima");
        n1.setType("ANNOUNCEMENT");
        n1.setRelatedId("2"); // ID do anúncio real
        n1.setRead(false);
        n1.setTimestamp(System.currentTimeMillis());
        mockNotifications.add(n1);
        
        // Notificação 2 - Anúncio 3: Torneio de Futebol
        Notification n2 = new Notification();
        n2.setId("notif2");
        n2.setTitle("💪 Torneio de Futebol");
        n2.setMessage("Inscreva sua equipe no torneio comunitário. Amantes de desporto bem-vindos! Localização: Marginal de Luanda. Autor: Carol Lima");
        n2.setType("ANNOUNCEMENT");
        n2.setRelatedId("3"); // ID do anúncio real
        n2.setRead(false);
        n2.setTimestamp(System.currentTimeMillis() - 3600000); // 1 hora atrás
        mockNotifications.add(n2);
        
        // Notificação 3 - Anúncio 4: Visita Guiada Histórica
        Notification n3 = new Notification();
        n3.setId("notif3");
        n3.setTitle("📚 Visita Guiada Histórica");
        n3.setMessage("Conheça a história de Luanda através dos seus monumentos. Localização: Fortaleza de São Miguel. Autor: Alice Silva");
        n3.setType("ANNOUNCEMENT");
        n3.setRelatedId("4"); // ID do anúncio real
        n3.setRead(false);
        n3.setTimestamp(System.currentTimeMillis() - 7200000); // 2 horas atrás
        mockNotifications.add(n3);
        
        // Notificação 4 - Anúncio 1: Workshop de Programação
        Notification n4 = new Notification();
        n4.setId("notif4");
        n4.setTitle("☕ Workshop de Programação");
        n4.setMessage("Venha aprender Java e Android! Inscrições abertas para estudantes. Localização: ISPTEC. Autor: Bob Santos");
        n4.setType("ANNOUNCEMENT");
        n4.setRelatedId("1"); // ID do anúncio real
        n4.setRead(false);
        n4.setTimestamp(System.currentTimeMillis() - 10800000); // 3 horas atrás
        mockNotifications.add(n4);
        
        // Notificação 5 - Anúncio 6: Hackathon 2025
        Notification n5 = new Notification();
        n5.setId("notif5");
        n5.setTitle("🎧 Hackathon 2025");
        n5.setMessage("48 horas de código, inovação e prêmios! Interessados em tecnologia, participem! Localização: ISPTEC. Autor: Bob Santos");
        n5.setType("ANNOUNCEMENT");
        n5.setRelatedId("6"); // ID do anúncio real
        n5.setRead(false);
        n5.setTimestamp(System.currentTimeMillis() - 14400000); // 4 horas atrás
        mockNotifications.add(n5);
        
        // Salva na lista atual
        currentNotifications = mockNotifications;
        
        // Atualiza o adapter
        adapter.setNotifications(currentNotifications);
        updateUI();
    }
    
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        
        // Salva os IDs das notificações lidas
        List<String> readIds = new ArrayList<>();
        for (Notification notification : currentNotifications) {
            if (notification.isRead()) {
                readIds.add(notification.getId());
            }
        }
        outState.putStringArrayList(KEY_READ_IDS, new ArrayList<>(readIds));
        
        Log.d(TAG, "Estado salvo: " + readIds.size() + " notificações lidas");
    }
    
    private void restoreNotificationsState(Bundle savedInstanceState) {
        // Carrega notificações mockadas primeiro
        loadMockNotifications();
        
        // Restaura quais estavam lidas
        ArrayList<String> readIds = savedInstanceState.getStringArrayList(KEY_READ_IDS);
        if (readIds != null && !readIds.isEmpty()) {
            for (Notification notification : currentNotifications) {
                if (readIds.contains(notification.getId())) {
                    notification.setRead(true);
                }
            }
            
            adapter.setNotifications(currentNotifications);
            updateUI();
            
            Log.d(TAG, "Estado restaurado: " + readIds.size() + " notificações marcadas como lidas");
        }
    }
    
    private void updateUI() {
        updateSavedStates();
        
        if (currentNotifications != null && !currentNotifications.isEmpty()) {
            txtEmpty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            txtEmpty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }
}
