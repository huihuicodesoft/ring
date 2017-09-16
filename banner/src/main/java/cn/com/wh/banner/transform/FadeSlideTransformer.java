package cn.com.wh.banner.transform;

import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.view.View;

public class FadeSlideTransformer implements ViewPager.PageTransformer {
    @Override
    public void transformPage(View page, float position) {

        ViewCompat.setTranslationX(page, 0);

        if (position <= -1.0F || position >= 1.0F) {
            ViewCompat.setAlpha(page, 0.0F);
        } else if (position == 0.0F) {
            ViewCompat.setAlpha(page, 1.0F);
        } else {
            // position is between -1.0F & 0.0F OR 0.0F & 1.0F
            ViewCompat.setAlpha(page, 1.0F - Math.abs(position));
        }
    }
}
