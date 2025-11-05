package ao.co.isptec.aplm.anunciosloc.utils;

import android.util.Patterns;

/**
 * Utilitários para validação de dados
 */
public class ValidationUtils {
    
    /**
     * Valida se um email é válido
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    
    /**
     * Valida se uma senha é forte (mínimo 6 caracteres)
     */
    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }
    
    /**
     * Valida se um texto não está vazio
     */
    public static boolean isNotEmpty(String text) {
        return text != null && !text.trim().isEmpty();
    }
    
    /**
     * Valida se coordenadas de latitude são válidas
     */
    public static boolean isValidLatitude(double latitude) {
        return latitude >= -90.0 && latitude <= 90.0;
    }
    
    /**
     * Valida se coordenadas de longitude são válidas
     */
    public static boolean isValidLongitude(double longitude) {
        return longitude >= -180.0 && longitude <= 180.0;
    }
    
    /**
     * Valida se um raio é válido (positivo)
     */
    public static boolean isValidRadius(int radius) {
        return radius > 0 && radius <= 10000; // máximo 10km
    }
    
    /**
     * Valida se um nome de usuário é válido (3-20 caracteres alfanuméricos)
     */
    public static boolean isValidUsername(String username) {
        if (username == null || username.length() < 3 || username.length() > 20) {
            return false;
        }
        return username.matches("^[a-zA-Z0-9_]+$");
    }
}
