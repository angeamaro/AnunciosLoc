package ao.co.isptec.aplm.anunciosloc.ui.adapter;

import android.content.Context;
import android.text.format.DateUtils;
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
import ao.co.isptec.aplm.anunciosloc.data.model.P2PAnnouncement;

public class P2PAnnouncementAdapter extends RecyclerView.Adapter<P2PAnnouncementAdapter.ViewHolder> {

    private Context context;
    private List<P2PAnnouncement> announcements;
    private OnItemClickListener onItemClickListener;
    private OnRetransmitClickListener onRetransmitClickListener;

    public interface OnItemClickListener {
        void onItemClick(P2PAnnouncement announcement);
    }

    public interface OnRetransmitClickListener {
        void onRetransmitClick(P2PAnnouncement announcement);
    }

    public P2PAnnouncementAdapter(Context context, 
                                   OnItemClickListener onItemClickListener,
                                   OnRetransmitClickListener onRetransmitClickListener) {
        this.context = context;
        this.announcements = new ArrayList<>();
        this.onItemClickListener = onItemClickListener;
        this.onRetransmitClickListener = onRetransmitClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_p2p_announcement, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        P2PAnnouncement announcement = announcements.get(position);

        // Título e Descrição
        holder.tvTitle.setText(announcement.getTitle());
        holder.tvDescription.setText(announcement.getDescription());

        // Local
        holder.tvLocation.setText(announcement.getLocationName() != null 
            ? announcement.getLocationName() 
            : "Local não especificado");

        // Sender
        holder.tvSender.setText("De: " + (announcement.getSenderUsername() != null 
            ? announcement.getSenderUsername() 
            : "Anônimo"));

        // Badge de Tipo (Direto / Via Mula)
        holder.tvReceivedTypeBadge.setText(announcement.getReceivedTypeBadge());
        if (announcement.getReceivedType() == P2PAnnouncement.ReceivedType.DIRECT) {
            holder.tvReceivedTypeBadge.setBackgroundResource(R.drawable.bg_badge_blue);
            holder.tvReceivedTypeBadge.setTextColor(context.getColor(R.color.blue_600));
        } else {
            holder.tvReceivedTypeBadge.setBackgroundResource(R.drawable.bg_badge_orange);
            holder.tvReceivedTypeBadge.setTextColor(context.getColor(R.color.orange_600));
        }

        // Data de Recebimento (relativa)
        CharSequence relativeTime = DateUtils.getRelativeTimeSpanString(
            announcement.getReceivedAt().getTime(),
            System.currentTimeMillis(),
            DateUtils.MINUTE_IN_MILLIS
        );
        holder.tvReceivedDate.setText(relativeTime);

        // Selo de Autenticidade
        if (announcement.isAuthentic()) {
            holder.ivAuthenticBadge.setVisibility(View.VISIBLE);
        } else {
            holder.ivAuthenticBadge.setVisibility(View.GONE);
        }

        // Contador de Retransmissões (só mostra se > 0)
        if (announcement.getRetransmissionCount() > 0) {
            holder.tvRetransmissionCount.setVisibility(View.VISIBLE);
            holder.tvRetransmissionCount.setText("Retransmitido " 
                + announcement.getRetransmissionCount() + "x");
        } else {
            holder.tvRetransmissionCount.setVisibility(View.GONE);
        }

        // Botão Retransmitir (só aparece para anúncios VIA_MULE pendentes)
        if (announcement.getReceivedType() == P2PAnnouncement.ReceivedType.VIA_MULE 
            && announcement.isPendingRetransmission()) {
            holder.btnRetransmit.setVisibility(View.VISIBLE);
            holder.btnRetransmit.setOnClickListener(v -> {
                if (onRetransmitClickListener != null) {
                    onRetransmitClickListener.onRetransmitClick(announcement);
                }
            });
        } else {
            holder.btnRetransmit.setVisibility(View.GONE);
        }

        // Botão Ver Detalhes
        holder.btnViewDetails.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(announcement);
            }
        });

        // Click no card inteiro também abre detalhes
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(announcement);
            }
        });
    }

    @Override
    public int getItemCount() {
        return announcements.size();
    }

    public void updateList(List<P2PAnnouncement> newList) {
        this.announcements = newList;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDescription, tvLocation, tvSender;
        TextView tvReceivedTypeBadge, tvReceivedDate;
        TextView tvRetransmissionCount;
        ImageView ivAuthenticBadge;
        MaterialButton btnRetransmit, btnViewDetails;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            tvSender = itemView.findViewById(R.id.tvSender);
            tvReceivedTypeBadge = itemView.findViewById(R.id.tvReceivedTypeBadge);
            tvReceivedDate = itemView.findViewById(R.id.tvReceivedDate);
            tvRetransmissionCount = itemView.findViewById(R.id.tvRetransmissionCount);
            ivAuthenticBadge = itemView.findViewById(R.id.ivAuthenticBadge);
            btnRetransmit = itemView.findViewById(R.id.btnRetransmit);
            btnViewDetails = itemView.findViewById(R.id.btnViewDetails);
        }
    }
}
