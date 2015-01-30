package com.harreke.easyapp.widgets.animators;

import android.graphics.PointF;
import android.view.View;

import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.ViewHelper;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/01/26
 */
public abstract class SlideAlphaAnimator extends SlideAnimator implements IAlphaAnimation {
    private float mCurrentAlpha = 0f;
    private float mEndAlpha = 0f;
    private float mStartAlpha = 0f;

    public SlideAlphaAnimator(View view) {
        super(view);
    }

    @Override
    protected ValueAnimator animateClose(View view) {
        mStartAlpha = getCurrentAlpha(view);
        mEndAlpha = getCloseAlpha(view);

        return super.animateClose(view);
    }

    @Override
    protected ValueAnimator animateOpen(View view) {
        mStartAlpha = getCurrentAlpha(view);
        mEndAlpha = getOpenAlpha(view);

        return super.animateOpen(view);
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
        super.animationUpdate(view, animator);
        setAlpha(view, mCurrentAlpha);
    }

    @Override
    public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
        mCurrentAlpha = mStartAlpha + fraction * (mEndAlpha - mStartAlpha);

        return super.evaluate(fraction, startValue, endValue);
    }

    private float getCurrentAlpha(View view) {
        return ViewHelper.getAlpha(view);
    }

    private void setAlpha(View view, float alpha) {
        ViewHelper.setAlpha(view, alpha);
    }

    @Override
    protected void setClose(View view) {
        super.setClose(view);
        setAlpha(view, getCloseAlpha(view));
    }

    @Override
    protected void setOpen(View view) {
        super.setOpen(view);
        setAlpha(view, getOpenAlpha(view));
    }
}