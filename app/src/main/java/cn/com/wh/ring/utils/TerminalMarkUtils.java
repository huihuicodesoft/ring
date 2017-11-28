package cn.com.wh.ring.utils;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

import java.util.UUID;

import cn.com.wh.ring.MainApplication;

/**
 * Created by Hui on 2017/7/20.
 */

public class TerminalMarkUtils {

    /**
     * 获取IMEI
     *
     * @return
     */
    public static String getImei() {
        String result = null;
        if (MainApplication.getInstance() != null) {
            result = ((TelephonyManager) MainApplication.getInstance().getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE))
                    .getDeviceId();
        }
        return result;
    }

    /**
     * mac地址
     *
     * @return
     */
    public static String getMacAddress() {
        String result = null;
        if (MainApplication.getInstance() != null) {
            WifiManager wm = (WifiManager) MainApplication.getInstance().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            result = wm.getConnectionInfo().getMacAddress();
        }
        return result;
    }

    /**
     * UUID
     *
     * @return
     */
    public static String getUUID() {
        return UUID.randomUUID().toString();
    }
}
