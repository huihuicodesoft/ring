package cn.com.wh.ring.network.service;

import cn.com.wh.ring.database.bean.UserInfo;
import cn.com.wh.ring.network.response.Response;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Hui on 2017/7/13.
 */

public interface UserService {
    @POST("users/v1/avatar/upload")
    Call<Response<String>> uploadAvatar(@Body MultipartBody multipartBody);


    @POST("users/v1/userInfo/update")
    Call<Response<Object>> uploadUserInfo(@Body UserInfo userInfo);
}
