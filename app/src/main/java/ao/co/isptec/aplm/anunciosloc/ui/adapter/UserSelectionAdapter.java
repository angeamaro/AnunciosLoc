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
 * Adapter para selecionar usuários em whitelist/blacklist
 */
public class UserSelectionAdapter extends RecyclerView.Adapter<UserSelectionAdapter.UserViewHolder> {
    
    private List<User> users;
    private Set<String> selectedKeys;
    private boolean isWhitelist;
    
    public UserSelectionAdapter(List<User> users, List<String> initialSelectedKeys, boolean isWhitelist) {
        this.users = users;
        this.selectedKeys = new HashSet<>(initialSelectedKeys);
        this.isWhitelist = isWhitelist;
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
    
    public List<String> getSelectedKeys() {
        return new ArrayList<>(selectedKeys);
    }
    
    class UserViewHolder extends RecyclerView.ViewHolder {
        private TextView txtUserName;
        private TextView txtUserEmail;
        private TextView txtPublicKey;
        private CheckBox checkBox;
        
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            txtUserName = itemView.findViewById(R.id.txtUserName);
            txtUserEmail = itemView.findViewById(R.id.txtUserEmail);
            txtPublicKey = itemView.findViewById(R.id.txtPublicKey);
            checkBox = itemView.findViewById(R.id.checkBox);
        }
        
        public void bind(User user) {
            txtUserName.setText(user.getName());
            txtUserEmail.setText(user.getEmail());
            
            // Mostra a chave pública truncada
            String publicKey = user.getPublicKey();
            if (publicKey != null && publicKey.length() > 20) {
                txtPublicKey.setText(publicKey.substring(0, 20) + "...");
            } else {
                txtPublicKey.setText(publicKey);
            }
            
            // Define se está selecionado
            checkBox.setChecked(selectedKeys.contains(user.getPublicKey()));
            
            // Listener para seleção
            itemView.setOnClickListener(v -> {
                boolean isChecked = !checkBox.isChecked();
                checkBox.setChecked(isChecked);
                
                if (isChecked) {
                    selectedKeys.add(user.getPublicKey());
                } else {
                    selectedKeys.remove(user.getPublicKey());
                }
            });
            
            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    selectedKeys.add(user.getPublicKey());
                } else {
                    selectedKeys.remove(user.getPublicKey());
                }
            });
        }
    }
}
