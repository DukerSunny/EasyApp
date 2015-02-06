package com.harreke.easyapp.widgets.transitions;

import android.view.View;

import com.harreke.easyapp.widgets.animators.ViewValueAnimator;
import com.nineoldandroids.animation.Animator;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/02/02
 */
public class HeroRunnable implements Runnable {
    private ViewValueAnimator mAnimator;

    public HeroRunnable(View startView, int endViewWidth, int endViewHeight, float endViewX, float endViewY,
            Animator.AnimatorListener listener) {
        mAnimator = ViewValueAnimator.animate(startView).width(endViewWidth).height(endViewHeight).x(endViewX).y(endViewY)
                .listener(listener);
    }

    public void cancel() {
        mAnimator.cancel();
        mAnimator = null;
    }

    @Override
    public void run() {

    }
}
