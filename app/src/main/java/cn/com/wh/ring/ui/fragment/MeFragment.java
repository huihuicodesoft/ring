package cn.com.wh.ring.ui.fragment;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;

import com.flyco.tablayout.SlidingTabLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindDimen;
import butterknife.BindString;
import cn.com.wh.ring.R;
import cn.com.wh.ring.event.PostPublishEvent;
import cn.com.wh.ring.ui.fragment.me.CollectionFragment;
import cn.com.wh.ring.ui.fragment.me.CommentFragment;
import cn.com.wh.ring.ui.fragment.me.PostPublishFragment;
import cn.com.wh.ring.ui.view.scrollable.ScrollableHelper;
import cn.com.wh.ring.ui.view.scrollable.ScrollableLayout;
import cn.com.wh.ring.utils.SystemUtils;

/**
 * Created by Hui on 2017/7/13.
 */

public class MeFragment extends TitleFragment {
    private static final int PAGE_NUMBER = 3;
    private static final String SAVE_STATE_KEY_PAGE_ADAPTER = "pagerAdapter";

    View selfRootView = null;
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

    private ViewPageAdapter mViewPagerAdapter;

    private class ViewPageAdapter extends FragmentStatePagerAdapter {
        public ViewPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            String className;
            if (position == 0) {
                className = PostPublishFragment.class.getName();
            } else if (position == 1) {
                className = CollectionFragment.class.getName();
            } else {
                className = CommentFragment.class.getName();
            }
            Fragment fragment = Fragment.instantiate(getContext(), className);
            return fragment;
        }

        @Override
        public int getCount() {
            return PAGE_NUMBER;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String title;
            if (position == 0) {
                title = publishPostStr;
            } else if (position == 1) {
                title = collectionStr;
            } else {
                title = commentStr;
            }
            return title;
        }
    }

    @Override
    public View getTitleView() {
        return null;
    }

    @Override
    public View getContentView() {
        selfRootView = View.inflate(getContext(), R.layout.fragment_me, null);
        return selfRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    public void initView(Bundle savedInstanceState) {
        if (selfRootView == null)
            return;
        mScrollableLayout = (ScrollableLayout) selfRootView.findViewById(R.id.me_scrollableLayout);
        mHeadLl = (LinearLayout) selfRootView.findViewById(R.id.me_head_ll);
        mTabLayout = (SlidingTabLayout) selfRootView.findViewById(R.id.me_tabLayout);
        mViewPager = (ViewPager) selfRootView.findViewById(R.id.me_viewPager);
        mViewPagerAdapter = new ViewPageAdapter(getChildFragmentManager());

        initRestoreState(savedInstanceState);

        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.setOffscreenPageLimit(2);

        mTabLayout.setTabWidth(SystemUtils.getDpScreenWidth() / PAGE_NUMBER);
        mTabLayout.setViewPager(mViewPager);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                Fragment fragment = (Fragment) mViewPager.getAdapter().instantiateItem(mViewPager, position);
                if (fragment instanceof ScrollableHelper.ScrollableContainer) {
                    mScrollableLayout.getHelper().setCurrentScrollableContainer((ScrollableHelper.ScrollableContainer) fragment);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void initRestoreState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            Parcelable pagerAdapterParcel = savedInstanceState.getParcelable(SAVE_STATE_KEY_PAGE_ADAPTER);
            if (pagerAdapterParcel != null)
                mViewPagerAdapter.restoreState(pagerAdapterParcel, ClassLoader.getSystemClassLoader());
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mViewPagerAdapter != null) {
            outState.putParcelable(SAVE_STATE_KEY_PAGE_ADAPTER, mViewPagerAdapter.saveState());
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
