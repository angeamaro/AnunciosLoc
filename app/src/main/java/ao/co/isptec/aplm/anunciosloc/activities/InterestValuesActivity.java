package ao.co.isptec.aplm.anunciosloc.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ao.co.isptec.aplm.anunciosloc.R;
import ao.co.isptec.aplm.anunciosloc.adapters.InterestValueAdapter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class InterestValuesActivity extends AppCompatActivity {

    private TextView txtCategoryName;
    private TextView txtSelectedCount;
    private RecyclerView recyclerValues;
    private LinearLayout emptyState;
    private InterestValueAdapter adapter;
    
    private String categoryId;
    private String categoryName;
    private List<String> categoryValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interest_values);

        getIntentData();
        initializeViews();
        setupRecyclerView();
        loadValues();
    }

    private void getIntentData() {
        categoryId = getIntent().getStringExtra("category_id");
        categoryName = getIntent().getStringExtra("category_name");
        categoryValues = getIntent().getStringArrayListExtra("category_values");
        
        if (categoryValues == null) {
            categoryValues = new ArrayList<>();
        }
    }

    private void initializeViews() {
        ImageButton btnBack = findViewById(R.id.btnBack);
        ImageButton btnSave = findViewById(R.id.btnSave);
        txtCategoryName = findViewById(R.id.txtCategoryName);
        txtSelectedCount = findViewById(R.id.txtSelectedCount);
        recyclerValues = findViewById(R.id.recyclerValues);
        emptyState = findViewById(R.id.emptyState);

        txtCategoryName.setText(categoryName);

        btnBack.setOnClickListener(v -> onBackPressed());
        btnSave.setOnClickListener(v -> saveSelections());
    }

    private void setupRecyclerView() {
        adapter = new InterestValueAdapter(selectedCount -> {
            updateSelectedCount(selectedCount);
        });

        recyclerValues.setLayoutManager(new LinearLayoutManager(this));
        recyclerValues.setAdapter(adapter);
    }

    private void loadValues() {
        if (categoryValues.isEmpty()) {
            recyclerValues.setVisibility(View.GONE);
            emptyState.setVisibility(View.VISIBLE);
        } else {
            recyclerValues.setVisibility(View.VISIBLE);
            emptyState.setVisibility(View.GONE);
            adapter.setValues(categoryValues);
            
            // Load previously saved selections
            Set<String> savedSelections = loadSavedSelections();
            adapter.setSelectedValues(savedSelections);
            updateSelectedCount(savedSelections.size());
        }
    }

    private Set<String> loadSavedSelections() {
        SharedPreferences prefs = getSharedPreferences("interests_prefs", MODE_PRIVATE);
        return prefs.getStringSet("interest_" + categoryId, new HashSet<>());
    }

    private void saveSelections() {
        Set<String> selectedValues = adapter.getSelectedValues();
        
        SharedPreferences prefs = getSharedPreferences("interests_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putStringSet("interest_" + categoryId, selectedValues);
        editor.apply();

        Toast.makeText(this, "Interesses salvos com sucesso!", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void updateSelectedCount(int count) {
        txtSelectedCount.setText(count + " selecionados");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
