package ao.co.isptec.aplm.anunciosloc.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ao.co.isptec.aplm.anunciosloc.data.model.User;
import ao.co.isptec.aplm.anunciosloc.data.repository.UserRepository;

/**
 * ViewModel para gerenciar autenticação (Login, Registro, Recuperação de senha)
 */
public class AuthViewModel extends ViewModel {
    
    private final UserRepository userRepository;
    
    private final MutableLiveData<User> authenticatedUser = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> loginSuccess = new MutableLiveData<>();
    private final MutableLiveData<Boolean> registerSuccess = new MutableLiveData<>();
    private final MutableLiveData<Boolean> passwordRecoverySuccess = new MutableLiveData<>();
    
    public AuthViewModel() {
        this.userRepository = UserRepository.getInstance();
    }
    
    // Getters para LiveData
    public LiveData<User> getAuthenticatedUser() {
        return authenticatedUser;
    }
    
    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }
    
    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }
    
    public LiveData<Boolean> getLoginSuccess() {
        return loginSuccess;
    }
    
    public LiveData<Boolean> getRegisterSuccess() {
        return registerSuccess;
    }
    
    public LiveData<Boolean> getPasswordRecoverySuccess() {
        return passwordRecoverySuccess;
    }
    
    /**
     * Realiza login
     */
    public void login(String email, String password) {
        isLoading.setValue(true);
        errorMessage.setValue(null);
        
        // Simula delay de rede
        new Thread(() -> {
            try {
                Thread.sleep(1000);
                
                User user = userRepository.login(email, password);
                
                if (user != null) {
                    authenticatedUser.postValue(user);
                    loginSuccess.postValue(true);
                } else {
                    errorMessage.postValue("Email ou senha inválidos");
                    loginSuccess.postValue(false);
                }
            } catch (InterruptedException e) {
                errorMessage.postValue("Erro ao fazer login");
                loginSuccess.postValue(false);
            } finally {
                isLoading.postValue(false);
            }
        }).start();
    }
    
    /**
     * Registra novo usuário
     */
    public void register(String username, String email, String password, String name) {
        isLoading.setValue(true);
        errorMessage.setValue(null);
        
        // Simula delay de rede
        new Thread(() -> {
            try {
                Thread.sleep(1000);
                
                if (userRepository.emailExists(email)) {
                    errorMessage.postValue("Este email já está registrado");
                    registerSuccess.postValue(false);
                } else {
                    User user = userRepository.register(username, email, password, name);
                    if (user != null) {
                        authenticatedUser.postValue(user);
                        registerSuccess.postValue(true);
                    } else {
                        errorMessage.postValue("Erro ao registrar usuário");
                        registerSuccess.postValue(false);
                    }
                }
            } catch (InterruptedException e) {
                errorMessage.postValue("Erro ao registrar");
                registerSuccess.postValue(false);
            } finally {
                isLoading.postValue(false);
            }
        }).start();
    }
    
    /**
     * Recupera senha
     */
    public void recoverPassword(String email) {
        isLoading.setValue(true);
        errorMessage.setValue(null);
        
        // Simula delay de rede
        new Thread(() -> {
            try {
                Thread.sleep(1500);
                
                boolean success = userRepository.recoverPassword(email);
                
                if (success) {
                    passwordRecoverySuccess.postValue(true);
                } else {
                    errorMessage.postValue("Email não encontrado");
                    passwordRecoverySuccess.postValue(false);
                }
            } catch (InterruptedException e) {
                errorMessage.postValue("Erro ao recuperar senha");
                passwordRecoverySuccess.postValue(false);
            } finally {
                isLoading.postValue(false);
            }
        }).start();
    }
    
    /**
     * Faz logout
     */
    public void logout() {
        userRepository.logout();
        authenticatedUser.setValue(null);
    }
    
    /**
     * Verifica se há usuário logado
     */
    public boolean isUserLoggedIn() {
        return userRepository.getCurrentUser() != null;
    }
    
    /**
     * Obtém usuário atual
     */
    public User getCurrentUser() {
        return userRepository.getCurrentUser();
    }
    
    /**
     * Define usuário atual (para sessão salva)
     */
    public void setCurrentUser(User user) {
        userRepository.setCurrentUser(user);
        authenticatedUser.setValue(user);
    }
}
