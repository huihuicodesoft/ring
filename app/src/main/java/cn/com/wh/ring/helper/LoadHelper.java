package cn.com.wh.ring.helper;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import java.util.List;

import cn.com.wh.ring.R;
import cn.com.wh.ring.utils.ToastUtils;

/**
 * Created by Hui on 2017/8/7.
 */
public class LoadHelper {
    FrameLayout mRootFl;
    View mContentView;
    FrameLayout mLoadingFl;
    RelativeLayout mFailFl;
    FrameLayout mEmptyFl;
    OnLoadListener mOnLoadListener;

    public FrameLayout generateRoot(Context context, int resId) {
        mRootFl = new FrameLayout(context);
        mRootFl.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        mContentView = View.inflate(context, resId, null);
        mLoadingFl = (FrameLayout) View.inflate(context, R.layout.panel_loading, null);
        mFailFl = (RelativeLayout) View.inflate(context, R.layout.panel_load_fail, null);
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

    public boolean interceptSuccess(int page, boolean isPage, List data) {
        if (page == 1) {
            if (mOnLoadListener != null)
                mOnLoadListener.OnClearData();
            if (data == null || data.isEmpty()) {
                showEmpty();
                return true;
            }
            if (isPage) {
                showSuccess();
            } else {
                if (mOnLoadListener != null)
                    mOnLoadListener.OnFinishRefresh();
            }
        } else {
            if (mOnLoadListener != null)
                mOnLoadListener.OnFinishLoadMore();
            if (data == null || data.isEmpty()) {
                ToastUtils.showShortToast(R.string.loading_empty);
                return true;
            }
        }
        return false;
    }

    public boolean interceptFail(int page, boolean isPage, List data) {
        if (page == 1) {
            if (!isPage) {
                if (mOnLoadListener != null)
                    mOnLoadListener.OnFinishRefresh();
            }
            if (data == null || data.isEmpty()) {
                showFail();
                return true;
            }
        } else {
            if (mOnLoadListener != null)
                mOnLoadListener.OnFinishLoadMore();
        }
        return false;
    }


    public interface OnLoadListener {
        void OnLoad();

        void OnClearData();

        void OnFinishRefresh();

        void OnFinishLoadMore();
    }

}
