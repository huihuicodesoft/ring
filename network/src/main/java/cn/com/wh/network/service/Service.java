package cn.com.wh.network.service;

import cn.com.wh.network.retrofit.Server;

/**
 * Created by Hui on 2017/7/13.
 */

public class Service {
    public static final UserService userService;

    static {
        userService = Server.getInstance().getRetrofit().create(UserService.class);
    }
}
