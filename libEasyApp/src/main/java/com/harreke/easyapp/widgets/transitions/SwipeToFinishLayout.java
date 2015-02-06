package com.harreke.easyapp.widgets.transitions;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.MotionEvent;

import com.harreke.easyapp.frameworks.base.ApplicationFramework;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.ValueAnimator;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/12/29
 */
public class SwipeToFinishLayout extends TransitionLayout {
    private Animator.AnimatorListener mExitListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            onExit();
        }
    };
    private float mLastTouchX = 0f;
    private boolean mShouldIntercept = false;
    private float mSwipeOffset = 0f;
    private int mSwipeThreshold;
    private float mTouchDownThreshold = ApplicationFramework.Density * 16;
    private float mTouchThreshold = ApplicationFramework.TouchThreshold;

    public SwipeToFinishLayout(Context context) {
        super(context);

        setClickable(true);
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        setBackgroundColor(Color.argb((int) (192f - 192f * animation.getAnimatedFraction()), 0, 0, 0));
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        float dx;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastTouchX = event.getX();
                mShouldIntercept = (mLastTouchX <= mTouchDownThreshold);
                break;
            case MotionEvent.ACTION_MOVE:
                if (mShouldIntercept) {
                    dx = event.getX() - mLastTouchX;

                    return dx > mTouchThreshold || (dx < -mTouchThreshold && mSwipeOffset > 0);
                }
        }

        return false;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mSwipeThreshold = getContentWidth() / 3;
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        float touchX;
        float dx;

        if (mShouldIntercept) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_MOVE:
                    touchX = event.getX();
                    dx = touchX - mLastTouchX;
                    mLastTouchX = touchX;

                    mSwipeOffset += dx;
                    if (mSwipeOffset < 0f) {
                        mSwipeOffset = 0f;
                    } else if (mSwipeOffset > getContentWidth()) {
                        mSwipeOffset = getContentWidth();
                    }
                    setContentX(mSwipeOffset);

                    return true;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    if (mSwipeOffset > mSwipeThreshold) {
                        mSwipeOffset = 0;
                        getContentAnimator().x(getContentWidth()).listener(mExitListener).start(true);
                    } else {
                        mSwipeOffset = 0;
                        getContentAnimator().x(0f).start(true);
                    }

                    return true;
            }
        }

        return super.onTouchEvent(event);
    }

    @Override
    protected void setContentX(float contentX) {
        super.setContentX(contentX);
        setBackgroundColor(Color.argb((int) (192f - 192f * contentX / getContentWidth()), 0, 0, 0));
    }
}