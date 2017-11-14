package cn.com.wh.ring.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import cn.com.wh.ring.MainApplication;

/**
 * Created by Hui on 2017/11/14.
 */

public class NetworkUtils {
    public enum NETWORK_STATE {
        NONE, WIFI, TWO_G, THREE_G, FOUR_G
    }

    /**
     * 获取当前的网络类型
     *
     * @return
     */
    public static NETWORK_STATE getNetType() {
        NETWORK_STATE netType = NETWORK_STATE.NONE;
        ConnectivityManager manager = (ConnectivityManager) MainApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo == null) {
            return netType;
        }
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_WIFI) {
            netType = NETWORK_STATE.WIFI;
        } else if (nType == ConnectivityManager.TYPE_MOBILE) {
            int nSubType = networkInfo.getSubtype();
            TelephonyManager telephonyManager = (TelephonyManager) MainApplication.getInstance().getSystemService(Context.TELEPHONY_SERVICE);
            if (telephonyManager.isNetworkRoaming()) {
                netType = NETWORK_STATE.TWO_G;
            } else {
                if (nSubType == TelephonyManager.NETWORK_TYPE_LTE) {
                    netType = NETWORK_STATE.FOUR_G;
                } else if (nSubType == TelephonyManager.NETWORK_TYPE_UMTS
                        || nSubType == TelephonyManager.NETWORK_TYPE_HSDPA
                        || nSubType == TelephonyManager.NETWORK_TYPE_EVDO_0) {
                    netType = NETWORK_STATE.THREE_G;
                } else {
                    netType = NETWORK_STATE.TWO_G;
                }
            }
        }
        return netType;
    }
}
