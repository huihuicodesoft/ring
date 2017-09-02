package cn.com.wh.ring.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;

import com.flyco.tablayout.SlidingTabLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindDimen;
import butterknife.BindString;
import cn.com.wh.ring.R;
import cn.com.wh.ring.event.PostPublishEvent;
import cn.com.wh.ring.ui.fragment.base.ScrollAbleFragment;
import cn.com.wh.ring.ui.fragment.me.CollectionFragment;
import cn.com.wh.ring.ui.fragment.me.CommentFragment;
import cn.com.wh.ring.ui.fragment.me.PostPublishFragment;
import cn.com.wh.ring.ui.view.scrollable.ScrollableLayout;
import cn.com.wh.ring.utils.SystemUtils;

/**
 * Created by Hui on 2017/7/13.
 */

public class MeFragment extends TitleFragment {
    View rootContentView = null;
    ScrollableLayout mScrollableLayout;
    LinearLayout mHeadLl;
    SlidingTabLayout mTabLayout;
    ViewPager mViewPager;

    @BindString(R.string.publish_post)
    String publishPostStr;
    @BindString(R.string.collection)
    String collectionStr;
    @BindString(R.string.comment)
    String commentStr;
    @BindDimen(R.dimen.height_title)
    int titleHeight;
    @BindDimen(R.dimen.me_title_display_edge)
    int titleDisplayEdge;

    List<ScrollAbleFragmentName> fragmentNames = new ArrayList<>();

    public class ScrollAbleFragmentName {
        String name;
        ScrollAbleFragment scrollAbleFragment;

        public ScrollAbleFragmentName(String name, ScrollAbleFragment scrollAbleFragment) {
            this.name = name;
            this.scrollAbleFragment = scrollAbleFragment;
        }

        public String getName() {
            return name;
        }

        public ScrollAbleFragment getScrollAbleFragment() {
            return scrollAbleFragment;
        }
    }

    @Override
    public View getTitleView() {
        return null;
    }

    @Override
    public View getContentView() {
        rootContentView = View.inflate(getContext(), R.layout.fragment_me, null);
        return rootContentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        EventBus.getDefault().register(this);
    }

    private void initView() {
        if (rootContentView == null)
            return;
        mScrollableLayout = (ScrollableLayout) rootContentView.findViewById(R.id.me_scrollableLayout);
        mHeadLl = (LinearLayout) rootContentView.findViewById(R.id.me_head_ll);
        mTabLayout = (SlidingTabLayout) rootContentView.findViewById(R.id.me_tabLayout);
        mViewPager = (ViewPager) rootContentView.findViewById(R.id.me_viewPager);

        fragmentNames.clear();
        fragmentNames.add(new ScrollAbleFragmentName(publishPostStr, new PostPublishFragment()));
        fragmentNames.add(new ScrollAbleFragmentName(collectionStr, new CollectionFragment()));
        fragmentNames.add(new ScrollAbleFragmentName(commentStr, new CommentFragment()));
        mViewPager.setAdapter(new ViewPageAdapter(getChildFragmentManager()));
        mViewPager.setOffscreenPageLimit(2);
        mTabLayout.setTabWidth(SystemUtils.getDpScreenWidth() / fragmentNames.size());
        mTabLayout.setViewPager(mViewPager);
        mScrollableLayout.getHelper().setCurrentScrollableContainer(fragmentNames.get(0).getScrollAbleFragment());
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ScrollAbleFragment scrollAbleFragment = fragmentNames.get(position).getScrollAbleFragment();
                mScrollableLayout.getHelper().setCurrentScrollableContainer(scrollAbleFragment);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }



    private class ViewPageAdapter extends FragmentPagerAdapter {

        public ViewPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentNames.get(position).getScrollAbleFragment();
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PostPublishEvent event) {
        if (event.type == PostPublishEvent.TYPE_SKIP_POST) {
            if (mViewPager != null) {
                mViewPager.setCurrentItem(0, false);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }
}
