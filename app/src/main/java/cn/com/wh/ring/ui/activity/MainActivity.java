package cn.com.wh.ring.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.com.wh.ring.R;
import cn.com.wh.ring.ui.fragment.ActivityFragment;
import cn.com.wh.ring.ui.fragment.FindFragment;
import cn.com.wh.ring.ui.fragment.HomeFragment;
import cn.com.wh.ring.ui.fragment.MeFragment;

public class MainActivity extends FullScreenActivity {
    @BindView(R.id.unTouchViewPager)
    ViewPager mViewPager;

    private List<Fragment> fragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        fragments.add(new HomeFragment());
        fragments.add(new ActivityFragment());
        fragments.add(new FindFragment());
        fragments.add(new MeFragment());

        mViewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        });
        mViewPager.requestDisallowInterceptTouchEvent(true);
    }

    @OnClick(R.id.bottom_home_ll)
    void onHome(){
        mViewPager.setCurrentItem(0, false);
    }

    @OnClick(R.id.bottom_activity_ll)
    void onActivity(){
        mViewPager.setCurrentItem(1, false);
    }

    @OnClick(R.id.bottom_publish_ll)
    void onPublish(){
        LoginActivity.start(this);
    }

    @OnClick(R.id.bottom_find_ll)
    void onFind(){
        mViewPager.setCurrentItem(2, false);
    }

    @OnClick(R.id.bottom_me_ll)
    void onMe(){
        mViewPager.setCurrentItem(3, false);
    }


    public static void start(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }
}
