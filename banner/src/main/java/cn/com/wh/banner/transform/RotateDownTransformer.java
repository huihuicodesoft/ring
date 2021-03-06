package cn.com.wh.banner.transform;

import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.view.View;

public class RotateDownTransformer implements ViewPager.PageTransformer {

	private static final float ROT_MOD = -15f;

	@Override
	public void transformPage(View page, float position) {
		final float width = page.getWidth();
		final float height = page.getHeight();
		final float rotation = ROT_MOD * position * -1.25f;

		ViewCompat.setPivotX(page,width * 0.5f);
		ViewCompat.setPivotY(page,height);
		ViewCompat.setRotation(page,rotation);
	}
}
