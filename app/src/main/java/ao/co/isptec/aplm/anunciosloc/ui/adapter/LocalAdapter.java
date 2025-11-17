package ao.co.isptec.aplm.anunciosloc.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ao.co.isptec.aplm.anunciosloc.R;
import ao.co.isptec.aplm.anunciosloc.data.model.Location;

public class LocalAdapter extends RecyclerView.Adapter<LocalAdapter.ViewHolder> {

    private List<Location> locais;
    private List<Location> originalList;
    private OnLocalClickListener clickListener;

    public interface OnLocalClickListener {
        void onEditClick(Location local);
        void onDeleteClick(Location local);
    }

    public LocalAdapter(List<Location> locais) {
        this.originalList = new ArrayList<>(locais != null ? locais : new ArrayList<>());
        this.locais = new ArrayList<>(this.originalList);
    }

    public void setOnLocalClickListener(OnLocalClickListener listener) {
        this.clickListener = listener;
    }

    public void removeItem(Location local) {
        int positionInFullList = originalList.indexOf(local);
        int positionInCurrentList = locais.indexOf(local);

        if (positionInFullList != -1) originalList.remove(positionInFullList);
        if (positionInCurrentList != -1) {
            locais.remove(positionInCurrentList);
            notifyItemRemoved(positionInCurrentList);
            notifyItemRangeChanged(positionInCurrentList, locais.size());
        }
    }

    public void filterByMeusLocais(String currentUserId) {
        locais.clear();
        for (Location loc : originalList) {
            if (currentUserId.equals(loc.getCreatedBy())) locais.add(loc);
        }
        notifyDataSetChanged();
    }

    public void filterByOutrosLocais(String currentUserId) {
        locais.clear();
        for (Location loc : originalList) {
            if (!currentUserId.equals(loc.getCreatedBy())) locais.add(loc);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_local, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Location local = locais.get(position);
        boolean isMeuLocal = "usuario123".equals(local.getCreatedBy());
        boolean isWifi = local.getSsid() != null && !local.getSsid().isEmpty();

        // Título e data
        holder.txtTitle.setText(local.getName());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        holder.txtDate.setText("Criado em " + sdf.format(new Date(local.getCreatedAt())));

        // Ícones grandes
        if (isWifi) {
            holder.iconWifiCard.setVisibility(View.VISIBLE);
            holder.iconGpsCard.setVisibility(View.GONE);
            holder.txtDetails.setText(local.getSsid());
        } else {
            holder.iconWifiCard.setVisibility(View.GONE);
            holder.iconGpsCard.setVisibility(View.VISIBLE);
            holder.txtDetails.setText(local.getFormattedCoordinates());
        }

        // CHIPS — A MÁGICA FINAL (CORRIGIDO!)
        if (isWifi) {
            holder.chipWifi.setVisibility(View.VISIBLE);
            holder.chipGps.setVisibility(View.INVISIBLE);   // ← INVISIBLE (mantém espaço!)
        } else {
            holder.chipWifi.setVisibility(View.GONE);
            holder.chipGps.setVisibility(View.VISIBLE);
        }

        // "Meu Local" — sempre alinhado perfeitamente
        holder.chipMeuLocal.setVisibility(isMeuLocal ? View.VISIBLE : View.GONE);

        // Botões editar/eliminar
        if (isMeuLocal && clickListener != null) {
            holder.iconEdit.setVisibility(View.VISIBLE);
            holder.iconDelete.setVisibility(View.VISIBLE);
            holder.iconEdit.setOnClickListener(v -> clickListener.onEditClick(local));
            holder.iconDelete.setOnClickListener(v -> clickListener.onDeleteClick(local));
        } else {
            holder.iconEdit.setVisibility(View.GONE);
            holder.iconDelete.setVisibility(View.GONE);
            holder.iconEdit.setOnClickListener(null);
            holder.iconDelete.setOnClickListener(null);
        }
    }

    @Override
    public int getItemCount() {
        return locais.size();
    }

    // VIEWHOLDER CORRIGIDO (nomes certos!)
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle, txtDetails, txtDate, chipWifi, chipGps, chipMeuLocal;
        ImageView iconWifiCard, iconGpsCard, iconEdit, iconDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.text_local_title);
            txtDetails = itemView.findViewById(R.id.text_ssid);
            txtDate = itemView.findViewById(R.id.text_created_date);
            iconWifiCard = itemView.findViewById(R.id.icon_wifi_card);
            iconGpsCard = itemView.findViewById(R.id.icon_GPS_card);
            chipWifi = itemView.findViewById(R.id.chip_wifi);           // ← CORRETO
            chipGps = itemView.findViewById(R.id.chip_gps);             // ← CORRETO
            chipMeuLocal = itemView.findViewById(R.id.chip_meu_local);
            iconEdit = itemView.findViewById(R.id.icon_edit);
            iconDelete = itemView.findViewById(R.id.icon_delete);
        }
    }
}