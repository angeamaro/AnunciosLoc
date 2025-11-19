package ao.co.isptec.aplm.anunciosloc.utils;

import android.view.View;

import com.google.android.material.snackbar.Snackbar;

import ao.co.isptec.aplm.anunciosloc.R;

/**
 * Classe utilitária para exibir feedback visual ao usuário
 */
public class FeedbackUtils {
    
    /**
     * Exibe Snackbar de sucesso
     */
    public static void showSuccess(View view, String message) {
        if (view == null || message == null) return;
        
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        snackbar.setBackgroundTint(view.getContext().getColor(R.color.success));
        snackbar.setTextColor(view.getContext().getColor(R.color.white));
        snackbar.show();
    }
    
    /**
     * Exibe Snackbar de erro
     */
    public static void showError(View view, String message) {
        if (view == null || message == null) return;
        
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        snackbar.setBackgroundTint(view.getContext().getColor(R.color.error));
        snackbar.setTextColor(view.getContext().getColor(R.color.white));
        snackbar.setAction("OK", v -> snackbar.dismiss());
        snackbar.setActionTextColor(view.getContext().getColor(R.color.white));
        snackbar.show();
    }
    
    /**
     * Exibe Snackbar de informação
     */
    public static void showInfo(View view, String message) {
        if (view == null || message == null) return;
        
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        snackbar.setBackgroundTint(view.getContext().getColor(R.color.blue_600));
        snackbar.setTextColor(view.getContext().getColor(R.color.white));
        snackbar.show();
    }
    
    /**
     * Exibe Snackbar de aviso
     */
    public static void showWarning(View view, String message) {
        if (view == null || message == null) return;
        
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        snackbar.setBackgroundTint(view.getContext().getColor(R.color.orange_600));
        snackbar.setTextColor(view.getContext().getColor(R.color.white));
        snackbar.show();
    }
    
    /**
     * Exibe Snackbar com ação customizada
     */
    public static void showWithAction(View view, String message, String actionText, 
                                      View.OnClickListener actionListener) {
        if (view == null || message == null) return;
        
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        snackbar.setBackgroundTint(view.getContext().getColor(R.color.gray_800));
        snackbar.setTextColor(view.getContext().getColor(R.color.white));
        
        if (actionText != null && actionListener != null) {
            snackbar.setAction(actionText, actionListener);
            snackbar.setActionTextColor(view.getContext().getColor(R.color.blue_400));
        }
        
        snackbar.show();
    }
    
    /**
     * Exibe Snackbar indefinida (até ser fechada)
     */
    public static Snackbar showIndefinite(View view, String message, String actionText) {
        if (view == null || message == null) return null;
        
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE);
        snackbar.setBackgroundTint(view.getContext().getColor(R.color.gray_800));
        snackbar.setTextColor(view.getContext().getColor(R.color.white));
        
        if (actionText != null) {
            snackbar.setAction(actionText, v -> snackbar.dismiss());
            snackbar.setActionTextColor(view.getContext().getColor(R.color.blue_400));
        }
        
        snackbar.show();
        return snackbar;
    }
}
