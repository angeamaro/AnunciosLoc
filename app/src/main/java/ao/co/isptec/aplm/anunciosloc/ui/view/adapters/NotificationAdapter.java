package ao.co.isptec.aplm.anunciosloc.ui.view.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import ao.co.isptec.aplm.anunciosloc.R;
import ao.co.isptec.aplm.anunciosloc.data.model.Notification;
import ao.co.isptec.aplm.anunciosloc.utils.DateUtils;

/**
 * Adapter para exibir notificações em RecyclerView
 */
public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    public List<Notification> notifications;
    private OnNotificationClickListener clickListener;
    private OnNotificationActionListener actionListener;

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

    public void updateNotifications(List<Notification> newNotifications) {
        this.notifications = newNotifications != null ? newNotifications : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Notification notification = notifications.get(position);
        
        holder.txtTitle.setText(notification.getTitle());
        holder.txtMessage.setText(notification.getMessage());
        holder.txtTime.setText(DateUtils.formatDateTime(notification.getTimestamp()));
        
        // Define ícone baseado no tipo
        int iconRes;
        int colorRes;
        switch (notification.getType()) {
            case "ANNOUNCEMENT":
                iconRes = R.drawable.ic_announcement;
                colorRes = R.color.purple_600;
                break;
            case "LOCATION":
                iconRes = R.drawable.ic_location_on;
                colorRes = R.color.blue_600;
                break;
            case "SYSTEM":
                iconRes = R.drawable.ic_wifi;
                colorRes = R.color.orange_600;
                break;
            default:
                iconRes = R.drawable.ic_notifications;
                colorRes = R.color.gray_600;
                break;
        }
        
        holder.imgIcon.setImageResource(iconRes);
        // Define cor do ícone como branco
        holder.imgIcon.setColorFilter(holder.itemView.getContext().getColor(android.R.color.white));
        
        // Visual de lida/não lida
        if (notification.isRead()) {
            holder.itemView.setAlpha(0.6f);
            holder.imgUnread.setVisibility(View.GONE);
        } else {
            holder.itemView.setAlpha(1.0f);
            holder.imgUnread.setVisibility(View.VISIBLE);
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
            }
        });
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle, txtMessage, txtTime;
        ImageView imgIcon, imgUnread;
        MaterialButton btnView, btnSave;
        
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtMessage = itemView.findViewById(R.id.txtMessage);
            txtTime = itemView.findViewById(R.id.txtTime);
            imgIcon = itemView.findViewById(R.id.imgIcon);
            imgUnread = itemView.findViewById(R.id.imgUnread);
            btnView = itemView.findViewById(R.id.btnView);
            btnSave = itemView.findViewById(R.id.btnSave);
        }
    }
}
