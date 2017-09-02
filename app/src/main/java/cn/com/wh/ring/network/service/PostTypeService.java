package cn.com.wh.ring.network.service;

import cn.com.wh.ring.network.response.Page;
import cn.com.wh.ring.network.response.PostType;
import cn.com.wh.ring.network.response.Response;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Hui on 2017/8/1.
 */

public interface PostTypeService {
    @GET("postType/v1/page")
    Call<Response<Page<PostType>>> get(@Query("maxId") Long maxId,
                                       @Query("pageNumber") int pageNumber,
                                       @Query("pageSize") int pageSize);
}
