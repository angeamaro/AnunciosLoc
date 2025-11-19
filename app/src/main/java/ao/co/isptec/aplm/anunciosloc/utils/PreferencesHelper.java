package ao.co.isptec.aplm.anunciosloc.utils;

import android.content.Context;
import android.content.SharedPreferences;

import ao.co.isptec.aplm.anunciosloc.data.model.User;

public class PreferencesHelper {

    private static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static void saveUserSession(Context context, String userId, String userName, String token) {
        SharedPreferences.Editor editor = getPrefs(context).edit();
        editor.putString(Constants.KEY_USER_ID, userId);
        editor.putString(Constants.KEY_USER_NAME, userName);
        editor.putString(Constants.KEY_SESSION_TOKEN, token);
        editor.putBoolean(Constants.KEY_IS_LOGGED_IN, true);
        editor.apply();
    }

    public static boolean isLoggedIn(Context context) {
        return getPrefs(context).getBoolean(Constants.KEY_IS_LOGGED_IN, false);
    }

    public static String getUserId(Context context) {
        return getPrefs(context).getString(Constants.KEY_USER_ID, null);
    }

    public static String getUserName(Context context) {
        return getPrefs(context).getString(Constants.KEY_USER_NAME, null);
    }

    public static String getToken(Context context) {
        return getPrefs(context).getString(Constants.KEY_SESSION_TOKEN, null);
    }

    public static void clearSession(Context context) {
        SharedPreferences.Editor editor = getPrefs(context).edit();
        editor.remove(Constants.KEY_USER_ID);
        editor.remove(Constants.KEY_USER_NAME);
        editor.remove(Constants.KEY_SESSION_TOKEN);
        editor.putBoolean(Constants.KEY_IS_LOGGED_IN, false);
        editor.apply();
    }
}
