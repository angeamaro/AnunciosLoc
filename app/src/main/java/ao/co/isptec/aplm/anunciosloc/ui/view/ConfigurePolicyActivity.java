package ao.co.isptec.aplm.anunciosloc.ui.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ao.co.isptec.aplm.anunciosloc.R;
import ao.co.isptec.aplm.anunciosloc.data.model.PolicyFilter;
import ao.co.isptec.aplm.anunciosloc.ui.adapter.ProfileAttributeAdapter;
import ao.co.isptec.aplm.anunciosloc.utils.Constants;

/**
 * Activity para configurar regras de whitelist/blacklist baseadas em atributos de perfil
 */
public class ConfigurePolicyActivity extends AppCompatActivity {
    
    public static final String EXTRA_POLICY_TYPE = "policy_type";
    public static final String EXTRA_POLICY_FILTER = "policy_filter";
    
    private MaterialToolbar toolbar;
    private TextView tvDescription;
    private RecyclerView recyclerViewAttributes;
    private MaterialButton btnConfirm;
    
    private ProfileAttributeAdapter adapter;
    private String policyType;
    private Map<String, String> selectedAttributes = new HashMap<>();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure_policy);
        
        // Pega o tipo de política (whitelist ou blacklist)
        policyType = getIntent().getStringExtra(EXTRA_POLICY_TYPE);
        if (policyType == null) {
            policyType = Constants.DELIVERY_POLICY_WHITELIST;
        }
        
        // Recupera filtro previamente configurado (se houver)
        PolicyFilter previousFilter = (PolicyFilter) getIntent().getSerializableExtra(EXTRA_POLICY_FILTER);
        if (previousFilter != null && previousFilter.getAttributes() != null) {
            selectedAttributes = new HashMap<>(previousFilter.getAttributes());
        }
        
        initializeViews();
        setupToolbar();
        setupRecyclerView();
        setupListeners();
        loadAvailableAttributes();
    }
    
    private void initializeViews() {
        toolbar = findViewById(R.id.toolbar);
        tvDescription = findViewById(R.id.tvDescription);
        recyclerViewAttributes = findViewById(R.id.recyclerViewAttributes);
        btnConfirm = findViewById(R.id.btnConfirm);
    }
    
    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            
            String title = policyType.equals(Constants.DELIVERY_POLICY_WHITELIST) 
                ? "Configurar Whitelist" 
                : "Configurar Blacklist";
            getSupportActionBar().setTitle(title);
        }
        
        toolbar.setNavigationOnClickListener(v -> finish());
        
        // Atualiza descrição
        String description = policyType.equals(Constants.DELIVERY_POLICY_WHITELIST)
            ? "Selecione os atributos de perfil que os usuários devem ter para receber este anúncio"
            : "Selecione os atributos de perfil que impedirão usuários de receber este anúncio";
        tvDescription.setText(description);
    }
    
    private void setupRecyclerView() {
        adapter = new ProfileAttributeAdapter();
        adapter.setSelectedAttributes(selectedAttributes);
        
        recyclerViewAttributes.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAttributes.setAdapter(adapter);
    }
    
    private void setupListeners() {
        btnConfirm.setOnClickListener(v -> {
            selectedAttributes = adapter.getSelectedAttributes();
            
            if (selectedAttributes.isEmpty()) {
                String message = policyType.equals(Constants.DELIVERY_POLICY_WHITELIST)
                    ? "Selecione pelo menos um atributo para a whitelist"
                    : "Selecione pelo menos um atributo para a blacklist";
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                return;
            }
            
            // Cria o filtro de política
            PolicyFilter filter = new PolicyFilter(policyType);
            filter.setAttributes(selectedAttributes);
            
            // Retorna o filtro
            Intent resultIntent = new Intent();
            resultIntent.putExtra(EXTRA_POLICY_FILTER, filter);
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }
    
    /**
     * Carrega os atributos de perfil disponíveis para seleção
     */
    private void loadAvailableAttributes() {
        List<ProfileAttributeAdapter.ProfileAttribute> attributes = new ArrayList<>();
        
        // Interesses
        attributes.add(new ProfileAttributeAdapter.ProfileAttribute(
            "interesse", "Tecnologia", "Tecnologia"));
        attributes.add(new ProfileAttributeAdapter.ProfileAttribute(
            "interesse", "Desporto", "Desporto"));
        attributes.add(new ProfileAttributeAdapter.ProfileAttribute(
            "interesse", "Música", "Música"));
        attributes.add(new ProfileAttributeAdapter.ProfileAttribute(
            "interesse", "Arte", "Arte"));
        attributes.add(new ProfileAttributeAdapter.ProfileAttribute(
            "interesse", "Ciência", "Ciência"));
        attributes.add(new ProfileAttributeAdapter.ProfileAttribute(
            "interesse", "Culinária", "Culinária"));
        
        // Profissões
        attributes.add(new ProfileAttributeAdapter.ProfileAttribute(
            "profissao", "Estudante", "Estudante"));
        attributes.add(new ProfileAttributeAdapter.ProfileAttribute(
            "profissao", "Professor", "Professor"));
        attributes.add(new ProfileAttributeAdapter.ProfileAttribute(
            "profissao", "Engenheiro", "Engenheiro"));
        attributes.add(new ProfileAttributeAdapter.ProfileAttribute(
            "profissao", "Médico", "Médico"));
        attributes.add(new ProfileAttributeAdapter.ProfileAttribute(
            "profissao", "Empresário", "Empresário"));
        
        // Clubes
        attributes.add(new ProfileAttributeAdapter.ProfileAttribute(
            "clube", "Benfica", "Benfica"));
        attributes.add(new ProfileAttributeAdapter.ProfileAttribute(
            "clube", "Porto", "Porto"));
        attributes.add(new ProfileAttributeAdapter.ProfileAttribute(
            "clube", "Sporting", "Sporting"));
        attributes.add(new ProfileAttributeAdapter.ProfileAttribute(
            "clube", "1º de Agosto", "1º de Agosto"));
        attributes.add(new ProfileAttributeAdapter.ProfileAttribute(
            "clube", "Petro de Luanda", "Petro de Luanda"));
        
        // Idade (faixas etárias)
        attributes.add(new ProfileAttributeAdapter.ProfileAttribute(
            "faixa_etaria", "18-24", "18-24 anos"));
        attributes.add(new ProfileAttributeAdapter.ProfileAttribute(
            "faixa_etaria", "25-34", "25-34 anos"));
        attributes.add(new ProfileAttributeAdapter.ProfileAttribute(
            "faixa_etaria", "35-44", "35-44 anos"));
        attributes.add(new ProfileAttributeAdapter.ProfileAttribute(
            "faixa_etaria", "45+", "45+ anos"));
        
        // Cidade
        attributes.add(new ProfileAttributeAdapter.ProfileAttribute(
            "cidade", "Luanda", "Luanda"));
        attributes.add(new ProfileAttributeAdapter.ProfileAttribute(
            "cidade", "Benguela", "Benguela"));
        attributes.add(new ProfileAttributeAdapter.ProfileAttribute(
            "cidade", "Huambo", "Huambo"));
        
        adapter.setAttributes(attributes);
    }
}
