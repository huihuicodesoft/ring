package cn.com.wh.ring.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.com.wh.ring.MainApplication;
import cn.com.wh.ring.R;
import cn.com.wh.ring.database.bean.UserInfo;
import cn.com.wh.ring.database.dao.UserInfoDao;
import cn.com.wh.ring.event.UserInfoEvent;
import cn.com.wh.ring.helper.RequestHelper;
import cn.com.wh.ring.helper.UserHelper;
import cn.com.wh.ring.network.response.Response;
import cn.com.wh.ring.network.retrofit.ListenerCallBack;
import cn.com.wh.ring.network.retrofit.NetWorkException;
import cn.com.wh.ring.network.service.Services;
import cn.com.wh.ring.ui.activity.base.TitleActivity;
import cn.com.wh.ring.ui.view.dialog.ProgressDialog;
import cn.com.wh.ring.utils.LogUtils;
import cn.com.wh.ring.utils.ToastUtils;
import okhttp3.MultipartBody;
import retrofit2.Call;

/**
 * Created by Hui on 2017/9/13.
 */

public class MeEditActivity extends TitleActivity {
    private static final String TAG = MeEditActivity.class.getName();
    private static final int REQUEST_CODE_SELECT_AVATAR = 0X23;

    @BindView(R.id.info_avatar_iv)
    ImageView mAvatarIv;
    @BindView(R.id.info_app_account_tv)
    TextView mAppAccountTv;
    @BindView(R.id.info_nickname_tv)
    TextView mNicknameTv;
    @BindView(R.id.info_sex_tv)
    TextView mSexTv;
    @BindView(R.id.info_address_tv)
    TextView mAddressTv;
    @BindView(R.id.info_signature_tv)
    TextView mSignatureTv;

    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me_edit);
        unbinder = ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(R.string.dialog_upload_avatar);
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
        mNicknameTv.setText(TextUtils.isEmpty(userInfo.getNickname()) ?
                getResources().getString(R.string.format_default_nickname, String.valueOf(userInfo.getUserId())) : userInfo.getNickname());
        mAppAccountTv.setText(String.valueOf(userInfo.getUserId()));
        mAddressTv.setText(userInfo.getAddressCode());
        mSignatureTv.setText(String.valueOf(userInfo.getSignature()));

        int resId;
        if (userInfo.getSex() == UserInfo.SEX_MAN) {
            resId = R.string.man;
        } else if (userInfo.getSex() == UserInfo.SEX_WOMAN) {
            resId = R.string.woman;
        } else {
            resId = R.string.un_select;
        }
        mSexTv.setText(resId);

        UserHelper.loadAvatar(mAvatarIv, userInfo.getAvatar());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_SELECT_AVATAR) {
                ArrayList<String> images = PhotoPickerActivity.getSelectedImages(data);
                if (images != null && images.size() > 0) {
                    LogUtils.logI(TAG, "头像图片 = " + images.get(0) + " : "+ new File(images.get(0)).length());
                    uploadAvatar(images);
                }
            }
        }
    }

    private void uploadAvatar(final ArrayList<String> images) {
        if (mProgressDialog != null && !mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }

        MultipartBody body = RequestHelper.filesToMultipartBody(images);
        Call<Response<String>> call = Services.userService.uploadAvatar(body);
        call.enqueue(new ListenerCallBack<String>(MeEditActivity.this) {
            @Override
            public void onSuccess(String s) {
                UserInfoDao userInfoDao = MainApplication.getInstance().getDaoSession().getUserInfoDao();
                List<UserInfo> userInfoList = userInfoDao.queryBuilder().list();
                if (userInfoList.size() > 0){
                    UserInfo userInfo = userInfoList.get(0);
                    userInfo.setAvatar(s);
                    userInfoDao.update(userInfo);
                    EventBus.getDefault().post(new UserInfoEvent());
                }

                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(NetWorkException e) {
                ToastUtils.showShortToast(e.getMessage());
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
            }
        });
    }

//    private void updateUserInfo(final UserInfo userInfo) {
//        Call<Response<Object>> call = Services.userService.uploadUserInfo(userInfo);
//        call.enqueue(new ListenerCallBack<Object>(MeEditActivity.this) {
//            @Override
//            public void onSuccess(Object o) {
//                UserInfoDao userInfoDao = MainApplication.getInstance().getDaoSession().getUserInfoDao();
//                userInfoDao.update(userInfo);
//            }
//
//            @Override
//            public void onFailure(NetWorkException e) {
//                ToastUtils.showShortToast(e.getMessage());
//            }
//        });
//    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUserInfoEvent(UserInfoEvent event) {
        bindInfoUI();
    }

    @OnClick(R.id.info_avatar_ll)
    void onAvatarEdit() {
        PhotoPickerActivity.startSingleForResult(this, new File(Environment.getExternalStorageDirectory(), "BGAPhotoPickerTakePhoto"),
                true, REQUEST_CODE_SELECT_AVATAR);
    }

    @OnClick(R.id.info_account_ll)
    void onAccountEdit() {

    }

    @OnClick(R.id.info_nickname_ll)
    void onNicknameEdit() {

    }

    @OnClick(R.id.info_sex_ll)
    void onSexEdit() {

    }

    @OnClick(R.id.info_address_ll)
    void onAddressEdit() {

    }

    @OnClick(R.id.info_signature_ll)
    void onSignatureEdit() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, MeEditActivity.class);
        context.startActivity(intent);
    }
}
