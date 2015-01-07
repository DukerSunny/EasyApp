package com.harreke.easyapp.widgets.transitions;

import android.app.Activity;
import android.view.MotionEvent;

import com.harreke.easyapp.frameworks.bases.application.ApplicationFramework;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/12/29
 */
public class SwipeToFinishLayout extends TransitionLayout {
    private float mLastTouchX = 0f;
    private boolean mShouldIntercept = false;
    private float mSwipeOffset = 0f;
    private int mSwipeThreshold;
    private float mTouchDownThreshold = ApplicationFramework.Density * 16;
    private float mTouchThreshold = ApplicationFramework.TouchThreshold;

    public SwipeToFinishLayout(Activity activity) {
        super(activity);

        setClickable(true);
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
    public boolean onTouchEvent(MotionEvent event) {
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
                    setContentOffsetX(mSwipeOffset);

                    return true;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    if (mSwipeOffset > mSwipeThreshold) {
                        mSwipeOffset = 0;
                        startExitTransition(ExitTransition.Slide_Out_Right);
                    } else {
                        mSwipeOffset = 0;
                        animateToX(0f, null);
                    }

                    return true;
            }
        }

        return super.onTouchEvent(event);
    }
}