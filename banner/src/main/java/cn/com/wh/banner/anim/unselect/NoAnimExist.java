package cn.com.wh.banner.anim.unselect;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;

import cn.com.wh.banner.anim.BaseAnimator;

public class NoAnimExist extends BaseAnimator {
    public NoAnimExist() {
        this.mDuration = 200;
    }

    public void setAnimation(View view) {
        this.mAnimatorSet.playTogether(new Animator[]{
                ObjectAnimator.ofFloat(view, "alpha", 1, 1)});
    }
}
