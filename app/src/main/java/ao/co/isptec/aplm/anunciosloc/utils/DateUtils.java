package ao.co.isptec.aplm.anunciosloc.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Utilitários para manipulação de datas
 */
public class DateUtils {
    
    private static final SimpleDateFormat DATE_FORMAT = 
        new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    
    private static final SimpleDateFormat DATETIME_FORMAT = 
        new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
    
    private static final SimpleDateFormat TIME_FORMAT = 
        new SimpleDateFormat("HH:mm", Locale.getDefault());
    
    /**
     * Formata uma data no formato dd/MM/yyyy
     */
    public static String formatDate(Date date) {
        if (date == null) return "";
        return DATE_FORMAT.format(date);
    }
    
    /**
     * Formata um timestamp (long) no formato dd/MM/yyyy
     */
    public static String formatDate(long timestamp) {
        if (timestamp <= 0) return "";
        return DATE_FORMAT.format(new Date(timestamp));
    }
    
    /**
     * Formata uma data e hora no formato dd/MM/yyyy HH:mm
     */
    public static String formatDateTime(Date date) {
        if (date == null) return "";
        return DATETIME_FORMAT.format(date);
    }
    
    /**
     * Formata um timestamp (long) no formato dd/MM/yyyy HH:mm
     */
    public static String formatDateTime(long timestamp) {
        if (timestamp <= 0) return "";
        return DATETIME_FORMAT.format(new Date(timestamp));
    }
    
    /**
     * Formata apenas a hora no formato HH:mm
     */
    public static String formatTime(Date date) {
        if (date == null) return "";
        return TIME_FORMAT.format(date);
    }
    
    /**
     * Formata um timestamp (long) apenas a hora no formato HH:mm
     */
    public static String formatTime(long timestamp) {
        if (timestamp <= 0) return "";
        return TIME_FORMAT.format(new Date(timestamp));
    }
    
    /**
     * Calcula o tempo restante em formato legível
     */
    public static String getTimeRemaining(Date endDate) {
        if (endDate == null) return "Expirado";
        
        long diffInMillis = endDate.getTime() - System.currentTimeMillis();
        
        if (diffInMillis <= 0) {
            return "Expirado";
        }
        
        long days = TimeUnit.MILLISECONDS.toDays(diffInMillis);
        long hours = TimeUnit.MILLISECONDS.toHours(diffInMillis) % 24;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis) % 60;
        
        if (days > 0) {
            return days + "d " + hours + "h";
        } else if (hours > 0) {
            return hours + "h " + minutes + "m";
        } else {
            return minutes + " minutos";
        }
    }
    
    /**
     * Verifica se uma data ainda é válida (não expirou)
     */
    public static boolean isDateValid(Date endDate) {
        if (endDate == null) return false;
        return endDate.getTime() > System.currentTimeMillis();
    }
    
    /**
     * Verifica se uma data está dentro de uma janela temporal
     */
    public static boolean isInTimeWindow(Date startDate, Date endDate) {
        long currentTime = System.currentTimeMillis();
        
        if (startDate != null && startDate.getTime() > currentTime) {
            return false; // Ainda não começou
        }
        
        if (endDate != null && endDate.getTime() < currentTime) {
            return false; // Já expirou
        }
        
        return true;
    }
    
    /**
     * Formata um timestamp em tempo relativo (ex: "Há 5 minutos", "Há 2 horas")
     */
    public static String formatRelativeTime(long timestamp) {
        if (timestamp <= 0) return "Agora";
        
        long diffInMillis = System.currentTimeMillis() - timestamp;
        
        if (diffInMillis < 0) {
            return "Agora";
        }
        
        long seconds = TimeUnit.MILLISECONDS.toSeconds(diffInMillis);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis);
        long hours = TimeUnit.MILLISECONDS.toHours(diffInMillis);
        long days = TimeUnit.MILLISECONDS.toDays(diffInMillis);
        
        if (seconds < 60) {
            return "Agora";
        } else if (minutes < 60) {
            return "Há " + minutes + (minutes == 1 ? " minuto" : " minutos");
        } else if (hours < 24) {
            return "Há " + hours + (hours == 1 ? " hora" : " horas");
        } else if (days < 7) {
            return "Há " + days + (days == 1 ? " dia" : " dias");
        } else {
            // Mais de 7 dias, mostra a data
            return formatDate(timestamp);
        }
    }
}
