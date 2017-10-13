package cn.com.wh.ring.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;
import cn.com.wh.ring.R;
import cn.com.wh.ring.database.bean.UserInfo;
import cn.com.wh.ring.ui.activity.base.TitleActivity;

/**
 * Created by Hui on 2017/9/27.
 */

public class EditSignatureActivity extends TitleActivity {
    @BindView(R.id.edit_signature_et)
    EditText mSignatureEt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_signature);
        unbinder = ButterKnife.bind(this);

        mRightTv.setText(R.string.save);
        mRightTv.setEnabled(false);
        mRightTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSignature();
            }
        });
    }

    private void updateSignature() {
        String signature = mSignatureEt.getText().toString();

        UserInfo userInfo = new UserInfo();
        userInfo.setNickname(TextUtils.isEmpty(signature) ? "" : signature);
        //UserHelper.updateUserInfo(this, userInfo);
    }

    @OnTextChanged(R.id.edit_signature_et)
    void onSignatureTextChange(CharSequence text) {
        mRightTv.setEnabled(true);
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, EditSignatureActivity.class);
        context.startActivity(intent);
    }
}
