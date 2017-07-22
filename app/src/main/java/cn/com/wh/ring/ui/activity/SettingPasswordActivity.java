package cn.com.wh.ring.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import cn.com.wh.ring.R;
import cn.com.wh.ring.utils.ToastUtils;

/**
 * Created by Hui on 2017/7/22.
 */

public class SettingPasswordActivity extends TitleActivity {
    private static final String KEY_MOBILE = "mobile";
    private static final String KEY_TYPE = "type";

    public static final int TYPE_REGISTER = 1;
    public static final int TYPE_RESET = 2;

    @BindView(R.id.password_tv)
    TextView mPasswordTv;
    @BindView(R.id.password_et)
    EditText mPasswordEt;
    @BindView(R.id.confirm_password_et)
    EditText mConfirmPasswordEt;
    @BindView(R.id.verification_code_et)
    EditText mVerificationCodeEt;
    @BindView(R.id.register_tv)
    TextView mRegisterTv;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_password);
        unbinder = ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        int type = getIntent().getIntExtra(KEY_TYPE, 0);
        switch (type) {
            case TYPE_REGISTER:
                mTitleTv.setText(R.string.register_password);
                mPasswordTv.setText(R.string.password);
                mRegisterTv.setText(R.string.register);
                break;
            case TYPE_RESET:
                mTitleTv.setText(R.string.reset_password);
                mPasswordTv.setText(R.string.new_password);
                mRegisterTv.setText(R.string.ok);
                break;
        }
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
        //获取验证码
    }

    @OnClick(R.id.register_tv)
    void onRegister() {
        Editable password = mPasswordEt.getText();
        Editable confirmPassword = mConfirmPasswordEt.getText();
        Editable verificationCode = mVerificationCodeEt.getText();

        if (!password.toString().equals(confirmPassword.toString())) {
            ToastUtils.showShortToast(R.string.tip_confirm_password_diff);
            return;
        }
        if (verificationCode.length() != 6) {
            ToastUtils.showShortToast(R.string.tip_verification_code_invalid);
            return;
        }
        String mobile = getIntent().getStringExtra(KEY_MOBILE);
        if (TextUtils.isEmpty(mobile)) {
            ToastUtils.showShortToast(R.string.tip_unkown_error);
            return;
        }

        int type = getIntent().getIntExtra(KEY_TYPE, 0);
        switch (type) {
            case TYPE_REGISTER:
                //注册
                break;
            case TYPE_RESET:
                //重设

                break;
        }
    }

    private void checkAllEt() {
        Editable password = mPasswordEt.getText();
        Editable confirmPassword = mConfirmPasswordEt.getText();
        Editable verificationCode = mVerificationCodeEt.getText();

        boolean isNoNull = !TextUtils.isEmpty(password) && !TextUtils.isEmpty(confirmPassword)
                && !TextUtils.isEmpty(verificationCode);
        mRegisterTv.setEnabled(isNoNull);
    }

    public static void start(Context context, String mobile, int type) {
        Intent intent = new Intent(context, SettingPasswordActivity.class);
        intent.putExtra(KEY_MOBILE, mobile);
        intent.putExtra(KEY_TYPE, type);
        context.startActivity(intent);
    }
}