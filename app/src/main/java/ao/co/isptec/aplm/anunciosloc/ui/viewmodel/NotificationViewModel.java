package ao.co.isptec.aplm.anunciosloc.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import ao.co.isptec.aplm.anunciosloc.data.model.Notification;
import ao.co.isptec.aplm.anunciosloc.data.repository.NotificationRepository;

/**
 * ViewModel para gerenciar notificações
 */
public class NotificationViewModel extends ViewModel {
    
    private final NotificationRepository notificationRepository;
    
    private final MutableLiveData<List<Notification>> notifications = new MutableLiveData<>();
    private final MutableLiveData<List<Notification>> unreadNotifications = new MutableLiveData<>();
    private final MutableLiveData<Integer> unreadCount = new MutableLiveData<>(0);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    
    public NotificationViewModel() {
        this.notificationRepository = NotificationRepository.getInstance();
        // NÃO carrega automaticamente - o fragment gerencia suas próprias notificações mockadas
        // Se quiser usar o Repository, chame loadNotifications() manualmente
    }
    
    // Getters para LiveData
    public LiveData<List<Notification>> getNotifications() {
        return notifications;
    }
    
    public LiveData<List<Notification>> getUnreadNotifications() {
        return unreadNotifications;
    }
    
    public LiveData<Integer> getUnreadCount() {
        return unreadCount;
    }
    
    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }
    
    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }
    
    /**
     * Carrega todas as notificações
     */
    public void loadNotifications() {
        isLoading.setValue(true);
        
        new Thread(() -> {
            try {
                Thread.sleep(400); // Simula delay
                List<Notification> notificationList = notificationRepository.getAllNotifications();
                notifications.postValue(notificationList);
                updateUnreadCount();
            } catch (InterruptedException e) {
                errorMessage.postValue("Erro ao carregar notificações");
            } finally {
                isLoading.postValue(false);
            }
        }).start();
    }
    
    /**
     * Carrega apenas notificações não lidas
     */
    public void loadUnreadNotifications() {
        isLoading.setValue(true);
        
        new Thread(() -> {
            try {
                Thread.sleep(300);
                List<Notification> unreadList = notificationRepository.getUnreadNotifications();
                unreadNotifications.postValue(unreadList);
                updateUnreadCount();
            } catch (InterruptedException e) {
                errorMessage.postValue("Erro ao carregar notificações não lidas");
            } finally {
                isLoading.postValue(false);
            }
        }).start();
    }
    
    /**
     * Marca notificação como lida
     */
    public void markAsRead(String notificationId) {
        new Thread(() -> {
            boolean success = notificationRepository.markAsRead(notificationId);
            if (success) {
                loadNotifications();
            }
        }).start();
    }
    
    /**
     * Deleta notificação
     */
    public void deleteNotification(String notificationId) {
        new Thread(() -> {
            boolean success = notificationRepository.deleteNotification(notificationId);
            if (success) {
                loadNotifications();
            }
        }).start();
    }
    
    /**
     * Limpa todas as notificações
     */
    public void clearAllNotifications() {
        isLoading.setValue(true);
        
        new Thread(() -> {
            try {
                Thread.sleep(500);
                notificationRepository.clearAllNotifications();
                loadNotifications();
            } catch (InterruptedException e) {
                errorMessage.postValue("Erro ao limpar notificações");
            } finally {
                isLoading.postValue(false);
            }
        }).start();
    }
    
    /**
     * Atualiza contador de não lidas
     */
    private void updateUnreadCount() {
        int count = notificationRepository.getUnreadCount();
        unreadCount.postValue(count);
    }
    
    /**
     * Cria nova notificação (para testes/simulação)
     */
    public void createNotification(String type, String title, String message, String relatedId) {
        new Thread(() -> {
            Notification notification = notificationRepository.createNotification(type, title, message, relatedId);
            if (notification != null) {
                loadNotifications();
            }
        }).start();
    }
    
    /**
     * Obtém notificações por tipo
     */
    public void loadNotificationsByType(String type) {
        isLoading.setValue(true);
        
        new Thread(() -> {
            try {
                Thread.sleep(300);
                List<Notification> typeNotifications = notificationRepository.getNotificationsByType(type);
                notifications.postValue(typeNotifications);
            } catch (InterruptedException e) {
                errorMessage.postValue("Erro ao carregar notificações");
            } finally {
                isLoading.postValue(false);
            }
        }).start();
    }
}
