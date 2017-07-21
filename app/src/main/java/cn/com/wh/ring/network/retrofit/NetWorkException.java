package cn.com.wh.ring.network.retrofit;

/**
 * Created by Hui on 2017/7/21.
 */

public class NetWorkException extends Exception {
    private int code;
    private String message;

    public NetWorkException(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
