package cn.com.wh.banner.transform;

import android.support.v4.view.ViewCompat;
import android.view.View;
import android.support.v4.view.ViewPager;

public class DepthTransformer implements ViewPager.PageTransformer {
    private static final float MIN_SCALE_DEPTH = 0.75f;

    @Override
    public void transformPage(View page, float position) {
        float alpha, scale, translationX;
        if (position > 0 && position < 1) {
            // moving to the right
            alpha = (1 - position);
            scale = MIN_SCALE_DEPTH + (1 - MIN_SCALE_DEPTH) * (1 - Math.abs(position));
            translationX = (page.getWidth() * -position);
        } else {
            // use default for all other cases
            alpha = 1;
            scale = 1;
            translationX = 0;
        }

        ViewCompat.setAlpha(page, alpha);
        ViewCompat.setTranslationX(page, translationX);
        ViewCompat.setScaleX(page, scale);
        ViewCompat.setScaleY(page, scale);
    }
}
