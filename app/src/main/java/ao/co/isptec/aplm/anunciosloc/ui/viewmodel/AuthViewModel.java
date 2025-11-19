package ao.co.isptec.aplm.anunciosloc.ui.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import ao.co.isptec.aplm.anunciosloc.data.model.LoginRequest;
import ao.co.isptec.aplm.anunciosloc.data.model.LoginResponse;
import ao.co.isptec.aplm.anunciosloc.data.model.RegisterRequest;
import ao.co.isptec.aplm.anunciosloc.data.network.ApiService;
import ao.co.isptec.aplm.anunciosloc.data.network.RetrofitClient;
import ao.co.isptec.aplm.anunciosloc.utils.PreferencesHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthViewModel extends AndroidViewModel {

    private final ApiService apiService;
    private final MutableLiveData<Boolean> loginSuccess = new MutableLiveData<>();
    private final MutableLiveData<Boolean> registerSuccess = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    public AuthViewModel(@NonNull Application application) {
        super(application);
        this.apiService = RetrofitClient.getApiService();
    }

    public LiveData<Boolean> getLoginSuccess() { return loginSuccess; }
    public LiveData<Boolean> getRegisterSuccess() { return registerSuccess; }
    public LiveData<String> getErrorMessage() { return errorMessage; }
    public LiveData<Boolean> getIsLoading() { return isLoading; }

    public void login(String username, String password) {
        isLoading.setValue(true);
        apiService.login(new LoginRequest(username, password)).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                isLoading.postValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();
                    PreferencesHelper.saveUserSession(
                        getApplication(),
                        String.valueOf(loginResponse.getUserId()),
                        loginResponse.getUsername(),
                        loginResponse.getToken()
                    );
                    loginSuccess.postValue(true);
                } else {
                    errorMessage.postValue("Login falhou: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                isLoading.postValue(false);
                errorMessage.postValue("Erro de conexão: " + t.getMessage());
            }
        });
    }

    public void register(String username, String password) {
        isLoading.setValue(true);
        apiService.register(new RegisterRequest(username, password)).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                isLoading.postValue(false);
                if (response.isSuccessful()) {
                    registerSuccess.postValue(true);
                } else {
                    errorMessage.postValue("Registo falhou: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                isLoading.postValue(false);
                errorMessage.postValue("Erro de conexão: " + t.getMessage());
            }
        });
    }
}
