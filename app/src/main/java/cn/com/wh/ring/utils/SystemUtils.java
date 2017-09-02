package cn.com.wh.ring.utils;

import android.util.DisplayMetrics;
import android.view.WindowManager;

import cn.com.wh.ring.MainApplication;

/**
 * Created by Hui on 2017/8/22.
 */

public class SystemUtils {
    public static float getDpScreenWidth() {
        WindowManager wm = (WindowManager) MainApplication.getInstance().getSystemService(MainApplication.getInstance().WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels / dm.density;
    }
}
