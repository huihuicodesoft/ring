package cn.com.wh.ring.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.com.wh.ring.MainApplication;
import cn.com.wh.ring.R;
import cn.com.wh.ring.database.bean.UserInfo;
import cn.com.wh.ring.database.dao.UserInfoDao;
import cn.com.wh.ring.event.UserInfoEvent;
import cn.com.wh.ring.network.response.Response;
import cn.com.wh.ring.network.retrofit.ListenerCallBack;
import cn.com.wh.ring.network.retrofit.NetWorkException;
import cn.com.wh.ring.network.service.Services;
import cn.com.wh.ring.ui.activity.base.TitleActivity;
import cn.com.wh.ring.utils.LogUtils;
import cn.com.wh.ring.utils.ToastUtils;
import retrofit2.Call;

/**
 * Created by Hui on 2017/9/27.
 */

public class EditSexActivity extends TitleActivity {
    private static final String TAG = EditSexActivity.class.getName();
    @BindView(R.id.edit_sex_man_iv)
    ImageView mManIv;
    @BindView(R.id.edit_sex_woman_iv)
    ImageView mWomanIv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_sex);
        unbinder = ButterKnife.bind(this);

        bindInfoUI();
    }

    private void bindInfoUI() {
        UserInfoDao userInfoDao = MainApplication.getInstance().getDaoSession().getUserInfoDao();
        List<UserInfo> userInfoList = userInfoDao.queryBuilder().list();
        if (userInfoList.size() > 0) {
            UserInfo userInfo = userInfoList.get(0);
            bindData(userInfo);
        }
    }

    private void bindData(UserInfo userInfo) {
        LogUtils.logI(TAG, "用户信息 = " + userInfo.toString());
        Integer sex = userInfo.getSex();
        if (sex != null) {
            int sexValue = sex.intValue();
            mManIv.setSelected(sexValue == UserInfo.SEX_MAN);
            mWomanIv.setSelected(sexValue == UserInfo.SEX_WOMAN);
        } else {
            mManIv.setSelected(false);
            mWomanIv.setSelected(false);
        }
    }

    @OnClick(R.id.edit_sex_man_ll)
    void onMan() {
        updateSex(UserInfo.SEX_MAN);
    }

    @OnClick(R.id.edit_sex_woman_ll)
    void onWoman() {
        updateSex(UserInfo.SEX_WOMAN);
    }

    private void updateSex(final int sex) {
        UserInfo userInfo = new UserInfo();
        userInfo.setSex(sex);
        Call<Response<Object>> call = Services.userService.uploadUserInfo(userInfo);
        call.enqueue(new ListenerCallBack<Object>(this) {
            @Override
            public void onSuccess(Object o) {
                UserInfoDao userInfoDao = MainApplication.getInstance().getDaoSession().getUserInfoDao();
                List<UserInfo> userInfoList = userInfoDao.queryBuilder().list();
                if (userInfoList.size() > 0) {
                    UserInfo dbUserInfo = userInfoList.get(0);
                    dbUserInfo.setSex(sex);
                    userInfoDao.update(dbUserInfo);

                    if (mManIv != null)
                        mManIv.setSelected(sex == UserInfo.SEX_MAN);
                    if (mWomanIv != null)
                        mWomanIv.setSelected(sex == UserInfo.SEX_WOMAN);

                    EventBus.getDefault().post(new UserInfoEvent());
                    finish();
                }
                ToastUtils.showShortToast(R.string.toast_sex_update_success);
                finish();
            }

            @Override
            public void onFailure(NetWorkException e) {
                ToastUtils.showShortToast(e.getMessage());
            }
        });
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, EditSexActivity.class);
        context.startActivity(intent);
    }
}
