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

import com.harreke.easyapp.frameworks.bases.application.ApplicationFramework;
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
    private final static long DURATION_TOAST = 2500;
    private boolean mCanLoad = true;
    private boolean mCanRefresh = true;
    private boolean mEnabled = true;
    private boolean mFirstMeasure = true;
    private int mHeight = 0;
    private float mLastTouchY = 0f;
    private Runnable mLoadCompleteRunnable = new Runnable() {
        @Override
        public void run() {
            mLoadIndicator.setProgress(0);
            loadJumpToIdle();
        }
    };
    private Runnable mLoadIndicatorCollapseRunnable = new Runnable() {
        @Override
        public void run() {
            mLoadIndicator.collapse();
            postDelayed(mLoadCompleteRunnable, PullableIndicator.DURATION_MORPH);
        }
    };
    private PullableIndicator mLoadIndicator;
    private ViewPropertyAnimator mLoadIndicatorAnimator;
    private int mLoadIndicatorHeight = 0;
    private float mLoadIndicatorOffset = 0f;
    private int mLoadThreshold = 0;
    private boolean mLoading = false;
    private OnPullableListener mOnPullableListener = null;
    private Runnable mRefreshCompleteRunnable = new Runnable() {
        @Override
        public void run() {
            mRefreshIndicator.setProgress(0);
            refreshJumpToIdle();
        }
    };
    private Runnable mRefreshIndicatorCollapseRunnable = new Runnable() {
        @Override
        public void run() {
            mRefreshIndicator.collapse();
            postDelayed(mRefreshCompleteRunnable, PullableIndicator.DURATION_MORPH);
        }
    };
    private PullableIndicator mRefreshIndicator;
    private ViewPropertyAnimator mRefreshIndicatorAnimator;
    private int mRefreshIndicatorHeight = 0;
    private float mRefreshIndicatorOffset = 0f;
    private int mRefreshThreshold = 0;
    private boolean mRefreshing = false;
    private float mTouchThreshold = ApplicationFramework.Density * 8;
    private IViewDelegate mViewDelegate = null;

    public PullableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        setOnHierarchyChangeListener(this);
    }

    public boolean canLoad() {
        return mCanLoad;
    }

    public boolean canRefresh() {
        return mCanRefresh;
    }

    private float computeLoadY() {
        return mHeight - mLoadIndicatorOffset;
    }

    private float computeLoadYProgress() {
        return (mLoadIndicatorOffset - mLoadIndicatorHeight) / mLoadIndicatorHeight;
    }

    private float computeRefreshY() {
        return mRefreshIndicatorOffset - mRefreshIndicatorHeight;
    }

    private float computeRefreshYProgress() {
        return (mRefreshIndicatorOffset - mRefreshIndicatorHeight) / mRefreshIndicatorHeight;
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

    private void loadJumpTo(float offset) {
        mLoadIndicatorOffset = offset;
        mLoadIndicatorAnimator.y(computeLoadY()).start();
    }

    private void loadJumpToIdle() {
        loadJumpTo(0);
    }

    private void loadJumpToStart() {
        loadJumpTo(mLoadIndicatorHeight);
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
        Context context;
        LayoutParams params;

        if (mViewDelegate == null) {
            throw new IllegalArgumentException("Cannot find a pullable view!");
        } else {
            context = getContext();

            params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.CENTER_HORIZONTAL;
            mRefreshIndicator = new PullableIndicator(context);
            mRefreshIndicator.setLayoutParams(params);
            mRefreshIndicatorAnimator =
                    ViewPropertyAnimator.animate(mRefreshIndicator).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            if (mRefreshIndicatorOffset == 0) {
                                mRefreshing = false;
                            }
                        }
                    });
            addView(mRefreshIndicator);

            params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.CENTER_HORIZONTAL;
            mLoadIndicator = new PullableIndicator(context);
            mLoadIndicator.setLayoutParams(params);
            mLoadIndicatorAnimator = ViewPropertyAnimator.animate(mLoadIndicator).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    if (mLoadIndicatorOffset == 0) {
                        mLoading = false;
                    }
                }
            });
            addView(mLoadIndicator);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        float touchY;
        float dy;

        if (mOnPullableListener != null && mEnabled) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mLastTouchY = event.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    touchY = event.getY();
                    dy = touchY - mLastTouchY;

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
        mRefreshThreshold = mRefreshIndicatorHeight * 2;
        mLoadIndicatorHeight = mLoadIndicator.getMeasuredHeight();
        mLoadThreshold = mLoadIndicatorHeight * 2;
        if (mFirstMeasure) {
            mFirstMeasure = false;
            mRefreshIndicatorOffset = 0f;
            ViewHelper.setY(mRefreshIndicator, computeRefreshY());
            mLoadIndicatorOffset = 0f;
            ViewHelper.setY(mLoadIndicator, computeLoadY());
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
                    if (mRefreshIndicatorOffset > mRefreshIndicatorHeight) {
                        dy *= mRefreshIndicatorHeight / mRefreshIndicatorOffset;
                    }
                    mRefreshIndicatorOffset += dy;
                    if (mRefreshIndicatorOffset < 0) {
                        mRefreshIndicatorOffset = 0;
                    }
                    ViewHelper.setY(mRefreshIndicator, computeRefreshY());
                    if (mRefreshIndicatorOffset > mRefreshIndicatorHeight) {
                        mRefreshIndicator.setProgress(computeRefreshYProgress());
                    }

                    return true;
                } else if (isScrollBottom()) {
                    if (mLoadIndicatorOffset > mLoadIndicatorHeight) {
                        dy *= mLoadIndicatorHeight / mLoadIndicatorOffset;
                    }
                    mLoadIndicatorOffset -= dy;
                    if (mLoadIndicatorOffset < 0) {
                        mLoadIndicatorOffset = 0;
                    }
                    ViewHelper.setY(mLoadIndicator, computeLoadY());
                    if (mLoadIndicatorOffset > mLoadIndicatorHeight) {
                        mLoadIndicator.setProgress(computeLoadYProgress());
                    }

                    return true;
                }
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (mRefreshIndicatorOffset > 0) {
                    if (mRefreshIndicatorOffset > mRefreshThreshold) {
                        setRefreshStart();
                    } else {
                        refreshJumpToIdle();
                    }

                    return true;
                } else if (mLoadIndicatorOffset > 0) {
                    if (mLoadIndicatorOffset > mLoadThreshold) {
                        setLoadStart();
                    } else {
                        loadJumpToIdle();
                    }
                }
        }

        return super.onTouchEvent(event);
    }

    private void refreshJumpTo(float offset) {
        mRefreshIndicatorOffset = offset;
        mRefreshIndicatorAnimator.y(-mRefreshIndicatorHeight + mRefreshIndicatorOffset).start();
    }

    private void refreshJumpToIdle() {
        refreshJumpTo(0);
    }

    private void refreshJumpToStart() {
        refreshJumpTo(mRefreshIndicatorHeight);
    }

    public void setCanLoad(boolean canLoad) {
        mCanLoad = canLoad;
    }

    public void setCanRefresh(boolean canRefresh) {
        mCanRefresh = canRefresh;
    }

    public void setEnabled(boolean enabled) {
        mEnabled = enabled;
    }

    public void setLoadComplete() {
        setLoadComplete(null);
    }

    public void setLoadComplete(String toast) {
        if (toast == null) {
            mLoadCompleteRunnable.run();
        } else {
            mLoadIndicator.expand(toast);
            postDelayed(mLoadIndicatorCollapseRunnable, DURATION_TOAST + PullableIndicator.DURATION_MORPH);
        }
    }

    public void setLoadStart() {
        if (canLoad() && !isLoading()) {
            mLoading = true;
            mLoadIndicator.setProgress(-1);
            loadJumpToStart();
            mOnPullableListener.onPullToLoad();
        }
    }

    public void setOnPullableListener(OnPullableListener onPullableListener) {
        mOnPullableListener = onPullableListener;
    }

    public void setRefreshComplete(String toast) {
        if (toast == null) {
            mRefreshCompleteRunnable.run();
        } else {
            mRefreshIndicator.expand(toast);
            postDelayed(mRefreshIndicatorCollapseRunnable, DURATION_TOAST + PullableIndicator.DURATION_MORPH);
        }
    }

    public void setRefreshComplete() {
        mRefreshIndicator.setProgress(0);
        refreshJumpToIdle();
    }

    public void setRefreshStart() {
        if (canRefresh() && !mRefreshing) {
            mRefreshing = true;
            mRefreshIndicator.setProgress(-1);
            mRefreshIndicatorAnimator.cancel();
            refreshJumpToStart();
            mOnPullableListener.onPullToRefresh();
        }
    }

    private boolean shouldInterceptForLoad(float dy) {
        return isScrollBottom() && canLoad() && !isLoading() && !isRefreshing() &&
                (dy < -mTouchThreshold || dy > mTouchThreshold && mLoadIndicatorOffset > 0);
    }

    private boolean shouldInterceptForRefresh(float dy) {
        return isScrollTop() && canRefresh() && !isRefreshing() && !isLoading() &&
                (dy > mTouchThreshold || (dy < -mTouchThreshold && mRefreshIndicatorOffset > 0));
    }

    public interface OnPullableListener {
        public void onPullToLoad();

        public void onPullToRefresh();
    }
}