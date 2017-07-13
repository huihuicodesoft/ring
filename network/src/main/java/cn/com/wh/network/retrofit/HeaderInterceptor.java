package cn.com.wh.network.retrofit;

import java.io.IOException;

import cn.com.wh.network.config.ServerConstants;
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
                .header(ServerConstants.HEADER_TOKEN, "gzip")
                .build();
        return chain.proceed(compressedRequest);
    }
}
