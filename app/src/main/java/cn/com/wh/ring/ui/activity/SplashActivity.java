package cn.com.wh.ring.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.FragmentActivity;

import cn.com.wh.ring.R;

/**
 * Created by Hui on 2017/7/14.
 */

public class SplashActivity extends FragmentActivity {
    private static final int STAY_TIME = 1000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                skipMain();
            }
        }, STAY_TIME);
    }

    private void skipMain() {
        ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(SplashActivity.this, R.anim.left_to_right, R.anim.right_to_left);
        ActivityCompat.startActivity(SplashActivity.this,
                new Intent(SplashActivity.this, MainActivity.class), compat.toBundle());
        finish();
    }
}
