package ao.co.isptec.aplm.anunciosloc.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ao.co.isptec.aplm.anunciosloc.R;
import ao.co.isptec.aplm.anunciosloc.data.model.Announcement;

public class AnnouncementCardAdapter extends RecyclerView.Adapter<AnnouncementCardAdapter.ViewHolder> {

    private List<Announcement> announcements;
    private List<String> savedAnnouncementIds;
    private OnAnnouncementActionListener listener;
    private SimpleDateFormat dateFormat;

    public interface OnAnnouncementActionListener {
        void onViewDetails(Announcement announcement);
        void onToggleSave(Announcement announcement, boolean isSaved);
    }

    public AnnouncementCardAdapter(OnAnnouncementActionListener listener) {
        this.announcements = new ArrayList<>();
        this.savedAnnouncementIds = new ArrayList<>();
        this.listener = listener;
        this.dateFormat = new SimpleDateFormat("dd/MM/yyyy", new Locale("pt", "PT"));
    }

    public void setAnnouncements(List<Announcement> announcements) {
        this.announcements = announcements != null ? announcements : new ArrayList<>();
        notifyDataSetChanged();
    }

    public void setSavedAnnouncementIds(List<String> savedIds) {
        this.savedAnnouncementIds = savedIds != null ? savedIds : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_announcement_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Announcement announcement = announcements.get(position);
        holder.bind(announcement);
    }

    @Override
    public int getItemCount() {
        return announcements.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtTitle, txtContent, txtLocation, txtAuthor, txtDate;
        private ImageButton btnSave;
        private MaterialButton btnViewDetails;
        private Chip chipSaved;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtContent = itemView.findViewById(R.id.txtContent);
            txtLocation = itemView.findViewById(R.id.txtLocation);
            txtAuthor = itemView.findViewById(R.id.txtAuthor);
            txtDate = itemView.findViewById(R.id.txtDate);
            btnSave = itemView.findViewById(R.id.btnSave);
            btnViewDetails = itemView.findViewById(R.id.btnViewDetails);
            chipSaved = itemView.findViewById(R.id.chipSaved);
        }

        public void bind(Announcement announcement) {
            txtTitle.setText(announcement.getTitle());
            txtContent.setText(announcement.getContent() != null ? announcement.getContent() : "");
            txtLocation.setText(announcement.getLocationName() != null 
                    ? announcement.getLocationName() 
                    : "Localização não especificada");
            txtAuthor.setText(announcement.getAuthorName() != null 
                    ? announcement.getAuthorName() 
                    : "Anunciante");
            txtDate.setText(dateFormat.format(announcement.getCreatedAt()));

            // Verifica se o anúncio está guardado
            boolean isSaved = savedAnnouncementIds.contains(String.valueOf(announcement.getId()));
            updateSaveButton(isSaved);
            chipSaved.setVisibility(isSaved ? View.VISIBLE : View.GONE);

            // Listeners
            btnViewDetails.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onViewDetails(announcement);
                }
            });

            btnSave.setOnClickListener(v -> {
                if (listener != null) {
                    boolean newSavedState = !isSaved;
                    listener.onToggleSave(announcement, newSavedState);
                    updateSaveButton(newSavedState);
                    chipSaved.setVisibility(newSavedState ? View.VISIBLE : View.GONE);
                }
            });
        }

        private void updateSaveButton(boolean isSaved) {
            if (isSaved) {
                btnSave.setImageResource(R.drawable.ic_favorite);
                btnSave.setBackgroundResource(R.drawable.bg_icon_circle_orange);
                btnSave.setImageTintList(itemView.getContext().getColorStateList(R.color.orange_600));
            } else {
                btnSave.setImageResource(R.drawable.ic_favorite);
                btnSave.setBackgroundResource(R.drawable.bg_circle_gray);
                btnSave.setImageTintList(itemView.getContext().getColorStateList(R.color.gray_400));
            }
        }
    }
}
