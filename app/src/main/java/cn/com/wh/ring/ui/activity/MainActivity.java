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

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.com.wh.permission.AndPermission;
import cn.com.wh.permission.PermissionListener;
import cn.com.wh.ring.R;
import cn.com.wh.ring.event.PostPublishEvent;
import cn.com.wh.ring.helper.LoginHelper;
import cn.com.wh.ring.ui.activity.base.DarkStatusBarActivity;
import cn.com.wh.ring.ui.fragment.main.MainFindFragment;
import cn.com.wh.ring.ui.fragment.main.MainHelpFragment;
import cn.com.wh.ring.ui.fragment.main.MainHomeFragment;
import cn.com.wh.ring.ui.fragment.main.MainMeFragment;
import cn.com.wh.ring.utils.ToastUtils;

public class MainActivity extends DarkStatusBarActivity {
    private static final String SAVE_STATE_KEY_PAGE_ADAPTER = "pagerAdapter";

    @BindView(R.id.unTouchViewPager)
    ViewPager mViewPager;

    private AMapLocationClient mLocationClient;
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
                className = MainHelpFragment.class.getName();
            } else if (position == 2) {
                className = MainFindFragment.class.getName();
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
                .permission(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                .callback(new PermissionListener() {
                    @Override
                    public void onSucceed(int requestCode, List<String> grantedPermissions) {
                        if (requestCode == 200) {
                            location();
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

    private void location() {
        mLocationClient = new AMapLocationClient(getApplicationContext());
        AMapLocationListener mAMapLocationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation amapLocation) {
                if (amapLocation != null) {
                    if (amapLocation.getErrorCode() == 0) {
                        //解析定位结果
                        ToastUtils.showShortToast(amapLocation.getAdCode());
                    } else {
                        ToastUtils.showShortToast("" + amapLocation.getErrorCode());
                    }
                }
                mLocationClient.stopLocation();
            }
        };
        mLocationClient.setLocationListener(mAMapLocationListener);
        mLocationClient.startLocation();
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

    @OnClick(R.id.bottom_activity_ll)
    void onActivity() {
        mViewPager.setCurrentItem(1, false);
    }

    @OnClick(R.id.bottom_publish_ll)
    void onPublish() {
        if (LoginHelper.isNoIntercept2Login(this)) {
            PublishActivity.start(this);
        }
    }

    @OnClick(R.id.bottom_find_ll)
    void onFind() {
        mViewPager.setCurrentItem(2, false);
    }

    @OnClick(R.id.bottom_me_ll)
    void onMe() {
        mViewPager.setCurrentItem(3, false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PostPublishEvent event) {
        if (event.type == PostPublishEvent.TYPE_SKIP_ME && isTaskRoot()) {
            if (mViewPager != null) {
                mViewPager.setCurrentItem(4, false);
                EventBus.getDefault().post(new PostPublishEvent(PostPublishEvent.TYPE_SKIP_POST, event.id));
            }
        }
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }
}
