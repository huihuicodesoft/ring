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

import com.flyco.tablayout.SlidingTabLayout;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.wh.ring.R;
import cn.com.wh.ring.ui.fragment.base.TitleFragment;
import cn.com.wh.ring.ui.fragment.find.ActivityFragment;
import cn.com.wh.ring.ui.fragment.find.FunnyCircleFragment;
import cn.com.wh.ring.ui.fragment.find.TalentFragment;

/**
 * Created by Hui on 2017/7/13.
 */

public class MainFindFragment extends TitleFragment {
    private static final String CLASS_SIMPLE_NAME = MainFindFragment.class.getSimpleName();
    private static final String SAVE_STATE_KEY_PAGE_ADAPTER = CLASS_SIMPLE_NAME + "pagerAdapter";

    @BindView(R.id.commonTabLayout)
    SlidingTabLayout mTabLayout;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    ViewPageAdapter mViewPageAdapter;

    @BindString(R.string.funny_circle)
    String funnyCircleStr;
    @BindString(R.string.talent)
    String talentStr;
    @BindString(R.string.activity)
    String activityStr;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, root);
        return root;
    }

    @Override
    public View getTitleView() {
        return View.inflate(getContext(), R.layout.title_main, null);
    }

    @Override
    public View getContentView() {
        return View.inflate(getContext(), R.layout.fragment_main_find, null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView(savedInstanceState);
    }

    private void initView(@Nullable Bundle savedInstanceState) {
        mViewPageAdapter = new ViewPageAdapter(getChildFragmentManager());

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
            String className;
            if (position == 0) {
                className = FunnyCircleFragment.class.getName();
            } else if (position == 1) {
                className = ActivityFragment.class.getName();
            } else {
                className = TalentFragment.class.getName();
            }
            return Fragment.instantiate(getContext(), className);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String title;
            if (position == 0) {
                title = funnyCircleStr;
            } else if (position == 1) {
                title = talentStr;
            } else {
                title = activityStr;
            }
            return title;
        }
    }
}
