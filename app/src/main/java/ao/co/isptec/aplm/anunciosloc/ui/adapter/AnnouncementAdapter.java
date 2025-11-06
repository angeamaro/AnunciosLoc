package ao.co.isptec.aplm.anunciosloc.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ao.co.isptec.aplm.anunciosloc.R;
import ao.co.isptec.aplm.anunciosloc.data.model.Announcement;
import ao.co.isptec.aplm.anunciosloc.utils.DateUtils;

/**
 * Adapter para exibir anúncios em RecyclerView
 */
public class AnnouncementAdapter extends RecyclerView.Adapter<AnnouncementAdapter.ViewHolder> {

    private List<Announcement> announcements;
    private OnAnnouncementClickListener clickListener;

    public interface OnAnnouncementClickListener {
        void onAnnouncementClick(Announcement announcement);
    }

    public AnnouncementAdapter(List<Announcement> announcements) {
        this.announcements = announcements != null ? announcements : new ArrayList<>();
    }

    public void setOnAnnouncementClickListener(OnAnnouncementClickListener listener) {
        this.clickListener = listener;
    }

    public void updateAnnouncements(List<Announcement> newAnnouncements) {
        this.announcements = newAnnouncements != null ? newAnnouncements : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_announcement, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Announcement announcement = announcements.get(position);
        
        holder.txtTitle.setText(announcement.getTitle());
        holder.txtContent.setText(announcement.getContent());
        holder.txtLocation.setText(announcement.getLocationName());
        holder.txtAuthor.setText("Por: " + announcement.getAuthorName());
        
        // Formata data
        String dateText = DateUtils.formatDateTime(announcement.getCreatedAt());
        holder.txtDate.setText(dateText);
        
        // Tempo restante
        if (announcement.getEndDate() != null) {
            String remaining = DateUtils.getTimeRemaining(announcement.getEndDate());
            holder.txtTimeRemaining.setText(remaining);
            holder.txtTimeRemaining.setVisibility(View.VISIBLE);
        } else {
            holder.txtTimeRemaining.setVisibility(View.GONE);
        }
        
        // View count
        holder.txtViewCount.setText(String.valueOf(announcement.getViewCount()) + " visualizações");
        
        // Status icon
        if (announcement.isActive()) {
            holder.imgStatus.setImageResource(R.drawable.dot);
            holder.imgStatus.setColorFilter(holder.itemView.getContext().getColor(R.color.success));
        } else {
            holder.imgStatus.setImageResource(R.drawable.dot);
            holder.imgStatus.setColorFilter(holder.itemView.getContext().getColor(R.color.gray_400));
        }
        
        // Click listener
        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onAnnouncementClick(announcement);
            }
        });
    }

    @Override
    public int getItemCount() {
        return announcements.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle, txtContent, txtLocation, txtAuthor, txtDate, txtTimeRemaining, txtViewCount;
        ImageView imgStatus, imgOptions;
        
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtContent = itemView.findViewById(R.id.txtContent);
            txtLocation = itemView.findViewById(R.id.txtLocation);
            txtAuthor = itemView.findViewById(R.id.txtAuthor);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtTimeRemaining = itemView.findViewById(R.id.txtTimeRemaining);
            txtViewCount = itemView.findViewById(R.id.txtViewCount);
            imgStatus = itemView.findViewById(R.id.imgStatus);
            imgOptions = itemView.findViewById(R.id.imgOptions);
        }
    }
}
