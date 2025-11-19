package ao.co.isptec.aplm.anunciosloc.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import ao.co.isptec.aplm.anunciosloc.data.model.User;
import ao.co.isptec.aplm.anunciosloc.data.repository.UserRepository;

/**
 * ViewModel para gerenciar operações relacionadas a usuários
 */
public class UserViewModel extends ViewModel {
    
    private final UserRepository userRepository;
    private final MutableLiveData<List<User>> allUsers;
    private final MutableLiveData<Boolean> isLoading;
    private final MutableLiveData<String> errorMessage;
    
    public UserViewModel() {
        this.userRepository = UserRepository.getInstance();
        this.allUsers = new MutableLiveData<>();
        this.isLoading = new MutableLiveData<>(false);
        this.errorMessage = new MutableLiveData<>();
    }
    
    /**
     * Carrega todos os usuários
     */
    public void loadAllUsers() {
        isLoading.setValue(true);
        errorMessage.setValue(null);
        
        try {
            // UserRepository.getAllUsers() é síncrono (mock), então chamamos diretamente
            List<User> users = userRepository.getAllUsers();
            allUsers.setValue(users);
            isLoading.setValue(false);
        } catch (Exception e) {
            errorMessage.setValue(e.getMessage());
            isLoading.setValue(false);
        }
    }
    
    public LiveData<List<User>> getAllUsers() {
        return allUsers;
    }
    
    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }
    
    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }
}
