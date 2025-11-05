package ao.co.isptec.aplm.anunciosloc.utils;

import android.location.Location;

/**
 * Utilitários para cálculos de localização e GPS
 */
public class LocationUtils {
    
    /**
     * Calcula a distância entre duas coordenadas em metros
     */
    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        float[] results = new float[1];
        Location.distanceBetween(lat1, lon1, lat2, lon2, results);
        return results[0];
    }
    
    /**
     * Verifica se um ponto está dentro de um raio de outro ponto
     */
    public static boolean isWithinRadius(double lat1, double lon1, double lat2, double lon2, int radiusInMeters) {
        double distance = calculateDistance(lat1, lon1, lat2, lon2);
        return distance <= radiusInMeters;
    }
    
    /**
     * Formata coordenadas para exibição
     */
    public static String formatCoordinates(double latitude, double longitude) {
        return String.format("%.6f, %.6f", latitude, longitude);
    }
    
    /**
     * Formata uma distância em formato legível
     */
    public static String formatDistance(double distanceInMeters) {
        if (distanceInMeters < 1000) {
            return String.format("%.0fm", distanceInMeters);
        } else {
            return String.format("%.2fkm", distanceInMeters / 1000);
        }
    }
}
