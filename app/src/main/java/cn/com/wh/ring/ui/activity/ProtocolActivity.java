package cn.com.wh.ring.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.com.wh.ring.R;
import cn.com.wh.ring.database.sp.DataCenter;
import cn.com.wh.ring.ui.activity.base.TitleActivity;

/**
 * Created by Hui on 2017/7/21.
 */

public class ProtocolActivity extends TitleActivity {
    public static final int REQUEST_CODE_LOGIN = 0x12;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_protocol);

        setTitle(R.string.app_protocol);

        unbinder = ButterKnife.bind(this);
    }

    @OnClick(R.id.agree_tv)
    void onAgree(){
        DataCenter.getInstance().setAgreeProtocol(true);
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        finish();
    }
}
