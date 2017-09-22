package cn.com.wh.ring.network.request;

/**
 * Created by Hui on 2017/7/23.
 */

public class LoginMobile {
    private String mobile;
    private String password;
    private TerminalDetailInfo terminalDetailInfo;

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

    public TerminalDetailInfo getTerminalDetailInfo() {
        return terminalDetailInfo;
    }

    public void setTerminalDetailInfo(TerminalDetailInfo terminalDetailInfo) {
        this.terminalDetailInfo = terminalDetailInfo;
    }
}
