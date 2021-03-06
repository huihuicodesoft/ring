package cn.com.wh.banner.transform;

import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.view.View;

public class ZoomOutSlideTransformer implements ViewPager.PageTransformer {

    private static final float MIN_SCALE = 0.85f;
    private static final float MIN_ALPHA = 0.9f;

    @Override
    public void transformPage(View page, float position) {
        if (position >= -1 || position <= 1) {
            // Modify the default slide transition to shrink the page as well
            final float height = page.getHeight();
            final float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
            final float vertMargin = height * (1 - scaleFactor) / 2;
            final float horzMargin = page.getWidth() * (1 - scaleFactor) / 2;

            // Center vertically
            ViewCompat.setPivotY(page, 0.5f * height);


            if (position < 0) {
                ViewCompat.setTranslationX(page, horzMargin - vertMargin / 2);
            } else {
                ViewCompat.setTranslationX(page, -horzMargin + vertMargin / 2);
            }

            // Scale the page down (between MIN_SCALE and 1)
            ViewCompat.setScaleX(page, scaleFactor);
            ViewCompat.setScaleY(page, scaleFactor);

            // Fade the page relative to its size.
            ViewCompat.setAlpha(page, MIN_ALPHA + (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA));
        }
    }
}
