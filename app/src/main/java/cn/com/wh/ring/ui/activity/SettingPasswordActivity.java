package cn.com.wh.ring.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import cn.com.wh.ring.R;
import cn.com.wh.ring.helper.LocationHelper;
import cn.com.wh.ring.network.request.Address;
import cn.com.wh.ring.network.request.RegisterMobile;
import cn.com.wh.ring.network.request.SmsCode;
import cn.com.wh.ring.network.response.Response;
import cn.com.wh.ring.network.retrofit.ListenerCallBack;
import cn.com.wh.ring.network.retrofit.NetWorkException;
import cn.com.wh.ring.network.service.Services;
import cn.com.wh.ring.ui.activity.base.TitleActivity;
import cn.com.wh.ring.utils.RSAUtils;
import cn.com.wh.ring.utils.ToastUtils;
import retrofit2.Call;

/**
 * Created by Hui on 2017/7/22.
 */

public class SettingPasswordActivity extends TitleActivity {
    private static final String KEY_MOBILE = "mobile";
    private static final String KEY_TYPE = "type";

    public static final int TYPE_REGISTER = 1;
    public static final int TYPE_RESET = 2;

    private final int VCODE_DIVIDE_TIME = 60;

    @BindView(R.id.password_tv)
    TextView mPasswordTv;
    @BindView(R.id.password_et)
    EditText mPasswordEt;
    @BindView(R.id.confirm_password_et)
    EditText mConfirmPasswordEt;
    @BindView(R.id.verification_code_et)
    EditText mVerificationCodeEt;
    @BindView(R.id.get_verification_code_tv)
    TextView mVerificationCodeTv;
    @BindView(R.id.next_tv)
    TextView mNextTv;

    private boolean isWaitGetVerificationCode;
    private Runnable mTimeRunnable;

    private static final String SAVE_KEY_MAP = "save_key_map";
    private AMapLocation mAmapLocation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_password);
        unbinder = ButterKnife.bind(this);
        initView();

        LocationHelper.location(getApplicationContext(), new LocationHelper.AMapLocationAdapter() {
            @Override
            public void onSuccess(AMapLocation amapLocation) {
                mAmapLocation = amapLocation;
            }
        });
    }

    private void initView() {
        int type = getIntent().getIntExtra(KEY_TYPE, 0);
        switch (type) {
            case TYPE_REGISTER:
                setTitle(R.string.register_password);
                mPasswordTv.setText(R.string.password);
                mNextTv.setText(R.string.register);
                break;
            case TYPE_RESET:
                setTitle(R.string.reset_password);
                mPasswordTv.setText(R.string.new_password);
                mNextTv.setText(R.string.ok);
                break;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mAmapLocation != null)
            outState.putParcelable(SAVE_KEY_MAP, mAmapLocation);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mAmapLocation = savedInstanceState.getParcelable(SAVE_KEY_MAP);
    }

    @OnTextChanged(R.id.password_et)
    void onPassword(CharSequence text) {
        checkAllEt();
    }

    @OnTextChanged(R.id.confirm_password_et)
    void onConfirmPassword(CharSequence text) {
        checkAllEt();
    }

    @OnTextChanged(R.id.verification_code_et)
    void onVerificationCode(CharSequence text) {
        checkAllEt();
    }

    @OnClick(R.id.get_verification_code_tv)
    void onGetVerificationCode() {
        countTime(VCODE_DIVIDE_TIME);
        //获取验证码
        String mobile = getIntent().getStringExtra(KEY_MOBILE);
        SmsCode smsCode = new SmsCode();
        smsCode.setMobile(mobile);
        Call<Response<String>> call = Services.smsService.getVerificationCode(smsCode);
        call.enqueue(new ListenerCallBack<String>(this) {
            @Override
            public void onSuccess(String s) {
                ToastUtils.showLongToast(R.string.tip_verification_code_send);
            }

            @Override
            public void onFailure(NetWorkException e) {
                stopCountTime();
            }
        });
    }

    @OnClick(R.id.next_tv)
    void onNext() {
        String password = mPasswordEt.getText().toString();
        String confirmPassword = mConfirmPasswordEt.getText().toString();
        String verificationCode = mVerificationCodeEt.getText().toString();
        String mobile = getIntent().getStringExtra(KEY_MOBILE);

        if (!password.equals(confirmPassword)) {
            ToastUtils.showShortToast(R.string.tip_confirm_password_diff);
            return;
        }
        if (verificationCode.length() != 6) {
            ToastUtils.showShortToast(R.string.tip_verification_code_invalid);
            return;
        }
        if (TextUtils.isEmpty(mobile)) {
            ToastUtils.showShortToast(R.string.tip_unkown_error);
            return;
        }

        stopCountTime();

        RegisterMobile registerMobile = new RegisterMobile();
        registerMobile.setMobile(mobile);
        registerMobile.setPassword(RSAUtils.encrypt(password));
        registerMobile.setCode(verificationCode);
        if (mAmapLocation != null) {
            registerMobile.setAddress(new Address(mAmapLocation));
        }

        int type = getIntent().getIntExtra(KEY_TYPE, 0);
        switch (type) {
            case TYPE_REGISTER:
                //注册
                requestRegister(registerMobile);
                break;
            case TYPE_RESET:
                //registerMobile
                requestResetPassword(registerMobile);
                break;
        }
    }

    private void requestRegister(RegisterMobile registerMobile) {
        Call<Response<String>> call = Services.accountService.registerMobile(registerMobile);
        call.enqueue(new ListenerCallBack<String>(this) {
            @Override
            public void onSuccess(String s) {
                ToastUtils.showLongToast(R.string.tip_success_register);
                LoginMobileActivity.startClearTop(SettingPasswordActivity.this);
            }

            @Override
            public void onFailure(NetWorkException e) {
                stopCountTime();
                ToastUtils.showLongToast(e.getMessage());
            }
        });
    }

    private void requestResetPassword(RegisterMobile registerMobile) {
        Call<Response<String>> call = Services.accountService.resetPassword(registerMobile);
        call.enqueue(new ListenerCallBack<String>(this) {
            @Override
            public void onSuccess(String s) {
                ToastUtils.showShortToast(R.string.tip_success_reset_password);
                LoginMobileActivity.startClearTop(SettingPasswordActivity.this);
            }

            @Override
            public void onFailure(NetWorkException e) {
                stopCountTime();
                ToastUtils.showShortToast(R.string.tip_fail_reset_password);
            }
        });
    }

    private void stopCountTime() {
        if (isWaitGetVerificationCode) {
            isWaitGetVerificationCode = false;
            countTime(0);
        }
    }

    private void countTime(int time) {
        if (mVerificationCodeTv == null || getResources() == null)
            return;

        if (time == 0) {
            isWaitGetVerificationCode = false;
            mVerificationCodeTv.setEnabled(true);
            mVerificationCodeTv.setText(R.string.get_verification_code);
            return;
        }
        isWaitGetVerificationCode = true;
        mVerificationCodeTv.setEnabled(false);
        mVerificationCodeTv.setText(getResources().getString(R.string.format_second, time--));
        final int finalTime = time;

        mTimeRunnable = new Runnable() {
            @Override
            public void run() {
                if (isWaitGetVerificationCode) {
                    countTime(finalTime);
                }
            }
        };
        mVerificationCodeTv.postDelayed(mTimeRunnable, 1000);
    }

    private void checkAllEt() {
        Editable password = mPasswordEt.getText();
        Editable confirmPassword = mConfirmPasswordEt.getText();
        Editable verificationCode = mVerificationCodeEt.getText();

        boolean isNoNull = !TextUtils.isEmpty(password) && !TextUtils.isEmpty(confirmPassword)
                && !TextUtils.isEmpty(verificationCode);
        mNextTv.setEnabled(isNoNull);
    }

    @Override
    protected void onDestroy() {
        if (mVerificationCodeTv != null && mTimeRunnable != null) {
            mVerificationCodeTv.removeCallbacks(mTimeRunnable);
        }
        super.onDestroy();
    }

    public static void start(Context context, String mobile, int type) {
        Intent intent = new Intent(context, SettingPasswordActivity.class);
        intent.putExtra(KEY_MOBILE, mobile);
        intent.putExtra(KEY_TYPE, type);
        context.startActivity(intent);
    }
}