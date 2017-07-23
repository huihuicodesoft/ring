package cn.com.wh.ring.network.retrofit;

import java.util.concurrent.TimeUnit;

import cn.com.wh.ring.BuildConfig;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Hui on 2017/7/13.
 */

public class Server {
    public static final String HOST = "http://192.168.2.101:8080/rest/";

    public static final String HEADER_TOKEN = "token";
    public static String TOKEN = "";
    public static final String HEADER_API_VERSION = "ApiVersion";
    public static final String API_VERSION = "A1";

    public final Retrofit retrofit;

    private static Server ourInstance = new Server();

    public static Server getInstance() {
        return ourInstance;
    }

    private Server() {
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            client.addInterceptor(loggingInterceptor);
        }
        client.addInterceptor(new HeaderInterceptor());
        client.connectTimeout(8, TimeUnit.SECONDS);
        client.retryOnConnectionFailure(false);

        retrofit = new Retrofit.Builder()
                .baseUrl(HOST)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client.build())
                .build();
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }
}
