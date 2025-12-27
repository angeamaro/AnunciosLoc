package ao.co.isptec.aplm.anunciosloc.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ao.co.isptec.aplm.anunciosloc.R;
import ao.co.isptec.aplm.anunciosloc.ui.view.P2PDiscoveryActivity.P2PDevice;

/**
 * Adapter para lista de dispositivos WiFi Direct descobertos
 */
public class P2PDeviceAdapter extends RecyclerView.Adapter<P2PDeviceAdapter.DeviceViewHolder> {

    private List<P2PDevice> devices;
    private OnDeviceSelectListener listener;
    private int selectedPosition = -1;

    public interface OnDeviceSelectListener {
        void onDeviceSelected(P2PDevice device);
    }

    public P2PDeviceAdapter(List<P2PDevice> devices, OnDeviceSelectListener listener) {
        this.devices = devices;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DeviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_p2p_device, parent, false);
        return new DeviceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceViewHolder holder, int position) {
        P2PDevice device = devices.get(position);
        
        holder.tvDeviceName.setText(device.getDeviceName());
        holder.tvDeviceId.setText(device.getDeviceId());
        holder.tvStatus.setText(device.getStatus());

        // Muda cor do status
        if (device.getStatus().equals("Available")) {
            holder.tvStatus.setTextColor(holder.itemView.getContext().getColor(R.color.green_600));
        } else if (device.getStatus().equals("Connected")) {
            holder.tvStatus.setTextColor(holder.itemView.getContext().getColor(R.color.blue_600));
        } else {
            holder.tvStatus.setTextColor(holder.itemView.getContext().getColor(R.color.text_secondary));
        }

        // Visual de seleção
        if (selectedPosition == position) {
            holder.cardDevice.setCardBackgroundColor(
                holder.itemView.getContext().getColor(R.color.purple_100));
            holder.ivCheckmark.setVisibility(View.VISIBLE);
        } else {
            holder.cardDevice.setCardBackgroundColor(
                holder.itemView.getContext().getColor(R.color.white));
            holder.ivCheckmark.setVisibility(View.GONE);
        }

        // Click para selecionar
        holder.itemView.setOnClickListener(v -> {
            int oldPosition = selectedPosition;
            selectedPosition = holder.getAdapterPosition();
            
            // Notifica mudanças
            if (oldPosition != -1) {
                notifyItemChanged(oldPosition);
            }
            notifyItemChanged(selectedPosition);
            
            if (listener != null) {
                listener.onDeviceSelected(device);
            }
        });
    }

    @Override
    public int getItemCount() {
        return devices.size();
    }

    static class DeviceViewHolder extends RecyclerView.ViewHolder {
        CardView cardDevice;
        TextView tvDeviceName, tvDeviceId, tvStatus;
        ImageView ivCheckmark;

        public DeviceViewHolder(@NonNull View itemView) {
            super(itemView);
            cardDevice = (CardView) itemView;
            tvDeviceName = itemView.findViewById(R.id.tvDeviceName);
            tvDeviceId = itemView.findViewById(R.id.tvDeviceId);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            ivCheckmark = itemView.findViewById(R.id.ivCheckmark);
        }
    }
}
