package cn.com.wh.ring.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.com.wh.ring.R;
import cn.com.wh.ring.ui.activity.base.FullScreenActivity;

/**
 * Created by Hui on 2016/7/15.
 */
public class MenuActivity extends FullScreenActivity {
    @BindView(R.id.menu_back)
    View mBackView;
    @BindView(R.id.menu_more_iv)
    ImageView mMenuMoreView;
    @BindView(R.id.menu_publish_help_ll)
    LinearLayout mHelpLl;
    @BindView(R.id.menu_publish_post_ll)
    LinearLayout mPostLl;

    private final static long ENTER_ANIM_TIME = 300;
    private final static long EXIT_ANIM_TIME = 300;
    private final static int ROTATE_DEGREE = 225;
    private final static float BASE_SCALE = 0.8f;

    private EnterAnimation mHelpEnterAnimation;
    private EnterAnimation mPostEnterAnimation;
    private RotateAnimation mRotateAnimation;
    private ExitAnimation mExitAnimation;

    private Animation.AnimationListener mExitAnimationListener;

    private OvershootInterpolator mOvershootInterpolator;
    private OvershootInterpolator mRotateInterpolator;

    private Point mMoreCenter = new Point();
    private Point mSubmitQuestionCenter = new Point();
    private Point mPublishFlowerCenter = new Point();

    private boolean isAnimFinish = false;
    private boolean isEnterAnimStarted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(0, 0);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        unbinder = ButterKnife.bind(this);
        init();
    }

    private void init() {
        ViewTreeObserver viewTreeObserver = getWindow().getDecorView().getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getLocations();
            }
        });

        initAnimation();

        mMenuMoreView.startAnimation(mRotateAnimation);
        mHelpLl.startAnimation(mHelpEnterAnimation);
        mPostLl.startAnimation(mPostEnterAnimation);
    }

    private void initAnimation() {
        mHelpEnterAnimation = new EnterAnimation(mHelpLl, mSubmitQuestionCenter);
        mPostEnterAnimation = new EnterAnimation(mPostLl, mPublishFlowerCenter);
        mRotateAnimation = new RotateAnimation(0, ROTATE_DEGREE,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);

        mRotateAnimation.setFillAfter(true);

        mOvershootInterpolator = new OvershootInterpolator(1.3f);
        mRotateInterpolator = new OvershootInterpolator();

        mRotateAnimation.setInterpolator(mRotateInterpolator);
        mHelpEnterAnimation.setInterpolator(mOvershootInterpolator);
        mPostEnterAnimation.setInterpolator(mOvershootInterpolator);

        mHelpEnterAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                isEnterAnimStarted = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                isEnterAnimStarted = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        mExitAnimationListener = new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                isAnimFinish = true;
                MenuActivity.this.finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        };


        mExitAnimation = new ExitAnimation();
        mExitAnimation.setDuration(EXIT_ANIM_TIME);
        mExitAnimation.setAnimationListener(mExitAnimationListener);
    }

    private void getLocations() {
        if (isEnterAnimStarted)
            return;

        int[] var1 = new int[2];
        int[] var2 = new int[2];
        int[] var3 = new int[2];
        mMenuMoreView.getLocationOnScreen(var1);
        mHelpLl.getLocationOnScreen(var2);
        mPostLl.getLocationOnScreen(var3);

        mMoreCenter.set(var1[0] + mMenuMoreView.getWidth() / 2,
                var1[1] + mMenuMoreView.getHeight() / 2);
        mSubmitQuestionCenter.set(var2[0] + mHelpLl.getWidth() / 2,
                var2[1] + mHelpLl.getHeight() / 2);
        mPublishFlowerCenter.set(var3[0] + mPostLl.getWidth() / 2,
                var3[1] + mPostLl.getHeight() / 2);

        mHelpEnterAnimation.setDuration(ENTER_ANIM_TIME);
        mPostEnterAnimation.setDuration(ENTER_ANIM_TIME);
        mRotateAnimation.setDuration(ENTER_ANIM_TIME);
    }

    @OnClick(R.id.menu_more_iv)
    void onClickMenuMore() {
        mBackView.clearAnimation();
        mBackView.startAnimation(mExitAnimation);
    }

    @OnClick(R.id.menu_publish_help_ll)
    void onClickMenuHelp() {
        finish();
    }

    @OnClick(R.id.menu_publish_post_ll)
    void onClickMenuPost() {
        PublishActivity.start(this);
        finish();
    }

    @OnClick(R.id.menu_back)
    void onClickMenuBack() {
        mBackView.clearAnimation();
        mBackView.startAnimation(mExitAnimation);
    }

    @Override
    public void onBackPressed() {
        mBackView.clearAnimation();
        mBackView.startAnimation(mExitAnimation);
    }

    /**
     * @param var
     * @param type 1,X轴; 2,Y轴
     * @return
     */
    private int getDistance(Point var, int type) {
        int distance = 0;
        switch (type) {
            case 1:
                distance = var.x - mMoreCenter.x;
                break;
            case 2:
                distance = var.y - mMoreCenter.y;
                break;
        }

        return distance;
    }

    class EnterAnimation extends Animation {
        private View view;
        private Point center;

        public EnterAnimation(View view, Point center) {
            this.view = view;
            this.center = center;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            ViewCompat.setTranslationX(view, -getDistance(center, 1) *
                    (1 - interpolatedTime));
            ViewCompat.setTranslationY(view, -getDistance(center, 2) *
                    (1 - interpolatedTime));
            ViewCompat.setScaleX(view, interpolatedTime);
            ViewCompat.setScaleY(view, interpolatedTime);
        }
    }

    class ExitAnimation extends Animation {
        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            float percent = 1 - interpolatedTime;

            ViewCompat.setRotation(mMenuMoreView, ROTATE_DEGREE * percent);

            ViewCompat.setAlpha(mMenuMoreView, percent);
            ViewCompat.setAlpha(mHelpLl, percent);
            ViewCompat.setAlpha(mPostLl, percent);
            ViewCompat.setAlpha(mBackView, percent);

            float scale = 1f + interpolatedTime * BASE_SCALE;
            ViewCompat.setScaleX(mHelpLl, scale);
            ViewCompat.setScaleY(mHelpLl, scale);
            ViewCompat.setScaleX(mPostLl, scale);
            ViewCompat.setScaleY(mPostLl, scale);
        }
    }

    @Override
    public void finish() {
        super.finish();
        if (isAnimFinish) {
            overridePendingTransition(0, 0);
        } else {
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, MenuActivity.class);
        context.startActivity(intent);
    }
}
