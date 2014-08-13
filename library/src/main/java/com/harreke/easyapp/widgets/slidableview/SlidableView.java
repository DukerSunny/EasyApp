package com.harreke.easyapp.widgets.slidableview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.harreke.easyapp.widgets.slidableview.inners.HeaderView;
import com.harreke.easyapp.widgets.slidableview.inners.OverlayView;
import com.harreke.easyapp.R;
import com.harreke.easyapp.helpers.GestureHelper;
import com.harreke.easyapp.listeners.OnGestureListener;
import com.harreke.easyapp.listeners.OnSlidableTriggerListener;
import com.harreke.easyapp.widgets.slidableview.inners.ContentView;
import com.harreke.easyapp.widgets.slidableview.inners.TabView;

import java.util.ArrayList;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/29
 *
 * 滑动视图
 *
 * 滑动视图是一个可上下滑动的复合视图，包含有Header层、Tab层、Overlay层和Content层
 * Header层存放页眉，默认隐藏；
 * Tab层存放标签，默认显示；
 * Overlay层存放悬浮提示，强制隐藏；
 * Content层存放实际内容页面，强制显示
 *
 * 具有以下功能：
 * 1.页眉操作
 * 通过向下拉动滑动视图，可以显示页眉视图
 * 2.标签操作
 * 通过设置偏移等级，可以强制显示/隐藏部分或全部标签视图
 * 3.覆盖层操作
 * 通过特定条件触发，可以显示或隐藏悬浮视图
 * 4.下拉刷新
 * 若Content层包含有列表视图，并且Header层包含RefreshHeader，当触发下拉刷新状态时，可以显示RefreshHeader
 * 5.加载更多
 * 若Content层包含有列表视图，并且Overlay层包含LoadOverlay，当触发加载更多状态时，可以显示LoadOverlay
 */
public class SlidableView extends FrameLayout implements ViewGroup.OnHierarchyChangeListener, OnGestureListener {
    private static int ANIMATE_COUNT = 10;
    private static int ANIMATE_DURATION = 200;

    private Animate mAnimate = new Animate();
    private AnimateOverlay mAnimateOverlay = new AnimateOverlay();
    private int mBarHeight;
    private int mBaseOffset = 0;
    private ContentView mContent;
    private Runnable mDelayedLoadCompleteRunnable = new Runnable() {
        @Override
        public void run() {
            mAnimateOverlay.scrollTo(0);
        }
    };
    private boolean mFirstMeasure = false;
    private GestureHelper mGesture;
    private LinearLayout mHeaders;
    private int mHeadersHeight;
    private boolean mIntercept = false;
    private float mLastScrollY;
    private boolean mLoading = false;
    private float mOffset = 0;
    private float mOverlayOffset = 0;
    private LinearLayout mOverlays;
    private int mOverlaysHeight = 0;
    private boolean mRefreshing = false;
    private OnSlidableTriggerListener mSlidableTriggerListener = null;
    private ArrayList<Integer> mTabHeight = new ArrayList<Integer>();
    private int mTabOffsetLevel = 0;
    private LinearLayout mTabs;
    private int mTabsHeight;
    private Runnable mDelayedRefreshCompleteRunnable = new Runnable() {
        @Override
        public void run() {
            if (mRefreshing) {
                mRefreshing = false;
            }
            mAnimate.scrollTo(mTabsHeight);
        }
    };

    public SlidableView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.slidableViewStyle);
    }

    public SlidableView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray style;

        style = context.obtainStyledAttributes(attrs, R.styleable.SlidableView, defStyle, 0);
        mTabOffsetLevel = style.getInt(R.styleable.SlidableView_offsetLevel, 0);
        style.recycle();

        mHeaders = new LinearLayout(context);
        mHeaders.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mHeaders.setOrientation(LinearLayout.VERTICAL);
        addView(mHeaders);

        mTabs = new LinearLayout(context);
        mTabs.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mTabs.setOrientation(LinearLayout.VERTICAL);
        addView(mTabs);

        mContent = new ContentView(context);
        mContent.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        addView(mContent);

        mOverlays = new LinearLayout(context);
        mOverlays.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mOverlays.setOrientation(LinearLayout.VERTICAL);
        addView(mOverlays);

        setOnHierarchyChangeListener(this);

        mGesture = new GestureHelper(context);
        mGesture.setOnGestureListener(this);
    }

    private void calculateRefreshTrigger() {
        IRefreshHeader header;
        int progress;
        int i;
        /**
         * 遍历Header，如果有RefreshHeader就设置其刷新进度
         */
        for (i = 0; i < mHeaders.getChildCount(); i++) {
            if (mHeaders.getChildAt(i) instanceof RefreshHeaderView) {
                header = (IRefreshHeader) mHeaders.getChildAt(i);
                progress = (int) (100 * (mOffset - mBarHeight) / mHeadersHeight);
                header.setRefreshTrigger(progress);
            }
        }
    }

    @Override
    public void onChildViewAdded(View parent, View child) {
        removeView(child);
        if (this == parent) {
            if (child instanceof HeaderView) {
                mHeaders.addView(child);
            } else if (child instanceof TabView) {
                mTabs.addView(child);
                mTabHeight.add(0);
            } else if (child instanceof OverlayView) {
                mOverlays.addView(child);
            } else {
                mContent.removeAllViews();
                mContent.addView(child);
            }
        }
    }

    @Override
    public void onChildViewRemoved(View parent, View child) {
    }

    @Override
    public boolean onDown() {
        mLastScrollY = 0;
        mAnimate.cancel();

        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        mIntercept = mGesture.onTouch(event);

        return mIntercept;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (mHeaders.getChildCount() > 0) {
            mHeaders.layout(l, (int) (t + mOffset - mBarHeight), r, (int) (b + mOffset - mBarHeight));
        }
        if (mTabs.getChildCount() > 0) {
            mTabs.layout(l, (int) (t + mOffset - mTabsHeight), r, (int) (b + mOffset - mTabsHeight));
        }
        if (mOverlays.getChildCount() > 0) {
            mOverlays.layout(l, (int) (t + mOffset), r, (int) (t + mOffset + mOverlayOffset));
        }
        if (mContent.getChildCount() > 0) {
            mContent.layout(l, (int) (t + mOffset), r, (int) (b + mOffset));
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width;
        int height;
        int i;

        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, height);
        measureChildWithMargins(mHeaders, widthMeasureSpec, 0, heightMeasureSpec, 0);
        mHeadersHeight = mHeaders.getMeasuredHeight();
        measureChildWithMargins(mTabs, widthMeasureSpec, 0, heightMeasureSpec, 0);
        mTabsHeight = mTabs.getMeasuredHeight();
        mBarHeight = mHeadersHeight + mTabsHeight;
        if (mTabs.getChildCount() > 0) {
            for (i = 0; i < mTabs.getChildCount(); i++) {
                mTabHeight.set(i, mTabs.getChildAt(i).getMeasuredHeight());
            }
            if (mTabOffsetLevel <= 0) {
                mBaseOffset = 0;
            } else if (mTabOffsetLevel > mTabHeight.size()) {
                mBaseOffset = mBarHeight;
            } else {
                mBaseOffset = 0;
                for (i = 0; i < mTabOffsetLevel; i++) {
                    mBaseOffset += mTabHeight.get(mTabHeight.size() - i - 1);
                }
            }
        }
        measureChildWithMargins(mOverlays, widthMeasureSpec, 0, heightMeasureSpec, mBaseOffset);
        mOverlaysHeight = mOverlays.getMeasuredHeight();
        measureChildWithMargins(mContent, widthMeasureSpec, 0, heightMeasureSpec, mBaseOffset);
        if (!mFirstMeasure) {
            mFirstMeasure = true;
            mOffset = mBaseOffset;
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
        float offsetScale;
        float deltaScrollY;

        if (!mRefreshing || mOffset > mHeadersHeight) {
            deltaScrollY = scrollY - mLastScrollY;
            if (scrollY > 0f) {
                /**
                 * 往下滑
                 */
                if (mContent.isSlidableTouchTop()) {
                    /**
                     * 内容层已经滑动至顶部，触发下拉操作
                     */
                    if (mOffset > mBarHeight + mHeadersHeight) {
                        /**
                         * 如果下拉偏移总量超过Header，则设置阻力系数
                         */
                        offsetScale = 0.5f;
                    } else {
                        offsetScale = 1f;
                    }
                    mOffset += deltaScrollY * offsetScale;
                    mLastScrollY = scrollY;
                    refresh();

                    return true;
                }
            } else if (scrollY < 0f) {
                /**
                 * 往上滑
                 */
                if (mOffset > mBaseOffset) {
                    if (mOffset > mBarHeight) {
                        /**
                         * 如果下拉偏移总量已经超过Header，则设置阻力系数
                         */
                        offsetScale = 0.5f;
                    } else {
                        offsetScale = 1f;
                    }
                    mOffset += deltaScrollY * offsetScale;
                    if (mOffset < mBaseOffset) {
                        mOffset = mBaseOffset;
                    }
                    mLastScrollY = scrollY;
                    refresh();

                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public void onTaps(float x, float y, int tapCount) {
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        return !mIntercept || mGesture.onTouch(event);
    }

    @Override
    public boolean onTranslate(float translateX, float translateY, long duration) {
        return false;
    }

    @Override
    public boolean onUp() {
        if (!mRefreshing) {
            if (mOffset > mBarHeight + mHeadersHeight) {
                if (mSlidableTriggerListener != null) {
                    mSlidableTriggerListener.onRefreshTrigger();
                }
            } else if (mOffset > mTabsHeight) {
                mAnimate.scrollTo(mTabsHeight);
            } else {
                mAnimate.scrollTo(mBaseOffset);
            }
        } else {
            mAnimate.scrollTo(mBarHeight);
        }
        mIntercept = false;

        return false;
    }

    private void refresh() {
        if (!mRefreshing) {
            calculateRefreshTrigger();
        }
        requestLayout();
    }

    /**
     * 手动触发结束加载状态
     *
     * 默认有一秒的延迟
     *
     * @param pageSize
     *         页面加载的条目数
     *
     *         -1为不需要显示加载条目数
     */
    public final void setLoadComplete(int pageSize) {
        setLoadComplete(pageSize, 1000);
    }

    /**
     * 手动触发结束加载状态
     *
     * @param pageSize
     *         页面加载的条目数
     *
     *         -1为不需要显示加载条目数
     * @param delay
     *         收起状态提示的延迟（毫秒）
     *
     *         0为不需要延迟
     */
    public final void setLoadComplete(int pageSize, int delay) {
        if (mLoading) {
            mLoading = false;
            ILoadOverlay overlay;
            int i;

            /**
             * 遍历Overlay，如果有LoadOverlay就设置其加载状态
             */
            for (i = 0; i < mOverlays.getChildCount(); i++) {
                overlay = (ILoadOverlay) mOverlays.getChildAt(i);
                overlay.setLoadComplete(pageSize);
            }
            if (delay > 0) {
                postDelayed(mDelayedLoadCompleteRunnable, delay);
            } else {
                mDelayedLoadCompleteRunnable.run();
            }
        }
    }

    /**
     * 手动触发开始加载状态
     */
    public final void setLoadStart() {
        ILoadOverlay overlay;
        int i;

        if (!mLoading) {
            mLoading = true;
            mAnimateOverlay.scrollTo(mOverlaysHeight);
            /**
             * 遍历Overlay，如果有LoadOverlay就设置其加载状态
             */
            for (i = 0; i < mOverlays.getChildCount(); i++) {
                overlay = (ILoadOverlay) mOverlays.getChildAt(i);
                overlay.setLoadStart();
            }
        }
    }

    /**
     * 设置偏移等级
     *
     * 如果滑动视图包含页眉或标签视图，则偏移等级表示第强制显示第几个页眉或标签视图
     *
     * 强制显示的页眉或标签视图，以及其下方的视图，都不会响应垂直滑动事件，不会因为垂直滑动而滑出布局之外
     * 强制隐藏的页眉或标签视图，以及其上方的视图，默认在布局之外，只有垂直滑动布局才能拉出来
     *
     * 小于或等于0：不强制显示任何页眉或标签视图
     * 1至标签视图数量：强制显示指定的标签视图
     * 大于标签视图数量：强制显示所有的标签视图和页眉视图
     *
     * 例：一个滑动视图包含1个页眉视图、2个标签视图
     * 若设置偏移等级为0，则强制隐藏所有页眉视图和标签视图；
     * 若设置偏移等级为1，则强制隐藏页眉视图、强制显示最下面的一个标签视图；
     * 若设置偏移等级为2，则强制隐藏页眉视图、强制显示所有标签视图；
     * 若设置偏移等级为3，则强制显示所有页眉视图和标签视图
     *
     * @param offsetLevel
     *         偏移等级
     *
     * @see com.harreke.easyapp.widgets.slidableview.inners.HeaderView
     * @see com.harreke.easyapp.widgets.slidableview.inners.TabView
     */
    public final void setOffsetLevel(int offsetLevel) {
        mTabOffsetLevel = offsetLevel;
    }

    /**
     * 设置触发监听器
     *
     * @param slidableTriggerListener
     *         触发监听器
     */
    public final void setOnSlidableTriggerListener(OnSlidableTriggerListener slidableTriggerListener) {
        mSlidableTriggerListener = slidableTriggerListener;
    }

    /**
     * 手动触发结束刷新状态
     *
     * 默认有一秒的延迟
     */
    public final void setRefreshComplete() {
        setRefreshComplete(1000);
    }

    /**
     * 手动触发结束刷新状态
     *
     * @param delay
     *         收起状态提示的延迟（毫秒）
     *
     *         0为不需要延迟
     */
    public final void setRefreshComplete(int delay) {
        IRefreshHeader header;
        int i;

        /**
         * 遍历Header，如果有RefreshHeader就设置其刷新状态
         */
        for (i = 0; i < mHeaders.getChildCount(); i++) {
            if (mHeaders.getChildAt(i) instanceof RefreshHeaderView) {
                header = (IRefreshHeader) mHeaders.getChildAt(i);
                header.setRefreshComplete();
            }
        }
        if (delay > 0) {
            postDelayed(mDelayedRefreshCompleteRunnable, delay);
        } else {
            mDelayedRefreshCompleteRunnable.run();
        }
    }

    /**
     * 手动触发开始刷新状态
     */
    public final void setRefreshStart() {
        IRefreshHeader header;
        int i;

        if (!mRefreshing) {
            mRefreshing = true;
            mAnimate.scrollTo(mBarHeight);
            /**
             * 遍历Header，如果有RefreshHeader就设置其刷新状态
             */
            for (i = 0; i < mHeaders.getChildCount(); i++) {
                if (mHeaders.getChildAt(i) instanceof RefreshHeaderView) {
                    header = (IRefreshHeader) mHeaders.getChildAt(i);
                    header.setRefreshStart();
                }
            }
        }
    }

    /**
     * 设置可滑动内容视图
     *
     * 可滑动内容视图为ListView、ScrollView等视图
     *
     * @param slidableContentViewId
     *         可滑动内容视图Id
     */
    public final void setSlidableContentView(int slidableContentViewId) {
        if (mContent != null && slidableContentViewId > 0) {
            mContent.setSlidableView(slidableContentViewId);
        }
    }

    private class Animate implements Runnable {
        private int mAnimateInterval = 0;
        private float mAnimateStep = 0f;
        private boolean mAnimating = false;
        private int mTargetCount;
        private float mTargetOffset;

        public final void cancel() {
            mAnimating = false;
        }

        @Override
        public void run() {
            if (mAnimating) {
                if (mTargetCount > 0) {
                    mOffset += mAnimateStep;
                } else {
                    mOffset = mTargetOffset;
                    mAnimating = false;
                }
                refresh();
                mTargetCount--;
                if (mAnimating) {
                    postDelayed(this, mAnimateInterval);
                }
            }
        }

        public final void scrollTo(float targetOffset) {
            mTargetOffset = targetOffset;
            mTargetCount = ANIMATE_COUNT;
            mAnimateStep = (mTargetOffset - mOffset) / mTargetCount;
            mAnimateInterval = ANIMATE_DURATION / mTargetCount;
            mAnimating = true;
            postDelayed(this, mAnimateInterval);
        }
    }

    private class AnimateOverlay implements Runnable {
        private int mAnimateInterval = 0;
        private float mAnimateStep = 0f;
        private boolean mAnimating = false;
        private int mTargetCount;
        private float mTargetOffset;

        public final void cancel() {
            mAnimating = false;
        }

        @Override
        public void run() {
            if (mAnimating) {
                if (mTargetCount > 0) {
                    mOverlayOffset += mAnimateStep;
                } else {
                    mOverlayOffset = mTargetOffset;
                    mAnimating = false;
                }
                refresh();
                mTargetCount--;
                if (mAnimating) {
                    postDelayed(this, mAnimateInterval);
                }
            }
        }

        public final void scrollTo(float targetOffset) {
            mTargetOffset = targetOffset;
            mTargetCount = ANIMATE_COUNT;
            mAnimateStep = (mTargetOffset - mOverlayOffset) / mTargetCount;
            mAnimateInterval = ANIMATE_DURATION / mTargetCount;
            mAnimating = true;
            postDelayed(this, mAnimateInterval);
        }
    }
}