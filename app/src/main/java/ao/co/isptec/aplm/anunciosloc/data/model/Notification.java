package ao.co.isptec.aplm.anunciosloc.data.model;

import ao.co.isptec.aplm.anunciosloc.utils.Constants;

/**
 * Modelo de Notificação
 */
public class Notification {
    private String id;
    private String type; // NEW_ANNOUNCEMENT, LOCATION_ENTERED, MESSAGE_RECEIVED
    private String title;
    private String message;
    private String relatedId; // ID do anúncio ou localização relacionada
    private long timestamp;
    private boolean isRead;
    
    public Notification() {
        this.timestamp = System.currentTimeMillis();
        this.isRead = false;
    }
    
    public Notification(String id, String type, String title, String message) {
        this();
        this.id = id;
        this.type = type;
        this.title = title;
        this.message = message;
    }
    
    // Getters e Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public String getRelatedId() {
        return relatedId;
    }
    
    public void setRelatedId(String relatedId) {
        this.relatedId = relatedId;
    }
    
    public long getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    
    public boolean isRead() {
        return isRead;
    }
    
    public void setRead(boolean read) {
        isRead = read;
    }
    
    /**
     * Retorna o ícone apropriado baseado no tipo
     */
    public int getIconResource() {
        // Será implementado quando tivermos os drawables
        return 0;
    }
}
