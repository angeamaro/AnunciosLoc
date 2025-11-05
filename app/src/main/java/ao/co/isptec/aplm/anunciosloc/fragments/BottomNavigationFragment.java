package ao.co.isptec.aplm.anunciosloc.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import ao.co.isptec.aplm.anunciosloc.MainActivity;
import ao.co.isptec.aplm.anunciosloc.R;

public class BottomNavigationFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflar o layout
        View view = inflater.inflate(R.layout.fragment_bottom_navigation, container, false);

        // Referenciar os botões
        ImageButton btnHome = view.findViewById(R.id.btnHome);
        ImageButton btnAnuncios = view.findViewById(R.id.btnAnuncios);
        FloatingActionButton btnAdd = view.findViewById(R.id.btnAdd);
        ImageButton btnNotifications = view.findViewById(R.id.btnNotifications);
        ImageButton btnProfile = view.findViewById(R.id.btnProfile);

        // Configurar ações dos botões
        btnHome.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).navigateToHome();
            }
        });
        
        btnAnuncios.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).navigateToHome();
            }
        });
        
        btnAdd.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(getContext(), ao.co.isptec.aplm.anunciosloc.ui.view.CreateAnnouncementActivity.class);
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
                showToast("Erro ao abrir tela de criação");
            }
        });
        
        btnNotifications.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).navigateToNotifications();
            }
        });
        
        btnProfile.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).navigateToLocations();
            }
        });

        return view;
    }

    private void showToast(String msg) {
        if (getContext() != null)
            android.widget.Toast.makeText(getContext(), msg, android.widget.Toast.LENGTH_SHORT).show();
    }
}
