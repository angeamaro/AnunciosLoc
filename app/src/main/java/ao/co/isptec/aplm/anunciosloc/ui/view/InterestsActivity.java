package ao.co.isptec.aplm.anunciosloc.ui.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ao.co.isptec.aplm.anunciosloc.R;
import ao.co.isptec.aplm.anunciosloc.ui.adapter.InterestCategoryAdapter;
import ao.co.isptec.aplm.anunciosloc.data.InterestsData;
import ao.co.isptec.aplm.anunciosloc.data.model.InterestCategory;
import java.util.List;

public class InterestsActivity extends AppCompatActivity {

    private RecyclerView recyclerCategories;
    private LinearLayout emptyState;
    private InterestCategoryAdapter adapter;
    private List<InterestCategory> categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interests);

        initializeViews();
        setupRecyclerView();
        loadCategories();
    }

    private void initializeViews() {
        ImageButton btnBack = findViewById(R.id.btnBack);
        recyclerCategories = findViewById(R.id.recyclerCategories);
        emptyState = findViewById(R.id.emptyState);

        btnBack.setOnClickListener(v -> onBackPressed());
    }

    private void setupRecyclerView() {
        adapter = new InterestCategoryAdapter(category -> {
            // Open values screen
            Intent intent = new Intent(InterestsActivity.this, InterestValuesActivity.class);
            intent.putExtra("category_id", category.getId());
            intent.putExtra("category_name", category.getName());
            intent.putStringArrayListExtra("category_values", 
                    new java.util.ArrayList<>(category.getValues()));
            startActivity(intent);
        });

        recyclerCategories.setLayoutManager(new LinearLayoutManager(this));
        recyclerCategories.setAdapter(adapter);
    }

    private void loadCategories() {
        categories = InterestsData.getPredefinedInterests();
        
        if (categories.isEmpty()) {
            recyclerCategories.setVisibility(View.GONE);
            emptyState.setVisibility(View.VISIBLE);
        } else {
            recyclerCategories.setVisibility(View.VISIBLE);
            emptyState.setVisibility(View.GONE);
            adapter.setCategories(categories);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
