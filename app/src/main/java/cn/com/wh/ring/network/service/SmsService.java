package cn.com.wh.ring.network.service;

import cn.com.wh.ring.network.request.SmsCode;
import cn.com.wh.ring.network.response.Response;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Hui on 2017/7/23.
 */

public interface SmsService {

    @POST("sms/v1/code")
    Call<Response<String>> getVerificationCode(@Body SmsCode smsCode);
}
