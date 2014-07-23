package com.harreke.utils.widgets;

import android.content.Context;
import android.graphics.PointF;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class ChildViewPager extends ViewPager {
    private PointF mNowPoint = new PointF();
    private PointF mStartPoint = new PointF();

    public ChildViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
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
            if (Math.abs(mStartPoint.x - mNowPoint.x) < 20 && Math.abs(mStartPoint.y - mNowPoint.y) > 20) {
                getParent().requestDisallowInterceptTouchEvent(false);
            } else {
                getParent().requestDisallowInterceptTouchEvent(true);
            }
        }
        return super.onTouchEvent(event);
    }
}