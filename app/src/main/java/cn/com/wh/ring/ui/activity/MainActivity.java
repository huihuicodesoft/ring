package cn.com.wh.ring.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.com.wh.permission.AndPermission;
import cn.com.wh.permission.PermissionListener;
import cn.com.wh.ring.R;
import cn.com.wh.ring.ui.fragment.ActivityFragment;
import cn.com.wh.ring.ui.fragment.FindFragment;
import cn.com.wh.ring.ui.fragment.HomeFragment;
import cn.com.wh.ring.ui.fragment.MeFragment;
import cn.com.wh.ring.utils.ToastUtils;

public class MainActivity extends FullScreenActivity {
    @BindView(R.id.unTouchViewPager)
    ViewPager mViewPager;

    private AMapLocationClient mLocationClient;
    private List<Fragment> fragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);

        initView();

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
        startActivity(PhotoPickerActivity.newIntent(this, new File(Environment.getExternalStorageDirectory(), "BGAPhotoPickerTakePhoto"), 9, null, true));
    }

    @OnClick(R.id.bottom_publish_ll)
    void onPublish() {
        LoginActivity.start(this);
    }

    @OnClick(R.id.bottom_find_ll)
    void onFind() {
        mViewPager.setCurrentItem(2, false);
    }

    @OnClick(R.id.bottom_me_ll)
    void onMe() {
        mViewPager.setCurrentItem(3, false);
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }
}
