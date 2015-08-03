package com.harreke.easyapp.widgets.transitions;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.view.MotionEvent;

import com.harreke.easyapp.frameworks.base.ActivityFramework;
import com.harreke.easyapp.frameworks.base.ApplicationFramework;
import com.harreke.easyapp.widgets.animators.ViewAnimator;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.ValueAnimator;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/12/29
 */
@SuppressLint("ViewConstructor")
public class SwipeToFinishLayout extends TransitionLayout {
    private ViewAnimator mContentViewAnimator;
    private Animator.AnimatorListener mExitListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            onPostExit();
        }
    };
    private float mLastTouchX = 0f;
    private boolean mShouldIntercept = false;
    private float mSwipeOffset = 0f;
    private int mSwipeThreshold;
    private float mTouchDownThreshold = ApplicationFramework.Density * 16;
    private float mTouchThreshold = ApplicationFramework.TouchThreshold;
    private ValueAnimator.AnimatorUpdateListener mUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            setMask((float) animation.getAnimatedValue("x"));
        }
    };

    public SwipeToFinishLayout(ActivityFramework activity) {
        super(activity);

        setClickable(true);
        getContentView().post(new Runnable() {
            @Override
            public void run() {
                mContentViewAnimator = ViewAnimator.animate(getContentView());
                mContentViewAnimator.updateListener(mUpdateListener);
            }
        });
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
                        mContentViewAnimator.x(getContentWidth()).listener(mExitListener).play(true);
                    } else {
                        mSwipeOffset = 0;
                        mContentViewAnimator.x(0f).listener(null).play(true);
                    }

                    return true;
            }
        }

        return super.onTouchEvent(event);
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);

        if (hasWindowFocus) {
            mSwipeThreshold = getContentWidth() / 3;
        }
    }

    @Override
    protected void setContentX(float contentX) {
        super.setContentX(contentX);
        setMask(contentX);
    }

    private void setMask(float x) {
        setMaskAlpha(1 - x / getContentWidth());
    }
}