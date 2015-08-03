package com.harreke.easyapp.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.harreke.easyapp.R;
import com.harreke.easyapp.widgets.animators.ViewAnimatorUtil;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ValueAnimator;

/**
 * Created by 启圣 on 2015/6/16.
 */
public class SlidingPanelLayout extends FrameLayout implements ValueAnimator.AnimatorUpdateListener {
    private int mDefaultOffset = -1;
    private float mLastY = 0;
    private int mOffset = 0;
    private OnSlidingListener mOnSlidingListener = null;
    private View mPanelAbove = null;
    private int mPanelAboveMargin = 0;
    private View mViewBelow = null;
    private int mViewBelowMaxHeight = 0;
    private int mViewBelowMinHeight = 0;

    public SlidingPanelLayout(Context context) {
        this(context, null);
    }

    public SlidingPanelLayout(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.slidingPanelLayoutStyle);
    }

    public SlidingPanelLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray style;

        style = context.obtainStyledAttributes(attrs, R.styleable.SlidingPanelLayout, defStyle, 0);
        mPanelAboveMargin = (int) style.getDimension(R.styleable.SlidingPanelLayout_panelMargin, 0);
        style.recycle();
    }

    public int getDefaultScrollHeight() {
        return mDefaultOffset;
    }

    public int getScrollHeight() {
        return mOffset;
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        setOffset((int) animation.getAnimatedValue());
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        if (getChildCount() >= 2) {
            mViewBelow = getChildAt(0);
            mPanelAbove = getChildAt(1);
            mPanelAbove.setClickable(true);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        float nowY = ev.getY();

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = nowY;
                break;
            case MotionEvent.ACTION_MOVE:
                if (nowY > mLastY && mOffset < mViewBelowMaxHeight || nowY < mLastY && mOffset > mViewBelowMinHeight) {
                    mLastY = nowY;

                    return true;
                } else {
                    mLastY = nowY;
                }
        }

        return super.onInterceptTouchEvent(ev);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (mDefaultOffset == -1) {
            mDefaultOffset = mViewBelow.getMeasuredHeight();
            mViewBelowMaxHeight = mDefaultOffset;
            setOffset(mDefaultOffset);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float nowY = event.getY();
        float distance;
        int offset = mOffset;

        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                distance = nowY - mLastY;
                mLastY = nowY;
                offset += distance;
                if (offset < mViewBelowMinHeight) {
                    offset = mViewBelowMinHeight;
                } else if (offset > mViewBelowMaxHeight) {
                    offset = mViewBelowMaxHeight;
                }
                setOffset(offset);

                return true;
        }

        return super.onTouchEvent(event);
    }

    public void reset(Animator.AnimatorListener listener) {
        ValueAnimator animator = ValueAnimator.ofInt(mOffset, mDefaultOffset);

        animator.addUpdateListener(this);
        animator.addListener(listener);
        animator.start();
    }

    private void setOffset(int offset) {
        mOffset = offset;
        ViewAnimatorUtil.setHeight(mViewBelow, mOffset);
        ViewAnimatorUtil.setY(mPanelAbove, mOffset + mPanelAboveMargin);
        if (mOnSlidingListener != null) {
            mOnSlidingListener.onSliding(mOffset, mDefaultOffset);
        }
    }

    public void setOnSlidingListener(OnSlidingListener onSlidingListener) {
        mOnSlidingListener = onSlidingListener;
    }

    public void setPanelAboveMargin(int panelAboveMargin) {
        if (mPanelAboveMargin != panelAboveMargin) {
            mPanelAboveMargin = panelAboveMargin;
            setOffset(mOffset);
        } else {
            mPanelAboveMargin = panelAboveMargin;
        }
    }

    public void setViewBelowMinHeight(int minHeight) {
        mViewBelowMinHeight = minHeight;
    }

    public interface OnSlidingListener {
        void onSliding(int offset, int defaultOffset);
    }
}