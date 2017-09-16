package cn.com.wh.ring.helper;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.LayoutRes;
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
    public final static int SUCCESS = 0;
    public final static int EMPTY = 1;
    public final static int ERROR = 2;
    public final static int LOADING = 3;
    private int state;

    FrameLayout mRootFl;
    View mContentView;
    FrameLayout mLoadingFl;
    RelativeLayout mErrorFl;
    FrameLayout mEmptyFl;
    OnLoadListener mOnLoadListener;

    @IntDef({SUCCESS, EMPTY, ERROR, LOADING})
    public @interface State {

    }

    public FrameLayout generateRoot(Context context, @LayoutRes int contentResId) {
        return generateRoot(context, contentResId, R.layout.panel_loading, R.layout.panel_load_fail, R.layout.panel_load_empty);
    }

    /**
     * 給content加上
     *
     * @param context
     * @param contentResId
     * @param loadingResId
     * @param loadFailResId
     * @param loadEmptyResId
     * @return
     */
    public FrameLayout generateRoot(Context context, @LayoutRes int contentResId,
                                    @LayoutRes int loadingResId,
                                    @LayoutRes int loadFailResId,
                                    @LayoutRes int loadEmptyResId) {
        mRootFl = new FrameLayout(context);
        mRootFl.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        mContentView = View.inflate(context, contentResId, null);
        mLoadingFl = (FrameLayout) View.inflate(context, loadingResId, null);
        mErrorFl = (RelativeLayout) View.inflate(context, loadFailResId, null);
        mEmptyFl = (FrameLayout) View.inflate(context, loadEmptyResId, null);

        mRootFl.addView(mContentView);
        mRootFl.addView(mLoadingFl);
        mRootFl.addView(mErrorFl);
        mRootFl.addView(mEmptyFl);

        mContentView.setVisibility(View.GONE);
        mLoadingFl.setVisibility(View.GONE);
        mErrorFl.setVisibility(View.GONE);
        mEmptyFl.setVisibility(View.GONE);

        return mRootFl;
    }

    /**
     * 设置加载监听
     *
     * @param onLoadListener
     */
    public void setOnLoadListener(OnLoadListener onLoadListener) {
        mOnLoadListener = onLoadListener;
    }

    /**
     * 设置展示页面
     *
     * @param state
     */
    public void setState(@State int state) {
        this.state = state;
        switch (state) {
            case SUCCESS:
                showSuccess();
                break;
            case EMPTY:
                showEmpty();
                break;
            case ERROR:
                showError();
                break;
            case LOADING:
                showLoading();
                break;
            default:
                break;
        }
    }

    private void showLoading() {
        showLayer(2);
        if (mOnLoadListener != null) {
            mOnLoadListener.OnLoad();
        }
    }

    private void showSuccess() {
        showLayer(1);
    }

    private void showError() {
        showLayer(3);
        if (mErrorFl != null) {
            mErrorFl.findViewById(R.id.error_tip_ll).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showLoading();
                }
            });
        }
    }

    private void showEmpty() {
        showLayer(4);
    }

    private void showLayer(int layer) {
        if (mContentView != null) {
            mContentView.setVisibility(layer == 1 ? View.VISIBLE : View.GONE);
        }
        if (mLoadingFl != null) {
            mLoadingFl.setVisibility(layer == 2 ? View.VISIBLE : View.GONE);
        }
        if (mErrorFl != null) {
            mErrorFl.setVisibility(layer == 3 ? View.VISIBLE : View.GONE);
        }
        if (mEmptyFl != null) {
            mEmptyFl.setVisibility(layer == 4 ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * 针对与列表页加载成功
     *
     * @param requestPage     请求页数
     * @param isHelperRequest 是不是通过helper触发的加载
     * @param data            页面数据
     * @return 如果为true，则页面不需要处理数据，为false。页面还需处理展示数据
     */
    public boolean interceptSuccess(int requestPage, boolean isHelperRequest, List data) {
        if (requestPage == 1) {
            if (data == null || data.isEmpty()) {
                showEmpty();
                return true;
            }
            if (isHelperRequest) {
                showSuccess();
            }
        } else {
            if (data == null || data.isEmpty()) {
                ToastUtils.showShortToast(R.string.loading_empty);
                return true;
            }
        }
        return false;
    }

    /**
     * 针对与列表页加载失败
     *
     * @param requestPage
     * @param isHelperRequest
     * @param idFirstError 不管有无数据，加载首页失败都反馈错误页面
     * @return
     */
    public boolean interceptError(int requestPage, boolean isHelperRequest, boolean idFirstError) {
        if (requestPage == 1) {
            if (isHelperRequest || idFirstError) {
                showError();
                return true;
            }
        }
        return false;
    }


    public interface OnLoadListener {
        void OnLoad();
    }

}
