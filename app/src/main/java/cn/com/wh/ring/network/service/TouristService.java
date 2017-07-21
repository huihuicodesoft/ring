package cn.com.wh.ring.network.service;

import cn.com.wh.ring.network.request.TerminalMark;
import cn.com.wh.ring.network.response.Response;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Hui on 2017/7/20.
 */

public interface TouristService {

    @POST("tourist/v1/record")
    Call<Response<String>> getTerminalToken(@Body TerminalMark terminalMark);
}
