package com.harreke.easyapp.widgets.animators;

import android.graphics.PointF;
import android.view.View;

import com.nineoldandroids.animation.TypeEvaluator;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.ViewHelper;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/01/26
 */
public abstract class SlideAnimator extends ToggleAnimator implements TypeEvaluator<PointF>, ISlideAnimation {
    private PointF mCloseCoordinate = new PointF(0f, 0f);
    private PointF mCurrentCoordinate = new PointF(0f, 0f);
    private PointF mOpenCoordinate = new PointF(0f, 0f);

    public SlideAnimator(View view) {
        super(view);
    }

    @Override
    protected ValueAnimator animateClose(View view) {
        return ValueAnimator.ofObject(this, getCurrentCoordinate(view), getCloseCoordinate(view)).setDuration(300l);
    }

    @Override
    protected ValueAnimator animateOpen(View view) {
        return ValueAnimator.ofObject(this, getCurrentCoordinate(view), getOpenCoordinate(view)).setDuration(300l);
    }

    @Override
    protected void animationUpdate(View view, ValueAnimator animator) {
        mCurrentCoordinate.set((PointF) animator.getAnimatedValue());
        setCoordinate(view, mCurrentCoordinate);
    }

    @Override
    public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
        return new PointF(startValue.x + fraction * (endValue.x - startValue.x),
                startValue.y + fraction * (endValue.y - startValue.y));
    }

    protected PointF getCloseCoordinate(View view) {
        mCloseCoordinate.set(getCloseX(view), getCloseY(view));

        return mCloseCoordinate;
    }

    protected PointF getCurrentCoordinate(View view) {
        mCurrentCoordinate.set(ViewHelper.getX(view), ViewHelper.getY(view));

        return mCurrentCoordinate;
    }

    protected PointF getOpenCoordinate(View view) {
        mOpenCoordinate.set(getOpenX(view), getOpenY(view));

        return mOpenCoordinate;
    }

    @Override
    protected void setClose(View view) {
        setCoordinate(view, getCloseCoordinate(view));
    }

    private void setCoordinate(View view, PointF coordinate) {
        ViewHelper.setX(view, coordinate.x);
        ViewHelper.setY(view, coordinate.y);
    }

    @Override
    protected void setOpen(View view) {
        setCoordinate(view, getOpenCoordinate(view));
    }
}
