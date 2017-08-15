package cn.com.wh.ring.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindDimen;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.com.wh.ring.R;
import cn.com.wh.ring.ui.fragment.bean.FragmentName;
import cn.com.wh.ring.ui.fragment.me.PublishPostFragment;
import cn.com.wh.ring.utils.SystemBarUtils;

/**
 * Created by Hui on 2017/7/13.
 */

public class MeFragment extends Fragment {
    Unbinder unbinder;

    @BindView(R.id.me_tooBar)
    Toolbar mToolBar;
    @BindView(R.id.me_tabLayout)
    SlidingTabLayout mTabLayout;
    @BindView(R.id.me_viewPager)
    ViewPager mViewPager;

    @BindString(R.string.publish_post)
    String publishPostStr;
    @BindString(R.string.collection)
    String collectionStr;
    @BindString(R.string.comment)
    String commentStr;
    @BindDimen(R.dimen.height_title)
    int titleHeight;

    List<FragmentName> fragmentNames = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_me, container, false);
        unbinder = ButterKnife.bind(this, root);
        initToolBarHeight();
        return root;
    }

    private void initToolBarHeight() {
        int headerHeight = SystemBarUtils.getStatusBarHeight(getResources(), false) + titleHeight;
        ViewGroup.LayoutParams layoutParams = mToolBar.getLayoutParams();
        if (layoutParams == null) {
            layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, headerHeight);
        } else {
            layoutParams.height = headerHeight;
        }
        mToolBar.setLayoutParams(layoutParams);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        fragmentNames.clear();
        fragmentNames.add(new FragmentName(publishPostStr, new PublishPostFragment()));
        fragmentNames.add(new FragmentName(collectionStr, new PublishPostFragment()));
        fragmentNames.add(new FragmentName(commentStr, new PublishPostFragment()));
        mViewPager.setAdapter(new ViewPageAdapter(getChildFragmentManager()));
        mTabLayout.setViewPager(mViewPager);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }
}
