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
    private final MutableLiveData<List<Location>> userLocations = new MutableLiveData<>();
    private final MutableLiveData<Location> selectedLocation = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> operationSuccess = new MutableLiveData<>();
    private final MutableLiveData<Boolean> locationCreated = new MutableLiveData<>();
    private final MutableLiveData<Boolean> locationUpdated = new MutableLiveData<>();
    private final MutableLiveData<Boolean> locationDeleted = new MutableLiveData<>();
    
    public LocationViewModel() {
        this.locationRepository = LocationRepository.getInstance();
        loadLocations();
    }
    
    // Getters para LiveData
    public LiveData<List<Location>> getLocations() {
        return locations;
    }
    
    public LiveData<List<Location>> getUserLocations() {
        return userLocations;
    }
    
    public LiveData<Location> getSelectedLocation() {
        return selectedLocation;
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
    
    public LiveData<Boolean> getLocationCreated() {
        return locationCreated;
    }
    
    public LiveData<Boolean> getLocationUpdated() {
        return locationUpdated;
    }
    
    public LiveData<Boolean> getLocationDeleted() {
        return locationDeleted;
    }
    
    /**
     * Carrega todas as localizações
     */
    public void loadLocations() {
        isLoading.postValue(true);
        
        new Thread(() -> {
            try {
                Thread.sleep(500); // Simula delay
                List<Location> locationList = locationRepository.getActiveLocations();
                locations.postValue(locationList);
            } catch (InterruptedException e) {
                errorMessage.postValue("Erro ao carregar localizações");
            } finally {
                isLoading.postValue(false);
            }
        }).start();
    }
    
    /**
     * Carrega localizações de um usuário específico
     */
    public void loadUserLocations(String userId) {
        isLoading.postValue(true);
        
        new Thread(() -> {
            try {
                Thread.sleep(500);
                List<Location> locationList = locationRepository.getLocationsByUser(userId);
                userLocations.postValue(locationList);
            } catch (InterruptedException e) {
                errorMessage.postValue("Erro ao carregar localizações do usuário");
            } finally {
                isLoading.postValue(false);
            }
        }).start();
    }
    
    /**
     * Cria nova localização
     */
    public void createLocation(Location location) {
        isLoading.postValue(true);
        errorMessage.postValue(null);
        
        new Thread(() -> {
            try {
                Thread.sleep(800);
                Location createdLocation = locationRepository.createLocation(location);
                
                if (createdLocation != null) {
                    operationSuccess.postValue(true);
                    locationCreated.postValue(true);
                    loadLocations(); // Recarrega a lista
                } else {
                    errorMessage.postValue("Erro ao criar localização");
                    operationSuccess.postValue(false);
                    locationCreated.postValue(false);
                }
            } catch (InterruptedException e) {
                errorMessage.postValue("Erro ao criar localização");
                operationSuccess.postValue(false);
                locationCreated.postValue(false);
            } finally {
                isLoading.postValue(false);
            }
        }).start();
    }
    
    /**
     * Atualiza localização
     */
    public void updateLocation(Location location) {
        isLoading.postValue(true);
        errorMessage.postValue(null);
        
        new Thread(() -> {
            try {
                Thread.sleep(800);
                boolean success = locationRepository.updateLocation(location);
                
                if (success) {
                    operationSuccess.postValue(true);
                    locationUpdated.postValue(true);
                    loadLocations();
                } else {
                    errorMessage.postValue("Erro ao atualizar localização");
                    operationSuccess.postValue(false);
                    locationUpdated.postValue(false);
                }
            } catch (InterruptedException e) {
                errorMessage.postValue("Erro ao atualizar localização");
                operationSuccess.postValue(false);
                locationUpdated.postValue(false);
            } finally {
                isLoading.postValue(false);
            }
        }).start();
    }
    
    /**
     * Deleta localização
     */
    public void deleteLocation(String locationId) {
        isLoading.postValue(true);
        errorMessage.postValue(null);
        
        new Thread(() -> {
            try {
                Thread.sleep(500);
                boolean success = locationRepository.deleteLocation(locationId);
                
                if (success) {
                    operationSuccess.postValue(true);
                    locationDeleted.postValue(true);
                    loadLocations();
                } else {
                    errorMessage.postValue("Erro ao deletar localização");
                    operationSuccess.postValue(false);
                    locationDeleted.postValue(false);
                }
            } catch (InterruptedException e) {
                errorMessage.postValue("Erro ao deletar localização");
                operationSuccess.postValue(false);
                locationDeleted.postValue(false);
            } finally {
                isLoading.postValue(false);
            }
        }).start();
    }
    
    /**
     * Seleciona uma localização
     */
    public void selectLocation(Location location) {
        selectedLocation.setValue(location);
    }
    
    /**
     * Busca localizações por nome
     */
    public void searchLocations(String query) {
        isLoading.setValue(true);
        
        new Thread(() -> {
            try {
                Thread.sleep(300);
                List<Location> results = locationRepository.searchLocationsByName(query);
                locations.postValue(results);
            } catch (InterruptedException e) {
                errorMessage.postValue("Erro ao buscar localizações");
            } finally {
                isLoading.postValue(false);
            }
        }).start();
    }
    
    /**
     * Obtém localizações próximas
     */
    public void getNearbyLocations(double latitude, double longitude, int radius) {
        isLoading.setValue(true);
        
        new Thread(() -> {
            try {
                Thread.sleep(500);
                List<Location> nearby = locationRepository.getNearbyLocations(latitude, longitude, radius);
                locations.postValue(nearby);
            } catch (InterruptedException e) {
                errorMessage.postValue("Erro ao buscar localizações próximas");
            } finally {
                isLoading.postValue(false);
            }
        }).start();
    }
}
