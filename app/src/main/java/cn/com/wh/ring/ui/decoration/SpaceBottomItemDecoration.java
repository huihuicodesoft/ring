package cn.com.wh.ring.ui.decoration;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Hui on 2017/11/13.
 */

public class SpaceBottomItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public SpaceBottomItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.bottom = space;
    }
}
