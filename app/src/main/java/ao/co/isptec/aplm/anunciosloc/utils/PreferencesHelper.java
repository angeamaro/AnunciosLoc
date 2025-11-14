package ao.co.isptec.aplm.anunciosloc.utils;

import android.content.Context;
import android.content.SharedPreferences;

import ao.co.isptec.aplm.anunciosloc.data.model.User;

/**
 * Classe utilitária para gerenciar as SharedPreferences da aplicação.
 */
public class PreferencesHelper {
    private static final String PREF_NAME = "AnunciosLocPrefs";
    private static final String KEY_TOKEN = "session_token";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USERNAME = "username";

    private static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    /**
     * Salva a sessão do usuário após um login bem-sucedido.
     * @param context Contexto da aplicação.
     * @param token O token de sessão recebido da API.
     * @param user O objeto User que veio da API.
     */
    public static void saveUserSession(Context context, String token, User user) {
        if (user == null || user.getId() == null || user.getUsername() == null) {
            return; // Não salva uma sessão inválida
        }
        SharedPreferences.Editor editor = getPrefs(context).edit();
        editor.putString(KEY_TOKEN, token);
        editor.putString(KEY_USER_ID, String.valueOf(user.getId())); // Salva como String
        editor.putString(KEY_USERNAME, user.getUsername());
        editor.apply();
    }

    /**
     * Recupera o token de sessão salvo.
     * @param context Contexto da aplicação.
     * @return O token, ou null se não existir.
     */
    public static String getToken(Context context) {
        return getPrefs(context).getString(KEY_TOKEN, null);
    }

    /**
     * Recupera os dados do usuário logado para uso na aplicação.
     * @param context Contexto da aplicação.
     * @return Um objeto User com os dados salvos, ou null se não houver sessão.
     */
    public static User getCurrentUser(Context context) {
        String idStr = getPrefs(context).getString(KEY_USER_ID, null);
        String username = getPrefs(context).getString(KEY_USERNAME, null);

        if (idStr == null || username == null) {
            return null; // Não há sessão válida
        }

        try {
            long id = Long.parseLong(idStr);
            // Usa o construtor correto e define os dados. A senha não é necessária aqui.
            User user = new User(username, null); 
            user.setId(id);
            return user;
        } catch (NumberFormatException e) {
            // Se o ID salvo não for um número válido, a sessão está corrompida.
            return null;
        }
    }

    /**
     * Limpa todos os dados da sessão (usado no logout).
     * @param context Contexto da aplicação.
     */
    public static void clearSession(Context context) {
        getPrefs(context).edit().clear().apply();
    }

    /**
     * Verifica se existe um token de sessão, indicando que o usuário está logado.
     * @param context Contexto da aplicação.
     * @return true se o usuário está logado, false caso contrário.
     */
    public static boolean isLoggedIn(Context context) {
        return getToken(context) != null;
    }
}
