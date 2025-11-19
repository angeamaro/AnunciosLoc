package ao.co.isptec.aplm.anunciosloc.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.List;
import ao.co.isptec.aplm.anunciosloc.data.model.Announcement;
import ao.co.isptec.aplm.anunciosloc.data.model.PolicyFilter;

public class AnnouncementViewModel extends ViewModel {
    
    private final MutableLiveData<List<Announcement>> announcements = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<Boolean> announcementCreated = new MutableLiveData<>();
    private final MutableLiveData<Boolean> announcementDeleted = new MutableLiveData<>();

    public LiveData<List<Announcement>> getAnnouncements() { return announcements; }
    public LiveData<String> getErrorMessage() { return errorMessage; }
    public LiveData<Boolean> getIsLoading() { return isLoading; }
    public LiveData<Boolean> getAnnouncementCreated() { return announcementCreated; }
    public LiveData<Boolean> getAnnouncementDeleted() { return announcementDeleted; }

    public void loadAnnouncements() {
        // TODO: Implementar chamada à API para carregar anúncios
    }

    public void createAnnouncement(String title, String content, String locationId, long startDate, long endDate, String deliveryPolicy, PolicyFilter policyFilter, String deliveryMode) {
        // TODO: Implementar chamada à API para criar anúncio
    }

    public void deleteAnnouncement(String announcementId) {
        // TODO: Implementar chamada à API para deletar anúncio
    }
}
