package ao.co.isptec.aplm.anunciosloc.ui.view.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ao.co.isptec.aplm.anunciosloc.R;
import ao.co.isptec.aplm.anunciosloc.data.model.Location;

/**
 * Adapter para listar localizações
 */
public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationViewHolder> {
    
    private List<Location> locations;
    private OnLocationClickListener listener;
    
    public interface OnLocationClickListener {
        void onLocationClick(Location location);
    }
    
    public LocationAdapter(List<Location> locations, OnLocationClickListener listener) {
        this.locations = locations;
        this.listener = listener;
    }
    
    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_location, parent, false);
        return new LocationViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, int position) {
        Location location = locations.get(position);
        holder.bind(location, listener);
    }
    
    @Override
    public int getItemCount() {
        return locations.size();
    }
    
    public void updateLocations(List<Location> newLocations) {
        this.locations = newLocations;
        notifyDataSetChanged();
    }
    
    static class LocationViewHolder extends RecyclerView.ViewHolder {
        
        private TextView txtName, txtCoordinates, txtRadius, txtSsid;
        
        public LocationViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtCoordinates = itemView.findViewById(R.id.txtCoordinates);
            txtRadius = itemView.findViewById(R.id.txtRadius);
            txtSsid = itemView.findViewById(R.id.txtSsid);
        }
        
        public void bind(Location location, OnLocationClickListener listener) {
            txtName.setText(location.getName());
            txtCoordinates.setText(location.getFormattedCoordinates());
            txtRadius.setText(location.getRadiusInMeters() + "m");
            
            if (location.getSsid() != null && !location.getSsid().isEmpty()) {
                txtSsid.setVisibility(View.VISIBLE);
                txtSsid.setText("📶 " + location.getSsid());
            } else {
                txtSsid.setVisibility(View.GONE);
            }
            
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onLocationClick(location);
                }
            });
        }
    }
}
