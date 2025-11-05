package ao.co.isptec.aplm.anunciosloc.ui.view.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ao.co.isptec.aplm.anunciosloc.R;

/**
 * Adapter para exibir atributos de perfil em RecyclerView
 */
public class AttributeAdapter extends RecyclerView.Adapter<AttributeAdapter.ViewHolder> {

    private List<Map.Entry<String, String>> attributes;
    private OnAttributeDeleteListener deleteListener;

    public interface OnAttributeDeleteListener {
        void onDeleteAttribute(String key, String value);
    }

    public AttributeAdapter(List<Map.Entry<String, String>> attributes) {
        this.attributes = attributes != null ? attributes : new ArrayList<>();
    }

    public void setOnAttributeDeleteListener(OnAttributeDeleteListener listener) {
        this.deleteListener = listener;
    }

    public void updateAttributes(Map<String, String> attributesMap) {
        this.attributes = new ArrayList<>(attributesMap.entrySet());
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_attribute, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Map.Entry<String, String> entry = attributes.get(position);
        String key = entry.getKey();
        String value = entry.getValue();
        
        holder.txtKey.setText(key);
        holder.txtValue.setText(value);
        
        // Click listener para delete
        holder.imgDelete.setOnClickListener(v -> {
            if (deleteListener != null) {
                deleteListener.onDeleteAttribute(key, value);
            }
        });
    }

    @Override
    public int getItemCount() {
        return attributes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtKey, txtValue;
        ImageView imgDelete;
        
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtKey = itemView.findViewById(R.id.txtKey);
            txtValue = itemView.findViewById(R.id.txtValue);
            imgDelete = itemView.findViewById(R.id.imgDelete);
        }
    }
}
