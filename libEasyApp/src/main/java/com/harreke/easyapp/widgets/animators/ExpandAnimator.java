package com.harreke.easyapp.widgets.animators;

import android.graphics.Point;
import android.view.View;
import android.view.ViewGroup;

import com.nineoldandroids.animation.TypeEvaluator;
import com.nineoldandroids.animation.ValueAnimator;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/01/26
 */
public abstract class ExpandAnimator extends ToggleAnimator implements TypeEvaluator<Point>, IExpandAnimation {
    private Point mCloseSize = new Point(0, 0);
    private Point mCurrentSize = new Point(0, 0);
    private Point mOpenSize = new Point(0, 0);

    public ExpandAnimator(View view) {
        super(view);
    }

    @Override
    protected ValueAnimator animateClose(View view) {
        return ValueAnimator.ofObject(this, getCurrentSize(view), getCloseSize(view)).setDuration(300l);
    }

    @Override
    protected ValueAnimator animateOpen(View view) {
        return ValueAnimator.ofObject(this, getCurrentSize(view), getOpenSize(view)).setDuration(300l);
    }

    @Override
    protected void animationUpdate(View view, ValueAnimator animator) {
        Point point = (Point) animator.getAnimatedValue();

        mCurrentSize.set(point.x, point.y);
        setSize(view, mCurrentSize);
    }

    @Override
    public Point evaluate(float fraction, Point startValue, Point endValue) {
        return new Point((int) (startValue.x + fraction * (endValue.x - startValue.x)),
                (int) (startValue.y + fraction * (endValue.y - startValue.y)));
    }

    protected Point getCloseSize(View view) {
        mCloseSize.set(getCloseWidth(view), getCloseHeight(view));

        return mCloseSize;
    }

    protected Point getCurrentSize(View view) {
        mCurrentSize.set(view.getLayoutParams().width, view.getLayoutParams().height);

        return mCurrentSize;
    }

    protected Point getOpenSize(View view) {
        mOpenSize.set(getOpenWidth(view), getOpenHeight(view));

        return mOpenSize;
    }

    @Override
    protected void setClose(View view) {
        setSize(view, getCloseSize(view));
    }

    @Override
    protected void setOpen(View view) {
        setSize(view, getOpenSize(view));
    }

    private void setSize(View view, Point size) {
        ViewGroup.LayoutParams params = view.getLayoutParams();

        params.width = size.x;
        params.height = size.y;
        view.setLayoutParams(params);
    }
}