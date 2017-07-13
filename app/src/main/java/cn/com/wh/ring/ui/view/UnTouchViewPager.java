package cn.com.wh.ring.ui.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Hui on 2017/7/13.
 * 滑动不切换
 */
public class UnTouchViewPager extends ViewPager {
    public UnTouchViewPager(Context context) {
        super(context);
    }

    public UnTouchViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        return false;
    }
}
