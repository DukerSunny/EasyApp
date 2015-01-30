package com.harreke.easyapp.widgets.animators;

import android.view.View;
import android.view.ViewGroup;

import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.ViewHelper;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/01/24
 */
public class ScaleToExpandAnimator extends ControllerLayoutAnimator {
    private int mLayoutMaxHeight;

    public ScaleToExpandAnimator(View controller, View layout, int layoutMaxHeight) {
        super(controller, layout);
        mLayoutMaxHeight = layoutMaxHeight;
    }

    @Override
    protected ValueAnimator animateControllerClose(View controller) {
        return makeControllerScale(controller, 0f);
    }

    @Override
    protected ValueAnimator animateControllerOpen(View controller) {
        return makeControllerScale(controller, 1f);
    }

    @Override
    protected ValueAnimator animateLayoutClose(View layout) {
        return makeLayoutExpand(layout, 0);
    }

    @Override
    protected ValueAnimator animateLayoutOpen(View layout) {
        return makeLayoutExpand(layout, mLayoutMaxHeight);
    }

    @Override
    protected void controllerAnimationEnd(View controller) {
        if (ViewHelper.getScaleX(controller) == 0f) {
            controller.setVisibility(View.GONE);
        }
    }

    @Override
    protected void controllerAnimationStart(View controller) {
        if (ViewHelper.getScaleX(controller) == 0f) {
            controller.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void controllerAnimationUpdate(View controller, ValueAnimator controllerAnimation) {
        setControllerScale(controller, (float) controllerAnimation.getAnimatedValue());
    }

    @Override
    protected void layoutAnimationUpdate(View layout, ValueAnimator layoutAnimation) {
        setLayoutHeight(layout, (int) layoutAnimation.getAnimatedValue());
    }

    private ValueAnimator makeControllerScale(View controller, float scale) {
        return ValueAnimator.ofFloat(ViewHelper.getScaleX(controller), scale).setDuration(300l);
    }

    private ValueAnimator makeLayoutExpand(View layout, int height) {
        return ValueAnimator.ofInt(layout.getMeasuredHeight(), height);
    }

    @Override
    protected void setControllerClose(View controller) {
        setControllerScale(controller, -90f);
    }

    @Override
    protected void setControllerOpen(View controller) {
        setControllerScale(controller, 0f);
    }

    private void setControllerScale(View controller, float scale) {
        ViewHelper.setScaleX(controller, scale);
        ViewHelper.setScaleY(controller, scale);
    }

    @Override
    protected void setLayoutClose(View layout) {
        setLayoutHeight(layout, 0);
    }

    private void setLayoutHeight(View layout, int height) {
        ViewGroup.LayoutParams params = layout.getLayoutParams();

        params.height = height;
        layout.setLayoutParams(params);
    }

    @Override
    protected void setLayoutOpen(View layout) {
        setLayoutHeight(layout, mLayoutMaxHeight);
    }
}
