package cn.com.wh.ring.utils;

/**
 * Created by Hui on 2017/8/21.
 */

public class NumberUtils {
    private static final float W = 10000F;

    public static String getString(int number) {
        if (number < W) {
            return String.valueOf(number);
        } else {
            if (number < 100 * W) {
                return String.format("%.1f", number / W) + "万";
            } else {
                if (number < 1000 * W) {
                    return String.format("%.1f",number / (100 * W)) + "百万";
                } else {
                    if (number < W * W) {
                        return String.format("%.1f", number / (1000 * W)) + "千万";
                    } else {
                        return String.format("%.1f", number / (W * W)) + "亿";
                    }
                }
            }
        }
    }
}
