package cn.com.wh.ring.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.com.wh.ring.R;

/**
 * Created by Hui on 2017/7/22.
 */

public class LoginMobileActivity extends TitleActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_mobile);
        mTitleTv.setText(R.string.login_mobile);
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

    @OnClick(R.id.forget_password_tv)
    void onForgetPassword(){
        CheckMobileActivity.start(this, CheckMobileActivity.TYPE_BACK_PASSWORD);
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, LoginMobileActivity.class);
        context.startActivity(intent);
    }
}
