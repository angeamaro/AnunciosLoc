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
        // Notificação 1 - Anúncio 2: Promoção de Verão
        Notification notif1 = new Notification("1", 
            Constants.NOTIFICATION_NEW_ANNOUNCEMENT,
            "🍕 Promoção de Verão",
            "Descontos em todas as lojas! Válido até o fim do mês.");
        notif1.setRelatedId("2"); // ID do anúncio "Promoção de Verão"
        notif1.setTimestamp(System.currentTimeMillis());
        notificationsDatabase.put(notif1.getId(), notif1);
        
        // Notificação 2 - Anúncio 3: Torneio de Futebol
        Notification notif2 = new Notification("2",
            Constants.NOTIFICATION_NEW_ANNOUNCEMENT,
            "💪 Torneio de Futebol",
            "Inscreva sua equipe no torneio comunitário. Amantes de desporto bem-vindos!");
        notif2.setRelatedId("3"); // ID do anúncio "Torneio de Futebol"
        notif2.setTimestamp(System.currentTimeMillis() - (1 * 60 * 60 * 1000)); // 1 hora atrás
        notificationsDatabase.put(notif2.getId(), notif2);
        
        // Notificação 3 - Anúncio 4: Visita Guiada Histórica
        Notification notif3 = new Notification("3",
            Constants.NOTIFICATION_NEW_ANNOUNCEMENT,
            "📚 Visita Guiada Histórica",
            "Conheça a história de Luanda através dos seus monumentos.");
        notif3.setRelatedId("4"); // ID do anúncio "Visita Guiada Histórica"
        notif3.setTimestamp(System.currentTimeMillis() - (2 * 60 * 60 * 1000)); // 2 horas atrás
        notificationsDatabase.put(notif3.getId(), notif3);
        
        // Notificação 4 - Anúncio 1: Workshop de Programação
        Notification notif4 = new Notification("4",
            Constants.NOTIFICATION_NEW_ANNOUNCEMENT,
            "☕ Workshop de Programação",
            "Venha aprender Java e Android! Inscrições abertas para estudantes.");
        notif4.setRelatedId("1"); // ID do anúncio "Workshop de Programação"
        notif4.setTimestamp(System.currentTimeMillis() - (3 * 60 * 60 * 1000)); // 3 horas atrás
        notificationsDatabase.put(notif4.getId(), notif4);
        
        // Notificação 5 - Anúncio 6: Hackathon 2025
        Notification notif5 = new Notification("5",
            Constants.NOTIFICATION_NEW_ANNOUNCEMENT,
            "🎧 Hackathon 2025",
            "48 horas de código, inovação e prêmios! Interessados em tecnologia, participem!");
        notif5.setRelatedId("6"); // ID do anúncio "Hackathon 2025"
        notif5.setTimestamp(System.currentTimeMillis() - (4 * 60 * 60 * 1000)); // 4 horas atrás
        notificationsDatabase.put(notif5.getId(), notif5);
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
