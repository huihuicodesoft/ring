package cn.com.wh.ring.utils;

/**
 * Created by Hui on 2017/7/13.
 */

import android.app.Activity;
import android.content.res.Resources;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import cn.com.wh.ring.R;


/**
 * Created by Hui on 2016/3/10.
 * <p/>
 * set statusBar
 */
public final class SystemBarUtils {
    private static final String STATUS_BAR_HEIGHT_RES_NAME = "status_bar_height";

    public static boolean isMoreKITKAT() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    public static boolean isMoreLOLLIPOP() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    /**
     * 设置activity全屏,状态栏透明（4.4以上）
     *
     * @param activity
     */
    public static void initActivity(Activity activity) {
        if (isMoreLOLLIPOP()) {
            //设置全屏
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            activity.getWindow().setStatusBarColor(activity.getResources().getColor(R.color.transparent));
        } else {
            if (isMoreKITKAT()) {
                //4.4以上
                Window win = activity.getWindow();
                WindowManager.LayoutParams winParams = win.getAttributes();
                final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS; //魅族手机会变灰
                winParams.flags |= bits;
                win.setAttributes(winParams);
            }
        }
    }

    /**
     * 将view的高度设置为状态栏高度
     * @param res
     * @param view
     */
    public static void initStatusBarHeight(Resources res, View view) {
        int statusBarHeight = getStatusBarHeight(res, true);
        if (statusBarHeight != 0) {
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            layoutParams.height = statusBarHeight;
            view.setLayoutParams(layoutParams);
        }
    }

    /**
     * 获得状态栏高度
     * @param res
     * @param isVersion
     * @return
     */
    public static int getStatusBarHeight(Resources res, boolean isVersion) {
        int statusBarHeight = 0;
        if (isVersion) {
            if (isMoreKITKAT()) {
                //4.4以上
                statusBarHeight = getInternalDimensionSize(res, STATUS_BAR_HEIGHT_RES_NAME);
            }
        } else {
            statusBarHeight = getInternalDimensionSize(res, STATUS_BAR_HEIGHT_RES_NAME);
        }

        return statusBarHeight;
    }

    /**
     * 获得android 配置参数
     * @param res
     * @param key
     * @return
     */
    private static int getInternalDimensionSize(Resources res, String key) {
        int result = 0;
        int resourceId = res.getIdentifier(key, "dimen", "android");
        if (resourceId > 0) {
            result = res.getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
