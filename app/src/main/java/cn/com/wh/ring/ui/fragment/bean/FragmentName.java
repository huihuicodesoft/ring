package cn.com.wh.ring.ui.fragment.bean;

import android.support.v4.app.Fragment;

/**
 * Created by Hui on 2017/8/10.
 */

public class FragmentName {
    String name;
    Fragment fragment;

    public FragmentName(String name, Fragment fragment) {
        this.name = name;
        this.fragment = fragment;
    }

    public String getName() {
        return name;
    }

    public Fragment getFragment() {
        return fragment;
    }
}
