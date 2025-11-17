package ao.co.isptec.aplm.anunciosloc.ui.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import ao.co.isptec.aplm.anunciosloc.R;
import ao.co.isptec.aplm.anunciosloc.data.model.Location;
import ao.co.isptec.aplm.anunciosloc.ui.adapter.LocalAdapter;
import ao.co.isptec.aplm.anunciosloc.ui.view.AddLocationActivity;
import ao.co.isptec.aplm.anunciosloc.ui.view.MenuOptionsActivity;

public class LocalFragment extends Fragment {

    private RecyclerView recycler;
    private LocalAdapter adapter;
    private List<Location> locationList;
    private final String currentUserId = "usuario123";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_locals, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recycler = view.findViewById(R.id.recyclerAnnouncements);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));

        locationList = new ArrayList<>();
        criarDadosExemplo();

        adapter = new LocalAdapter(locationList);
        recycler.setAdapter(adapter);

        // LISTENERS DOS BOTÕES DO HEADER
        setupHeaderButtons(view);

        // LISTENER DAS TABS (Meus / Outros)
        setupTabLayout(view);

        // LISTENER DOS CARDS (EDITAR / ELIMINAR)
        adapter.setOnLocalClickListener(new LocalAdapter.OnLocalClickListener() {
            @Override
            public void onEditClick(Location local) {
                mostrarDialogEdicao(local);
            }

            @Override
            public void onDeleteClick(Location local) {
                mostrarDialogConfirmacao(local);
            }
        });

        // Inicia mostrando só os meus locais
        adapter.filterByMeusLocais(currentUserId);
    }

    private void setupHeaderButtons(View view) {
        // BOTÃO VOLTAR
        ImageButton btnBack = view.findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> requireActivity().finish());
        }

        // BOTÃO ADICIONAR LOCAL (+)
        ImageButton btnAddLocation = view.findViewById(R.id.btnAddLocation);
        if (btnAddLocation != null) {
            btnAddLocation.setOnClickListener(v -> {
                Intent intent = new Intent(requireActivity(), AddLocationActivity.class);
                startActivity(intent);
                // Animação suave de entrada
            });
        }

        // BOTÃO MENU (3 pontinhos)
        ImageButton btnMenu = view.findViewById(R.id.btnMenu);
        if (btnMenu != null) {
            btnMenu.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), MenuOptionsActivity.class);
                intent.putExtra("ORIGEM", "LOCAL");
                startActivity(intent);
            });
        }
    }

    private void setupTabLayout(View view) {
        TabLayout tabLayout = view.findViewById(R.id.tabMeusOutrosLocais);
        if (tabLayout != null) {
            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    if (tab.getPosition() == 0) {
                        adapter.filterByMeusLocais(currentUserId);
                    } else {
                        adapter.filterByOutrosLocais(currentUserId);
                    }
                }

                @Override public void onTabUnselected(TabLayout.Tab tab) {}
                @Override public void onTabReselected(TabLayout.Tab tab) {}
            });
        }
    }

    private void criarDadosExemplo() {
        locationList.clear();

        // ====================== MEUS LOCAIS ======================
        Location l1 = new Location("1", "Escola Secundária do Kikolo", -8.839, 13.289, 50);
        l1.setSsid("KikoloWiFi"); l1.setCreatedBy(currentUserId);
        locationList.add(l1);

        Location l2 = new Location("2", "Universidade Metodista", -8.825, 13.235, 0);
        l2.setSsid(""); l2.setCreatedBy(currentUserId);
        locationList.add(l2);

        Location l3 = new Location("3", "Café Central", -8.813, 13.230, 30);
        l3.setSsid("CafeCentral_Free"); l3.setCreatedBy(currentUserId);
        locationList.add(l3);

        Location l4 = new Location("4", "Praça da Independência", -8.817, 13.223, 0);
        l4.setSsid(""); l4.setCreatedBy(currentUserId);
        locationList.add(l4);

        Location l5 = new Location("5", "Shopping Avenida", -8.901, 13.190, 100);
        l5.setSsid("Shopping_Avenida"); l5.setCreatedBy(currentUserId);
        locationList.add(l5);

        // ====================== LOCAIS DE OUTROS ======================
        Location o1 = new Location("6", "Restaurante Mar e Sol", -8.789, 13.245, 40);
        o1.setSsid("MarSol_Guest"); o1.setCreatedBy("usuario999");
        locationList.add(o1);

        Location o2 = new Location("7", "Biblioteca Nacional", -8.810, 13.228, 0);
        o2.setSsid(""); o2.setCreatedBy("admin_biblio");
        locationList.add(o2);

        Location o3 = new Location("8", "Parque da Juventude", -8.835, 13.265, 0);
        o3.setSsid(""); o3.setCreatedBy("ze_pedro_22");
        locationList.add(o3);
    }

    private void mostrarDialogEdicao(Location local) {
        EditText input = new EditText(requireContext());
        input.setText(local.getName());
        input.setSelection(input.getText().length());
        input.setPadding(60, 50, 60, 50);

        new MaterialAlertDialogBuilder(requireContext(), R.style.CustomAlertDialog)
                .setTitle("Editar Local")
                .setView(input)
                .setNegativeButton("Cancelar", null)
                .setPositiveButton("Guardar", (dialog, which) -> {
                    String novoNome = input.getText().toString().trim();
                    if (!novoNome.isEmpty()) {
                        local.setName(novoNome);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(requireContext(), "Nome atualizado!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(requireContext(), "O nome não pode estar vazio!", Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }

    private void mostrarDialogConfirmacao(Location local) {
        new MaterialAlertDialogBuilder(requireContext(), R.style.CustomAlertDialog)
                .setTitle("Eliminar Local")
                .setMessage("Tem certeza que deseja eliminar este local?\nEsta ação não pode ser desfeita.")
                .setNegativeButton("Não", null)
                .setPositiveButton("Sim", (d, w) -> {
                    adapter.removeItem(local);
                    Toast.makeText(requireContext(), "Local eliminado!", Toast.LENGTH_SHORT).show();
                })
                .show();
    }
}