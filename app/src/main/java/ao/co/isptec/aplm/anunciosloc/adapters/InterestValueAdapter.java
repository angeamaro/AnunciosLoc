package ao.co.isptec.aplm.anunciosloc.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import ao.co.isptec.aplm.anunciosloc.R;
import com.google.android.material.checkbox.MaterialCheckBox;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class InterestValueAdapter extends RecyclerView.Adapter<InterestValueAdapter.ValueViewHolder> {

    private List<String> values;
    private Set<String> selectedValues;
    private OnSelectionChangedListener listener;

    public interface OnSelectionChangedListener {
        void onSelectionChanged(int selectedCount);
    }

    public InterestValueAdapter(OnSelectionChangedListener listener) {
        this.values = new ArrayList<>();
        this.selectedValues = new HashSet<>();
        this.listener = listener;
    }

    public void setValues(List<String> values) {
        this.values = values;
        notifyDataSetChanged();
    }

    public void setSelectedValues(Set<String> selectedValues) {
        this.selectedValues = selectedValues != null ? new HashSet<>(selectedValues) : new HashSet<>();
        notifyDataSetChanged();
    }

    public Set<String> getSelectedValues() {
        return new HashSet<>(selectedValues);
    }

    @NonNull
    @Override
    public ValueViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_interest_value, parent, false);
        return new ValueViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ValueViewHolder holder, int position) {
        String value = values.get(position);
        holder.bind(value);
    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    class ValueViewHolder extends RecyclerView.ViewHolder {
        private MaterialCheckBox checkbox;
        private TextView txtValueName;

        public ValueViewHolder(@NonNull View itemView) {
            super(itemView);
            checkbox = itemView.findViewById(R.id.checkbox);
            txtValueName = itemView.findViewById(R.id.txtValueName);
        }

        public void bind(String value) {
            txtValueName.setText(value);
            checkbox.setChecked(selectedValues.contains(value));

            // Click listeners for both checkbox and card
            View.OnClickListener clickListener = v -> {
                if (selectedValues.contains(value)) {
                    selectedValues.remove(value);
                    checkbox.setChecked(false);
                } else {
                    selectedValues.add(value);
                    checkbox.setChecked(true);
                }
                if (listener != null) {
                    listener.onSelectionChanged(selectedValues.size());
                }
            };

            itemView.setOnClickListener(clickListener);
            checkbox.setOnClickListener(clickListener);
        }
    }
}
