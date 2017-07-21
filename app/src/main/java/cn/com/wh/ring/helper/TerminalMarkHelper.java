package cn.com.wh.ring.helper;

import android.text.TextUtils;

import cn.com.wh.ring.network.request.TerminalMark;
import cn.com.wh.ring.utils.TerminalMarkUtils;

/**
 * Created by Hui on 2017/7/20.
 */

public class TerminalMarkHelper {
    private static final String COLON = "_";
    private static final String TYPE_WIFI = "wifi";
    private static final String TYPE_BLUETOOTH = "bluetooth";
    private static final String TYPE_UUID = "uuid";

    public static String createTerminalMark() {
        String result;
        String macAddress = TerminalMarkUtils.getMacAddress();
        if (TextUtils.isEmpty(macAddress)) {
            String bluetoothAddress = TerminalMarkUtils.getBluetoothAddress();
            if (TextUtils.isEmpty(bluetoothAddress)) {
                result = contact(TerminalMarkUtils.getUUID(), TYPE_UUID);
            } else {
                result = contact(bluetoothAddress, TYPE_BLUETOOTH);
            }
        } else {
            result = contact(macAddress, TYPE_WIFI);
        }
        return result;
    }

    public static final String contact(String mark, String type) {
        return mark + COLON + type;
    }

    public static final TerminalMark split(String mark) {
        TerminalMark terminalMark = null;
        if (!TextUtils.isEmpty(mark) && mark.contains(COLON)) {
            int index = mark.lastIndexOf(COLON);
            if (index < mark.length()) {
                terminalMark = new TerminalMark();
                terminalMark.setTerminalMark(mark.substring(0, index));
                terminalMark.setType(mark.substring(index + 1, mark.length()));
            }
        }
        return terminalMark;
    }

}
