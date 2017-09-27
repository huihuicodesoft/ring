package cn.com.wh.ring.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.com.wh.ring.MainApplication;
import cn.com.wh.ring.R;
import cn.com.wh.ring.database.dao.UserInfoDao;
import cn.com.wh.ring.database.sp.DataCenter;
import cn.com.wh.ring.event.LogoutEvent;
import cn.com.wh.ring.ui.activity.base.TitleActivity;
import cn.com.wh.ring.utils.ToastUtils;

/**
 * Created by Hui on 2017/9/13.
 */

public class SettingActivity extends TitleActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        unbinder = ButterKnife.bind(this);
    }

    @OnClick(R.id.setting_logout)
    void logout() {
        DataCenter.getInstance().setToken("");
        DataCenter.getInstance().setLogin(false);

        UserInfoDao userInfoDao = MainApplication.getInstance().getDaoSession().getUserInfoDao();
        userInfoDao.deleteAll();
        ToastUtils.showShortToast("退出登陆");
        EventBus.getDefault().post(new LogoutEvent());
        finish();
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, SettingActivity.class);
        context.startActivity(intent);
    }
}
