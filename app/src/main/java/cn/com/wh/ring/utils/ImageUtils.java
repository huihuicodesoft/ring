package cn.com.wh.ring.utils;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

/**
 * Created by Hui on 2017/11/14.
 */

public class ImageUtils {
    public static boolean isEmptyDrawable(ImageView imageView) {
        boolean result = true;
        if (imageView != null) {
            Drawable drawable = imageView.getDrawable();
            if (drawable != null && !(drawable instanceof ColorDrawable))
                result = false;
        }
        return result;
    }
}
