package cn.com.wh.ring.network.service;

import java.util.List;

import cn.com.wh.ring.network.response.Response;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Hui on 2017/7/31.
 */

public interface FileService {
    @POST("files/v1/image/upload")
    Call<Response<List<String>>> upload(@Body MultipartBody multipartBody);
}
