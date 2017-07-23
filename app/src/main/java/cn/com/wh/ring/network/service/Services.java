package cn.com.wh.ring.network.service;

import cn.com.wh.ring.network.retrofit.Server;
import retrofit2.Retrofit;

/**
 * Created by Hui on 2017/7/13.
 */

public class Services {
    public static final UserService userService;
    public static final TouristService touristService;
    public static final AccountService accountService;
    public static final SmsService smsService;

    static {
        Retrofit retrofit = Server.getInstance().getRetrofit();
        userService = retrofit.create(UserService.class);
        touristService = retrofit.create(TouristService.class);
        accountService = retrofit.create(AccountService.class);
        smsService = retrofit.create(SmsService.class);
    }
}
