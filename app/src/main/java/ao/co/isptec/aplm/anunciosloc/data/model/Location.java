package ao.co.isptec.aplm.anunciosloc.data.model;

/**
 * Modelo de Localização
 */
public class Location {
    private String id;
    private String name;
    private double latitude;
    private double longitude;
    private int radiusInMeters;
    private String ssid; // SSID do WiFi/Beacon BLE (opcional)
    private String description;
    private String createdBy; // ID do usuário que criou
    private long createdAt;
    private boolean isActive;
    
    public Location() {
        this.createdAt = System.currentTimeMillis();
        this.isActive = true;
    }
    
    public Location(String id, String name, double latitude, double longitude, int radiusInMeters) {
        this();
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.radiusInMeters = radiusInMeters;
    }
    
    // Getters e Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public double getLatitude() {
        return latitude;
    }
    
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    
    public double getLongitude() {
        return longitude;
    }
    
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    
    public int getRadiusInMeters() {
        return radiusInMeters;
    }
    
    public void setRadiusInMeters(int radiusInMeters) {
        this.radiusInMeters = radiusInMeters;
    }
    
    public String getSsid() {
        return ssid;
    }
    
    public void setSsid(String ssid) {
        this.ssid = ssid;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getCreatedBy() {
        return createdBy;
    }
    
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    
    public long getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    public void setActive(boolean active) {
        isActive = active;
    }
    
    /**
     * Retorna as coordenadas formatadas
     */
    public String getFormattedCoordinates() {
        return String.format("%.6f, %.6f", latitude, longitude);
    }
}
