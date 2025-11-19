package ao.co.isptec.aplm.anunciosloc.data.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import ao.co.isptec.aplm.anunciosloc.data.model.Location;
import ao.co.isptec.aplm.anunciosloc.utils.Constants;

/**
 * Repositório mock para gerenciar localizações
 */
public class LocationRepository {
    
    private static LocationRepository instance;
    private final Map<String, Location> locationsDatabase;
    
    private LocationRepository() {
        locationsDatabase = new HashMap<>();
        initializeMockData();
    }
    
    public static synchronized LocationRepository getInstance() {
        if (instance == null) {
            instance = new LocationRepository();
        }
        return instance;
    }
    
    /**
     * Inicializa dados mockados com locais em Luanda, Angola
     */
    private void initializeMockData() {
        // Largo da Independência
        Location loc1 = new Location("1", "Largo da Independência", -8.8137, 13.2302, 150);
        loc1.setDescription("Centro histórico de Luanda");
        loc1.setCreatedBy("1");
        locationsDatabase.put(loc1.getId(), loc1);
        
        // Marginal de Luanda
        Location loc2 = new Location("2", "Marginal de Luanda", -8.8080, 13.2343, 200);
        loc2.setDescription("Avenida costeira principal");
        loc2.setCreatedBy("1");
        locationsDatabase.put(loc2.getId(), loc2);
        
        // ISPTEC
        Location loc3 = new Location("3", "ISPTEC", -8.8400, 13.2890, 100);
        loc3.setDescription("Instituto Superior Politécnico de Tecnologias e Ciências");
        loc3.setSsid("ISPTEC_WIFI");
        loc3.setCreatedBy("2");
        locationsDatabase.put(loc3.getId(), loc3);
        
        // Fortaleza de São Miguel
        Location loc4 = new Location("4", "Fortaleza de São Miguel", -8.8068, 13.2307, 120);
        loc4.setDescription("Monumento histórico");
        loc4.setCreatedBy("2");
        locationsDatabase.put(loc4.getId(), loc4);
        
        // Belas Shopping
        Location loc5 = new Location("5", "Belas Shopping", -8.9289, 13.1845, 150);
        loc5.setDescription("Centro comercial");
        loc5.setSsid("BELAS_FREE_WIFI");
        loc5.setCreatedBy("3");
        locationsDatabase.put(loc5.getId(), loc5);
    }
    
    /**
     * Cria novo local
     */
    public Location createLocation(Location location) {
        if (location.getId() == null || location.getId().isEmpty()) {
            location.setId(UUID.randomUUID().toString());
        }
        location.setCreatedAt(System.currentTimeMillis());
        locationsDatabase.put(location.getId(), location);
        return location;
    }
    
    /**
     * Obtém todos os locais
     */
    public List<Location> getAllLocations() {
        return new ArrayList<>(locationsDatabase.values());
    }
    
    /**
     * Obtém locais ativos
     */
    public List<Location> getActiveLocations() {
        List<Location> activeLocations = new ArrayList<>();
        for (Location location : locationsDatabase.values()) {
            if (location.isActive()) {
                activeLocations.add(location);
            }
        }
        return activeLocations;
    }
    
    /**
     * Obtém local por ID
     */
    public Location getLocationById(String locationId) {
        return locationsDatabase.get(locationId);
    }
    
    /**
     * Obtém locais criados por um usuário
     */
    public List<Location> getLocationsByUser(String userId) {
        List<Location> userLocations = new ArrayList<>();
        for (Location location : locationsDatabase.values()) {
            if (userId.equals(location.getCreatedBy())) {
                userLocations.add(location);
            }
        }
        return userLocations;
    }
    
    /**
     * Atualiza local
     */
    public boolean updateLocation(Location location) {
        if (location == null || location.getId() == null) {
            return false;
        }
        
        if (!locationsDatabase.containsKey(location.getId())) {
            return false;
        }
        
        locationsDatabase.put(location.getId(), location);
        return true;
    }
    
    /**
     * Remove local
     */
    public boolean deleteLocation(String locationId) {
        if (locationId == null) {
            return false;
        }
        
        Location location = locationsDatabase.get(locationId);
        if (location != null) {
            location.setActive(false);
            return true;
        }
        return false;
    }
    
    /**
     * Remove local permanentemente
     */
    public boolean permanentlyDeleteLocation(String locationId) {
        return locationsDatabase.remove(locationId) != null;
    }
    
    /**
     * Busca locais por nome
     */
    public List<Location> searchLocationsByName(String query) {
        List<Location> results = new ArrayList<>();
        String lowerQuery = query.toLowerCase();
        
        for (Location location : locationsDatabase.values()) {
            if (location.getName().toLowerCase().contains(lowerQuery)) {
                results.add(location);
            }
        }
        return results;
    }
    
    /**
     * Obtém locais próximos a uma coordenada (simplificado)
     */
    public List<Location> getNearbyLocations(double latitude, double longitude, int radiusInMeters) {
        List<Location> nearbyLocations = new ArrayList<>();
        
        for (Location location : locationsDatabase.values()) {
            if (location.isActive()) {
                // Cálculo simplificado de distância
                double distance = calculateDistance(
                    latitude, longitude,
                    location.getLatitude(), location.getLongitude()
                );
                
                if (distance <= radiusInMeters) {
                    nearbyLocations.add(location);
                }
            }
        }
        return nearbyLocations;
    }
    
    /**
     * Calcula distância aproximada entre dois pontos (em metros)
     */
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        // Fórmula de Haversine simplificada
        final int R = 6371000; // Raio da Terra em metros
        
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        
        return R * c;
    }
}
