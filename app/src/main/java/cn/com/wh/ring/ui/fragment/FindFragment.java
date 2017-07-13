package cn.com.wh.ring.ui.fragment;

import android.view.View;

import cn.com.wh.ring.R;

/**
 * Created by Hui on 2017/7/13.
 */

public class FindFragment extends TitleFragment {

    @Override
    public View getTitleView() {
        return null;
    }

    @Override
    public View getContentView() {
        return View.inflate(getContext(), R.layout.fragment_find, null);
    }
}
