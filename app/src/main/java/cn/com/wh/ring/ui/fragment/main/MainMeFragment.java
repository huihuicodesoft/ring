package cn.com.wh.ring.ui.fragment.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.com.wh.ring.MainApplication;
import cn.com.wh.ring.R;
import cn.com.wh.ring.database.bean.UserInfo;
import cn.com.wh.ring.database.dao.UserInfoDao;
import cn.com.wh.ring.database.sp.DataCenter;
import cn.com.wh.ring.event.LogoutEvent;
import cn.com.wh.ring.event.UserInfoEvent;
import cn.com.wh.ring.helper.LoginHelper;
import cn.com.wh.ring.helper.UserHelper;
import cn.com.wh.ring.ui.activity.EditMeActivity;
import cn.com.wh.ring.ui.activity.MeAttentionActivity;
import cn.com.wh.ring.ui.activity.MeCollectionActivity;
import cn.com.wh.ring.ui.activity.MeFanActivity;
import cn.com.wh.ring.ui.activity.MeInteractionActivity;
import cn.com.wh.ring.ui.activity.MeMessageActivity;
import cn.com.wh.ring.ui.activity.SettingActivity;
import cn.com.wh.ring.ui.activity.base.DarkStatusBarActivity;
import cn.com.wh.ring.ui.fragment.base.ButterKnifeFragment;
import cn.com.wh.ring.utils.LogUtils;
import cn.com.wh.ring.utils.SystemBarUtils;

/**
 * Created by Hui on 2017/7/13.
 */

public class MainMeFragment extends ButterKnifeFragment {
    private static final String TAG = MainMeFragment.class.getName();

    @BindView(R.id.statusBar)
    View mStatusBar;
    @BindView(R.id.me_info_ll)
    LinearLayout mInfoLl;
    @BindView(R.id.me_nickname_tv)
    TextView mNicknameTv;
    @BindView(R.id.me_app_account_tv)
    TextView mAppAccountTv;
    @BindView(R.id.me_address_tv)
    TextView mAddressTv;
    @BindView(R.id.me_signature_tv)
    TextView mSignatureTv;
    @BindView(R.id.me_avatar_iv)
    ImageView mAvatarIv;

    @BindView(R.id.me_unLogin_tv)
    TextView mUnLoginTv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main_me, container, false);
        unbinder = ButterKnife.bind(this, root);
        EventBus.getDefault().register(this);
        initStatusBar();
        return root;
    }

    public void initStatusBar() {
        SystemBarUtils.initStatusBarHeight(getResources(), mStatusBar);
        if (getActivity() != null && getActivity() instanceof DarkStatusBarActivity) {
            mStatusBar.setBackgroundColor(((DarkStatusBarActivity) getActivity()).isStatusBarDark ?
                    getResources().getColor(R.color.status_white) : getResources().getColor(R.color.status_gray));
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        updateStateUI();
        bindInfoUI();
    }

    private void updateStateUI() {
        boolean isLogin = DataCenter.getInstance().isLogin();
        mInfoLl.setVisibility(isLogin ? View.VISIBLE : View.GONE);
        mUnLoginTv.setVisibility(isLogin ? View.GONE : View.VISIBLE);
    }

    @OnClick(R.id.me_info_ll)
    void toMeEditActivity() {
        if (LoginHelper.isNoIntercept2Login(getContext())) {
            EditMeActivity.start(getContext());
        }
    }

    @OnClick(R.id.me_attention_ll)
    void toMeAttentionActivity() {
        if (LoginHelper.isNoIntercept2Login(getContext())) {
            MeAttentionActivity.start(getContext());
        }
    }

    @OnClick(R.id.me_fan_ll)
    void toMeFanActivity() {
        if (LoginHelper.isNoIntercept2Login(getContext())) {
            MeFanActivity.start(getContext());
        }
    }

    @OnClick(R.id.me_interaction_ll)
    void toMeInteractionActivity() {
        if (LoginHelper.isNoIntercept2Login(getContext())) {
            MeInteractionActivity.start(getContext());
        }
    }

    @OnClick(R.id.me_collection_ll)
    void toMeCollectionActivity() {
        MeCollectionActivity.start(getContext());
    }

    @OnClick(R.id.me_message_ll)
    void toMeMessageActivity() {
        MeMessageActivity.start(getContext());
    }

    @OnClick(R.id.me_unLogin_tv)
    void toLoginActivity() {
        LoginHelper.isNoIntercept2Login(getContext());
    }

    @OnClick(R.id.me_setting_ll)
    void toSettingActivity() {
        SettingActivity.start(getContext());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogoutEvent(LogoutEvent event) {
        updateStateUI();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUserInfoEvent(UserInfoEvent event) {
        updateStateUI();
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
        mAppAccountTv.setText(getResources().getString(R.string.format_app_account, String.valueOf(userInfo.getUserId())));
        mAddressTv.setText(userInfo.getAddressCode());
        mSignatureTv.setText(getResources().getString(R.string.format_signature, String.valueOf(userInfo.getSignature())));
        UserHelper.loadAvatar(mAvatarIv, userInfo.getAvatar());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }
}
