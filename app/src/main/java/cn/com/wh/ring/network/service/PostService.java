package cn.com.wh.ring.network.service;

import cn.com.wh.ring.network.request.PostPublish;
import cn.com.wh.ring.network.response.Page;
import cn.com.wh.ring.network.response.Post;
import cn.com.wh.ring.network.response.Response;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Hui on 2017/8/17.
 */

public interface PostService {
    @POST("post/v1/publish")
    Call<Response<Object>> publish(@Body PostPublish postPublish);

    @GET("post/v1/user/page")
    Call<Response<Page<Post>>> get(@Query("userId") Long userId,
                                   @Query("maxId") Long maxId,
                                   @Query("pageNumber") int pageNumber,
                                   @Query("pageSize") int pageSize);
}
