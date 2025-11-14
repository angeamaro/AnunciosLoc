package ao.co.isptec.aplm.anunciosloc.data.network;

import ao.co.isptec.aplm.anunciosloc.data.model.RegisterRequest;
import ao.co.isptec.aplm.anunciosloc.data.model.LoginRequest;
import ao.co.isptec.aplm.anunciosloc.data.model.LoginResponse;
import ao.co.isptec.aplm.anunciosloc.data.model.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("api/auth/register")
    Call<User> register(@Body RegisterRequest request);

    @POST("api/auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);
}
