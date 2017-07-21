package cn.com.wh.ring.network.retrofit;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Hui on 2017/7/13.
 * application级别拦截器
 */

public class HeaderInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();

        Request compressedRequest = originalRequest.newBuilder()
                .header(Server.HEADER_TOKEN, Server.TOKEN)
                .header(Server.HEADER_API_VERSION, Server.API_VERSION)
                .header("Content-Type", "application/json")
                .build();
        return chain.proceed(compressedRequest);
    }
}
