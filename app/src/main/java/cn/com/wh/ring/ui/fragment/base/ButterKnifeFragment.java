package cn.com.wh.ring.ui.fragment.base;

import android.support.v4.app.Fragment;

import butterknife.Unbinder;

/**
 * Created by Hui on 2017/9/14.
 */

public class ButterKnifeFragment extends Fragment {
    public Unbinder unbinder;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }
}
