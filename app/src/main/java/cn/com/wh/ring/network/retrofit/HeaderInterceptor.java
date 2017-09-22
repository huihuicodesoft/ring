package cn.com.wh.ring.network.retrofit;

import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;

import cn.com.wh.ring.database.sp.DataCenter;
import cn.com.wh.ring.helper.TerminalMarkHelper;
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
        configToken();
        Log.v("token", ""+ Server.TOKEN);
        Request compressedRequest = originalRequest.newBuilder()
                .header(Server.HEADER_TOKEN, Server.TOKEN)
                .header(Server.HEADER_API_VERSION, Server.API_VERSION)
                .header("Content-Type", "application/json")
                .header("Connection", "close")
                .build();

        return chain.proceed(compressedRequest);
    }

    private void configToken() {
        String token = DataCenter.getInstance().getToken();
        if (TextUtils.isEmpty(token)) {
            String terminalMark = DataCenter.getInstance().getTerminalMark();
            if (TextUtils.isEmpty(terminalMark)) {
                String newTerminalMark = TerminalMarkHelper.createTerminalMark();
                Server.TOKEN = newTerminalMark;
                DataCenter.getInstance().setTerminalMark(newTerminalMark);
            } else {
                Server.TOKEN = terminalMark;
            }
        } else {
            Server.TOKEN = token;
        }
    }
}
