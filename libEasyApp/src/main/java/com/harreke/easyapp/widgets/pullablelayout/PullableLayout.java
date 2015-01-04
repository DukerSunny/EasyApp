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

import com.harreke.easyapp.R;
import com.harreke.easyapp.frameworks.bases.application.ApplicationFramework;
import com.harreke.easyapp.utils.ResourceUtil;
import com.harreke.easyapp.widgets.pullablelayout.viewdelegates.IViewDelegate;
import com.harreke.easyapp.widgets.pullablelayout.viewdelegates.RecyclerViewDelegate;
import com.harreke.easyapp.widgets.pullablelayout.viewdelegates.ScrollViewDelegate;
import com.harreke.easyapp.widgets.pullablelayout.viewdelegates.WebViewDelegate;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/12/09
 */
public class PullableLayout extends FrameLayout implements ViewGroup.OnHierarchyChangeListener {
    private boolean mCanLoad = true;
    private boolean mCanRefresh = true;
    private View mContentView;
    private ViewPropertyAnimator mContentViewAnimator;
    private boolean mFirstMeasure = true;
    private int mHeight = 0;
    private float mLastTouchY = 0f;
    private ViewPropertyAnimator mLoadAnimator;
    private PullableIndicator mLoadIndicator;
    private int mLoadIndicatorHeight = 0;
    private int mLoadIndicatorThreshold = 0;
    private float mLoadOffset = 0f;
    private boolean mLoading = false;
    private OnPullableListener mOnPullableListener = null;
    private Animator.AnimatorListener mLoadStartListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            if (mOnPullableListener != null) {
                mOnPullableListener.onPullToLoad();
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
    private boolean mPullableEnabled = true;
    private ViewPropertyAnimator mRefreshAnimator;
    private PullableIndicator mRefreshIndicator;
    private int mRefreshIndicatorHeight = 0;
    private int mRefreshIndicatorThreshold = 0;
    private float mRefreshOffset = 0f;
    private boolean mRefreshing = false;
    private float mTouchThreshold = ApplicationFramework.TouchThreshold;
    private IViewDelegate mViewDelegate = null;
    private String pullable_indicator_load_failure = null;
    private String pullable_indicator_load_success = null;
    private String pullable_indicator_loading = null;
    private String pullable_indicator_pulltoload = null;
    private String pullable_indicator_pulltorefresh = null;
    private String pullable_indicator_refresh_failure = null;
    private String pullable_indicator_refresh_success = null;
    private String pullable_indicator_refreshing = null;
    private String pullable_indicator_releasetoload = null;
    private String pullable_indicator_releasetorefresh = null;

    public PullableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutParams params;

        params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.TOP;
        mRefreshIndicator = new PullableIndicator(context);
        mRefreshIndicator.setLayoutParams(params);
        mRefreshIndicator.setProgress(0f);
        mRefreshIndicator.setToast(R.string.pullable_indicator_pulltorefresh);
        addView(mRefreshIndicator, params);

        params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.BOTTOM;
        mLoadIndicator = new PullableIndicator(context);
        mLoadIndicator.setLayoutParams(params);
        mLoadIndicator.setProgress(0f);
        mLoadIndicator.setToast(R.string.pullable_indicator_pulltoload);
        addView(mLoadIndicator, params);

        setOnHierarchyChangeListener(this);

        pullable_indicator_pulltorefresh = ResourceUtil.getString(context, R.string.pullable_indicator_pulltorefresh);
        pullable_indicator_pulltoload = ResourceUtil.getString(context, R.string.pullable_indicator_pulltoload);
        pullable_indicator_releasetorefresh = ResourceUtil.getString(context, R.string.pullable_indicator_releasetorefresh);
        pullable_indicator_releasetoload = ResourceUtil.getString(context, R.string.pullable_indicator_releasetoload);
        pullable_indicator_refresh_success = ResourceUtil.getString(context, R.string.pullable_indicator_refresh_success);
        pullable_indicator_refresh_failure = ResourceUtil.getString(context, R.string.pullable_indicator_refresh_failure);
        pullable_indicator_load_success = ResourceUtil.getString(context, R.string.pullable_indicator_load_success);
        pullable_indicator_load_failure = ResourceUtil.getString(context, R.string.pullable_indicator_load_failure);
        pullable_indicator_refreshing = ResourceUtil.getString(context, R.string.pullable_indicator_refreshing);
        pullable_indicator_loading = ResourceUtil.getString(context, R.string.pullable_indicator_loading);

        mRefreshAnimator = ViewPropertyAnimator.animate(mRefreshIndicator);
        mLoadAnimator = ViewPropertyAnimator.animate(mLoadIndicator);
    }

    public boolean canLoad() {
        return mCanLoad;
    }

    public boolean canRefresh() {
        return mCanRefresh;
    }

    public boolean isLoading() {
        return mLoading;
    }

    public boolean isRefreshing() {
        return mRefreshing;
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
            mContentView = child;
            mContentViewAnimator = ViewPropertyAnimator.animate(child);
        } else if (child instanceof WebView) {
            mViewDelegate = new WebViewDelegate((WebView) child);
            mContentView = child;
            mContentViewAnimator = ViewPropertyAnimator.animate(child);
        } else if (child instanceof ScrollView) {
            mViewDelegate = new ScrollViewDelegate((ScrollView) child);
            mContentView = child;
            mContentViewAnimator = ViewPropertyAnimator.animate(child);
        }
    }

    @Override
    public void onChildViewRemoved(View parent, View child) {
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        float dy;

        if (mOnPullableListener != null && mPullableEnabled) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mLastTouchY = event.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    dy = event.getY() - mLastTouchY;

                    return shouldInterceptForRefresh(dy) || shouldInterceptForLoad(dy);
            }
        }

        return false;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mHeight = getMeasuredHeight();

        mRefreshIndicatorHeight = mRefreshIndicator.getMeasuredHeight();
        mLoadIndicatorHeight = mLoadIndicator.getMeasuredHeight();

        mRefreshIndicatorThreshold = (int) (mRefreshIndicatorHeight * 2f);
        mLoadIndicatorThreshold = (int) (mLoadIndicatorHeight * 2f);

        if (mFirstMeasure) {
            mFirstMeasure = false;
            setRefreshOffset(mRefreshOffset);
            setLoadOffset(mLoadOffset);
        }
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

                if (isScrollTop()) {
                    if (mRefreshOffset > mRefreshIndicatorHeight) {
                        dy *= mRefreshIndicatorHeight / mRefreshOffset;
                    }
                    mRefreshOffset += dy;
                    if (mRefreshOffset < 0) {
                        mRefreshOffset = 0;
                    }
                    setContentOffset(mRefreshOffset);
                    setRefreshOffset(mRefreshOffset);

                    return true;
                } else if (isScrollBottom()) {
                    if (mLoadOffset > mLoadIndicatorHeight) {
                        dy *= mLoadIndicatorHeight / mLoadOffset;
                    }
                    mLoadOffset -= dy;
                    if (mLoadOffset < 0) {
                        mLoadOffset = 0;
                    }
                    setContentOffset(-mLoadOffset);
                    setLoadOffset(mLoadOffset);

                    return true;
                }
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (mRefreshOffset > 0) {
                    if (mRefreshOffset > mRefreshIndicatorThreshold) {
                        setRefreshStart();
                    } else {
                        setRefreshJumpToIdle(0l);
                    }

                    return true;
                } else if (mLoadOffset > 0) {
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

    public void setCanLoad(boolean canLoad) {
        mCanLoad = canLoad;
    }

    public void setCanRefresh(boolean canRefresh) {
        mCanRefresh = canRefresh;
    }

    private void setContentOffset(float offset) {
        ViewHelper.setY(mContentView, offset);
    }

    public void setLoadComplete(boolean success) {
        mLoading = false;
        if (success) {
            mLoadIndicator.setToast(pullable_indicator_load_success);
        } else {
            mLoadIndicator.setToast(pullable_indicator_load_failure);
        }
        mLoadIndicator.setProgress(0f);
        setLoadJumpToIdle(2500l);
    }

    private void setLoadJumpTo(float offset, long delay, Animator.AnimatorListener listener) {
        mLoadOffset = offset;
        mContentViewAnimator.y(-mLoadOffset).setDuration(300l).setStartDelay(delay).setListener(listener).start();
        if (offset < mLoadIndicatorHeight) {
            mLoadAnimator.y(mHeight).setDuration(300l).setStartDelay(delay).start();
        } else {
            mLoadAnimator.y(mHeight - mLoadIndicatorHeight).setDuration(300l).setStartDelay(delay).start();
        }
    }

    private void setLoadJumpToIdle(long delay) {
        setLoadJumpTo(0f, delay, null);
    }

    private void setLoadJumpToStart() {
        mLoadIndicator.setToast(pullable_indicator_loading);
        mLoadIndicator.setProgress(-1);
        setLoadJumpTo(mLoadIndicatorHeight, 0l, mLoadStartListener);
    }

    private void setLoadOffset(float offset) {
        if (offset < mLoadIndicatorHeight) {
            ViewHelper.setY(mLoadIndicator, mHeight - offset);
        } else {
            ViewHelper.setY(mLoadIndicator, mHeight);
        }
        if (offset > mLoadIndicatorHeight) {
            mLoadIndicator.setProgress((offset - mLoadIndicatorHeight) / mLoadIndicatorHeight);
            if (mLoadOffset > mLoadIndicatorThreshold) {
                mLoadIndicator.setToast(pullable_indicator_releasetoload);
            } else {
                mLoadIndicator.setToast(pullable_indicator_pulltoload);
            }
        } else {
            mLoadIndicator.setToast(pullable_indicator_pulltoload);
        }
    }

    public void setLoadStart() {
        if (canLoad() && !isLoading()) {
            mLoading = true;
            setLoadJumpToStart();
        }
    }

    public void setOnPullableListener(OnPullableListener onPullableListener) {
        mOnPullableListener = onPullableListener;
    }

    public void setPullableEnabled(boolean pullableEnabled) {
        mPullableEnabled = pullableEnabled;
    }

    public void setRefreshComplete(boolean success) {
        mRefreshing = false;
        if (success) {
            mRefreshIndicator.setToast(pullable_indicator_refresh_success);
        } else {
            mRefreshIndicator.setToast(pullable_indicator_refresh_failure);
        }
        mRefreshIndicator.setProgress(0);
        setRefreshJumpToIdle(2500l);
    }

    private void setRefreshJumpTo(float offset, long delay, Animator.AnimatorListener listener) {
        mRefreshOffset = offset;
        mContentViewAnimator.y(mRefreshOffset).setDuration(300l).setStartDelay(delay).setListener(listener).start();
        if (offset < mRefreshIndicatorHeight) {
            mRefreshAnimator.y(-mLoadIndicatorHeight).setDuration(300l).setStartDelay(delay).start();
        } else {
            mRefreshAnimator.y(0).setDuration(300l).setStartDelay(delay).start();
        }
    }

    private void setRefreshJumpToIdle(long delay) {
        setRefreshJumpTo(0f, delay, null);
    }

    private void setRefreshJumpToStart() {
        mRefreshIndicator.setToast(pullable_indicator_refreshing);
        mRefreshIndicator.setProgress(-1);
        setRefreshJumpTo(mRefreshIndicatorHeight, 0l, mRefreshStartListener);
    }

    private void setRefreshOffset(float offset) {
        if (offset < mRefreshIndicatorHeight) {
            ViewHelper.setY(mRefreshIndicator, -mRefreshIndicatorHeight + offset);
        } else {
            ViewHelper.setY(mRefreshIndicator, 0f);
        }
        if (offset > mRefreshIndicatorHeight) {
            mRefreshIndicator.setProgress((offset - mRefreshIndicatorHeight) / mRefreshIndicatorHeight);
            if (mRefreshOffset > mRefreshIndicatorThreshold) {
                mRefreshIndicator.setToast(pullable_indicator_releasetorefresh);
            } else {
                mRefreshIndicator.setToast(pullable_indicator_pulltorefresh);
            }
        } else {
            mRefreshIndicator.setToast(pullable_indicator_pulltorefresh);
        }
    }

    public void setRefreshStart() {
        if (canRefresh() && !mRefreshing) {
            mRefreshing = true;
            setRefreshJumpToStart();
        }
    }

    private boolean shouldInterceptForLoad(float dy) {
        return isScrollBottom() && canLoad() && !isLoading() && !isRefreshing() &&
                (dy < -mTouchThreshold || dy > mTouchThreshold && mLoadOffset > 0f);
    }

    private boolean shouldInterceptForRefresh(float dy) {
        return isScrollTop() && canRefresh() && !isRefreshing() && !isLoading() &&
                (dy > mTouchThreshold || (dy < -mTouchThreshold && mRefreshOffset > 0f));
    }

    public interface OnPullableListener {
        public void onPullToLoad();

        public void onPullToRefresh();
    }
}