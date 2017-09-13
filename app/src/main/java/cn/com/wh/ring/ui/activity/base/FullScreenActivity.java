package cn.com.wh.ring.ui.activity.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import butterknife.Unbinder;
import cn.com.wh.ring.utils.SystemBarUtils;

/**
 * Created by Hui on 2017/7/13.
 */

public abstract class FullScreenActivity extends FragmentActivity {
    public Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SystemBarUtils.initActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }
}
