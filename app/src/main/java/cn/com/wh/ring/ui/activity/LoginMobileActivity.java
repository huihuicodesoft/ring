package cn.com.wh.ring.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import cn.com.wh.ring.MainApplication;
import cn.com.wh.ring.R;
import cn.com.wh.ring.database.bean.UserInfo;
import cn.com.wh.ring.database.dao.UserInfoDao;
import cn.com.wh.ring.database.sp.DataCenter;
import cn.com.wh.ring.event.UserInfoEvent;
import cn.com.wh.ring.network.request.LoginMobile;
import cn.com.wh.ring.network.request.TerminalDetailInfo;
import cn.com.wh.ring.network.response.LoginUser;
import cn.com.wh.ring.network.response.Response;
import cn.com.wh.ring.network.retrofit.ListenerCallBack;
import cn.com.wh.ring.network.retrofit.NetWorkException;
import cn.com.wh.ring.network.retrofit.Server;
import cn.com.wh.ring.network.service.Services;
import cn.com.wh.ring.ui.activity.base.TitleActivity;
import cn.com.wh.ring.utils.LogUtils;
import cn.com.wh.ring.utils.RSAUtils;
import cn.com.wh.ring.utils.ToastUtils;
import retrofit2.Call;

/**
 * Created by Hui on 2017/7/22.
 */

public class LoginMobileActivity extends TitleActivity {
    private static final String TAG = LoginMobileActivity.class.getName();

    @BindView(R.id.mobile_et)
    EditText mMobileEt;
    @BindView(R.id.password_et)
    EditText mPasswordEt;
    @BindView(R.id.login_tv)
    TextView mLoginTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_mobile);
        mRightTv.setText(R.string.register);
        unbinder = ButterKnife.bind(this);

        initListener();
    }

    private void initListener() {
        mRightTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckMobileActivity.start(LoginMobileActivity.this, CheckMobileActivity.TYPE_REGISTER);
            }
        });
    }

    @OnTextChanged(R.id.mobile_et)
    void onMobile(CharSequence text) {
        checkAllEt();
    }

    @OnTextChanged(R.id.password_et)
    void onPassword(CharSequence text) {
        checkAllEt();
    }


    @OnClick(R.id.forget_password_tv)
    void onForgetPassword() {
        CheckMobileActivity.start(this, CheckMobileActivity.TYPE_BACK_PASSWORD);
    }

    @OnClick(R.id.login_tv)
    void onLogin() {
        String mobile = mMobileEt.getText().toString();
        String password = mPasswordEt.getText().toString();

        TerminalDetailInfo terminalDetailInfo = new TerminalDetailInfo();
        terminalDetailInfo.setModel(android.os.Build.MODEL);
        terminalDetailInfo.setSdk(String.valueOf(android.os.Build.VERSION.SDK_INT));
        terminalDetailInfo.setSystem(android.os.Build.VERSION.RELEASE);

        LoginMobile loginMobile = new LoginMobile();
        loginMobile.setMobile(mobile);
        loginMobile.setPassword(RSAUtils.encrypt(password));
        loginMobile.setTerminalDetailInfo(terminalDetailInfo);

        Call<Response<LoginUser>> call = Services.accountService.loginMobile(loginMobile);
        call.enqueue(new ListenerCallBack<LoginUser>(this) {
            @Override
            public void onSuccess(LoginUser loginUser) {
                if (loginUser != null && !TextUtils.isEmpty(loginUser.getToken()) && loginUser.getUserInfo() != null) {
                    Server.TOKEN = loginUser.getToken();
                    DataCenter.getInstance().setToken(loginUser.getToken());
                    DataCenter.getInstance().setLogin(true);

                    UserInfo userInfo = loginUser.getUserInfo();
                    LogUtils.logI(TAG, "用户信息 = " + userInfo.toString());
                    UserInfoDao userInfoDao = MainApplication.getInstance().getDaoSession().getUserInfoDao();
                    userInfoDao.insertOrReplace(userInfo);

                    EventBus.getDefault().post(new UserInfoEvent());

                    setResult(RESULT_OK);
                    finish();
                    ToastUtils.showLongToast(R.string.tip_success_login);
                } else {
                    ToastUtils.showLongToast(R.string.tip_fail_login);
                }
            }

            @Override
            public void onFailure(NetWorkException e) {
                ToastUtils.showLongToast(e.getMessage());
            }
        });
    }

    private void checkAllEt() {
        Editable mobile = mMobileEt.getText();
        Editable password = mPasswordEt.getText();

        boolean isNoNull = !TextUtils.isEmpty(password) && !TextUtils.isEmpty(mobile);
        mLoginTv.setEnabled(isNoNull);
    }

    public static void startClearTop(Context context) {
        Intent intent = new Intent(context, LoginMobileActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }
}
