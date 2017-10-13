package cn.com.wh.ring.network.retrofit;


import android.content.Context;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import cn.com.wh.ring.R;
import cn.com.wh.ring.network.response.Response;
import cn.com.wh.ring.ui.activity.LoginActivity;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by Hui on 2017/7/21.
 */

public abstract class ListenerCallBack<T> implements Callback<Response<T>> {
    private Context mContext;

    public ListenerCallBack(Context context) {
        mContext = context;
    }

    @Override
    public void onResponse(Call<Response<T>> call, retrofit2.Response<Response<T>> response) {
        if (mContext == null)
            return;

        if (response.isSuccessful()) {
            Response<T> resp = response.body();
            int respCode = resp.getCode();
            if (respCode == ReturnCode.OK) {
                onSuccess(resp.getPayload());
            } else if (respCode == ReturnCode.ERROR_TOKEN) {
                onFailure(new NetWorkException(ReturnCode.ERROR_TOKEN, mContext.getString(R.string.tip_illegal_request))); //请求不合法
            } else {
                if (respCode == ReturnCode.ERROR_PERMISSION || respCode == ReturnCode.ERROR_TOKEN
                        || respCode == ReturnCode.ERROR_TOKEN_INVALID
                        || respCode == ReturnCode.ERROR_TOKEN_ERROR
                        || respCode == ReturnCode.ERROR_TOKEN_NULL) {
                    LoginActivity.start(mContext);
                    onFailure(new NetWorkException(resp.getCode(), mContext.getString(R.string.tip_please_login)));
                } else {
                    onFailure(new NetWorkException(resp.getCode(), resp.getDescription()));
                }
            }
        } else {
            onFailure(new NetWorkException(ReturnCode.ERROR_REQUEST, mContext.getString(R.string.tip_request_error)));
        }
    }

    @Override
    public void onFailure(Call<Response<T>> call, Throwable t) {
        if (mContext == null)
            return;

        if (t instanceof SocketException || t instanceof UnknownHostException) {
            onFailure(new NetWorkException(ReturnCode.ERROR_APP_NETWORK, mContext.getString(R.string.tip_request_error)));
        } else if (t instanceof SocketTimeoutException) {
            onFailure(new NetWorkException(ReturnCode.ERROR_APP_CONNECT_TIME, mContext.getString(R.string.tip_request_out_time)));
        } else if (t instanceof IOException && "Canceled".equals(t.getMessage())) {
            onFailure(new NetWorkException(ReturnCode.ERROR_APP_USER_CANCELED, mContext.getString(R.string.tip_request_cancel)));
        } else {
            onFailure(new NetWorkException(ReturnCode.ERROR_APP_UNKOWN, mContext.getString(R.string.tip_unkown_error)));
        }
    }

    public abstract void onSuccess(T t);

    public abstract void onFailure(NetWorkException e);

}
