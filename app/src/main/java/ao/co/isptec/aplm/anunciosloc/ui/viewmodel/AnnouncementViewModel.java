package ao.co.isptec.aplm.anunciosloc.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.Map;

import ao.co.isptec.aplm.anunciosloc.data.model.Announcement;
import ao.co.isptec.aplm.anunciosloc.data.model.User;
import ao.co.isptec.aplm.anunciosloc.data.repository.AnnouncementRepository;
import ao.co.isptec.aplm.anunciosloc.data.repository.UserRepository;

/**
 * ViewModel para gerenciar anúncios
 */
public class AnnouncementViewModel extends ViewModel {
    
    private final AnnouncementRepository announcementRepository;
    private final UserRepository userRepository;
    
    private final MutableLiveData<List<Announcement>> announcements = new MutableLiveData<>();
    private final MutableLiveData<List<Announcement>> userAnnouncements = new MutableLiveData<>();
    private final MutableLiveData<List<Announcement>> locationAnnouncements = new MutableLiveData<>();
    private final MutableLiveData<Announcement> selectedAnnouncement = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> operationSuccess = new MutableLiveData<>();
    private final MutableLiveData<Boolean> announcementCreated = new MutableLiveData<>();
    private final MutableLiveData<Boolean> announcementUpdated = new MutableLiveData<>();
    private final MutableLiveData<Boolean> announcementDeleted = new MutableLiveData<>();
    
    public AnnouncementViewModel() {
        this.announcementRepository = AnnouncementRepository.getInstance();
        this.userRepository = UserRepository.getInstance();
        loadAnnouncements();
    }
    
    // Getters para LiveData
    public LiveData<List<Announcement>> getAnnouncements() {
        return announcements;
    }
    
    public LiveData<List<Announcement>> getUserAnnouncements() {
        return userAnnouncements;
    }
    
    public LiveData<List<Announcement>> getLocationAnnouncements() {
        return locationAnnouncements;
    }
    
    public LiveData<Announcement> getSelectedAnnouncement() {
        return selectedAnnouncement;
    }
    
    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }
    
    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }
    
    public LiveData<Boolean> getOperationSuccess() {
        return operationSuccess;
    }
    
    public LiveData<Boolean> getAnnouncementCreated() {
        return announcementCreated;
    }
    
    public LiveData<Boolean> getAnnouncementUpdated() {
        return announcementUpdated;
    }
    
    public LiveData<Boolean> getAnnouncementDeleted() {
        return announcementDeleted;
    }
    
    /**
     * Carrega todos os anúncios ativos
     */
    public void loadAnnouncements() {
        isLoading.setValue(true);
        
        new Thread(() -> {
            try {
                Thread.sleep(500); // Simula delay
                List<Announcement> announcementList = announcementRepository.getActiveAnnouncements();
                announcements.postValue(announcementList);
            } catch (InterruptedException e) {
                errorMessage.postValue("Erro ao carregar anúncios");
            } finally {
                isLoading.postValue(false);
            }
        }).start();
    }
    
    /**
     * Carrega anúncios filtrados por política de usuário
     */
    public void loadAnnouncementsForUser(Map<String, String> userAttributes) {
        isLoading.setValue(true);
        
        new Thread(() -> {
            try {
                Thread.sleep(500);
                List<Announcement> filtered = announcementRepository.getAnnouncementsForUser(userAttributes);
                announcements.postValue(filtered);
            } catch (InterruptedException e) {
                errorMessage.postValue("Erro ao carregar anúncios");
            } finally {
                isLoading.postValue(false);
            }
        }).start();
    }
    
    /**
     * Carrega anúncios de um usuário específico
     */
    public void loadUserAnnouncements(String userId) {
        isLoading.setValue(true);
        
        new Thread(() -> {
            try {
                Thread.sleep(500);
                List<Announcement> announcementList = announcementRepository.getAnnouncementsByUser(userId);
                userAnnouncements.postValue(announcementList);
            } catch (InterruptedException e) {
                errorMessage.postValue("Erro ao carregar anúncios do usuário");
            } finally {
                isLoading.postValue(false);
            }
        }).start();
    }
    
    /**
     * Carrega anúncios de uma localização específica
     */
    public void loadLocationAnnouncements(String locationId) {
        isLoading.setValue(true);
        
        new Thread(() -> {
            try {
                Thread.sleep(500);
                List<Announcement> announcementList = announcementRepository.getAnnouncementsByLocation(locationId);
                locationAnnouncements.postValue(announcementList);
            } catch (InterruptedException e) {
                errorMessage.postValue("Erro ao carregar anúncios da localização");
            } finally {
                isLoading.postValue(false);
            }
        }).start();
    }
    
    /**
     * Cria novo anúncio
     */
    public void createAnnouncement(Announcement announcement) {
        isLoading.setValue(true);
        errorMessage.setValue(null);
        
        new Thread(() -> {
            try {
                Thread.sleep(1000);
                Announcement created = announcementRepository.createAnnouncement(announcement);
                
                if (created != null) {
                    announcementCreated.postValue(true);
                    operationSuccess.postValue(true);
                    loadAnnouncements();
                } else {
                    errorMessage.postValue("Erro ao criar anúncio");
                    announcementCreated.postValue(false);
                    operationSuccess.postValue(false);
                }
            } catch (InterruptedException e) {
                errorMessage.postValue("Erro ao criar anúncio");
                operationSuccess.postValue(false);
            } finally {
                isLoading.postValue(false);
            }
        }).start();
    }
    
    /**
     * Cria novo anúncio com parâmetros individuais
     */
    public void createAnnouncement(String title, String content, String locationId, 
                                   long startDate, long endDate, String deliveryPolicy, 
                                   List<ao.co.isptec.aplm.anunciosloc.data.model.PolicyRule> policyRules) {
        // Cria objeto Announcement
        Announcement announcement = new Announcement();
        announcement.setTitle(title);
        announcement.setContent(content);
        announcement.setLocationId(locationId);
        announcement.setStartDate(new java.util.Date(startDate));
        announcement.setEndDate(new java.util.Date(endDate));
        announcement.setDeliveryPolicy(deliveryPolicy);
        announcement.setPolicyRules(policyRules);
        
        // Define autor (usuário atual)
        User currentUser = userRepository.getCurrentUser();
        if (currentUser != null) {
            announcement.setAuthorId(currentUser.getId());
            announcement.setAuthorName(currentUser.getName());
        }
        
        // Chama método principal
        createAnnouncement(announcement);
    }
    
    /**
     * Atualiza anúncio
     */
    public void updateAnnouncement(Announcement announcement) {
        isLoading.setValue(true);
        errorMessage.setValue(null);
        
        new Thread(() -> {
            try {
                Thread.sleep(800);
                boolean success = announcementRepository.updateAnnouncement(announcement);
                
                if (success) {
                    announcementUpdated.postValue(true);
                    operationSuccess.postValue(true);
                    loadAnnouncements();
                } else {
                    errorMessage.postValue("Erro ao atualizar anúncio");
                    announcementUpdated.postValue(false);
                    operationSuccess.postValue(false);
                }
            } catch (InterruptedException e) {
                errorMessage.postValue("Erro ao atualizar anúncio");
                operationSuccess.postValue(false);
            } finally {
                isLoading.postValue(false);
            }
        }).start();
    }
    
    /**
     * Deleta anúncio
     */
    public void deleteAnnouncement(String announcementId) {
        isLoading.setValue(true);
        errorMessage.setValue(null);
        
        new Thread(() -> {
            try {
                Thread.sleep(500);
                boolean success = announcementRepository.deleteAnnouncement(announcementId);
                
                if (success) {
                    announcementDeleted.postValue(true);
                    operationSuccess.postValue(true);
                    loadAnnouncements();
                } else {
                    errorMessage.postValue("Erro ao deletar anúncio");
                    announcementDeleted.postValue(false);
                    operationSuccess.postValue(false);
                }
            } catch (InterruptedException e) {
                errorMessage.postValue("Erro ao deletar anúncio");
                operationSuccess.postValue(false);
            } finally {
                isLoading.postValue(false);
            }
        }).start();
    }
    
    /**
     * Seleciona um anúncio e incrementa visualizações
     */
    public void selectAnnouncement(String announcementId) {
        Announcement announcement = announcementRepository.getAnnouncementById(announcementId);
        if (announcement != null) {
            announcementRepository.incrementViewCount(announcementId);
            selectedAnnouncement.setValue(announcement);
        }
    }
    
    /**
     * Busca anúncios
     */
    public void searchAnnouncements(String query) {
        isLoading.setValue(true);
        
        new Thread(() -> {
            try {
                Thread.sleep(300);
                List<Announcement> results = announcementRepository.searchAnnouncements(query);
                announcements.postValue(results);
            } catch (InterruptedException e) {
                errorMessage.postValue("Erro ao buscar anúncios");
            } finally {
                isLoading.postValue(false);
            }
        }).start();
    }
    
    /**
     * Carrega anúncios recentes
     */
    public void loadRecentAnnouncements() {
        isLoading.setValue(true);
        
        new Thread(() -> {
            try {
                Thread.sleep(500);
                List<Announcement> recent = announcementRepository.getRecentAnnouncements();
                announcements.postValue(recent);
            } catch (InterruptedException e) {
                errorMessage.postValue("Erro ao carregar anúncios recentes");
            } finally {
                isLoading.postValue(false);
            }
        }).start();
    }
    
    /**
     * Busca usuário por ID
     */
    public LiveData<User> getUserById(long userId) {
        MutableLiveData<User> userLiveData = new MutableLiveData<>();
        
        new Thread(() -> {
            try {
                User user = userRepository.getUserById(String.valueOf(userId));
                userLiveData.postValue(user);
            } catch (Exception e) {
                e.printStackTrace();
                userLiveData.postValue(null);
            }
        }).start();
        
        return userLiveData;
    }
}
