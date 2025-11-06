package ao.co.isptec.aplm.anunciosloc;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import ao.co.isptec.aplm.anunciosloc.ui.view.LoginActivity;
import ao.co.isptec.aplm.anunciosloc.ui.view.fragments.AnnouncementsFragment;
import ao.co.isptec.aplm.anunciosloc.ui.view.fragments.HomeFragment;
import ao.co.isptec.aplm.anunciosloc.ui.view.fragments.LocationsFragment;
import ao.co.isptec.aplm.anunciosloc.ui.view.fragments.NotificationsFragment;
import ao.co.isptec.aplm.anunciosloc.ui.view.fragments.ProfileFragment;
import ao.co.isptec.aplm.anunciosloc.utils.PreferencesHelper;

/**
 * MainActivity - Tela principal com BottomNavigation
 */
public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private FragmentManager fragmentManager;
    private PreferencesHelper preferencesHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        try {
            setContentView(R.layout.activity_main_new);

            // Inicializa PreferencesHelper
            preferencesHelper = new PreferencesHelper(this);

            // Verifica se usuário está logado
            if (!preferencesHelper.isUserLoggedIn()) {
                redirectToLogin();
                return;
            }

            // Esconde ActionBar
            if (getSupportActionBar() != null) {
                getSupportActionBar().hide();
            }

            initializeViews();
            setupBottomNavigation();
            setupBackPressHandler();

            // Carrega fragment inicial (Home)
            if (savedInstanceState == null) {
                // Verifica se há navegação específica do Intent
                String navigateTo = getIntent().getStringExtra("navigate_to");
                if (navigateTo != null) {
                    switch (navigateTo) {
                        case "announcements":
                            loadFragment(new AnnouncementsFragment(), "announcements");
                            bottomNavigationView.setSelectedItemId(R.id.nav_announcements);
                            break;
                        case "notifications":
                            loadFragment(new NotificationsFragment(), "notifications");
                            bottomNavigationView.setSelectedItemId(R.id.nav_notifications);
                            break;
                        case "locations":
                            loadFragment(new LocationsFragment(), "locations");
                            bottomNavigationView.setSelectedItemId(R.id.nav_locations);
                            break;
                        default:
                            loadFragment(new HomeFragment(), "home");
                            bottomNavigationView.setSelectedItemId(R.id.nav_home);
                            break;
                    }
                } else {
                    loadFragment(new HomeFragment(), "home");
                    bottomNavigationView.setSelectedItemId(R.id.nav_home);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Se houver erro, redireciona para login
            redirectToLogin();
        }
    }

    private void initializeViews() {
        bottomNavigationView = findViewById(R.id.bottomNavigation);
        fragmentManager = getSupportFragmentManager();
    }

    private void setupBottomNavigation() {
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                String tag = "";

                int itemId = item.getItemId();

                if (itemId == R.id.nav_home) {
                    // Home mostra HomeFragment (Bem-vindo + Anúncios Recentes)
                    fragment = new HomeFragment();
                    tag = "home";
                } else if (itemId == R.id.nav_announcements) {
                    // Anúncios mostra AnnouncementsFragment (Guardados/Todos)
                    fragment = new AnnouncementsFragment();
                    tag = "announcements";
                } else if (itemId == R.id.nav_create) {
                    // Botão criar - abre CreateAnnouncementActivity
                    try {
                        Intent intent = new Intent(MainActivity.this, ao.co.isptec.aplm.anunciosloc.ui.view.CreateAnnouncementActivity.class);
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                } else if (itemId == R.id.nav_notifications) {
                    fragment = new NotificationsFragment();
                    tag = "notifications";
                } else if (itemId == R.id.nav_locations) {
                    fragment = new LocationsFragment();
                    tag = "locations";
                }

                if (fragment != null) {
                    loadFragment(fragment, tag);
                    return true;
                }

                return false;
            }
        });
    }

    private void setupBackPressHandler() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Previne voltar para tela de login após logout
                if (fragmentManager.getBackStackEntryCount() > 0) {
                    fragmentManager.popBackStack();
                } else {
                    // Remove callback e chama o comportamento padrão
                    setEnabled(false);
                    getOnBackPressedDispatcher().onBackPressed();
                }
            }
        });
    }

    private void loadFragment(Fragment fragment, String tag) {
        try {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.fragmentContainer, fragment, tag);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            // Em caso de erro, tenta recarregar o fragment de anúncios
            try {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragmentContainer, new AnnouncementsFragment(), "announcements");
                transaction.commit();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    
    // Métodos públicos para navegação entre fragments
    public void navigateToLocations() {
        loadFragment(new LocationsFragment(), "locations");
        bottomNavigationView.setSelectedItemId(R.id.nav_locations);
    }
    
    public void navigateToNotifications() {
        loadFragment(new NotificationsFragment(), "notifications");
        bottomNavigationView.setSelectedItemId(R.id.nav_notifications);
    }
    
    public void navigateToHome() {
        loadFragment(new AnnouncementsFragment(), "home");
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
    }

    private void redirectToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Verifica novamente se usuário está logado
        if (!preferencesHelper.isUserLoggedIn()) {
            redirectToLogin();
        }
    }
}