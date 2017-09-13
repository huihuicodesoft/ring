package cn.com.wh.ring.ui.fragment.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.com.wh.ring.R;
import cn.com.wh.ring.database.sp.DataCenter;
import cn.com.wh.ring.helper.LoginHelper;
import cn.com.wh.ring.ui.activity.MeAttentionActivity;
import cn.com.wh.ring.ui.activity.MeCollectionActivity;
import cn.com.wh.ring.ui.activity.MeEditActivity;
import cn.com.wh.ring.ui.activity.MeFanActivity;
import cn.com.wh.ring.ui.activity.MeInteractionActivity;
import cn.com.wh.ring.ui.activity.MeMessageActivity;
import cn.com.wh.ring.ui.activity.SettingActivity;
import cn.com.wh.ring.ui.activity.base.DarkStatusBarActivity;
import cn.com.wh.ring.ui.fragment.base.TitleFragment;

/**
 * Created by Hui on 2017/7/13.
 */

public class MainMeFragment extends TitleFragment {
    @BindView(R.id.info_ll)
    LinearLayout mInfoLl;
    @BindView(R.id.unLogin_tv)
    TextView mUnLoginTv;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, root);
        return root;
    }

    @Override
    public View getTitleView() {
        return null;
    }

    @Override
    public View getContentView() {
        return View.inflate(getContext(), R.layout.fragment_main_me, null);
    }

    @Override
    public void initStatusBar() {
        super.initStatusBar();
        if (getActivity() != null && getActivity() instanceof DarkStatusBarActivity) {
            mStatusBar.setBackgroundColor(((DarkStatusBarActivity) getActivity()).isStatusBarDark ?
                    getResources().getColor(R.color.status_white) : getResources().getColor(R.color.status_gray));
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        boolean isLogin = DataCenter.getInstance().isLogin();
        mInfoLl.setVisibility(isLogin ? View.VISIBLE : View.GONE);
        mUnLoginTv.setVisibility(isLogin ? View.GONE : View.VISIBLE);
    }

    @OnClick(R.id.info_ll)
    void toMeEditActivity() {
        if (LoginHelper.isNoIntercept2Login(getContext())) {
            MeEditActivity.start(getContext());
        }
    }

    @OnClick(R.id.attention_ll)
    void toMeAttentionActivity() {
        if (LoginHelper.isNoIntercept2Login(getContext())) {
            MeAttentionActivity.start(getContext());
        }
    }

    @OnClick(R.id.fan_ll)
    void toMeFanActivity() {
        if (LoginHelper.isNoIntercept2Login(getContext())) {
            MeFanActivity.start(getContext());
        }
    }

    @OnClick(R.id.interaction_ll)
    void toMeInteractionActivity() {
        if (LoginHelper.isNoIntercept2Login(getContext())) {
            MeInteractionActivity.start(getContext());
        }
    }

    @OnClick(R.id.collection_ll)
    void toMeCollectionActivity() {
        MeCollectionActivity.start(getContext());
    }

    @OnClick(R.id.message_ll)
    void toMeMessageActivity() {
        MeMessageActivity.start(getContext());
    }

    @OnClick(R.id.unLogin_tv)
    void toLoginActivity() {
        LoginHelper.isNoIntercept2Login(getContext());
    }

    @OnClick(R.id.setting_ll)
    void toSettingActivity() {
        SettingActivity.start(getContext());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }
}
