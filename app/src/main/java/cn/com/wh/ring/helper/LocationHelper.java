package cn.com.wh.ring.helper;

import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationListener;

import org.greenrobot.greendao.annotation.NotNull;

import cn.com.wh.ring.utils.LogUtils;

/**
 * Created by Hui on 2017/11/29.
 */

public class LocationHelper {
    private static final String TAG = LocationHelper.class.getName();

    public static void location(Context context, @NotNull final AMapLocationAdapter aMapLocationAdapter) {
        final AMapLocationClient locationClient = new AMapLocationClient(context);
        AMapLocationListener mAMapLocationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(final AMapLocation amapLocation) {
                if (amapLocation != null) {
                    if (amapLocation.getErrorCode() == 0) {
                        //解析定位结果
                        LogUtils.logV(TAG, "定位信息 = " + amapLocation.toString());
                        aMapLocationAdapter.onSuccess(amapLocation);
                    } else {
                        LogUtils.logV(TAG, "定位失败错误码 = " + amapLocation.getErrorCode());
                        aMapLocationAdapter.onFail(amapLocation.getErrorCode(), amapLocation.getErrorInfo());
                    }
                } else {
                    aMapLocationAdapter.onFail(-1, "定位位置错误");
                }
                locationClient.stopLocation();
            }
        };
        locationClient.setLocationListener(mAMapLocationListener);
        locationClient.startLocation();
    }

    public static abstract class AMapLocationAdapter {

        public void onSuccess(AMapLocation amapLocation) {
        }

        public void onFail(int errorCode, String errorMessage) {
        }
    }
}
