package cn.com.wh.ring.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

import cn.com.wh.ring.R;

/**
 * Created by Hui on 2016/4/6.
 */
public class StateFilterImageView extends ImageView {
    private int mPressDownColor;

    public StateFilterImageView(Context context) {
        this(context, null);
    }

    public StateFilterImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StateFilterImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFocusable(true);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.StateFilterImageView, defStyleAttr, 0);
        mPressDownColor = a.getColor(R.styleable.StateFilterImageView_press_down_color, Color.GRAY);
        a.recycle();
    }

    public void setPressDownColor(int pressDownColor) {
        this.mPressDownColor = pressDownColor;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                setColorFilter(mPressDownColor, PorterDuff.Mode.MULTIPLY);
                break;
            case MotionEvent.ACTION_UP:
                performClick();
            case MotionEvent.ACTION_CANCEL:
                clearColorFilter();
                break;
            default:
                break;
        }
        return true;
    }
}
