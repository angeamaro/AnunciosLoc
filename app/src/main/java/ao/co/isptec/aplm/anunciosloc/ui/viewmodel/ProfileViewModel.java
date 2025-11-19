package ao.co.isptec.aplm.anunciosloc.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ao.co.isptec.aplm.anunciosloc.data.model.User;

public class ProfileViewModel extends ViewModel {
    
    private final MutableLiveData<User> user = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> updateSuccess = new MutableLiveData<>();

    public LiveData<User> getUser() { return user; }
    public LiveData<Boolean> getIsLoading() { return isLoading; }
    public LiveData<String> getErrorMessage() { return errorMessage; }
    public LiveData<Boolean> getUpdateSuccess() { return updateSuccess; }

    // Lógica para carregar e atualizar o usuário virá da API

    public void removeProfileAttribute(String key) {
        // TODO: Implementar chamada à API
    }

    public void addProfileAttribute(String key, String value) {
        // TODO: Implementar chamada à API
    }
}
