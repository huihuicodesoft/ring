package cn.com.wh.ring.ui.fragment.main;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.flyco.tablayout.SlidingTabLayout;

import butterknife.BindString;
import cn.com.wh.ring.R;
import cn.com.wh.ring.ui.fragment.base.TitleFragment;
import cn.com.wh.ring.ui.fragment.home.AttentionFragment;
import cn.com.wh.ring.ui.fragment.home.RecommendFragment;

/**
 * Created by Hui on 2017/7/13.
 */

public class MainHomeFragment extends TitleFragment {
    SlidingTabLayout mTabLayout;
    ViewPager mViewPager;

    @BindString(R.string.attention)
    String attentionStr;
    @BindString(R.string.recommend)
    String recommendStr;

    @Override
    public View getTitleView() {
        View view = View.inflate(getContext(), R.layout.title_home, null);
        mTabLayout = (SlidingTabLayout) view.findViewById(R.id.commonTabLayout);
        return view;
    }

    @Override
    public View getContentView() {
        mViewPager = (ViewPager) View.inflate(getContext(), R.layout.fragment_main_home, null);
        return mViewPager;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        mViewPager.setAdapter(new ViewPageAdapter(getChildFragmentManager()));
        mTabLayout.setViewPager(mViewPager);

    }

    private class ViewPageAdapter extends FragmentStatePagerAdapter {

        public ViewPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            String className;
            if (position == 0) {
                className = RecommendFragment.class.getName();
            } else {
                className = AttentionFragment.class.getName();
            }
            return Fragment.instantiate(getContext(), className);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String title;
            if (position == 0) {
                title = recommendStr;
            } else {
                title = attentionStr;
            }
            return title;
        }
    }
}