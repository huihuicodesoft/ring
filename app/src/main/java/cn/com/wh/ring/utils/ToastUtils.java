package cn.com.wh.ring.utils;

import android.widget.Toast;

import cn.com.wh.ring.MainApplication;

/**
 * Created by Hui on 2016/3/10.
 */
public final class ToastUtils {
    private static Toast mToastShort = null;
    private static Toast mToastLong = null;

    public static void showShortToast(CharSequence charSequence) {
        if (mToastShort == null) {
            mToastShort = Toast.makeText(MainApplication.getInstance(), charSequence, Toast.LENGTH_SHORT);
        } else {
            mToastShort.setText(charSequence);
        }
        mToastShort.show();
    }

    public static void showLongToast(CharSequence charSequence) {
        if (mToastLong == null) {
            mToastLong = Toast.makeText(MainApplication.getInstance(), charSequence, Toast.LENGTH_LONG);
        } else {
            mToastLong.setText(charSequence);
        }
        mToastLong.show();
    }

    public static void showShortToast(int resId) {
        if (mToastShort == null) {
            mToastShort = Toast.makeText(MainApplication.getInstance(), resId, Toast.LENGTH_SHORT);
        } else {
            mToastShort.setText(resId);
        }
        mToastShort.show();
    }


    public static void showLongToast(int resId) {
        if (mToastLong == null) {
            mToastLong = Toast.makeText(MainApplication.getInstance(), resId, Toast.LENGTH_LONG);
        } else {
            mToastLong.setText(resId);
        }
        mToastLong.show();
    }
}
