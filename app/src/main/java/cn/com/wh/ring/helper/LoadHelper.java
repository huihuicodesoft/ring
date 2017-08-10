package cn.com.wh.ring.helper;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import cn.com.wh.ring.R;

/**
 * Created by Hui on 2017/8/7.
 */
public class LoadHelper {
    FrameLayout mRootFl;
    View mContentView;
    FrameLayout mLoadingFl;
    FrameLayout mFailFl;
    FrameLayout mEmptyFl;
    OnLoadListener mOnLoadListener;

    public FrameLayout generateRoot(Context context, int resId) {
        mRootFl = new FrameLayout(context);
        mRootFl.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        mContentView = View.inflate(context, resId, null);
        mLoadingFl = (FrameLayout) View.inflate(context, R.layout.panel_loading, null);
        mFailFl = (FrameLayout) View.inflate(context, R.layout.panel_load_fail, null);
        mEmptyFl = (FrameLayout) View.inflate(context, R.layout.panel_load_empty, null);

        mRootFl.addView(mContentView);
        mRootFl.addView(mLoadingFl);
        mRootFl.addView(mFailFl);
        mRootFl.addView(mEmptyFl);

        mContentView.setVisibility(View.GONE);
        mLoadingFl.setVisibility(View.GONE);
        mFailFl.setVisibility(View.GONE);
        mEmptyFl.setVisibility(View.GONE);

        return mRootFl;
    }

    public void setOnLoadListener(OnLoadListener onLoadListener) {
        mOnLoadListener = onLoadListener;
    }

    public void showLoading() {
        showLayer(2);
        if (mOnLoadListener != null) {
            mOnLoadListener.OnLoad();
        }
    }

    public void showSuccess() {
        showLayer(1);
    }

    public void showFail() {
        showLayer(3);
        if (mFailFl != null) {
            mFailFl.findViewById(R.id.error_tip_ll).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showLoading();
                }
            });
        }
    }

    public void showEmpty() {
        showLayer(4);
    }

    private void showLayer(int layer) {
        if (mContentView != null) {
            mContentView.setVisibility(layer == 1 ? View.VISIBLE : View.GONE);
        }
        if (mLoadingFl != null) {
            mLoadingFl.setVisibility(layer == 2 ? View.VISIBLE : View.GONE);
        }
        if (mFailFl != null) {
            mFailFl.setVisibility(layer == 3 ? View.VISIBLE : View.GONE);
        }
        if (mEmptyFl != null) {
            mEmptyFl.setVisibility(layer == 4 ? View.VISIBLE : View.GONE);
        }
    }

    public interface OnLoadListener {
        void OnLoad();
    }
}
