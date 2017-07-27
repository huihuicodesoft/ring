package cn.com.wh.ring.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.com.wh.ring.R;
import cn.com.wh.ring.utils.SystemBarUtils;

/**
 * Created by Hui on 2017/7/21.
 */

public abstract class TitleActivity extends FullScreenActivity {
    LinearLayout mRootLl;
    View mStatusBar;
    RelativeLayout mTitleRl;
    ImageView mBackIv;
    TextView mTitleTv;
    TextView mRightTv;

    FrameLayout mContentFl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(getRootResId());

        mRootLl = (LinearLayout) findViewById(R.id.root_ll);
        mStatusBar = findViewById(R.id.statusBar);
        mTitleRl = (RelativeLayout) findViewById(R.id.title_rl);
        mBackIv = (ImageView) findViewById(R.id.back_iv);
        mTitleTv = (TextView) findViewById(R.id.title_tv);
        mRightTv = (TextView) findViewById(R.id.right_tv);

        if (getRootResId() == R.layout.activity_title_frame){
            mContentFl = (FrameLayout) findViewById(R.id.content_fl);
        }

        SystemBarUtils.initStatusBarHeight(getResources(), mStatusBar);

        mBackIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    protected int getRootResId() {
        return R.layout.activity_title;
    }

    @Override
    public void setContentView(int layoutResID) {
        this.setContentView(View.inflate(this, layoutResID, null));
    }

    @Override
    public void setContentView(View view) {
        this.setContentView(view, null);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        if (getRootResId() == R.layout.activity_title_frame){
            mContentFl.addView(view, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
        } else {
            mRootLl.addView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
        }
    }
}
