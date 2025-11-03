package ao.co.isptec.aplm.anunciosloc.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
        ImageButton btnAdd = view.findViewById(R.id.btnAdd);
        ImageButton btnNotifications = view.findViewById(R.id.btnNotifications);
        ImageButton btnProfile = view.findViewById(R.id.btnProfile);

        // Exemplo: ações simples
        btnHome.setOnClickListener(v ->
                showToast("Home clicado"));
        btnAnuncios.setOnClickListener(v ->
                showToast("Anúncios clicado"));
        btnAdd.setOnClickListener(v ->
                showToast("Adicionar novo anúncio"));
        btnNotifications.setOnClickListener(v ->
                showToast("Notificações clicado"));
        btnProfile.setOnClickListener(v ->
                showToast("Perfil clicado"));

        return view;
    }

    private void showToast(String msg) {
        if (getContext() != null)
            android.widget.Toast.makeText(getContext(), msg, android.widget.Toast.LENGTH_SHORT).show();
    }
}
