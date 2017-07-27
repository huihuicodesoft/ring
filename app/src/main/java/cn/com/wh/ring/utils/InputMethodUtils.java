package cn.com.wh.ring.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hui on 2016/5/9.
 */
public class InputMethodUtils {
    private InputMethodManager mInputMethodManager;
    private View mActivityView;
    private ViewTreeObserver.OnGlobalLayoutListener mOnGlobalLayoutListener;
    private List<OnKeyBoardChangeListener> mOnKeyBoardChangeListeners;
    private Rect mPreRect = new Rect();
    private Rect mRect = new Rect();
    private int mKeyBoardHeight;
    private KEY_BOARD_STATE mKeyBoardState = KEY_BOARD_STATE.SYSTEM_HIDED;

    public enum KEY_BOARD_STATE {
        SYSTEM_SHOWED, SYSTEM_HIDED,
        INITIATIVE_SYSTEM_SHOWING, INITIATIVE_SYSTEM_HIDING,
        INITIATIVE_CUSTOM_HIDING
    }

    public InputMethodUtils() {
        mOnKeyBoardChangeListeners = new ArrayList<>();
    }

    public void onCreated(final Activity activity) {
        Window window = activity.getWindow();
        mActivityView = window.getDecorView();

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        mInputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        mOnGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mActivityView.getWindowVisibleDisplayFrame(mRect);
                if (!mRect.equals(mPreRect) && mPreRect.height() > 0) {
                    int height = mActivityView.getHeight() -
                            SystemBarUtils.getStatusBarHeight(activity.getResources(), false);
                    if (mPreRect.height() - mRect.height() >= height / 3) {
                        //显示
                        boolean isInitiative = mKeyBoardState.equals(KEY_BOARD_STATE.INITIATIVE_SYSTEM_SHOWING);
                        mKeyBoardHeight = mPreRect.height() - mRect.height();
                        mKeyBoardState = KEY_BOARD_STATE.SYSTEM_SHOWED;

                        notifyKeyBoardChangeListeners(isInitiative);
                    }

                    if (mRect.height() - mPreRect.height() >= height / 3) {
                        //隐藏
                        boolean isInitiative = mKeyBoardState.equals(KEY_BOARD_STATE.INITIATIVE_SYSTEM_HIDING);
                        mKeyBoardHeight = mRect.height() - mPreRect.height();
                        mKeyBoardState = KEY_BOARD_STATE.SYSTEM_HIDED;

                        notifyKeyBoardChangeListeners(isInitiative);
                    }
                }
                mPreRect.set(mRect);
            }
        };

        mActivityView.getViewTreeObserver().addOnGlobalLayoutListener(mOnGlobalLayoutListener);
    }

    public void onDestroy() {
        mOnKeyBoardChangeListeners.clear();
        removeGlobalLayoutListener();
    }

    private void removeGlobalLayoutListener() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mActivityView.getViewTreeObserver().removeOnGlobalLayoutListener(mOnGlobalLayoutListener);
        } else {
            mActivityView.getViewTreeObserver().removeGlobalOnLayoutListener(mOnGlobalLayoutListener);
        }
    }

    public void addKeyBoardChangeListener(OnKeyBoardChangeListener onKeyBoardChangeListener) {
        mOnKeyBoardChangeListeners.add(onKeyBoardChangeListener);
    }

    public void removeKeyBoardChangeListener(OnKeyBoardChangeListener onKeyBoardChangeListener) {
        mOnKeyBoardChangeListeners.remove(onKeyBoardChangeListener);
    }

    public void toggleKeyBoardState(View view) {
        if (isInputMethodShowed()) {
            hideKeyBoardState(view, true);
        } else {
            showKeyBoardState(view);
        }
    }

    public void showKeyBoardState(View view) {
        if (!isInputMethodShowed()) {
            showKeyBoard(view);
            mKeyBoardState = KEY_BOARD_STATE.INITIATIVE_SYSTEM_SHOWING;
            notifyKeyBoardChangeListeners(true);
        }
    }

    public void hideKeyBoardState(View view, boolean isInitiative) {
        if (isInputMethodShowed()) {
            hideKeyBoard(view);
            if (isInitiative)
                mKeyBoardState = KEY_BOARD_STATE.INITIATIVE_SYSTEM_HIDING;
        } else {
            mKeyBoardState = KEY_BOARD_STATE.INITIATIVE_CUSTOM_HIDING;
        }
        notifyKeyBoardChangeListeners(isInitiative);
    }

    public void showKeyBoard(View view) {
        if (mInputMethodManager != null && view != null) {
            view.requestFocus();
            mInputMethodManager.showSoftInput(view, InputMethodManager.SHOW_FORCED);
        }
    }

    public void hideKeyBoard(View view) {
        if (mInputMethodManager != null && view != null) {
            view.requestFocus();
            mInputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public boolean isInputMethodShowed() {
        return mKeyBoardState.equals(KEY_BOARD_STATE.SYSTEM_SHOWED);
    }

    private void notifyKeyBoardChangeListeners(boolean isInitiative) {
        for (OnKeyBoardChangeListener onKeyBoardChangeListener : mOnKeyBoardChangeListeners) {
            onKeyBoardChangeListener.onChange(mKeyBoardState, mKeyBoardHeight, isInitiative);
        }
    }

    public int getKeyBoardHeight() {
        return mKeyBoardHeight;
    }

    public interface OnKeyBoardChangeListener {
        void onChange(KEY_BOARD_STATE keyBoardState, int keyBoardHeight, boolean isInitiative);
    }
}
