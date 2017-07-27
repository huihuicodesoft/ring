package cn.com.wh.ring.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.com.wh.ring.R;
import cn.com.wh.ring.database.sp.DataCenter;

/**
 * Created by Hui on 2017/7/20.
 */

public class LoginActivity extends TitleActivity {
    @BindView(R.id.agree_protocol_iv)
    ImageView mAgreeProtocolIv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mTitleTv.setText(R.string.login);
        unbinder = ButterKnife.bind(this);

        boolean isAgree = DataCenter.getInstance().isAgreeProtocol();
        if (!isAgree) {
            startProtocol();
        }
        mAgreeProtocolIv.setSelected(isAgree);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ProtocolActivity.REQUEST_CODE_LOGIN) {
            boolean isAgree = DataCenter.getInstance().isAgreeProtocol();
            if (isAgree) {
                mAgreeProtocolIv.setSelected(isAgree);
            } else {
                onBackPressed();
            }
        }
    }

    @OnClick(R.id.mobile_ll)
    void onMobile() {
        LoginMobileActivity.start(this);
    }

    @OnClick(R.id.wx_ll)
    void onWX() {
    }

    @OnClick(R.id.sina_ll)
    void onSina() {
    }

    @OnClick(R.id.qq_ll)
    void onQQ() {
    }

    @OnClick(R.id.agree_protocol_iv)
    void onAgree() {
        boolean isNewSelected = !mAgreeProtocolIv.isSelected();
        mAgreeProtocolIv.setSelected(isNewSelected);
        DataCenter.getInstance().setAgreeProtocol(isNewSelected);
        startProtocol();
    }

    public void startProtocol() {
        Intent intent = new Intent(this, ProtocolActivity.class);
        startActivityForResult(intent, ProtocolActivity.REQUEST_CODE_LOGIN);
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }
}
