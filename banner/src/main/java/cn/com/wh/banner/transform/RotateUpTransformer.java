package cn.com.wh.banner.transform;

import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.view.View;

public class RotateUpTransformer implements ViewPager.PageTransformer {

	private static final float ROT_MOD = -15f;

	@Override
	public void transformPage(View page, float position) {
		final float width = page.getWidth();
		final float rotation = ROT_MOD * position;

		ViewCompat.setPivotX(page,width * 0.5f);
		ViewCompat.setPivotY(page,0f);
		ViewCompat.setTranslationX(page,0f);
		ViewCompat.setRotation(page,rotation);
	}
}
