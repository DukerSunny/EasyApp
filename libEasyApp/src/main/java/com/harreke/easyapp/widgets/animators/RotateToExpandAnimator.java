package com.harreke.easyapp.widgets.animators;

import android.view.View;
import android.view.ViewGroup;

import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.ViewHelper;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/01/24
 */
public class RotateToExpandAnimator extends ControllerLayoutAnimator {
    private int mLayoutMaxHeight;

    public RotateToExpandAnimator(View controller, View layout, int layoutMaxHeight) {
        super(controller, layout);
        mLayoutMaxHeight = layoutMaxHeight;
    }

    @Override
    protected ValueAnimator animateControllerClose(View controller) {
        return makeControllerRotate(controller, -90f);
    }

    @Override
    protected ValueAnimator animateControllerOpen(View controller) {
        return makeControllerRotate(controller, 0f);
    }

    @Override
    protected ValueAnimator animateLayoutClose(View layout) {
        return makeLayoutExpand(layout, 0);
    }

    @Override
    protected ValueAnimator animateLayoutOpen(View layout) {
        return makeLayoutExpand(layout, mLayoutMaxHeight);
    }

    private ValueAnimator makeControllerRotate(View controller, float degree) {
        return ValueAnimator.ofFloat(ViewHelper.getRotation(controller), degree).setDuration(300l);
    }

    private ValueAnimator makeLayoutExpand(View layout, int height) {
        return ValueAnimator.ofInt(layout.getMeasuredHeight(), height);
    }

    @Override
    protected void setControllerClose(View controller) {
        setControllerRotation(controller, -90f);
    }

    @Override
    protected void setControllerOpen(View controller) {
        setControllerRotation(controller, 0f);
    }

    private void setControllerRotation(View controller, float degree) {
        ViewHelper.setRotation(controller, degree);
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

    @Override
    protected void controllerAnimationUpdate(View controller, ValueAnimator controllerAnimation) {
        setControllerRotation(controller, (float) controllerAnimation.getAnimatedValue());
    }

    @Override
    protected void layoutAnimationUpdate(View layout, ValueAnimator layoutAnimation) {
        setLayoutHeight(layout, (int) layoutAnimation.getAnimatedValue());
    }
}
