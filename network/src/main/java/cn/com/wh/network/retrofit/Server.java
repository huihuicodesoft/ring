package cn.com.wh.network.retrofit;

import cn.com.wh.network.config.ServerConstants;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * Created by Hui on 2017/7/13.
 */

public class Server {
    public final Retrofit retrofit;

    private static Server ourInstance = new Server();

    public static Server getInstance() {
        return ourInstance;
    }

    private Server() {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new HeaderInterceptor())
                .build();
        retrofit = new Retrofit.Builder().client(client).baseUrl(ServerConstants.HOST).build();
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }
}
