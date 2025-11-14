package ao.co.isptec.aplm.anunciosloc.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import ao.co.isptec.aplm.anunciosloc.data.model.Location;
import ao.co.isptec.aplm.anunciosloc.data.repository.LocationRepository;

/**
 * ViewModel para gerenciar localizações
 */
public class LocationViewModel extends ViewModel {
    
    private final LocationRepository locationRepository;
    
    private final MutableLiveData<List<Location>> locations = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> locationCreated = new MutableLiveData<>();
    private final MutableLiveData<Boolean> locationUpdated = new MutableLiveData<>();
    private final MutableLiveData<Boolean> locationDeleted = new MutableLiveData<>();
    
    public LocationViewModel() {
        this.locationRepository = LocationRepository.getInstance();
        loadLocations();
    }
    
    // Getters para LiveData
    public LiveData<List<Location>> getLocations() { return locations; }
    public LiveData<String> getErrorMessage() { return errorMessage; }
    public LiveData<Boolean> getIsLoading() { return isLoading; }
    public LiveData<Boolean> getLocationCreated() { return locationCreated; }
    public LiveData<Boolean> getLocationUpdated() { return locationUpdated; }
    public LiveData<Boolean> getLocationDeleted() { return locationDeleted; }
    
    public void loadLocations() {
        isLoading.postValue(true);
        new Thread(() -> {
            try {
                List<Location> locationList = locationRepository.getActiveLocations();
                locations.postValue(locationList);
            } finally {
                isLoading.postValue(false);
            }
        }).start();
    }
    
    public void createLocation(Location location, String createdBy) {
        isLoading.postValue(true);
        errorMessage.postValue(null);
        
        location.setCreatedBy(createdBy);

        new Thread(() -> {
            try {
                Location createdLocation = locationRepository.createLocation(location);
                if (createdLocation != null) {
                    locationCreated.postValue(true);
                    loadLocations(); // Recarrega a lista
                } else {
                    errorMessage.postValue("Erro ao criar localização");
                    locationCreated.postValue(false);
                }
            } finally {
                isLoading.postValue(false);
            }
        }).start();
    }
    
    public void updateLocation(Location location, String updatedBy) {
        isLoading.postValue(true);
        errorMessage.postValue(null);

        location.setCreatedBy(updatedBy); // Assume que o criador é quem atualiza

        new Thread(() -> {
            try {
                boolean success = locationRepository.updateLocation(location);
                if (success) {
                    locationUpdated.postValue(true);
                    loadLocations();
                } else {
                    errorMessage.postValue("Erro ao atualizar localização");
                    locationUpdated.postValue(false);
                }
            } finally {
                isLoading.postValue(false);
            }
        }).start();
    }
    
    public void deleteLocation(String locationId) {
        isLoading.postValue(true);
        errorMessage.postValue(null);
        new Thread(() -> {
            try {
                boolean success = locationRepository.deleteLocation(locationId);
                if (success) {
                    locationDeleted.postValue(true);
                    loadLocations();
                } else {
                    errorMessage.postValue("Erro ao deletar localização");
                    locationDeleted.postValue(false);
                }
            } finally {
                isLoading.postValue(false);
            }
        }).start();
    }
}
