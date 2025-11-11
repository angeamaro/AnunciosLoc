package ao.co.isptec.aplm.anunciosloc.utils;

/**
 * Classe para armazenar constantes da aplicação
 */
public class Constants {
    
    // SharedPreferences
    public static final String PREFS_NAME = "AnunciosLocPrefs";
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_USER_NAME = "user_name";
    public static final String KEY_USER_EMAIL = "user_email";
    public static final String KEY_IS_LOGGED_IN = "is_logged_in";
    public static final String KEY_SESSION_TOKEN = "session_token";
    
    // Request Codes
    public static final int REQUEST_LOCATION_PERMISSION = 1001;
    public static final int REQUEST_WIFI_PERMISSION = 1002;
    public static final int REQUEST_NOTIFICATION_PERMISSION = 1003;
    
    // Default Values
    public static final int DEFAULT_LOCATION_RADIUS = 100; // metros
    public static final long SPLASH_DELAY = 2500; // 2.5 segundos
    
    // Políticas de Entrega
    public static final String POLICY_WHITELIST = "WHITELIST";
    public static final String POLICY_BLACKLIST = "BLACKLIST";
    public static final String POLICY_EVERYONE = "EVERYONE";
    
    public static final String DELIVERY_POLICY_WHITELIST = "WHITELIST";
    public static final String DELIVERY_POLICY_BLACKLIST = "BLACKLIST";
    public static final String DELIVERY_POLICY_EVERYONE = "EVERYONE";
    
    // Modos de Entrega
    public static final String DELIVERY_MODE_CENTRALIZED = "CENTRALIZED";
    public static final String DELIVERY_MODE_DECENTRALIZED = "DECENTRALIZED";
    
    // Status de Anúncio
    public static final String STATUS_ACTIVE = "ACTIVE";
    public static final String STATUS_EXPIRED = "EXPIRED";
    public static final String STATUS_DRAFT = "DRAFT";
    
    // Tipos de Notificação
    public static final String NOTIFICATION_NEW_ANNOUNCEMENT = "NEW_ANNOUNCEMENT";
    public static final String NOTIFICATION_LOCATION_ENTERED = "LOCATION_ENTERED";
    public static final String NOTIFICATION_MESSAGE_RECEIVED = "MESSAGE_RECEIVED";
    
    private Constants() {
        // Previne instanciação
    }
}
