package com.harreke.easyapp.widgets.pullablelayout;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ScrollView;

import com.harreke.easyapp.frameworks.base.ApplicationFramework;
import com.harreke.easyapp.widgets.animators.ViewAnimator;
import com.harreke.easyapp.widgets.pullablelayout.viewdelegates.IViewDelegate;
import com.harreke.easyapp.widgets.pullablelayout.viewdelegates.RecyclerViewDelegate;
import com.harreke.easyapp.widgets.pullablelayout.viewdelegates.ScrollViewDelegate;
import com.harreke.easyapp.widgets.pullablelayout.viewdelegates.WebViewDelegate;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.view.ViewHelper;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/12/09
 */
public class PullableLayout extends FrameLayout implements ViewGroup.OnHierarchyChangeListener {
    public final static int ACTION_IDLE = 0;
    public final static int ACTION_LOAD = 2;
    public final static int ACTION_REFRESH = 1;
    private int mAction = ACTION_IDLE;
    private int mInterceptTouch = ACTION_IDLE;
    private float mLastTouchY = 0f;
    private int mLayoutHeight = 0;
    private ViewAnimator mLoadAnimator;
    private PullableIndicator mLoadIndicator;
    private int mLoadIndicatorHeight = 0;
    private int mLoadIndicatorThreshold = 0;
    private float mLoadOffset = 0f;
    private boolean mLoadable = false;
    private OnPullableListener mOnPullableListener = null;
    private Animator.AnimatorListener mLoadStartListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            if (mOnPullableListener != null) {
                mOnPullableListener.onPullToLoad();
            }
        }
    };
    private boolean mPullableEnabled = true;
    private ViewAnimator mRefreshAnimator;
    private PullableIndicator mRefreshIndicator;
    private int mRefreshIndicatorHeight = 0;
    private int mRefreshIndicatorThreshold = 0;
    private float mRefreshOffset = 0f;
    private Animator.AnimatorListener mIdleListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            if (mRefreshOffset == 0f) {
                mAction = ACTION_IDLE;
            }
        }
    };
    private Animator.AnimatorListener mRefreshStartListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            if (mOnPullableListener != null) {
                mOnPullableListener.onPullToRefresh();
            }
        }
    };
    private boolean mRefreshable = true;
    private float mTouchThreshold = ApplicationFramework.TouchThreshold;
    private IViewDelegate mViewDelegate = null;

    public PullableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        setOnHierarchyChangeListener(this);
    }

    public boolean isIdle() {
        return mAction == ACTION_IDLE;
    }

    public boolean isLoadable() {
        return mLoadable;
    }

    public boolean isLoading() {
        return mAction == ACTION_LOAD;
    }

    public boolean isRefreshable() {
        return mRefreshable;
    }

    public boolean isRefreshing() {
        return mAction == ACTION_REFRESH;
    }

    private boolean isScrollBottom() {
        return mViewDelegate.isScrollBottom();
    }

    private boolean isScrollTop() {
        return mViewDelegate.isScrollTop();
    }

    @Override
    public void onChildViewAdded(View parent, View child) {
        if (child instanceof RecyclerView) {
            mViewDelegate = new RecyclerViewDelegate((RecyclerView) child);
        } else if (child instanceof WebView) {
            mViewDelegate = new WebViewDelegate((WebView) child);
        } else if (child instanceof ScrollView) {
            mViewDelegate = new ScrollViewDelegate((ScrollView) child);
        }
    }

    @Override
    public void onChildViewRemoved(View parent, View child) {
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        FrameLayout.LayoutParams params;

        mRefreshIndicator = new PullableIndicator(getContext());
        params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
        mRefreshIndicator.setLayoutParams(params);
        mRefreshIndicator.setProgress(0f);
        mRefreshIndicator.setVisibility(INVISIBLE);
        addView(mRefreshIndicator);

        mLoadIndicator = new PullableIndicator(getContext());
        params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        mLoadIndicator.setLayoutParams(params);
        mLoadIndicator.setProgress(0f);
        mLoadIndicator.setVisibility(INVISIBLE);
        addView(mLoadIndicator);

        mRefreshAnimator = ViewAnimator.animate(mRefreshIndicator);
        mRefreshIndicator.post(new Runnable() {
            @Override
            public void run() {
                mRefreshIndicatorHeight = mRefreshIndicator.getMeasuredHeight();
                mRefreshIndicatorThreshold = (int) (mRefreshIndicatorHeight * 2f);
                setRefreshOffset(mRefreshOffset);
            }
        });
        mLoadAnimator = ViewAnimator.animate(mLoadIndicator);
        mLoadIndicator.post(new Runnable() {
            @Override
            public void run() {
                mLayoutHeight = mLoadIndicator.getBottom();
                mLoadIndicatorHeight = mLoadIndicator.getMeasuredHeight();
                mLoadIndicatorThreshold = (int) (mLoadIndicatorHeight * 2f);
                setLoadOffset(mLoadOffset);
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        float dy;

        if (mOnPullableListener != null && mPullableEnabled) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mLastTouchY = event.getY();
                    mInterceptTouch = ACTION_IDLE;
                    break;
                case MotionEvent.ACTION_MOVE:
                    dy = event.getY() - mLastTouchY;
                    if (shouldInterceptForRefresh(dy)) {
                        mInterceptTouch = ACTION_REFRESH;
                    } else if (shouldInterceptForLoad(dy)) {
                        mInterceptTouch = ACTION_LOAD;
                    }

                    return mInterceptTouch != ACTION_IDLE;
            }
        }

        return false;
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        float touchY;
        float dy;

        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                touchY = event.getY();
                dy = touchY - mLastTouchY;
                mLastTouchY = touchY;
                switch (mInterceptTouch) {
                    case ACTION_REFRESH:
                        if (mRefreshOffset > mRefreshIndicatorThreshold) {
                            dy *= mRefreshIndicatorThreshold / mRefreshOffset;
                        }
                        mRefreshOffset += dy;
                        if (mRefreshOffset < 0) {
                            mRefreshOffset = 0;
                        }
                        setRefreshOffset(mRefreshOffset);

                        return true;
                    case ACTION_LOAD:
                        if (mLoadOffset > mLoadIndicatorThreshold) {
                            dy *= mLoadIndicatorThreshold / mLoadOffset;
                        }
                        mLoadOffset -= dy;
                        if (mLoadOffset < 0) {
                            mLoadOffset = 0;
                        }
                        setLoadOffset(mLoadOffset);

                        return true;
                }
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                switch (mInterceptTouch) {
                    case ACTION_REFRESH:
                        mInterceptTouch = ACTION_IDLE;
                        if (mRefreshOffset > mRefreshIndicatorThreshold) {
                            setRefreshStart();
                        } else {
                            setRefreshJumpToIdle(0l);
                        }

                        return true;
                    case ACTION_LOAD:
                        mInterceptTouch = ACTION_IDLE;
                        if (mLoadOffset > mLoadIndicatorThreshold) {
                            setLoadStart();
                        } else {
                            setLoadJumpToIdle(0l);
                        }

                        return true;
                }
        }

        return super.onTouchEvent(event);
    }

    public void setLoadComplete() {
        mAction = ACTION_IDLE;
        mLoadIndicator.setProgress(0);
        setLoadJumpToIdle(600l);
    }

    private void setLoadJumpTo(float offset, long delay, Animator.AnimatorListener listener) {
        mLoadOffset = offset;
        mLoadAnimator.delay(delay).listener(listener);
        if (offset < mLoadIndicatorThreshold) {
            mLoadAnimator.y(mLayoutHeight).play(true);
        } else {
            mLoadAnimator.y(mLayoutHeight - mLoadIndicatorThreshold).play(true);
        }
    }

    private void setLoadJumpToIdle(long delay) {
        setLoadJumpTo(0f, delay, mIdleListener);
    }

    private void setLoadJumpToStart() {
        mLoadIndicator.setProgress(-1);
        setLoadJumpTo(mLoadIndicatorThreshold, 0l, mLoadStartListener);
    }

    private void setLoadOffset(float offset) {
        mLoadIndicator.setVisibility(VISIBLE);
        ViewHelper.setY(mLoadIndicator, mLayoutHeight - offset);
        if (offset >= mLoadIndicatorHeight) {
            mLoadIndicator.setProgress((offset - mLoadIndicatorHeight) / mLoadIndicatorHeight);
        }
    }

    public void setLoadStart() {
        if (isIdle()) {
            mAction = ACTION_REFRESH;
            setLoadJumpToStart();
        }
    }

    public void setLoadable(boolean loadable) {
        mLoadable = loadable;
    }

    public void setOnPullableListener(OnPullableListener onPullableListener) {
        mOnPullableListener = onPullableListener;
    }

    public void setPullableEnabled(boolean pullableEnabled) {
        mPullableEnabled = pullableEnabled;
    }

    public void setRefreshComplete() {
        mAction = ACTION_IDLE;
        mRefreshIndicator.setProgress(0);
        setRefreshJumpToIdle(600l);
    }

    private void setRefreshJumpTo(float offset, long delay, Animator.AnimatorListener listener) {
        mRefreshOffset = offset;
        mRefreshAnimator.delay(delay).listener(listener);
        if (offset < mRefreshIndicatorThreshold) {
            mRefreshAnimator.y(-mRefreshIndicatorHeight).play(true);
        } else {
            mRefreshAnimator.y(mRefreshIndicatorHeight).play(true);
        }
    }

    private void setRefreshJumpToIdle(long delay) {
        setRefreshJumpTo(0f, delay, mIdleListener);
    }

    private void setRefreshJumpToStart() {
        mRefreshIndicator.setProgress(-1);
        setRefreshJumpTo(mRefreshIndicatorThreshold, 0l, mRefreshStartListener);
    }

    private void setRefreshOffset(float offset) {
        mRefreshIndicator.setVisibility(VISIBLE);
        ViewHelper.setY(mRefreshIndicator, -mRefreshIndicatorHeight + offset);
        if (offset >= mRefreshIndicatorHeight) {
            mRefreshIndicator.setProgress((offset - mRefreshIndicatorHeight) / mRefreshIndicatorHeight);
        }
    }

    public void setRefreshStart() {
        if (isIdle()) {
            mAction = ACTION_REFRESH;
            mRefreshIndicator.post(new Runnable() {
                @Override
                public void run() {
                    mRefreshIndicatorHeight = mRefreshIndicator.getMeasuredHeight();
                    mRefreshIndicatorThreshold = (int) (mRefreshIndicatorHeight * 2f);
                    setRefreshJumpToStart();
                }
            });
        }
    }

    public void setRefreshable(boolean refreshable) {
        mRefreshable = refreshable;
    }

    private boolean shouldInterceptForLoad(float dy) {
        return isScrollBottom() && isLoadable() && isIdle() &&
                (dy < -mTouchThreshold || (dy > mTouchThreshold && mLoadOffset > 0f));
    }

    private boolean shouldInterceptForRefresh(float dy) {
        return isScrollTop() && isRefreshable() && isIdle() &&
                (dy > mTouchThreshold || (dy < -mTouchThreshold && mRefreshOffset > 0f));
    }
}