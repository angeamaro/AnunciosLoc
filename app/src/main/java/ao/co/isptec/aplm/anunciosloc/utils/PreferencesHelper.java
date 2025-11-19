package ao.co.isptec.aplm.anunciosloc.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Helper para gerenciar SharedPreferences
 */
public class PreferencesHelper {
    
    private final SharedPreferences preferences;
    
    public PreferencesHelper(Context context) {
        this.preferences = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
    }
    
    /**
     * Salva dados de login do usuário
     */
    public void saveUserSession(String userId, String userName, String email, String token) {
        preferences.edit()
                .putString(Constants.KEY_USER_ID, userId)
                .putString(Constants.KEY_USER_NAME, userName)
                .putString(Constants.KEY_USER_EMAIL, email)
                .putString(Constants.KEY_SESSION_TOKEN, token)
                .putBoolean(Constants.KEY_IS_LOGGED_IN, true)
                .apply();
    }
    
    /**
     * Verifica se o usuário está logado
     */
    public boolean isUserLoggedIn() {
        return preferences.getBoolean(Constants.KEY_IS_LOGGED_IN, false);
    }
    
    /**
     * Obtém o ID do usuário logado
     */
    public String getUserId() {
        return preferences.getString(Constants.KEY_USER_ID, null);
    }
    
    /**
     * Obtém o nome do usuário logado
     */
    public String getUserName() {
        return preferences.getString(Constants.KEY_USER_NAME, null);
    }
    
    /**
     * Obtém o email do usuário logado
     */
    public String getUserEmail() {
        return preferences.getString(Constants.KEY_USER_EMAIL, null);
    }
    
    /**
     * Obtém o token de sessão
     */
    public String getSessionToken() {
        return preferences.getString(Constants.KEY_SESSION_TOKEN, null);
    }
    
    /**
     * Limpa todos os dados de sessão (logout)
     */
    public void clearUserSession() {
        preferences.edit()
                .remove(Constants.KEY_USER_ID)
                .remove(Constants.KEY_USER_NAME)
                .remove(Constants.KEY_USER_EMAIL)
                .remove(Constants.KEY_SESSION_TOKEN)
                .putBoolean(Constants.KEY_IS_LOGGED_IN, false)
                .apply();
    }
    
    /**
     * Salva um valor string genérico
     */
    public void saveString(String key, String value) {
        preferences.edit().putString(key, value).apply();
    }
    
    /**
     * Obtém um valor string genérico
     */
    public String getString(String key, String defaultValue) {
        return preferences.getString(key, defaultValue);
    }
    
    /**
     * Salva um valor booleano
     */
    public void saveBoolean(String key, boolean value) {
        preferences.edit().putBoolean(key, value).apply();
    }
    
    /**
     * Obtém um valor booleano
     */
    public boolean getBoolean(String key, boolean defaultValue) {
        return preferences.getBoolean(key, defaultValue);
    }
}
