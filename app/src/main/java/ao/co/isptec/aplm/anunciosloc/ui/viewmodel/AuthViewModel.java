package ao.co.isptec.aplm.anunciosloc.ui.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import ao.co.isptec.aplm.anunciosloc.utils.PreferencesHelper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import ao.co.isptec.aplm.anunciosloc.data.model.User;
import ao.co.isptec.aplm.anunciosloc.data.model.LoginRequest;
import ao.co.isptec.aplm.anunciosloc.data.model.LoginResponse;
import ao.co.isptec.aplm.anunciosloc.data.model.RegisterRequest;
import ao.co.isptec.aplm.anunciosloc.data.network.RetrofitClient;

/**
 * ViewModel para gerenciar autenticação, focado na API.
 */
public class AuthViewModel extends AndroidViewModel {
    
    private final MutableLiveData<User> authenticatedUser = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> loginSuccess = new MutableLiveData<>();
    private final MutableLiveData<Boolean> registerSuccess = new MutableLiveData<>();
    
    public AuthViewModel(@NonNull Application application) {
        super(application);
    }
    
    // Getters para LiveData
    public LiveData<User> getAuthenticatedUser() { return authenticatedUser; }
    public LiveData<String> getErrorMessage() { return errorMessage; }
    public LiveData<Boolean> getIsLoading() { return isLoading; }
    public LiveData<Boolean> getLoginSuccess() { return loginSuccess; }
    public LiveData<Boolean> getRegisterSuccess() { return registerSuccess; }
    
    /**
     * Realiza login através da API.
     */
    public void login(String username, String password) {
        isLoading.setValue(true);
        errorMessage.setValue(null);
        loginSuccess.setValue(false);

        LoginRequest request = new LoginRequest(username, password);
        RetrofitClient.getApiService().login(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                isLoading.postValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse resp = response.body();
                    User user = resp.getUser();

                    // SALVA A SESSÃO COM O TOKEN REAL
                    PreferencesHelper.saveUserSession(
                            getApplication(),
                            resp.getSessionToken(),
                            user 
                    );

                    authenticatedUser.postValue(user);
                    loginSuccess.postValue(true);
                } else {
                    errorMessage.postValue("Usuário ou senha inválidos (código: " + response.code() + ")");
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                isLoading.postValue(false);
                errorMessage.postValue("Erro de conexão: " + t.getMessage());
            }
        });
    }
    
    /**
     * Registra novo usuário através da API.
     */
    public void register(String username, String password) {
        isLoading.setValue(true);
        errorMessage.setValue(null);
        registerSuccess.setValue(false);

        RegisterRequest request = new RegisterRequest(username, password);

        RetrofitClient.getApiService().register(request).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                isLoading.postValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    registerSuccess.postValue(true);
                } else {
                    // MOSTRA O CÓDIGO DE ERRO PARA DEPURAR
                    errorMessage.postValue("Falha no registo (código: " + response.code() + ")");
                    registerSuccess.postValue(false);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                isLoading.postValue(false);
                errorMessage.postValue("Falha na conexão: " + t.getMessage());
                registerSuccess.postValue(false);
            }
        });
    }
    
    /**
     * Faz logout.
     */
    public void logout() {
        authenticatedUser.setValue(null);
        loginSuccess.setValue(false); // Garante que o estado de sucesso seja limpo
        PreferencesHelper.clearSession(getApplication());
    }
    
    /**
     * Obtém usuário atual.
     */
    public User getCurrentUser() {
        return authenticatedUser.getValue();
    }
}
