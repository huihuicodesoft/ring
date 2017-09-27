package cn.com.wh.ring.network.response;


import cn.com.wh.ring.database.bean.UserInfo;

/**
 * Created by Hui on 2017/9/22.
 */
public class LoginUser {
    private String token;
    private UserInfo userInfo;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
}
