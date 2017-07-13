package cn.com.wh.ring.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;
import cn.com.wh.ring.R;
import cn.com.wh.ring.ui.fragment.home.AnecdoteFragment;
import cn.com.wh.ring.ui.fragment.home.AttentionFragment;
import cn.com.wh.ring.ui.fragment.home.RecommendFragment;
import cn.com.wh.ring.ui.fragment.home.RedHallFragment;

/**
 * Created by Hui on 2017/7/13.
 */

public class HomeFragment extends TitleFragment {
    TabLayout mTabLayout;
    ViewPager mViewPager;

    @BindString(R.string.attention)
    String attentionStr;
    @BindString(R.string.recommend)
    String recommendStr;
    @BindString(R.string.anecdote)
    String anecdoteStr;
    @BindString(R.string.red_hall)
    String redHallStr;

    List<FragmentName> fragmentNames = new ArrayList<>();

    class FragmentName {
        String name;
        Fragment fragment;

        public FragmentName(String name, Fragment fragment) {
            this.name = name;
            this.fragment = fragment;
        }

        public String getName() {
            return name;
        }

        public Fragment getFragment() {
            return fragment;
        }
    }

    @Override
    public View getTitleView() {
        mTabLayout = (TabLayout) View.inflate(getContext(), R.layout.title_home, null);
        return mTabLayout;
    }

    @Override
    public View getContentView() {
        mViewPager  = (ViewPager) View.inflate(getContext(), R.layout.fragment_home, null);
        return mViewPager;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        fragmentNames.clear();
        fragmentNames.add(new FragmentName(attentionStr, new AttentionFragment()));
        fragmentNames.add(new FragmentName(recommendStr, new RecommendFragment()));
        fragmentNames.add(new FragmentName(anecdoteStr, new AnecdoteFragment()));
        fragmentNames.add(new FragmentName(redHallStr, new RedHallFragment()));
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        mViewPager.setAdapter(new ViewPageAdapter(getChildFragmentManager()));
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private class ViewPageAdapter extends FragmentPagerAdapter {

        public ViewPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentNames.get(position).getFragment();
        }

        @Override
        public int getCount() {
            return fragmentNames.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentNames.get(position).getName();
        }
    }
}
