package com.harreke.easyapp.widgets.animators;

import android.view.View;

import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.ViewHelper;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/01/26
 */
public abstract class AlphaAnimator extends ToggleAnimator implements IAlphaAnimation {
    public AlphaAnimator(View view) {
        super(view);
    }

    @Override
    protected ValueAnimator animateClose(View view) {
        return ValueAnimator.ofFloat(getCurrentAlpha(view), getCloseAlpha(view)).setDuration(300l);
    }

    @Override
    protected ValueAnimator animateOpen(View view) {
        return ValueAnimator.ofFloat(getCurrentAlpha(view), getOpenAlpha(view)).setDuration(300l);
    }

    @Override
    protected void animationCloseEnd(View view) {
        view.setVisibility(View.GONE);
    }

    @Override
    protected void animationOpenStart(View view) {
        view.setVisibility(View.VISIBLE);
    }

    @Override
    protected void animationUpdate(View view, ValueAnimator animator) {
        setAlpha(view, (float) animator.getAnimatedValue());
    }

    protected float getCurrentAlpha(View view) {
        return ViewHelper.getAlpha(view);
    }

    private void setAlpha(View view, float rotation) {
        ViewHelper.setAlpha(view, rotation);
    }

    @Override
    protected void setClose(View view) {
        setAlpha(view, getCloseAlpha(view));
    }

    @Override
    protected void setOpen(View view) {
        setAlpha(view, getOpenAlpha(view));
    }
}