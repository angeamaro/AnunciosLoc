package ao.co.isptec.aplm.anunciosloc.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ao.co.isptec.aplm.anunciosloc.R;

/**
 * Adapter para selecionar atributos de perfil (pares chave-valor)
 */
public class ProfileAttributeAdapter extends RecyclerView.Adapter<ProfileAttributeAdapter.ViewHolder> {
    
    private List<ProfileAttribute> attributes;
    private Map<String, String> selectedAttributes; // chave -> valor
    
    public static class ProfileAttribute {
        public String key;
        public String value;
        public String displayText;
        
        public ProfileAttribute(String key, String value, String displayText) {
            this.key = key;
            this.value = value;
            this.displayText = displayText;
        }
    }
    
    public ProfileAttributeAdapter() {
        this.attributes = new ArrayList<>();
        this.selectedAttributes = new HashMap<>();
    }
    
    public void setAttributes(List<ProfileAttribute> attributes) {
        this.attributes = attributes;
        notifyDataSetChanged();
    }
    
    public void setSelectedAttributes(Map<String, String> selected) {
        if (selected != null) {
            this.selectedAttributes = new HashMap<>(selected);
            notifyDataSetChanged();
        }
    }
    
    public Map<String, String> getSelectedAttributes() {
        return new HashMap<>(selectedAttributes);
    }
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_profile_attribute, parent, false);
        return new ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProfileAttribute attribute = attributes.get(position);
        
        holder.tvAttributeName.setText(attribute.displayText);
        holder.tvAttributeKey.setText(attribute.key + " = " + attribute.value);
        
        // Verifica se está selecionado
        boolean isSelected = selectedAttributes.containsKey(attribute.key) &&
                selectedAttributes.get(attribute.key).equals(attribute.value);
        holder.checkbox.setChecked(isSelected);
        
        // Listener de seleção
        holder.checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                selectedAttributes.put(attribute.key, attribute.value);
            } else {
                selectedAttributes.remove(attribute.key);
            }
        });
        
        holder.itemView.setOnClickListener(v -> {
            holder.checkbox.setChecked(!holder.checkbox.isChecked());
        });
    }
    
    @Override
    public int getItemCount() {
        return attributes.size();
    }
    
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvAttributeName;
        TextView tvAttributeKey;
        CheckBox checkbox;
        
        ViewHolder(View itemView) {
            super(itemView);
            tvAttributeName = itemView.findViewById(R.id.tvAttributeName);
            tvAttributeKey = itemView.findViewById(R.id.tvAttributeKey);
            checkbox = itemView.findViewById(R.id.checkbox);
        }
    }
}
