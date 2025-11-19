package ao.co.isptec.aplm.anunciosloc.ui.view;

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

import ao.co.isptec.aplm.anunciosloc.R;

import ao.co.isptec.aplm.anunciosloc.ui.view.fragment.AnnouncementsFragment;
import ao.co.isptec.aplm.anunciosloc.ui.view.fragment.HomeFragment;
import ao.co.isptec.aplm.anunciosloc.ui.view.fragment.LocationsFragment;
import ao.co.isptec.aplm.anunciosloc.ui.view.fragment.NotificationsFragment;
import ao.co.isptec.aplm.anunciosloc.utils.PreferencesHelper;

/**
 * MainActivity - Tela principal com BottomNavigation
 */
public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Verifica se usuário está logado ANTES de carregar o layout
        if (!PreferencesHelper.isLoggedIn(this)) {
            redirectToLogin();
            return;
        }

        setContentView(R.layout.activity_main_new);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        initializeViews();
        setupBottomNavigation();
        setupBackPressHandler();

        if (savedInstanceState == null) {
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
    }

    private void initializeViews() {
        bottomNavigationView = findViewById(R.id.bottomNavigation);
        fragmentManager = getSupportFragmentManager();
    }

    private void setupBottomNavigation() {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment fragment = null;
            String tag = "";
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                fragment = new HomeFragment();
                tag = "home";
            } else if (itemId == R.id.nav_announcements) {
                fragment = new AnnouncementsFragment();
                tag = "announcements";
            } else if (itemId == R.id.nav_create) {
                startActivity(new Intent(MainActivity.this, CreateAnnouncementActivity.class));
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
        });
    }

    private void setupBackPressHandler() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (fragmentManager.getBackStackEntryCount() > 0) {
                    fragmentManager.popBackStack();
                } else {
                    setEnabled(false);
                    getOnBackPressedDispatcher().onBackPressed();
                }
            }
        });
    }

    private void loadFragment(Fragment fragment, String tag) {
        fragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment, tag)
            .commit();
    }

    public void navigateToLocations() {
        loadFragment(new LocationsFragment(), "locations");
        bottomNavigationView.setSelectedItemId(R.id.nav_locations);
    }
    
    public void navigateToNotifications() {
        loadFragment(new NotificationsFragment(), "notifications");
        bottomNavigationView.setSelectedItemId(R.id.nav_notifications);
    }
    
    public void navigateToHome() {
        loadFragment(new HomeFragment(), "home");
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
        if (!PreferencesHelper.isLoggedIn(this)) {
            redirectToLogin();
        }
    }
}
