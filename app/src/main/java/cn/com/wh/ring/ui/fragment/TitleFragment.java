package cn.com.wh.ring.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.com.wh.ring.R;
import cn.com.wh.ring.utils.SystemBarUtils;

/**
 * Created by Hui on 2017/7/13.
 * 不要覆盖onCreateView方法
 * 不能使用@bindView
 */
public abstract class TitleFragment extends Fragment {
    @BindView(R.id.root_ll)
    LinearLayout mRootLl;
    @BindView(R.id.statusBar)
    View mStatusBar;
    @BindView(R.id.title_ll)
    LinearLayout mTitleLl;

    Unbinder unbinder;

    @Nullable
    @Override
    @Deprecated
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_title, container, false);
        unbinder = ButterKnife.bind(this, root);

        SystemBarUtils.initStatusBarHeight(getResources(), mStatusBar);

        View titleView = getTitleView();
        if (titleView != null) {
            mTitleLl.addView(titleView, new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
        View contentView = getContentView();
        if (contentView != null) {
            mRootLl.addView(contentView, new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }

        return root;
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
