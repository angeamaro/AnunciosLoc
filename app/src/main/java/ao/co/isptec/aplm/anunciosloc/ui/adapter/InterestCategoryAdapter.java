package ao.co.isptec.aplm.anunciosloc.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import ao.co.isptec.aplm.anunciosloc.R;
import ao.co.isptec.aplm.anunciosloc.data.model.InterestCategory;
import java.util.ArrayList;
import java.util.List;

public class InterestCategoryAdapter extends RecyclerView.Adapter<InterestCategoryAdapter.CategoryViewHolder> {

    private List<InterestCategory> categories;
    private OnCategoryClickListener listener;

    public interface OnCategoryClickListener {
        void onCategoryClick(InterestCategory category);
    }

    public InterestCategoryAdapter(OnCategoryClickListener listener) {
        this.categories = new ArrayList<>();
        this.listener = listener;
    }

    public void setCategories(List<InterestCategory> categories) {
        this.categories = categories;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_interest_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        InterestCategory category = categories.get(position);
        holder.bind(category);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgIcon;
        private TextView txtCategoryName;
        private TextView txtItemCount;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            imgIcon = itemView.findViewById(R.id.imgIcon);
            txtCategoryName = itemView.findViewById(R.id.txtCategoryName);
            txtItemCount = itemView.findViewById(R.id.txtItemCount);
        }

        public void bind(InterestCategory category) {
            txtCategoryName.setText(category.getName());
            txtItemCount.setText(category.getValuesCount() + " itens");
            imgIcon.setImageResource(category.getIconRes());

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onCategoryClick(category);
                }
            });
        }
    }
}
