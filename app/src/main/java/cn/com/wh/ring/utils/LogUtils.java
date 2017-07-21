package cn.com.wh.ring.utils;

import android.util.Log;

import cn.com.wh.ring.BuildConfig;

/**
 * Created by Hui on 2017/7/21.
 */

public final class LogUtils {
    private static final boolean IS_DEBUG = BuildConfig.DEBUG;

    public static void logV(String tag, String msg) {
        if (!IS_DEBUG) {
            return;
        }
        Log.v(tag, msg);
    }

    public static void logD(String tag, String msg) {
        if (!IS_DEBUG) {
            return;
        }
        Log.d(tag, msg);
    }


    public static void logI(String tag, String msg) {
        if (!IS_DEBUG) {
            return;
        }
        Log.i(tag, msg);
    }

    public static void logW(String tag, String msg) {
        if (!IS_DEBUG) {
            return;
        }
        Log.w(tag, msg);
    }

}
