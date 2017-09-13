package cn.com.wh.ring.ui.fragment.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindDimen;
import butterknife.BindString;
import cn.com.wh.ring.R;
import cn.com.wh.ring.ui.fragment.base.TitleFragment;

/**
 * Created by Hui on 2017/7/13.
 */

public class MainMeFragment extends TitleFragment {
    View selfRootView = null;

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

    @Override
    public View getTitleView() {
        return null;
    }

    @Override
    public View getContentView() {
        selfRootView = View.inflate(getContext(), R.layout.fragment_main_me, null);
        return selfRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }
}
