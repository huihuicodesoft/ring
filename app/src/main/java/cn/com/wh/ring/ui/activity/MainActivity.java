package cn.com.wh.ring.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.com.wh.permission.AndPermission;
import cn.com.wh.permission.PermissionListener;
import cn.com.wh.ring.R;
import cn.com.wh.ring.event.PostPublishEvent;
import cn.com.wh.ring.helper.LocationHelper;
import cn.com.wh.ring.helper.LoginHelper;
import cn.com.wh.ring.ui.activity.base.DarkStatusBarActivity;
import cn.com.wh.ring.ui.fragment.main.MainFindFragment;
import cn.com.wh.ring.ui.fragment.main.MainHomeFragment;
import cn.com.wh.ring.ui.fragment.main.MainMeFragment;
import cn.com.wh.ring.ui.fragment.main.MainTopicFragment;
import cn.com.wh.ring.utils.ToastUtils;

public class MainActivity extends DarkStatusBarActivity {
    private static final String TAG = MainActivity.class.getName();
    private static final String SAVE_STATE_KEY_PAGE_ADAPTER = "pagerAdapter";

    @BindView(R.id.unTouchViewPager)
    ViewPager mViewPager;

    private ViewPagerAdapter mViewPagerAdapter;

    private class ViewPagerAdapter extends FragmentStatePagerAdapter {
        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            String className;
            if (position == 0) {
                className = MainHomeFragment.class.getName();
            } else if (position == 1) {
                className = MainFindFragment.class.getName();
            } else if (position == 2) {
                className = MainTopicFragment.class.getName();
            } else {
                className = MainMeFragment.class.getName();
            }
            return Fragment.instantiate(MainActivity.this, className);
        }

        @Override
        public int getCount() {
            return 4;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initView(savedInstanceState);

        requestPermission();
    }

    private void requestPermission() {
        AndPermission.with(this)
                .requestCode(200)
                .permission(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.READ_PHONE_STATE)
                .callback(new PermissionListener() {
                    @Override
                    public void onSucceed(int requestCode, List<String> grantedPermissions) {
                        if (requestCode == 200) {
                            LocationHelper.location(getApplicationContext(), new LocationHelper.AMapLocationAdapter() {
                            });
                        }
                    }

                    @Override
                    public void onFailed(int requestCode, List<String> deniedPermissions) {
                        if (requestCode == 200) {
                            AndPermission.defaultSettingDialog(MainActivity.this).show();
                        }
                    }
                }).start();
    }

    private void initView(Bundle savedInstanceState) {
        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        initRestoreState(savedInstanceState);

        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.requestDisallowInterceptTouchEvent(true);
    }

    private void initRestoreState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            Parcelable pagerAdapterParcel = savedInstanceState.getParcelable(SAVE_STATE_KEY_PAGE_ADAPTER);
            if (pagerAdapterParcel != null && mViewPagerAdapter != null)
                mViewPagerAdapter.restoreState(pagerAdapterParcel, ClassLoader.getSystemClassLoader());
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        if (mViewPagerAdapter != null) {
            outState.putParcelable(SAVE_STATE_KEY_PAGE_ADAPTER, mViewPagerAdapter.saveState());
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ToastUtils.showShortToast("MainActivity onNewIntent");
        setIntent(intent);
    }

    @OnClick(R.id.bottom_home_ll)
    void onHome() {
        mViewPager.setCurrentItem(0, false);
    }

    @OnClick(R.id.bottom_find_ll)
    void onFind() {
        mViewPager.setCurrentItem(1, false);
    }

    @OnClick(R.id.bottom_publish_ll)
    void onPublish() {
        if (LoginHelper.isNoIntercept2Login(this)) {
            MenuActivity.start(this);
        }
    }

    @OnClick(R.id.bottom_topic_ll)
    void onTopic() {
        mViewPager.setCurrentItem(2, false);
    }

    @OnClick(R.id.bottom_me_ll)
    void onMe() {
        mViewPager.setCurrentItem(3, false);
    }

    @Subscribe
    public void onEvent(PostPublishEvent postPublishEvent) {
        if (postPublishEvent.type == PostPublishEvent.TYPE_SKIP_POST) {
            if (mViewPager != null)
                mViewPager.setCurrentItem(3);
            MeInteractionActivity.start(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }
}
