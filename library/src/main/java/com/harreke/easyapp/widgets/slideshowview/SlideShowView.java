package com.harreke.easyapp.widgets.slideshowview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.harreke.easyapp.helpers.GestureHelper;
import com.harreke.easyapp.listeners.OnGestureListener;

import java.util.ArrayList;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/08/07
 *
 * 未完成
 */
public class SlideShowView extends ViewGroup implements OnGestureListener {
    private GestureHelper mGesture;
    private float mOffset = 0;
    private int mPageLimit = 2;
    private int mPosition = 0;
    private float mStartOffset = 0;
    private ArrayList<Integer> mViewIndexList = new ArrayList<Integer>();
    private ArrayList<View> mViewList = new ArrayList<View>();
    private ArrayList<Integer> mViewWidthList = new ArrayList<Integer>();

    public SlideShowView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mGesture = new GestureHelper(context);
        mGesture.setOnGestureListener(this);
    }

    public final void addSlideShow(View view) {
        mViewList.add(view);
        mViewWidthList.add(0);
        mViewIndexList.add(0);
    }

    @Override
    public boolean onDown() {
        mStartOffset = mOffset;

        return true;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        View child;
        int size = getChildCount();
        int left;
        int right;
        int leftWidth;
        int rightWidth;
        int i;

        //        if (size > 0) {
        //            left = mPosition - 1;
        //            if (left < 0) {
        //                left = size - 1;
        //                leftWidth = getChildAt
        //            }
        //            right = mPosition + 1;
        //            if (right == size) {
        //                right = 0;
        //            }
        //            leftWidth = 0;
        //            for (i = target - 1; i > -1; i--) {
        //                child = getChildAt(i);
        //                leftWidth += child.getMeasuredWidth();
        //                child.layout((int) (l + mOffset - leftWidth), t, (int) (r + mOffset - leftWidth), b);
        //            }
        //            rightWidth = 0;
        //            for (i = target; i < size; i++) {
        //                child = getChildAt(i);
        //                child.layout((int) (l + mOffset + rightWidth), t, (int) (r + mOffset + rightWidth), b);
        //                rightWidth += child.getMeasuredWidth();
        //            }
        //        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        View child;
        int width;
        int height;
        int i;

        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, height);
        for (i = 0; i < getChildCount(); i++) {
            child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            mViewWidthList.set(mViewIndexList.get(i), child.getMeasuredWidth());
        }
    }

    @Override
    public boolean onPointerDown() {
        return false;
    }

    @Override
    public boolean onPointerUp() {
        return false;
    }

    @Override
    public boolean onScale(float scale, float scaleX, float scaleY, long duration) {
        return false;
    }

    @Override
    public boolean onScroll(float scrollX, float scrollY, long duration) {
        if (Math.abs(scrollX) > mGesture.getThreshold()) {
            mOffset = mStartOffset + scrollX;

            requestLayout();

            return true;
        }

        return false;
    }

    @Override
    public void onTaps(float x, float y, int tapCount) {
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGesture.onTouch(event);
    }

    @Override
    public boolean onTranslate(float translateX, float translateY, long duration) {
        return false;
    }

    @Override
    public boolean onUp() {
        return true;
    }

    public final void refresh() {
        int size;
        int i;

        removeAllViews();
        if (mViewList.size() > mPageLimit) {
            size = mPageLimit;
        } else {
            size = mViewList.size();
        }
        for (i = 0; i < size; i++) {
            addView(mViewList.get(mPosition + i));
        }
        invalidate();
    }
}