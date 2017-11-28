package cn.com.wh.ring.helper;

import android.text.TextUtils;

import cn.com.wh.ring.utils.RSAUtils;
import cn.com.wh.ring.utils.TerminalMarkUtils;

/**
 * Created by Hui on 2017/7/20.
 */

public class TerminalMarkHelper {
    private static final String COLON = "_";
    private static final String TYPE = "1";
    private static final String TOURIST = "/tourist";

    public static String createTerminalMark() {
        String uuid = TerminalMarkUtils.getUUID();
        String imei = TerminalMarkUtils.getImei();
        String macAddress = TerminalMarkUtils.getMacAddress();
        StringBuilder stringBuilder = new StringBuilder(uuid);
        stringBuilder.append(COLON).append(TextUtils.isEmpty(imei) ? "" : imei)
                .append(COLON).append(TextUtils.isEmpty(macAddress) ? "" : macAddress)
                .append(COLON).append(TYPE);
        return RSAUtils.encrypt(stringBuilder.toString()) + TOURIST;
    }

}
