package cn.com.wh.ring.ui.fragment.main;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.wh.ring.R;
import cn.com.wh.ring.ui.activity.base.DarkStatusBarActivity;
import cn.com.wh.ring.ui.fragment.base.ButterKnifeFragment;
import cn.com.wh.ring.ui.fragment.topic.TopicAttentionFragment;
import cn.com.wh.ring.ui.fragment.topic.TopicFragment;
import cn.com.wh.ring.utils.SystemBarUtils;
import cn.com.wh.tablelayout.SlidingTabLayout;

/**
 * Created by Hui on 2017/7/13.
 */

public class MainTopicFragment extends ButterKnifeFragment {
    private static final String CLASS_SIMPLE_NAME = MainTopicFragment.class.getSimpleName();
    private static final String SAVE_STATE_KEY_PAGE_ADAPTER = CLASS_SIMPLE_NAME + "pagerAdapter";

    @BindView(R.id.statusBar)
    View mStatusBar;
    @BindView(R.id.slidingTabLayout)
    SlidingTabLayout mTabLayout;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    MainTopicFragment.ViewPageAdapter mViewPageAdapter;

    @BindString(R.string.topic)
    String topicStr;
    @BindString(R.string.attention)
    String attentionStr;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main_topic, container, false);
        unbinder = ButterKnife.bind(this, root);
        initStatusBar();
        return root;
    }

    public void initStatusBar() {
        SystemBarUtils.initStatusBarHeight(getResources(), mStatusBar);
        if (getActivity() != null && getActivity() instanceof DarkStatusBarActivity) {
            mStatusBar.setBackgroundColor(((DarkStatusBarActivity) getActivity()).isStatusBarDark ?
                    getResources().getColor(R.color.status_title_back) : getResources().getColor(R.color.status_gray));
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView(savedInstanceState);
    }

    private void initView(@Nullable Bundle savedInstanceState) {
        mViewPageAdapter = new MainTopicFragment.ViewPageAdapter(getChildFragmentManager());

        initRestoreState(savedInstanceState);

        mViewPager.setAdapter(mViewPageAdapter);
        mTabLayout.setViewPager(mViewPager);

    }

    private void initRestoreState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            Parcelable pagerAdapterParcel = savedInstanceState.getParcelable(SAVE_STATE_KEY_PAGE_ADAPTER);
            if (pagerAdapterParcel != null)
                mViewPageAdapter.restoreState(pagerAdapterParcel, ClassLoader.getSystemClassLoader());
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mViewPageAdapter != null) {
            outState.putParcelable(SAVE_STATE_KEY_PAGE_ADAPTER, mViewPageAdapter.saveState());
        }
    }

    private class ViewPageAdapter extends FragmentStatePagerAdapter {

        public ViewPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            String className = Fragment.class.getName();
            switch (position) {
                case 0:
                    className = TopicFragment.class.getName();
                    break;
                case 1:
                    className = TopicAttentionFragment.class.getName();
                    break;
            }
            return Fragment.instantiate(getContext(), className);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String title = "";
            switch (position) {
                case 0:
                    title = topicStr;
                    break;
                case 1:
                    title = attentionStr;
                    break;
            }
            return title;
        }
    }
}
