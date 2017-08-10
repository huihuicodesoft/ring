package cn.com.wh.ring.helper;

import cn.com.wh.ring.R;

/**
 * Created by Hui on 2017/8/1.
 */

public class PostTypeHelper {
    private static final int POST_TYPE_SUPPORT_ALL = 0; //全被格式
    private static final int POST_TYPE_SUPPORT_W = 1; //只支持文字
    private static final int POST_TYPE_SUPPORT_P = 2; //只支持图片
    private static final int POST_TYPE_SUPPORT_V = 3; //只支持视频
    private static final int POST_TYPE_SUPPORT_G = 4; //只支持gif
    private static final int POST_TYPE_SUPPORT_WP = 5; //只支持文字、图片
    private static final int POST_TYPE_SUPPORT_WV = 6; //只支持文字、视频
    private static final int POST_TYPE_SUPPORT_WG = 7;//只支持文字、gif
    private static final int POST_TYPE_SUPPORT_PV = 8;//只支持图片、视频
    private static final int POST_TYPE_SUPPORT_PG = 9;//只支持图片、gif
    private static final int POST_TYPE_SUPPORT_VG = 10;//只支持视频、gif
    private static final int POST_TYPE_SUPPORT_WPV = 11;//只支持文字、图片、视频
    private static final int POST_TYPE_SUPPORT_PVG = 12;//只支持图片、视频、gif

    public static int getTipResId(int support) {
        int resId = -1;
        switch (support) {
            case POST_TYPE_SUPPORT_W:
                resId = R.string.tip_post_type_w;
                break;
            case POST_TYPE_SUPPORT_P:
                resId = R.string.tip_post_type_p;
                break;
            case POST_TYPE_SUPPORT_V:
                resId = R.string.tip_post_type_v;
                break;
            case POST_TYPE_SUPPORT_G:
                resId = R.string.tip_post_type_g;
                break;
            case POST_TYPE_SUPPORT_WP:
                resId = R.string.tip_post_type_wp;
                break;
            case POST_TYPE_SUPPORT_WV:
                resId = R.string.tip_post_type_wv;
                break;
            case POST_TYPE_SUPPORT_WG:
                resId = R.string.tip_post_type_wg;
                break;
            case POST_TYPE_SUPPORT_PV:
                resId = R.string.tip_post_type_pv;
                break;
            case POST_TYPE_SUPPORT_PG:
                resId = R.string.tip_post_type_pg;
                break;
            case POST_TYPE_SUPPORT_VG:
                resId = R.string.tip_post_type_vg;
                break;
            case POST_TYPE_SUPPORT_WPV:
                resId = R.string.tip_post_type_wpv;
                break;
            case POST_TYPE_SUPPORT_PVG:
                resId = R.string.tip_post_type_pvg;
                break;
        }
        return resId;
    }
}
