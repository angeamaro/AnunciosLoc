package ao.co.isptec.aplm.anunciosloc.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Map;

import ao.co.isptec.aplm.anunciosloc.data.model.User;
import ao.co.isptec.aplm.anunciosloc.data.repository.UserRepository;

/**
 * ViewModel para gerenciar perfil do usuário
 */
public class ProfileViewModel extends ViewModel {
    
    private final UserRepository userRepository;
    
    private final MutableLiveData<User> currentUser = new MutableLiveData<>();
    private final MutableLiveData<Map<String, String>> profileAttributes = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> updateSuccess = new MutableLiveData<>();
    
    public ProfileViewModel() {
        this.userRepository = UserRepository.getInstance();
        loadCurrentUser();
    }
    
    // Getters para LiveData
    public LiveData<User> getCurrentUser() {
        return currentUser;
    }
    
    public LiveData<User> getUser() {
        return currentUser;
    }
    
    public LiveData<Map<String, String>> getProfileAttributes() {
        return profileAttributes;
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
     * Carrega usuário atual
     */
    public void loadCurrentUser() {
        try {
            User user = userRepository.getCurrentUser();
            if (user != null) {
                currentUser.setValue(user);
                profileAttributes.setValue(user.getProfileAttributes());
            }
        } catch (Exception e) {
            e.printStackTrace();
            errorMessage.setValue("Erro ao carregar usuário: " + e.getMessage());
        }
    }
    
    /**
     * Atualiza perfil do usuário
     */
    public void updateProfile(User user) {
        isLoading.setValue(true);
        errorMessage.setValue(null);
        
        new Thread(() -> {
            try {
                Thread.sleep(800); // Simula delay
                
                boolean success = userRepository.updateUserProfile(user);
                
                if (success) {
                    currentUser.postValue(user);
                    profileAttributes.postValue(user.getProfileAttributes());
                    updateSuccess.postValue(true);
                } else {
                    errorMessage.postValue("Erro ao atualizar perfil");
                    updateSuccess.postValue(false);
                }
            } catch (InterruptedException e) {
                errorMessage.postValue("Erro ao atualizar perfil");
                updateSuccess.postValue(false);
            } finally {
                isLoading.postValue(false);
            }
        }).start();
    }
    
    /**
     * Adiciona atributo ao perfil
     */
    public void addProfileAttribute(String key, String value) {
        try {
            if (key == null || value == null || key.isEmpty() || value.isEmpty()) {
                errorMessage.setValue("Chave e valor não podem ser vazios");
                return;
            }
            
            isLoading.setValue(true);
            errorMessage.setValue(null);
            
            new Thread(() -> {
                try {
                    Thread.sleep(500);
                    
                    boolean success = userRepository.addProfileAttribute(key, value);
                    
                    if (success) {
                        loadCurrentUser();
                        updateSuccess.postValue(true);
                    } else {
                        errorMessage.postValue("Erro ao adicionar atributo");
                        updateSuccess.postValue(false);
                    }
                } catch (InterruptedException e) {
                    errorMessage.postValue("Erro ao adicionar atributo");
                    updateSuccess.postValue(false);
                } catch (Exception e) {
                    e.printStackTrace();
                    errorMessage.postValue("Erro ao adicionar atributo: " + e.getMessage());
                    updateSuccess.postValue(false);
                } finally {
                    isLoading.postValue(false);
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
            errorMessage.setValue("Erro ao adicionar atributo: " + e.getMessage());
            isLoading.setValue(false);
        }
    }
    
    /**
     * Remove atributo do perfil
     */
    public void removeProfileAttribute(String key) {
        try {
            if (key == null || key.isEmpty()) {
                errorMessage.setValue("Chave não pode ser vazia");
                return;
            }
            
            isLoading.setValue(true);
            errorMessage.setValue(null);
            
            new Thread(() -> {
                try {
                    Thread.sleep(500);
                    
                    boolean success = userRepository.removeProfileAttribute(key);
                    
                    if (success) {
                        loadCurrentUser();
                        updateSuccess.postValue(true);
                    } else {
                        errorMessage.postValue("Erro ao remover atributo");
                        updateSuccess.postValue(false);
                    }
                } catch (InterruptedException e) {
                    errorMessage.postValue("Erro ao remover atributo");
                    updateSuccess.postValue(false);
                } catch (Exception e) {
                    e.printStackTrace();
                    errorMessage.postValue("Erro ao remover atributo: " + e.getMessage());
                    updateSuccess.postValue(false);
                } finally {
                    isLoading.postValue(false);
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
            errorMessage.setValue("Erro ao remover atributo: " + e.getMessage());
            isLoading.setValue(false);
        }
    }
    
    /**
     * Atualiza informações básicas (nome, email, telefone)
     */
    public void updateBasicInfo(String name, String phoneNumber) {
        User user = currentUser.getValue();
        if (user != null) {
            user.setName(name);
            user.setPhoneNumber(phoneNumber);
            updateProfile(user);
        }
    }
    
    /**
     * Atualiza foto de perfil
     */
    public void updateProfilePhoto(String photoUrl) {
        User user = currentUser.getValue();
        if (user != null) {
            user.setPhotoUrl(photoUrl);
            updateProfile(user);
        }
    }
}
