package cn.com.wh.ring.network.request;

/**
 * Created by Hui on 2017/7/23.
 */
public class LoginThird {
    private String account;
    private int accountType;
    private String accessToken;
    private String refreshToken;
    private TerminalDetailInfo terminalDetailInfo;
    private Address address;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public int getAccountType() {
        return accountType;
    }

    public void setAccountType(int accountType) {
        this.accountType = accountType;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public TerminalDetailInfo getTerminalDetailInfo() {
        return terminalDetailInfo;
    }

    public void setTerminalDetailInfo(TerminalDetailInfo terminalDetailInfo) {
        this.terminalDetailInfo = terminalDetailInfo;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
