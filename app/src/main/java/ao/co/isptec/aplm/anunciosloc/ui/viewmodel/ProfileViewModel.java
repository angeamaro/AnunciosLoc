package ao.co.isptec.aplm.anunciosloc.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Map;

import ao.co.isptec.aplm.anunciosloc.data.model.User;
import ao.co.isptec.aplm.anunciosloc.data.repository.UserRepository;

/**
 * ViewModel para gerenciar perfil do usuário, alinhado com o novo modelo de dados.
 */
public class ProfileViewModel extends ViewModel {
    
    private final UserRepository userRepository;
    
    private final MutableLiveData<User> currentUser = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> updateSuccess = new MutableLiveData<>();
    
    public ProfileViewModel() {
        this.userRepository = UserRepository.getInstance();
        loadCurrentUser();
    }
    
    // Getters para LiveData
    public LiveData<User> getUser() {
        return currentUser;
    }
    
    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }
    
    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }
    
    public LiveData<Boolean> getUpdateSuccess() {
        return updateSuccess;
    }
    
    /**
     * Carrega o usuário atual do repositório (que o obteve durante o login).
     */
    public void loadCurrentUser() {
        try {
            User user = userRepository.getCurrentUser();
            if (user != null) {
                currentUser.setValue(user);
            }
        } catch (Exception e) {
            errorMessage.setValue("Erro ao carregar usuário: " + e.getMessage());
        }
    }
    
    /**
     * Simula a atualização do perfil. No futuro, isto chamaria uma API.
     */
    public void updateProfile(User user) {
        isLoading.setValue(true);
        new Thread(() -> {
            try {
                Thread.sleep(800); // Simula delay de rede

                // Lógica de API viria aqui. Por agora, apenas simulamos sucesso.
                currentUser.postValue(user);
                updateSuccess.postValue(true);

            } catch (InterruptedException e) {
                errorMessage.postValue("Erro ao atualizar perfil");
                updateSuccess.postValue(false);
            } finally {
                isLoading.postValue(false);
            }
        }).start();
    }
    
    /**
     * Adiciona atributo ao perfil do usuário atual.
     */
    public void addProfileAttribute(String key, String value) {
        User user = currentUser.getValue();
        if (user == null) {
            errorMessage.setValue("Usuário não carregado");
            return;
        }
        if (key == null || value == null || key.isEmpty() || value.isEmpty()) {
            errorMessage.setValue("Chave e valor não podem ser vazios");
            return;
        }
        
        // Modifica o objeto localmente e depois "salva"
        Map<String, String> attributes = user.getProfileAttributes();
        attributes.put(key, value);
        user.setProfileAttributes(attributes);

        updateProfile(user); // Chama o método de atualização
    }
    
    /**
     * Remove atributo do perfil do usuário atual.
     */
    public void removeProfileAttribute(String key) {
        User user = currentUser.getValue();
        if (user == null) {
            errorMessage.setValue("Usuário não carregado");
            return;
        }
        if (key == null || key.isEmpty()) {
            errorMessage.setValue("Chave não pode ser vazia");
            return;
        }

        // Modifica o objeto localmente e depois "salva"
        Map<String, String> attributes = user.getProfileAttributes();
        attributes.remove(key);
        user.setProfileAttributes(attributes);

        updateProfile(user); // Chama o método de atualização
    }
    
    // Os métodos updateBasicInfo e updateProfilePhoto foram removidos 
    // porque os campos `name`, `phoneNumber` e `photoUrl` não existem mais no modelo User.
}
