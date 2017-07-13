package cn.com.wh.ring.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import cn.com.wh.ring.utils.SystemBarUtils;

/**
 * Created by Hui on 2017/7/13.
 */

public class FullScreenActivity extends FragmentActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SystemBarUtils.initActivity(this);
    }
}
