package com.harreke.easyapp.widgets.animators;

import android.view.View;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ValueAnimator;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/01/08
 */
public abstract class ControllerLayoutAnimator {
    private View mController;
    private Animator.AnimatorListener mControllerListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationCancel(Animator animation) {
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            controllerAnimationEnd(mController);
        }

        @Override
        public void onAnimationRepeat(Animator animation) {
        }

        @Override
        public void onAnimationStart(Animator animation) {
            controllerAnimationStart(mController);
        }
    };
    private ValueAnimator.AnimatorUpdateListener mControllerUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            controllerAnimationUpdate(mController, animation);
        }
    };
    private ValueAnimator mControllerAnimator = null;
    private View mLayout;
    private Animator.AnimatorListener mLayoutListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationCancel(Animator animation) {
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            layoutAnimationEnd(mLayout);
        }

        @Override
        public void onAnimationRepeat(Animator animation) {
        }

        @Override
        public void onAnimationStart(Animator animation) {
            layoutAnimationStart(mLayout);
        }
    };
    private ValueAnimator.AnimatorUpdateListener mLayoutUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            layoutAnimationUpdate(mLayout, animation);
        }
    };
    private ValueAnimator mLayoutAnimator = null;
    private boolean mOpen = false;

    public ControllerLayoutAnimator(View controller, View layout) {
        mController = controller;
        mLayout = layout;
    }

    protected abstract ValueAnimator animateControllerClose(View controller);

    protected abstract ValueAnimator animateControllerOpen(View controller);

    protected abstract ValueAnimator animateLayoutClose(View layout);

    protected abstract ValueAnimator animateLayoutOpen(View layout);

    protected void cancelController() {
        if (mControllerAnimator != null) {
            mControllerAnimator.cancel();
            mControllerAnimator = null;
        }
    }

    protected void cancelLayout() {
        if (mLayoutAnimator != null) {
            mLayoutAnimator.cancel();
            mLayoutAnimator = null;
        }
    }

    public void close(boolean animate) {
        mOpen = false;
        cancelController();
        cancelLayout();
        if (animate) {
            mControllerAnimator = animateControllerClose(mController);
            mControllerAnimator.addUpdateListener(mControllerUpdateListener);
            mControllerAnimator.addListener(mControllerListener);
            mControllerAnimator.start();
            mLayoutAnimator = animateLayoutClose(mLayout);
            mLayoutAnimator.addUpdateListener(mLayoutUpdateListener);
            mLayoutAnimator.addListener(mLayoutListener);
            mLayoutAnimator.start();
        } else {
            setControllerClose(mController);
            controllerAnimationEnd(mController);
            setLayoutClose(mLayout);
            layoutAnimationEnd(mLayout);
        }
    }

    protected void controllerAnimationEnd(View controller) {
    }

    protected void controllerAnimationStart(View controller) {
    }

    protected abstract void controllerAnimationUpdate(View controller, ValueAnimator controllerAnimation);

    public View getController() {
        return mController;
    }

    public View getLayout() {
        return mLayout;
    }

    public boolean isOpen() {
        return mOpen;
    }

    protected void layoutAnimationEnd(View layout) {
    }

    protected void layoutAnimationStart(View layout) {
    }

    protected abstract void layoutAnimationUpdate(View layout, ValueAnimator layoutAnimation);

    public void open(boolean animate) {
        mOpen = true;
        cancelController();
        cancelLayout();
        if (animate) {
            mControllerAnimator = animateControllerOpen(mController);
            mControllerAnimator.addUpdateListener(mControllerUpdateListener);
            mControllerAnimator.addListener(mControllerListener);
            mControllerAnimator.start();
            mLayoutAnimator = animateLayoutOpen(mLayout);
            mLayoutAnimator.addUpdateListener(mLayoutUpdateListener);
            mLayoutAnimator.addListener(mLayoutListener);
            mLayoutAnimator.start();
        } else {
            controllerAnimationStart(mController);
            setControllerOpen(mController);
            layoutAnimationEnd(mLayout);
            setLayoutOpen(mLayout);
        }
    }

    protected abstract void setControllerClose(View controller);

    protected abstract void setControllerOpen(View controller);

    protected abstract void setLayoutClose(View layout);

    protected abstract void setLayoutOpen(View layout);

    public void toggleOpen() {
        if (mOpen) {
            close(true);
        } else {
            open(true);
        }
    }
}