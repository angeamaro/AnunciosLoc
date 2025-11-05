package ao.co.isptec.aplm.anunciosloc.data.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import ao.co.isptec.aplm.anunciosloc.data.model.Notification;
import ao.co.isptec.aplm.anunciosloc.utils.Constants;

/**
 * Repositório mock para gerenciar notificações
 */
public class NotificationRepository {
    
    private static NotificationRepository instance;
    private final Map<String, Notification> notificationsDatabase;
    
    private NotificationRepository() {
        notificationsDatabase = new HashMap<>();
        initializeMockData();
    }
    
    public static synchronized NotificationRepository getInstance() {
        if (instance == null) {
            instance = new NotificationRepository();
        }
        return instance;
    }
    
    /**
     * Inicializa dados mockados
     */
    private void initializeMockData() {
        // Notificação 1
        Notification notif1 = new Notification("1", 
            Constants.NOTIFICATION_NEW_ANNOUNCEMENT,
            "Novo Anúncio Disponível",
            "Workshop de Programação no ISPTEC");
        notif1.setRelatedId("1");
        notif1.setTimestamp(System.currentTimeMillis() - (2 * 60 * 60 * 1000)); // 2 horas atrás
        notificationsDatabase.put(notif1.getId(), notif1);
        
        // Notificação 2
        Notification notif2 = new Notification("2",
            Constants.NOTIFICATION_LOCATION_ENTERED,
            "Você entrou em uma nova área",
            "Existem anúncios disponíveis para Belas Shopping");
        notif2.setRelatedId("5");
        notif2.setTimestamp(System.currentTimeMillis() - (24 * 60 * 60 * 1000)); // 1 dia atrás
        notif2.setRead(true);
        notificationsDatabase.put(notif2.getId(), notif2);
        
        // Notificação 3
        Notification notif3 = new Notification("3",
            Constants.NOTIFICATION_MESSAGE_RECEIVED,
            "Mensagem Recebida via P2P",
            "Você recebeu um anúncio de Alice Silva");
        notif3.setTimestamp(System.currentTimeMillis() - (4 * 60 * 60 * 1000)); // 4 horas atrás
        notificationsDatabase.put(notif3.getId(), notif3);
        
        // Notificação 4
        Notification notif4 = new Notification("4",
            Constants.NOTIFICATION_NEW_ANNOUNCEMENT,
            "Novo Evento Cultural",
            "Visita Guiada Histórica na Fortaleza de São Miguel");
        notif4.setRelatedId("4");
        notif4.setTimestamp(System.currentTimeMillis() - (30 * 60 * 1000)); // 30 minutos atrás
        notificationsDatabase.put(notif4.getId(), notif4);
    }
    
    /**
     * Cria nova notificação
     */
    public Notification createNotification(String type, String title, String message, String relatedId) {
        String id = UUID.randomUUID().toString();
        Notification notification = new Notification(id, type, title, message);
        notification.setRelatedId(relatedId);
        notificationsDatabase.put(id, notification);
        return notification;
    }
    
    /**
     * Obtém todas as notificações
     */
    public List<Notification> getAllNotifications() {
        List<Notification> notifications = new ArrayList<>(notificationsDatabase.values());
        // Ordena por timestamp (mais recentes primeiro)
        notifications.sort((n1, n2) -> Long.compare(n2.getTimestamp(), n1.getTimestamp()));
        return notifications;
    }
    
    /**
     * Obtém notificações não lidas
     */
    public List<Notification> getUnreadNotifications() {
        List<Notification> unreadNotifications = new ArrayList<>();
        for (Notification notification : notificationsDatabase.values()) {
            if (!notification.isRead()) {
                unreadNotifications.add(notification);
            }
        }
        unreadNotifications.sort((n1, n2) -> Long.compare(n2.getTimestamp(), n1.getTimestamp()));
        return unreadNotifications;
    }
    
    /**
     * Obtém notificação por ID
     */
    public Notification getNotificationById(String notificationId) {
        return notificationsDatabase.get(notificationId);
    }
    
    /**
     * Marca notificação como lida
     */
    public boolean markAsRead(String notificationId) {
        Notification notification = notificationsDatabase.get(notificationId);
        if (notification != null) {
            notification.setRead(true);
            return true;
        }
        return false;
    }
    
    /**
     * Marca todas como lidas
     */
    public void markAllAsRead() {
        for (Notification notification : notificationsDatabase.values()) {
            notification.setRead(true);
        }
    }
    
    /**
     * Remove notificação
     */
    public boolean deleteNotification(String notificationId) {
        return notificationsDatabase.remove(notificationId) != null;
    }
    
    /**
     * Remove todas as notificações
     */
    public void clearAllNotifications() {
        notificationsDatabase.clear();
    }
    
    /**
     * Obtém contagem de notificações não lidas
     */
    public int getUnreadCount() {
        int count = 0;
        for (Notification notification : notificationsDatabase.values()) {
            if (!notification.isRead()) {
                count++;
            }
        }
        return count;
    }
    
    /**
     * Obtém notificações por tipo
     */
    public List<Notification> getNotificationsByType(String type) {
        List<Notification> typeNotifications = new ArrayList<>();
        for (Notification notification : notificationsDatabase.values()) {
            if (type.equals(notification.getType())) {
                typeNotifications.add(notification);
            }
        }
        typeNotifications.sort((n1, n2) -> Long.compare(n2.getTimestamp(), n1.getTimestamp()));
        return typeNotifications;
    }
}
