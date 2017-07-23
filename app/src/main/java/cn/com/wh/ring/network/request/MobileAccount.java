package cn.com.wh.ring.network.request;

/**
 * Created by Hui on 2017/7/23.
 */

public class MobileAccount {
    private String mobile;
    private String password;
    private String code;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
