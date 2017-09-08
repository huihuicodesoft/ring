package cn.com.wh.ring.helper;

import android.text.TextUtils;

import cn.com.wh.ring.utils.RSAUtils;
import cn.com.wh.ring.utils.TerminalMarkUtils;

/**
 * Created by Hui on 2017/7/20.
 */

public class TerminalMarkHelper {
    private static final String PROPERTY_COLON = "+";
    private static final String COLON = "_";
    private static final String TYPE_WIFI = "wifi";
    private static final String TYPE_BLUETOOTH = "bluetooth";
    private static final String TYPE_UUID = "uuid";
    private static final String TYPE = "1";
    private static final String TOURIST = "/tourist";

    public static String createTerminalMark() {
        String result = "";
        String macAddress = TerminalMarkUtils.getMacAddress();
        if (TextUtils.isEmpty(macAddress)) {
            String bluetoothAddress = TerminalMarkUtils.getBluetoothAddress();
            if (!TextUtils.isEmpty(bluetoothAddress)) {
                result = contact(bluetoothAddress, TYPE_BLUETOOTH);
            }
        } else {
            result = contact(macAddress, TYPE_WIFI);
        }

        String uuid = contact(TerminalMarkUtils.getUUID(), TYPE_UUID);
        result = contact(result, uuid);
        return RSAUtils.encrypt(result + COLON + TYPE) + TOURIST;
    }

    public static final String contact(String mark, String type) {
        return mark + PROPERTY_COLON + type;
    }

}
