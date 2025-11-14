package ao.co.isptec.aplm.anunciosloc.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ao.co.isptec.aplm.anunciosloc.R;
import ao.co.isptec.aplm.anunciosloc.data.model.User;

/**
 * Adapter para selecionar usuários, adaptado ao novo modelo User.
 */
public class UserSelectionAdapter extends RecyclerView.Adapter<UserSelectionAdapter.UserViewHolder> {
    
    private List<User> users;
    private Set<Long> selectedIds; // Alterado de String para Long
    
    public UserSelectionAdapter(List<User> users, List<Long> initialSelectedIds) {
        this.users = users;
        this.selectedIds = new HashSet<>(initialSelectedIds);
    }
    
    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_user_selection, parent, false);
        return new UserViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = users.get(position);
        holder.bind(user);
    }
    
    @Override
    public int getItemCount() {
        return users.size();
    }
    
    public void updateUsers(List<User> newUsers) {
        this.users = newUsers;
        notifyDataSetChanged();
    }
    
    public List<Long> getSelectedIds() { // Renomeado e tipo de retorno alterado
        return new ArrayList<>(selectedIds);
    }
    
    class UserViewHolder extends RecyclerView.ViewHolder {
        private TextView txtUserName;
        private CheckBox checkBox;
        
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            txtUserName = itemView.findViewById(R.id.txtUserName);
            checkBox = itemView.findViewById(R.id.checkBox);
            
            // Os campos de email e public key serão removidos do layout a seguir
        }
        
        public void bind(User user) {
            txtUserName.setText(user.getUsername());
            
            // Define se está selecionado com base no ID
            checkBox.setChecked(selectedIds.contains(user.getId()));
            
            // Listener para seleção
            itemView.setOnClickListener(v -> {
                // Inverte o estado do checkbox ao clicar em qualquer sítio do item
                checkBox.setChecked(!checkBox.isChecked());
            });
            
            // Listener para quando o estado do checkbox muda
            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    selectedIds.add(user.getId());
                } else {
                    selectedIds.remove(user.getId());
                }
            });
        }
    }
}
