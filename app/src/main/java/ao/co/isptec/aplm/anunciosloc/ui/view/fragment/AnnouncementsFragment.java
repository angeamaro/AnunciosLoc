package ao.co.isptec.aplm.anunciosloc.ui.view.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import ao.co.isptec.aplm.anunciosloc.R;
import ao.co.isptec.aplm.anunciosloc.ui.adapter.AnnouncementCardAdapter;
import ao.co.isptec.aplm.anunciosloc.data.model.Announcement;
import ao.co.isptec.aplm.anunciosloc.ui.view.AnnouncementDetailActivity;
import ao.co.isptec.aplm.anunciosloc.ui.view.MenuOptionsActivity;
import ao.co.isptec.aplm.anunciosloc.ui.viewmodel.AnnouncementViewModel;

/**
 * Fragment para gerenciar anúncios (Todos e Guardados)
 */
public class AnnouncementsFragment extends Fragment {
    
    private static final String PREFS_NAME = "saved_announcements";
    private static final String KEY_SAVED_IDS = "saved_ids";
    
    private RecyclerView recyclerView;
    private AnnouncementCardAdapter adapter;
    private ProgressBar progressBar;
    private LinearLayout emptyState;
    private TextView txtAnnouncementCount, txtEmptyTitle, txtEmptyMessage;
    private ImageButton btnMenu;
    private TabLayout tabLayout;
    private TextInputEditText editSearch;
    
    private AnnouncementViewModel viewModel;
    private List<Announcement> allAnnouncements = new ArrayList<>();
    private List<String> savedAnnouncementIds = new ArrayList<>();
    private String currentTab = "saved"; // "saved" ou "all"
    private String searchQuery = "";
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_announcements, container, false);
        
        initializeViews(view);
        setupViewModel();
        setupRecyclerView();
        setupTabs();
        setupSearch();
        setupListeners();
        loadSavedIds();
        observeViewModel();
        
        return view;
    }
    
    private void initializeViews(View view) {
        recyclerView = view.findViewById(R.id.recyclerAnnouncements);
        progressBar = view.findViewById(R.id.progressBar);
        emptyState = view.findViewById(R.id.emptyState);
        txtAnnouncementCount = view.findViewById(R.id.txtAnnouncementCount);
        txtEmptyTitle = view.findViewById(R.id.txtEmptyTitle);
        txtEmptyMessage = view.findViewById(R.id.txtEmptyMessage);
        btnMenu = view.findViewById(R.id.btnMenu);
        tabLayout = view.findViewById(R.id.tabLayout);
        editSearch = view.findViewById(R.id.editSearch);
    }
    
    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(AnnouncementViewModel.class);
    }
    
    private void setupRecyclerView() {
        adapter = new AnnouncementCardAdapter(new AnnouncementCardAdapter.OnAnnouncementActionListener() {
            @Override
            public void onViewDetails(Announcement announcement) {
                Intent intent = new Intent(getContext(), AnnouncementDetailActivity.class);
                intent.putExtra(AnnouncementDetailActivity.EXTRA_ANNOUNCEMENT_ID, announcement.getId());
                startActivity(intent);
            }

            @Override
            public void onToggleSave(Announcement announcement, boolean isSaved) {
                String announcementId = String.valueOf(announcement.getId());
                if (isSaved) {
                    if (!savedAnnouncementIds.contains(announcementId)) {
                        savedAnnouncementIds.add(announcementId);
                    }
                } else {
                    savedAnnouncementIds.remove(announcementId);
                }
                saveSavedIds();
                adapter.setSavedAnnouncementIds(savedAnnouncementIds);
                updateAnnouncementsList();
            }
        });
        
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }
    
    private void setupTabs() {
        tabLayout.addTab(tabLayout.newTab().setText("Guardados"));
        tabLayout.addTab(tabLayout.newTab().setText("Todos"));
        
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                currentTab = tab.getPosition() == 0 ? "saved" : "all";
                updateAnnouncementsList();
                updateEmptyState();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }
    
    private void setupSearch() {
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchQuery = s.toString().toLowerCase();
                updateAnnouncementsList();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
    
    private void setupListeners() {
        btnMenu.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), MenuOptionsActivity.class);
            startActivity(intent);
        });
    }
    
    private void observeViewModel() {
        viewModel.getAnnouncements().observe(getViewLifecycleOwner(), announcements -> {
            if (announcements != null) {
                allAnnouncements = announcements;
                adapter.setSavedAnnouncementIds(savedAnnouncementIds);
                updateAnnouncementsList();
            }
        });
    }
    
    private void updateAnnouncementsList() {
        List<Announcement> filteredAnnouncements;
        
        // Filtra por tab
        if ("saved".equals(currentTab)) {
            filteredAnnouncements = allAnnouncements.stream()
                    .filter(a -> savedAnnouncementIds.contains(String.valueOf(a.getId())))
                    .collect(Collectors.toList());
        } else {
            filteredAnnouncements = new ArrayList<>(allAnnouncements);
        }
        
        // Filtra por pesquisa
        if (!searchQuery.isEmpty()) {
            filteredAnnouncements = filteredAnnouncements.stream()
                    .filter(a -> a.getTitle().toLowerCase().contains(searchQuery) ||
                            (a.getContent() != null && a.getContent().toLowerCase().contains(searchQuery)))
                    .collect(Collectors.toList());
        }
        
        adapter.setAnnouncements(filteredAnnouncements);
        updateCount(filteredAnnouncements.size());
        updateEmptyState();
    }
    
    private void updateCount(int count) {
        String text = count + (count == 1 ? " anúncio" : " anúncios");
        txtAnnouncementCount.setText(text);
    }
    
    private void updateEmptyState() {
        boolean isEmpty = adapter.getItemCount() == 0;
        emptyState.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        recyclerView.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
        
        if (isEmpty) {
            if ("saved".equals(currentTab)) {
                txtEmptyTitle.setText("Nenhum anúncio guardado");
                txtEmptyMessage.setText("Guarde anúncios interessantes para acessar rapidamente");
            } else {
                txtEmptyTitle.setText("Nenhum anúncio disponível");
                txtEmptyMessage.setText("Não há anúncios para exibir no momento");
            }
        }
    }
    
    private void loadSavedIds() {
        if (getContext() == null) return;
        SharedPreferences prefs = getContext().getSharedPreferences(PREFS_NAME, getContext().MODE_PRIVATE);
        Set<String> savedSet = prefs.getStringSet(KEY_SAVED_IDS, new HashSet<>());
        savedAnnouncementIds = new ArrayList<>(savedSet);
    }
    
    private void saveSavedIds() {
        if (getContext() == null) return;
        SharedPreferences prefs = getContext().getSharedPreferences(PREFS_NAME, getContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putStringSet(KEY_SAVED_IDS, new HashSet<>(savedAnnouncementIds));
        editor.apply();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Recarrega os IDs salvos quando o fragment retoma
        loadSavedIds();
        adapter.setSavedAnnouncementIds(savedAnnouncementIds);
        updateAnnouncementsList();
    }
}
