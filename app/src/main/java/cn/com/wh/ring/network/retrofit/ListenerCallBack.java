package cn.com.wh.ring.network.retrofit;


import java.net.ConnectException;

import cn.com.wh.ring.network.response.Response;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by Hui on 2017/7/21.
 */

public abstract class ListenerCallBack<T> implements Callback<cn.com.wh.ring.network.response.Response<T>> {

    @Override
    public void onResponse(Call<Response<T>> call, retrofit2.Response<Response<T>> response) {
        Response<T> resp = response.body();
        if (resp.getCode() == ReturnCode.OK) {
            onSuccess(resp.getPayload());
        } else if (resp.getCode() == ReturnCode.ERROR_TOKEN) {
            //无权
        } else {
            onFailure(new NetWorkException(resp.getCode(), resp.getDescription()));
        }
    }

    @Override
    public void onFailure(Call<Response<T>> call, Throwable t) {
        if (t instanceof ConnectException) {
            onFailure(new NetWorkException(ReturnCode.ERROR_APP_CONNECT, "连接失败"));
        }
    }

    public abstract void onSuccess(T t);

    public abstract void onFailure(NetWorkException e);

}
