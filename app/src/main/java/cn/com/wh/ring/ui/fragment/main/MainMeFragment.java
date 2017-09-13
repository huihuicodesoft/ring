package cn.com.wh.ring.ui.fragment.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindDimen;
import butterknife.BindString;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.com.wh.ring.R;
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
    @BindString(R.string.publish_post)
    String publishPostStr;
    @BindString(R.string.collection)
    String collectionStr;
    @BindString(R.string.comment)
    String commentStr;
    @BindDimen(R.dimen.height_title)
    int titleHeight;
    @BindDimen(R.dimen.me_title_display_edge)
    int titleDisplayEdge;

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
                    getResources().getColor(R.color.status_white) :  getResources().getColor(R.color.status_gray));
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @OnClick(R.id.info_ll)
    void toMeEditActivity() {
        MeEditActivity.start(getContext());
    }

    @OnClick(R.id.attention_ll)
    void toMeAttentionActivity() {
        MeAttentionActivity.start(getContext());
    }

    @OnClick(R.id.fan_ll)
    void toMeFanActivity() {
        MeFanActivity.start(getContext());
    }

    @OnClick(R.id.interaction_ll)
    void toMeInteractionActivity() {
        MeInteractionActivity.start(getContext());
    }

    @OnClick(R.id.collection_ll)
    void toMeCollectionActivity() {
        MeCollectionActivity.start(getContext());
    }

    @OnClick(R.id.message_ll)
    void toMeMessageActivity() {
        MeMessageActivity.start(getContext());
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
