package ao.co.isptec.aplm.anunciosloc.ui.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;



import java.util.ArrayList;
import java.util.List;

import ao.co.isptec.aplm.anunciosloc.R;
import ao.co.isptec.aplm.anunciosloc.data.model.Location;
import ao.co.isptec.aplm.anunciosloc.ui.adapter.LocalAdapter;

public class LocalFragment extends Fragment {

    private RecyclerView recycler;
    private LocalAdapter adapter;
    private List<Location> locationList;

    public LocalFragment() {
        // Construtor público vazio necessário para o Fragment
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_locals, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inicializa RecyclerView
        recycler = view.findViewById(R.id.recyclerAnnouncements);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));

        // Lista de exemplo
        locationList = new ArrayList<>();

        Location escolaA = new Location("1", "Escola A", -8.839, 13.289, 50);
        escolaA.setSsid("EscolaWiFi"); // Wi-Fi
        locationList.add(escolaA);

        Location escritorioB = new Location("2", "Escritório B", -8.840, 13.290, 30);
        escritorioB.setSsid(""); // GPS
        locationList.add(escritorioB);

        adapter = new LocalAdapter(locationList);
        recycler.setAdapter(adapter);

        // Botão de voltar
        ImageButton btnBack = view.findViewById(R.id.btnBack);

    }
}
