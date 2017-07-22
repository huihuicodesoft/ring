package cn.com.wh.ring.database.sp;

import android.content.Context;
import android.content.SharedPreferences;

import cn.com.wh.ring.MainApplication;

/**
 * Created by Hui on 2016/4/14.
 */
public class DataCenter {
    private static final String TOKEN = "token";
    private static final String TERMINAL_MARK = "terminalMark";
    private static final String AGREE_PROTOCOL = "agreeProtocol";

    private SharedPreferences sp;
    private SharedPreferences.Editor mEditor;

    private static DataCenter mSharedPreferencesManager;

    public synchronized static DataCenter getInstance() {
        if (mSharedPreferencesManager == null) {
            mSharedPreferencesManager = new DataCenter();
        }

        return mSharedPreferencesManager;
    }

    private DataCenter() {
        sp = MainApplication.getInstance().getSharedPreferences("data", Context.MODE_PRIVATE);
        mEditor = sp.edit();
    }

    public void setToken(String token) {
        mEditor.putString(TOKEN, token);
        mEditor.commit();
    }

    public String getToken() {
        return sp.getString(TOKEN, "");
    }

    public void setTerminalMark(String terminalMark) {
        mEditor.putString(TERMINAL_MARK, terminalMark);
        mEditor.commit();
    }

    public String getTerminalMark() {
        return sp.getString(TERMINAL_MARK, "");
    }

    public void setAgreeProtol(boolean isAgreeProtocol) {
        mEditor.putBoolean(AGREE_PROTOCOL, isAgreeProtocol);
        mEditor.commit();
    }

    public boolean isAgreeProtol() {
        return sp.getBoolean(AGREE_PROTOCOL, false);
    }
}
