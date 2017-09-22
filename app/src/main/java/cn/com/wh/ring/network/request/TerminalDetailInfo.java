package cn.com.wh.ring.network.request;

/**
 * Created by Hui on 2017/9/21.
 * 设备信息
 */
public class TerminalDetailInfo {
    private String model;
    private String sdk;
    private String system;

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSdk() {
        return sdk;
    }

    public void setSdk(String sdk) {
        this.sdk = sdk;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }
}
