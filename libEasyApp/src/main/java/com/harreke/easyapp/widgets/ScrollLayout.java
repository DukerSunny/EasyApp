package com.harreke.easyapp.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.harreke.easyapp.helpers.GestureHelper;
import com.harreke.easyapp.listeners.OnGestureListener;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/09/16
 *
 * 滑动布局
 *
 * 滑动布局是一个自定义{@link android.widget.LinearLayout}，可通过手势滑动内部的子视图
 */
public class ScrollLayout extends LinearLayout {
    private int mChildTotalHeight;
    private int mChildTotalWidth;
    private GestureHelper mGesture;
    private boolean mIntercept = false;
    private int mLayoutHeight;
    private int mLayoutWidth;
    private int mOffset = 0;
    private OnGestureListener mGestureListener = new OnGestureListener() {
        @Override
        public boolean onDown() {
            return false;
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
            if (getOrientation() == HORIZONTAL) {
                return scrollHorizontal(scrollX);
            } else {
                return scrollVertical(scrollY);
            }
        }

        @Override
        public void onTaps(float x, float y, int tapCount) {

        }

        @Override
        public boolean onTranslate(float translateX, float translateY, long duration) {
            return false;
        }

        @Override
        public boolean onUp() {
            return false;
        }

        private boolean scrollHorizontal(float scrollX) {
            int maxOffset = mChildTotalWidth - mLayoutWidth;

            if (mOffset >= 0 && mChildTotalWidth > mLayoutWidth && mOffset <= maxOffset) {
                mOffset += scrollX;
                if (mOffset < 0) {
                    mOffset = 0;
                } else if (mOffset > maxOffset) {
                    mOffset = maxOffset;
                }

                return true;
            } else {
                return false;
            }
        }

        private boolean scrollVertical(float scrollY) {
            int maxOffset = mChildTotalHeight - mLayoutHeight;

            if (mOffset >= 0 && mChildTotalHeight > mLayoutHeight && mOffset <= maxOffset) {
                mOffset += scrollY;
                if (mOffset < 0) {
                    mOffset = 0;
                } else if (mOffset > maxOffset) {
                    mOffset = maxOffset;
                }

                return true;
            } else {
                return false;
            }
        }
    };
    private boolean mScrollEnabled = false;

    public ScrollLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        mGesture = new GestureHelper(context);
        mGesture.setOnGestureListener(mGestureListener);
    }

    private void layoutHorizontal(int l, int t, int r, int b) {
        View child;
        int width;
        int i;

        width = mOffset;
        for (i = 0; i < getChildCount(); i++) {
            child = getChildAt(i);
            child.layout(l + width, t, r + width, b);
            width += child.getMeasuredWidth();
        }
    }

    private void layoutVertical(int l, int t, int r, int b) {
        View child;
        int width;
        int i;

        width = mOffset;
        for (i = 0; i < getChildCount(); i++) {
            child = getChildAt(i);
            child.layout(l, t + width, r, b + width);
            width += child.getMeasuredWidth();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mScrollEnabled) {
            mIntercept = mGesture.onTouch(ev);

            return mIntercept;
        } else {
            return false;
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed) {
            if (getOrientation() == HORIZONTAL) {
                layoutHorizontal(l, t, r, b);
            } else {
                layoutVertical(l, t, r, b);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        View child;
        int i;
        //
        //        mLayoutWidth = MeasureSpec.getSize(widthMeasureSpec);
        //        mLayoutHeight = MeasureSpec.getSize(heightMeasureSpec);
        //        setMeasuredDimension(mLayoutWidth, mLayoutHeight);
        //
        mLayoutWidth = getMeasuredWidth();
        mLayoutHeight = getMeasuredHeight();
        mChildTotalWidth = 0;
        mChildTotalHeight = 0;
        for (i = 0; i < getChildCount(); i++) {
            child = getChildAt(i);
            mChildTotalWidth += child.getMeasuredWidth();
            mChildTotalHeight += child.getMeasuredHeight();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mScrollEnabled && !mIntercept || mGesture.onTouch(event);
    }
    //
    //    /**
    //     * 设置是否允许循环展示
    //     *
    //     * 只有在子视图总宽度超过该布局宽度，并且设置允许滑动时，该选项才有意义
    //     *
    //     * @param loopEnabled
    //     *         是否允许循环展示
    //     *
    //     *         如果为true，则从一边滑出的子视图会从另一边重新滑入；
    //     *         如果为false，则布局整体滑动到最开始或最末尾时，会停止滑动
    //     */
    //    public final void setLoopEnabled(boolean loopEnabled) {
    //        mLoopEnabled = loopEnabled;
    //    }

    /**
     * 设置是否允许滑动
     *
     * 只有在子视图总宽度超过该布局宽度时，该选项才有意义
     *
     * @param scrollEnabled
     *         是否允许滑动
     *
     *         如果为true，则可通过手势左右（布局方向为{@link #HORIZONTAL}）或上下（布局方向为{@link #VERTICAL}）滑动布局中子视图的位置；
     *         如果为false，则布局不响应手势动作
     */
    public final void setScrollEnabled(boolean scrollEnabled) {
        mScrollEnabled = scrollEnabled;
    }
}