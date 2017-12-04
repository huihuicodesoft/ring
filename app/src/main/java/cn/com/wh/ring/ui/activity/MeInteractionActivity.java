package cn.com.wh.ring.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.wh.ring.R;
import cn.com.wh.ring.ui.activity.base.TitleActivity;
import cn.com.wh.ring.ui.fragment.PostPublishFragment;
import cn.com.wh.ring.ui.fragment.home.HomeChoiceFragment;
import cn.com.wh.tablelayout.CommonTabLayout;
import cn.com.wh.tablelayout.entity.TabEntity;
import cn.com.wh.tablelayout.listener.OnTabSelectListener;

/**
 * Created by Hui on 2017/9/13.
 */

public class MeInteractionActivity extends TitleActivity {
    private static final String CLASS_SIMPLE_NAME = MeInteractionActivity.class.getSimpleName();
    private static final String SAVE_STATE_KEY_PAGE_ADAPTER = CLASS_SIMPLE_NAME + "pagerAdapter";

    @BindView(R.id.commonTabLayout)
    CommonTabLayout mTabLayout;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    ViewPageAdapter mViewPageAdapter;

    @BindString(R.string.post)
    String postStr;
    @BindString(R.string.comment)
    String commentStr;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me_interaction);
        unbinder = ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        mViewPageAdapter = new ViewPageAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mViewPageAdapter);

        ArrayList<TabEntity> tabEntities = new ArrayList<>();
        tabEntities.add(new TabEntity(postStr));
        tabEntities.add(new TabEntity(commentStr));
        mTabLayout.setTabData(tabEntities);

        mTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mViewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {
            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTabLayout.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
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
                    className = PostPublishFragment.class.getName();
                    break;
                case 1:
                    className = HomeChoiceFragment.class.getName();
                    break;
            }
            return Fragment.instantiate(MeInteractionActivity.this, className);
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
                    title = postStr;
                    break;
                case 1:
                    title = commentStr;
                    break;
            }
            return title;
        }
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, MeInteractionActivity.class);
        context.startActivity(intent);
    }
}
