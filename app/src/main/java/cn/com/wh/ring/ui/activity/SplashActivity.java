package cn.com.wh.ring.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import cn.com.wh.ring.R;
import cn.com.wh.ring.database.sp.DataCenter;
import cn.com.wh.ring.helper.TerminalMarkHelper;
import cn.com.wh.ring.network.request.TerminalMark;
import cn.com.wh.ring.network.response.Response;
import cn.com.wh.ring.network.retrofit.ListenerCallBack;
import cn.com.wh.ring.network.retrofit.NetWorkException;
import cn.com.wh.ring.network.retrofit.Server;
import cn.com.wh.ring.network.service.Services;
import retrofit2.Call;

/**
 * Created by Hui on 2017/7/14.
 */

public class SplashActivity extends FragmentActivity {
    private static final int STAY_TIME = 1000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        startConfig();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                skipMain();
            }
        }, STAY_TIME);
    }

    private void startConfig() {
        String token = DataCenter.getInstance().getToken();
        if (TextUtils.isEmpty(token)) {
            String terminalMark = DataCenter.getInstance().getTerminalMark();
            if (TextUtils.isEmpty(terminalMark)) {
                terminalMark = TerminalMarkHelper.createTerminalMark();
                DataCenter.getInstance().setTerminalMark(terminalMark);
            }
            TerminalMark tm = TerminalMarkHelper.split(terminalMark);

            Call<Response<String>> call = Services.touristService.getTerminalToken(tm);
            call.enqueue(new ListenerCallBack<String>() {
                @Override
                public void onSuccess(String s) {
                    DataCenter.getInstance().setToken(s);
                    Server.TOKEN = s;
                }

                @Override
                public void onFailure(NetWorkException e) {

                }
            });

        } else {
            Server.TOKEN = token;
        }
    }

    private void skipMain() {
        ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(SplashActivity.this, R.anim.left_to_right, R.anim.right_to_left);
        ActivityCompat.startActivity(SplashActivity.this,
                new Intent(SplashActivity.this, MainActivity.class), compat.toBundle());
        finish();
    }
}
