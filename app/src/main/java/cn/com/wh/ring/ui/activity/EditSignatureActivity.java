package cn.com.wh.ring.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
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

public class EditSignatureActivity extends TitleActivity {
    private static final String TAG = EditSignatureActivity.class.getName();

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
        mSignatureEt.setText(TextUtils.isEmpty(userInfo.getSignature()) ? "" : userInfo.getSignature());
        mSignatureEt.setSelection(mSignatureEt.getText().length());
        mSignatureEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mRightTv.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void updateSignature() {
        final String signature = mSignatureEt.getText().toString();

        UserInfo userInfo = new UserInfo();
        userInfo.setSignature(TextUtils.isEmpty(signature) ? "" : signature);
        Call<Response<Object>> call = Services.userService.uploadUserInfo(userInfo);
        call.enqueue(new ListenerCallBack<Object>(this) {
            @Override
            public void onSuccess(Object o) {
                UserInfoDao userInfoDao = MainApplication.getInstance().getDaoSession().getUserInfoDao();
                List<UserInfo> userInfoList = userInfoDao.queryBuilder().list();
                if (userInfoList.size() > 0) {
                    UserInfo dbUserInfo = userInfoList.get(0);
                    dbUserInfo.setSignature(signature);
                    userInfoDao.update(dbUserInfo);
                    EventBus.getDefault().post(new UserInfoEvent());
                }
                ToastUtils.showShortToast(R.string.toast_signature_update_success);
                finish();
            }

            @Override
            public void onFailure(NetWorkException e) {
                ToastUtils.showShortToast(e.getMessage());
            }
        });
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, EditSignatureActivity.class);
        context.startActivity(intent);
    }
}
