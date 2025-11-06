package ao.co.isptec.aplm.anunciosloc.ui.view.fragment;

import android.content.Intent;
import android.os.Bundle;
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

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import ao.co.isptec.aplm.anunciosloc.R;
import ao.co.isptec.aplm.anunciosloc.ui.adapter.AnnouncementAdapter;
import ao.co.isptec.aplm.anunciosloc.data.model.Announcement;
import ao.co.isptec.aplm.anunciosloc.data.model.User;
import ao.co.isptec.aplm.anunciosloc.data.repository.UserRepository;
import ao.co.isptec.aplm.anunciosloc.ui.view.AnnouncementDetailActivity;
import ao.co.isptec.aplm.anunciosloc.ui.view.MenuOptionsActivity;
import ao.co.isptec.aplm.anunciosloc.ui.viewmodel.AnnouncementViewModel;
import ao.co.isptec.aplm.anunciosloc.utils.PreferencesHelper;

/**
 * HomeFragment - Tela inicial com Bem-vindo e Anúncios Recentes
 */
public class HomeFragment extends Fragment {

    private TextView txtUserName;
    private LinearLayout txtEmpty;
    private RecyclerView recyclerAnnouncements;
    private ProgressBar progressBar;
    private MaterialButton btnViewAll;
    private ImageButton btnMenu;

    private AnnouncementViewModel viewModel;
    private AnnouncementAdapter adapter;
    private UserRepository userRepository;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        initializeViews(view);
        loadUserName();
        initializeViewModel();
        setupRecyclerView();
        setupListeners();
        observeViewModel();

        return view;
    }

    private void initializeViews(View view) {
        txtUserName = view.findViewById(R.id.txtUserName);
        txtEmpty = view.findViewById(R.id.txtEmpty);
        recyclerAnnouncements = view.findViewById(R.id.recyclerAnnouncements);
        progressBar = view.findViewById(R.id.progressBar);
        btnViewAll = view.findViewById(R.id.btnViewAll);
        btnMenu = view.findViewById(R.id.btnMenu);
    }

    private void loadUserName() {
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
            // Tenta obter do PreferencesHelper
            try {
                PreferencesHelper preferencesHelper = new PreferencesHelper(getContext());
                String userName = preferencesHelper.getUserName();
                if (userName != null && !userName.isEmpty()) {
                    txtUserName.setText(userName);
                } else {
                    txtUserName.setText("Usuário");
                }
            } catch (Exception e) {
                txtUserName.setText("Usuário");
            }
        }
    }

    private void initializeViewModel() {
        viewModel = new ViewModelProvider(this).get(AnnouncementViewModel.class);
    }

    private void setupRecyclerView() {
        adapter = new AnnouncementAdapter(new ArrayList<>());
        adapter.setOnAnnouncementClickListener(announcement -> {
            Intent intent = new Intent(getContext(), AnnouncementDetailActivity.class);
            intent.putExtra(AnnouncementDetailActivity.EXTRA_ANNOUNCEMENT_ID, announcement.getId());
            startActivity(intent);
        });

        recyclerAnnouncements.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerAnnouncements.setAdapter(adapter);
    }

    private void setupListeners() {
        btnMenu.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), MenuOptionsActivity.class);
            startActivity(intent);
        });

        btnViewAll.setOnClickListener(v -> {
            // Navega para a aba de Anúncios
            if (getActivity() != null) {
                com.google.android.material.bottomnavigation.BottomNavigationView bottomNav = 
                    requireActivity().findViewById(R.id.bottomNavigation);
                if (bottomNav != null) {
                    bottomNav.setSelectedItemId(R.id.nav_announcements);
                }
            }
        });
    }

    private void observeViewModel() {
        // Observa anúncios
        viewModel.getAnnouncements().observe(getViewLifecycleOwner(), announcements -> {
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    if (adapter == null || recyclerAnnouncements == null || txtEmpty == null) return;

                    if (announcements != null && !announcements.isEmpty()) {
                        // Mostra apenas os 5 mais recentes
                        List<Announcement> recentAnnouncements = announcements.size() > 5 
                            ? announcements.subList(0, 5) 
                            : announcements;
                        adapter.updateAnnouncements(recentAnnouncements);
                        txtEmpty.setVisibility(View.GONE);
                        recyclerAnnouncements.setVisibility(View.VISIBLE);
                    } else {
                        txtEmpty.setVisibility(View.VISIBLE);
                        recyclerAnnouncements.setVisibility(View.GONE);
                    }
                    progressBar.setVisibility(View.GONE);
                });
            }
        });

        // Observa loading
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading != null && isLoading) {
                progressBar.setVisibility(View.VISIBLE);
            } else {
                progressBar.setVisibility(View.GONE);
            }
        });

        // Observa erros
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null && !error.isEmpty()) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // Recarrega dados quando o fragment retoma
        if (viewModel != null) {
            viewModel.loadAnnouncements();
        }
    }
}
