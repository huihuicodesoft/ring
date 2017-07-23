package cn.com.wh.ring.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import cn.com.wh.ring.R;
import cn.com.wh.ring.network.response.Response;
import cn.com.wh.ring.network.retrofit.ListenerCallBack;
import cn.com.wh.ring.network.retrofit.NetWorkException;
import cn.com.wh.ring.network.retrofit.ReturnCode;
import cn.com.wh.ring.network.service.Services;
import cn.com.wh.ring.utils.ToastUtils;
import retrofit2.Call;

/**
 * Created by Hui on 2017/7/22.
 */

public class CheckMobileActivity extends TitleActivity {
    private static final String KEY_TYPE = "type";

    public static final int TYPE_REGISTER = 1;
    public static final int TYPE_BACK_PASSWORD = 2;

    @BindView(R.id.mobile_number_et)
    EditText mMobileNumberEt;
    @BindView(R.id.next_tv)
    TextView mNextTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_mobile);
        initTitle();
        unbinder = ButterKnife.bind(this);
    }

    private void initTitle() {
        int type = getIntent().getIntExtra(KEY_TYPE, 0);
        switch (type) {
            case TYPE_REGISTER:
                mTitleTv.setText(R.string.register_mobile);
                break;
            case TYPE_BACK_PASSWORD:
                mTitleTv.setText(R.string.back_password);
                break;
        }
    }

    @OnClick(R.id.next_tv)
    void onNext() {
        final String mobile = mMobileNumberEt.getText().toString();
        final int type = getIntent().getIntExtra(KEY_TYPE, 0);

        //核对手机号
        Call<Response<String>> call = Services.accountService.validMobile(mobile);
        call.enqueue(new ListenerCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                switch (type) {
                    case TYPE_REGISTER:
                        ToastUtils.showShortToast("账号已注册");
                        break;
                    case TYPE_BACK_PASSWORD:
                        SettingPasswordActivity.start(CheckMobileActivity.this, mobile, SettingPasswordActivity.TYPE_RESET);
                        break;
                }
            }

            @Override
            public void onFailure(NetWorkException e) {
                switch (type) {
                    case TYPE_REGISTER:
                        if (e.getCode() == ReturnCode.ERROR_MOBILE_UN_EXIST) {
                            SettingPasswordActivity.start(CheckMobileActivity.this, mobile, SettingPasswordActivity.TYPE_REGISTER);
                        } else {
                            ToastUtils.showShortToast(e.getMessage());
                        }
                        break;
                    case TYPE_BACK_PASSWORD:
                        ToastUtils.showShortToast(e.getMessage());
                        break;
                }
            }
        });
    }

    @OnTextChanged(R.id.mobile_number_et)
    void onMobileNumber(CharSequence text) {
        mNextTv.setEnabled(text.length() == 11);
    }

    public static void start(Context context, int type) {
        Intent intent = new Intent(context, CheckMobileActivity.class);
        intent.putExtra(KEY_TYPE, type);
        context.startActivity(intent);
    }
}
