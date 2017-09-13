package cn.com.wh.ring.helper;

import android.content.Context;

import cn.com.wh.ring.database.sp.DataCenter;
import cn.com.wh.ring.ui.activity.LoginActivity;

/**
 * Created by Hui on 2017/9/13.
 */

public class LoginHelper {

    public static boolean isNoIntercept2Login(Context context) {
        if (!DataCenter.getInstance().isLogin()) {
            LoginActivity.start(context);
            return false;
        } else {
            return true;
        }
    }
}
