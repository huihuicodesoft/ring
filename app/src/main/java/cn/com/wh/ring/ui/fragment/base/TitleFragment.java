package cn.com.wh.ring.ui.fragment.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import butterknife.Unbinder;
import cn.com.wh.ring.R;
import cn.com.wh.ring.ui.activity.base.DarkStatusBarActivity;
import cn.com.wh.ring.utils.SystemBarUtils;

/**
 * Created by Hui on 2017/7/13.
 * 不要覆盖onCreateView方法
 * 不能使用@bindView
 */
public abstract class TitleFragment extends Fragment {
    public LinearLayout mRootLl;
    public View mStatusBar;
    public LinearLayout mTitleLl;

    public Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_title, container, false);
        initView(root);
        initStatusBar();
        fillView();
        return root;
    }

    private void fillView() {
        View titleView = getTitleView();
        if (titleView != null) {
            mTitleLl.setVisibility(View.VISIBLE);
            mTitleLl.addView(titleView, 0, new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        } else {
            mTitleLl.setVisibility(View.GONE);
        }
        View contentView = getContentView();
        if (contentView != null) {
            mRootLl.addView(contentView, new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
    }

    private void initView(@NonNull View root) {
        mRootLl = (LinearLayout) root.findViewById(R.id.root_ll);
        mStatusBar = root.findViewById(R.id.statusBar);
        mTitleLl = (LinearLayout) root.findViewById(R.id.title_ll);
    }

    public void initStatusBar() {
        SystemBarUtils.initStatusBarHeight(getResources(), mStatusBar);
        if (getActivity() != null && getActivity() instanceof DarkStatusBarActivity) {
            mStatusBar.setBackgroundColor(((DarkStatusBarActivity) getActivity()).isStatusBarDark ?
                    getResources().getColor(R.color.status_title_back) :  getResources().getColor(R.color.status_gray));
        }
    }

    public abstract View getTitleView();

    public abstract View getContentView();


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }
}
