package ao.co.isptec.aplm.anunciosloc.data.network;

import androidx.annotation.NonNull;

import java.io.IOException;

import ao.co.isptec.aplm.anunciosloc.App;
import ao.co.isptec.aplm.anunciosloc.utils.PreferencesHelper;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {

    @NonNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        String path = originalRequest.url().encodedPath();

        if (path.endsWith("/register") || path.endsWith("/login")) {
            return chain.proceed(originalRequest);
        }

        String token = PreferencesHelper.getToken(App.getContext());
        
        Request.Builder builder = originalRequest.newBuilder();
        if (token != null && !token.isEmpty()) {
            builder.header("Authorization", "Bearer " + token);
        }

        Request newRequest = builder.build();
        return chain.proceed(newRequest);
    }
}
