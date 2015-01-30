package com.harreke.easyapp.widgets.animators;

import android.view.View;

import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.ViewHelper;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/01/26
 */
public abstract class RotateAnimator extends ToggleAnimator implements IRotateAnimation {
    public RotateAnimator(View view) {
        super(view);
    }

    @Override
    protected ValueAnimator animateClose(View view) {
        return ValueAnimator.ofFloat(getCurrentRotation(view), getCloseRotation(view)).setDuration(300l);
    }

    @Override
    protected ValueAnimator animateOpen(View view) {
        return ValueAnimator.ofFloat(getCurrentRotation(view), getOpenRotation(view)).setDuration(300l);
    }

    @Override
    protected void animationUpdate(View view, ValueAnimator animator) {
        setRotation(view, (float) animator.getAnimatedValue());
    }

    protected float getCurrentRotation(View view) {
        return ViewHelper.getRotation(view);
    }

    @Override
    protected void setClose(View view) {
        setRotation(view, getCloseRotation(view));
    }

    @Override
    protected void setOpen(View view) {
        setRotation(view, getOpenRotation(view));
    }

    private void setRotation(View view, float rotation) {
        ViewHelper.setRotation(view, rotation);
    }
}
