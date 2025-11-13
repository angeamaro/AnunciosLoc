package ao.co.isptec.aplm.anunciosloc.ui.adapter;

import android.graphics.Color;
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
    private OnLocalClickListener clickListener;

    public interface OnLocalClickListener {
        void onEditClick(Location local);
        void onDeleteClick(Location local);
    }

    public LocalAdapter(List<Location> locais) {
        this.locais = locais != null ? locais : new ArrayList<>();
    }

    public void setOnLocalClickListener(OnLocalClickListener listener) {
        this.clickListener = listener;
    }

    public void updateLocais(List<Location> newLocais) {
        this.locais = newLocais != null ? newLocais : new ArrayList<>();
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

        // Título
        holder.txtTitle.setText(local.getName());

        // Data formatada
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String formattedDate = sdf.format(new Date(local.getCreatedAt()));
        holder.txtDate.setText("Criado em " + formattedDate);

        // Determina se é Wi-Fi ou GPS
        boolean isWifi = local.getSsid() != null && !local.getSsid().isEmpty();

        if (isWifi) {
            holder.iconType.setImageResource(R.drawable.ic_wifi_large);
            holder.iconType.setBackgroundResource(R.drawable.bg_circle_purple); // fundo roxo
            holder.txtSsidOrCoords.setText(local.getSsid());

            holder.chipWifi.setVisibility(View.VISIBLE);
            holder.chipGps.setVisibility(View.GONE); // esconde GPS
        } else { // GPS
            holder.iconType.setImageResource(R.drawable.ic_location_on);
            holder.iconType.setBackgroundResource(R.drawable.bg_circle_blue_light); // fundo azul claro
            holder.iconType.setColorFilter(Color.BLUE); // ícone azul
            holder.txtSsidOrCoords.setText(local.getFormattedCoordinates());

            holder.chipWifi.setVisibility(View.GONE);
            holder.chipGps.setVisibility(View.VISIBLE); // mostra chip GPS azul
        }

        // "Meu Local" escondido por padrão
        holder.chipMeuLocal.setVisibility(View.GONE);

        // Listeners para ações
        if (clickListener != null) {
            holder.iconEdit.setOnClickListener(v -> clickListener.onEditClick(local));
            holder.iconDelete.setOnClickListener(v -> clickListener.onDeleteClick(local));
        }
    }

    @Override
    public int getItemCount() {
        return locais.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle, txtSsidOrCoords, txtDate;
        TextView chipWifi, chipMeuLocal, chipGps;
        ImageView iconType, iconEdit, iconDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.text_local_title);
            txtSsidOrCoords = itemView.findViewById(R.id.text_ssid);
            txtDate = itemView.findViewById(R.id.text_created_date);

            iconType = itemView.findViewById(R.id.icon_wifi_card);
            chipWifi = itemView.findViewById(R.id.chip_wifi);
            chipMeuLocal = itemView.findViewById(R.id.chip_meu_local);
            chipGps = itemView.findViewById(R.id.chip_gps); // novo chip GPS

            iconEdit = itemView.findViewById(R.id.icon_edit);
            iconDelete = itemView.findViewById(R.id.icon_delete);
        }
    }
}
