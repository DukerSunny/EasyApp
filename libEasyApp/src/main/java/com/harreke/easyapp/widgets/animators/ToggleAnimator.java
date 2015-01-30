package com.harreke.easyapp.widgets.animators;

import android.view.View;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.ValueAnimator;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/01/26
 */
public abstract class ToggleAnimator {
    private ValueAnimator mAnimator = null;
    private boolean mOpen = false;
    private View mView;
    private Animator.AnimatorListener mOpenListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            animationOpenEnd(mView);
        }

        @Override
        public void onAnimationStart(Animator animation) {
            animationOpenStart(mView);
        }
    };
    private Animator.AnimatorListener mCloseListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            animationCloseEnd(mView);
        }

        @Override
        public void onAnimationStart(Animator animation) {
            animationCloseStart(mView);
        }
    };
    private ValueAnimator.AnimatorUpdateListener mUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            animationUpdate(mView, animation);
        }
    };

    public ToggleAnimator(View view) {
        mView = view;
    }

    protected abstract ValueAnimator animateClose(View view);

    protected abstract ValueAnimator animateOpen(View view);

    protected void animationCloseEnd(View view) {
    }

    protected void animationCloseStart(View view) {
    }

    protected void animationOpenEnd(View view) {
    }

    protected void animationOpenStart(View view) {
    }

    protected abstract void animationUpdate(View view, ValueAnimator animator);

    protected void cancel() {
        if (mAnimator != null) {
            mAnimator.cancel();
            mAnimator = null;
        }
    }

    public void close(boolean animate) {
        mOpen = false;
        cancel();
        if (animate) {
            mAnimator = animateClose(mView);
            mAnimator.addUpdateListener(mUpdateListener);
            mAnimator.addListener(mCloseListener);
            mAnimator.start();
        } else {
            setClose(mView);
            animationCloseEnd(mView);
        }
    }

    public View getView() {
        return mView;
    }

    public boolean isOpen() {
        return mOpen;
    }

    public void open(boolean animate) {
        mOpen = true;
        cancel();
        if (animate) {
            mAnimator = animateOpen(mView);
            mAnimator.addUpdateListener(mUpdateListener);
            mAnimator.addListener(mOpenListener);
            mAnimator.start();
        } else {
            animationOpenStart(mView);
            setOpen(mView);
        }
    }

    protected abstract void setClose(View view);

    protected abstract void setOpen(View view);

    public void toggle() {
        if (isOpen()) {
            close(true);
        } else {
            open(true);
        }
    }
}