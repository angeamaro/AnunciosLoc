package ao.co.isptec.aplm.anunciosloc.ui.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ao.co.isptec.aplm.anunciosloc.R;
import ao.co.isptec.aplm.anunciosloc.data.model.Notification;
import ao.co.isptec.aplm.anunciosloc.utils.DateUtils;

/**
 * Adapter para exibir notificações em RecyclerView
 */
public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private static final String PREFS_NAME = "saved_announcements";
    private static final String KEY_SAVED_IDS = "saved_ids";

    private List<Notification> notifications;
    private OnNotificationClickListener clickListener;
    private OnNotificationActionListener actionListener;
    private Context context;

    public interface OnNotificationClickListener {
        void onNotificationClick(Notification notification);
    }

    public interface OnNotificationActionListener {
        void onViewNotification(Notification notification);
        void onSaveNotification(Notification notification);
    }

    public NotificationAdapter(List<Notification> notifications) {
        this.notifications = notifications != null ? notifications : new ArrayList<>();
    }

    public void setOnNotificationClickListener(OnNotificationClickListener listener) {
        this.clickListener = listener;
    }

    public void setOnNotificationActionListener(OnNotificationActionListener listener) {
        this.actionListener = listener;
    }

    public void setNotifications(List<Notification> newNotifications) {
        this.notifications = newNotifications != null ? newNotifications : new ArrayList<>();
        notifyDataSetChanged();
    }

    public List<Notification> getNotifications() {
        return new ArrayList<>(notifications);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_notification, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Notification notification = notifications.get(position);
        
        holder.txtTitle.setText(notification.getTitle());
        holder.txtMessage.setText(notification.getMessage());
        
        // Formata a data no formato pt-PT (dd/MM/yyyy)
        holder.txtTime.setText(DateUtils.formatDate(notification.getTimestamp()));
        
        // Extrai localização e autor da mensagem (se existirem)
        String message = notification.getMessage();
        String location = "Localização não especificada";
        String author = "Autor desconhecido";
        
        if (message != null) {
            // Extrai localização
            int locIndex = message.indexOf("Localização:");
            if (locIndex != -1) {
                int locEnd = message.indexOf(".", locIndex);
                if (locEnd == -1) locEnd = message.indexOf(" Autor:", locIndex);
                if (locEnd == -1) locEnd = message.length();
                location = message.substring(locIndex + 12, locEnd).trim();
            }
            
            // Extrai autor
            int authorIndex = message.indexOf("Autor:");
            if (authorIndex != -1) {
                int authorEnd = message.length();
                author = message.substring(authorIndex + 6, authorEnd).trim();
            }
            
            // Remove as informações de localização e autor da mensagem exibida
            String cleanMessage = message;
            if (locIndex != -1) {
                cleanMessage = message.substring(0, locIndex).trim();
            }
            holder.txtMessage.setText(cleanMessage);
        }
        
        holder.txtLocation.setText(location);
        holder.txtAuthor.setText(author);
        
        // Badge "Novo" visível apenas para não lidas
        if (notification.isRead()) {
            holder.badgeNew.setVisibility(View.GONE);
            holder.itemView.setAlpha(0.85f);
        } else {
            holder.badgeNew.setVisibility(View.VISIBLE);
            holder.itemView.setAlpha(1.0f);
        }
        
        // Verifica se o anúncio está guardado e atualiza o botão
        boolean isSaved = isAnnouncementSaved(notification.getRelatedId());
        updateSaveButton(holder.btnSave, isSaved);
        
        // Mostra botões de ação apenas para anúncios
        if ("ANNOUNCEMENT".equals(notification.getType())) {
            holder.btnView.setVisibility(View.VISIBLE);
            holder.btnSave.setVisibility(View.VISIBLE);
        } else {
            holder.btnView.setVisibility(View.VISIBLE);
            holder.btnSave.setVisibility(View.GONE);
        }
        
        // Click listener no card
        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onNotificationClick(notification);
            }
        });

        // Click listener no botão Ver
        holder.btnView.setOnClickListener(v -> {
            if (actionListener != null) {
                actionListener.onViewNotification(notification);
            }
        });

        // Click listener no botão Guardar
        holder.btnSave.setOnClickListener(v -> {
            if (actionListener != null) {
                actionListener.onSaveNotification(notification);
                // Atualiza o visual do botão imediatamente
                boolean nowSaved = isAnnouncementSaved(notification.getRelatedId());
                updateSaveButton(holder.btnSave, nowSaved);
            }
        });
    }

    /**
     * Atualiza o visual do botão de guardar
     */
    private void updateSaveButton(MaterialButton button, boolean isSaved) {
        if (isSaved) {
            // Anúncio já está guardado - mostra em laranja
            button.setText("Guardado");
            button.setTextColor(context.getColor(R.color.orange_600));
            button.setIconTint(context.getColorStateList(R.color.orange_600));
        } else {
            // Anúncio não está guardado - mostra em cinza
            button.setText("Guardar");
            button.setTextColor(context.getColor(R.color.gray_600));
            button.setIconTint(context.getColorStateList(R.color.gray_400));
        }
    }

    /**
     * Verifica se um anúncio está guardado
     */
    private boolean isAnnouncementSaved(String announcementId) {
        if (announcementId == null || context == null) return false;
        
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Set<String> savedIds = prefs.getStringSet(KEY_SAVED_IDS, new HashSet<>());
        return savedIds.contains(announcementId);
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle, txtMessage, txtTime, txtLocation, txtAuthor, badgeNew;
        MaterialButton btnView, btnSave;
        
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtMessage = itemView.findViewById(R.id.txtMessage);
            txtTime = itemView.findViewById(R.id.txtTime);
            txtLocation = itemView.findViewById(R.id.txtLocation);
            txtAuthor = itemView.findViewById(R.id.txtAuthor);
            badgeNew = itemView.findViewById(R.id.badgeNew);
            btnView = itemView.findViewById(R.id.btnView);
            btnSave = itemView.findViewById(R.id.btnSave);
        }
    }
}

