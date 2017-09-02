package cn.com.wh.ring.network.service;

import cn.com.wh.ring.network.request.MobileAccount;
import cn.com.wh.ring.network.request.ThirdAccount;
import cn.com.wh.ring.network.response.Response;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Hui on 2017/7/23.
 */

public interface AccountService {

    @POST("account/v1/login/mobile")
    Call<Response<String>> loginMobile(@Body MobileAccount mobileAccount);

    @POST("account/v1/register/mobile")
    Call<Response<String>> registerMobile(@Body MobileAccount mobileAccount);

    @POST("account/v1/login/third")
    Call<Response<String>> loginThird(@Body ThirdAccount thirdAccount);

    @POST("account/v1/reset/mobile/password")
    Call<Response<String>> resetPassword(@Body MobileAccount mobileAccount);

    @POST("account/v1/valid/mobile")
    Call<Response<String>> validMobile(@Query("mobile") String mobile);
}