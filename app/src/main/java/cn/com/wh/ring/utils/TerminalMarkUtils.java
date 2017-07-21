package cn.com.wh.ring.utils;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.net.wifi.WifiManager;

import java.util.UUID;

import cn.com.wh.ring.MainApplication;

/**
 * Created by Hui on 2017/7/20.
 */

public class TerminalMarkUtils {
    /**
     * wifi
     *
     * @return
     */
    public static String getMacAddress() {
        String result = null;
        if (MainApplication.getInstance() != null) {
            WifiManager wm = (WifiManager) MainApplication.getInstance().getSystemService(Context.WIFI_SERVICE);
            result = wm.getConnectionInfo().getMacAddress();
        }
        return result;
    }

    /**
     * 蓝牙
     *
     * @return
     */
    public static String getBluetoothAddress() {
        return BluetoothAdapter.getDefaultAdapter().getAddress();
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
