package com.harreke.easyapp.widgets;

import android.content.Context;
import android.graphics.PointF;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 *
 * 子ViewPager
 *
 * 用以解决ViewPager嵌套ViewPager时，其中的子ViewPager滑动不正常的问题
 */
public class ChildViewPager extends ViewPager {
    private PointF mNowPoint = new PointF();
    private PointF mStartPoint = new PointF();
    private float mThreshold;

    public ChildViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);

        mThreshold = context.getResources().getDisplayMetrics().density * 8;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mNowPoint.x = event.getX();
        mNowPoint.y = event.getY();
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            mStartPoint.x = event.getX();
            mStartPoint.y = event.getY();
        }
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            if (Math.abs(mStartPoint.x - mNowPoint.x) < mThreshold && Math.abs(mStartPoint.y - mNowPoint.y) > mThreshold) {
                getParent().requestDisallowInterceptTouchEvent(false);
            } else {
                getParent().requestDisallowInterceptTouchEvent(true);
            }
        }
        return super.onTouchEvent(event);
    }
}