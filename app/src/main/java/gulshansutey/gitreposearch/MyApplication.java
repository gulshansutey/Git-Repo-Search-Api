package gulshansutey.gitreposearch;

import android.app.Application;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by asus on 11/5/2017.
 */

public class MyApplication extends Application {


    public static String BASE_URL = "https://api.github.com/";
    /**
     * A singleton instance of the application class for easy access in other places
     */
    private static MyApplication retroSingletonInstance;
    private static Retrofit builder;
    private static Retroface retroface;

    /**
     * @return MyApplication singleton instance
     */
    public static synchronized MyApplication getInstance() {
        return retroSingletonInstance;
    }

    private static OkHttpClient getHeaders() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.addInterceptor(logging);
        httpClientBuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder().addHeader("Accept:", "application/vnd.github.mercy-preview+json")
                        .addHeader("Authentication:", "token 3deef189877ce1629f1751e80699943370b380fb").build();
                return chain.proceed(request);
            }
        });
        return httpClientBuilder.build();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        retroSingletonInstance = this;
    }

    public Retroface makeRequest() {
        if (builder == null) {
            builder = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(getHeaders())
                    .build();
        }
        return builder.create(Retroface.class);
    }

}
