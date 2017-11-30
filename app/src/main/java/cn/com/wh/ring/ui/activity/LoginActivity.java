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
import cn.com.wh.ring.ui.activity.base.TitleActivity;

/**
 * Created by Hui on 2017/7/20.
 */

public class LoginActivity extends TitleActivity {
    public static final int REQUEST_CODE_PROTOCOL = 0x12;
    public static final int REQUEST_CODE_LOGIN_MOBILE = 0x13;

    @BindView(R.id.agree_protocol_iv)
    ImageView mAgreeProtocolIv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
        if (requestCode == REQUEST_CODE_PROTOCOL) {
            boolean isAgree = DataCenter.getInstance().isAgreeProtocol();
            if (isAgree) {
                mAgreeProtocolIv.setSelected(isAgree);
            } else {
                onBackPressed();
            }
        } else if (requestCode == REQUEST_CODE_LOGIN_MOBILE && resultCode == RESULT_OK){
            finish();
        }
    }

    @OnClick(R.id.mobile_ll)
    void onMobile() {
        Intent intent = new Intent(this, LoginMobileActivity.class);
        startActivityForResult(intent, REQUEST_CODE_LOGIN_MOBILE);
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
        startActivityForResult(intent, REQUEST_CODE_PROTOCOL);
    }

    public static void start(Context context) {
        //清空token
        DataCenter.getInstance().setToken("");
        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
