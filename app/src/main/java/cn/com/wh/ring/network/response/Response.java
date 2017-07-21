package cn.com.wh.ring.network.response;

/**
 * Created by Hui on 2017/7/7.
 */
public class Response<T> {
    private int code;
    private String description;
    private T payload;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public T getPayload() {
        return payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }
}
