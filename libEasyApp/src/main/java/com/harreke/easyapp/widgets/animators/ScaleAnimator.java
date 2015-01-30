package com.harreke.easyapp.widgets.animators;

import android.graphics.PointF;
import android.view.View;

import com.nineoldandroids.animation.TypeEvaluator;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.ViewHelper;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/01/26
 */
public abstract class ScaleAnimator extends ToggleAnimator implements TypeEvaluator<PointF>, IScaleAnimation {
    private PointF mCloseScale = new PointF(0f, 0f);
    private PointF mCurrentScale = new PointF(0f, 0f);
    private PointF mOpenScale = new PointF(0f, 0f);

    public ScaleAnimator(View view) {
        super(view);
    }

    @Override
    protected ValueAnimator animateClose(View view) {
        return ValueAnimator.ofObject(this, getCurrentScale(view), getCloseScale(view)).setDuration(300l);
    }

    @Override
    protected ValueAnimator animateOpen(View view) {
        return ValueAnimator.ofObject(this, getCurrentScale(view), getOpenScale(view)).setDuration(300l);
    }

    @Override
    protected void animationUpdate(View view, ValueAnimator animator) {
        mCurrentScale.set((PointF) animator.getAnimatedValue());
        setScale(view, mCurrentScale);
    }

    @Override
    public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
        return new PointF(startValue.x + fraction * (endValue.x - startValue.x),
                startValue.y + fraction * (endValue.y - startValue.y));
    }

    protected PointF getCloseScale(View view) {
        mCloseScale.set(getCloseScaleX(view), getCloseScaleY(view));

        return mCloseScale;
    }

    protected PointF getCurrentScale(View view) {
        mCurrentScale.set(ViewHelper.getScaleX(view), ViewHelper.getScaleY(view));

        return mCurrentScale;
    }

    protected PointF getOpenScale(View view) {
        mOpenScale.set(getOpenScaleX(view), getOpenScaleY(view));

        return mOpenScale;
    }

    @Override
    protected void setClose(View view) {
        setScale(view, getCloseScale(view));
    }

    @Override
    protected void setOpen(View view) {
        setScale(view, getOpenScale(view));
    }

    private void setScale(View view, PointF coordinate) {
        ViewHelper.setScaleX(view, coordinate.x);
        ViewHelper.setScaleY(view, coordinate.y);
    }
}