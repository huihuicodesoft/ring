package cn.com.wh.ring.ui.activity.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import cn.com.wh.ring.utils.SystemBarUtils;

/**
 * Created by Hui on 2017/9/13.
 */

public abstract class DarkStatusBarActivity extends FullScreenActivity {
    public boolean isStatusBarDark = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isStatusBarDark = SystemBarUtils.setStatusIcon(this, true);
    }
}
