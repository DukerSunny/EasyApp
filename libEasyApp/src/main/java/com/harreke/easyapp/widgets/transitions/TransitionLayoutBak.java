package com.harreke.easyapp.widgets.transitions;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.harreke.easyapp.utils.ResourceUtil;
import com.harreke.easyapp.widgets.animators.ViewValueAnimator;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.view.ViewHelper;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/12/29
 */
public class TransitionLayoutBak extends FrameLayout implements ViewGroup.OnHierarchyChangeListener {
    private int[] mColors;
    private ViewValueAnimator mContentAnimator;
    private Runnable mContentRunnable = null;
    private ViewGroup mContentView;
    private Animator.AnimatorListener mEnterCompleteListener = null;
    private Animator.AnimatorListener mExitCompleteListener = null;
    private ViewGroup mOverlayView;

    public TransitionLayoutBak(Context context) {
        super(context);

        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mContentView = new FrameLayout(context);
        mContentView.setLayoutParams(
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mOverlayView = new FrameLayout(context);
        mOverlayView.setLayoutParams(
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mOverlayView.setVisibility(INVISIBLE);

        mContentAnimator = ViewValueAnimator.animate(mContentView);

        mColors = ResourceUtil.obtainThemeColor(context);

        setOnHierarchyChangeListener(this);
    }

    protected void animateContentX(float targetX, Animator.AnimatorListener completeListener) {
        mContentAnimator.x(targetX).listener(completeListener).start(true);
    }

    protected void animateContentY(float targetY, Animator.AnimatorListener completeListener) {
        mContentAnimator.y(targetY).listener(completeListener).start(true);
    }

    private void animateRippleIn(int sharedViewId, Animator.AnimatorListener completeListener) {

    }

    private void animateRippleOut(int sharedViewId, Animator.AnimatorListener completeListener) {

    }

    public void destroy() {
        if (mContentAnimator != null) {
            mContentAnimator.cancel();
        }
        removeCallbacks(mContentRunnable);
        mContentView = null;
        mOverlayView = null;
    }

    protected int getContentHeight() {
        return mContentView.getMeasuredHeight();
    }

    protected int getContentWidth() {
        return mContentView.getMeasuredWidth();
    }

    protected float getContentX() {
        return ViewHelper.getX(mContentView);
    }

    protected float getContentY() {
        return ViewHelper.getY(mContentView);
    }

    private void inflateOverlay(int targetLayoutId) {
        mOverlayView.removeAllViews();
        LayoutInflater.from(getContext()).inflate(targetLayoutId, mOverlayView, true);
    }

    @Override
    public void onChildViewAdded(View parent, View child) {
        if (parent.equals(this)) {
            removeView(child);
            mContentView.addView(child);
        }
    }

    @Override
    public void onChildViewRemoved(View parent, View child) {
    }

    protected void setContentX(float contentX) {
        ViewHelper.setX(mContentView, contentX);
        setBackgroundColor(Color.argb((int) (192f - 192f * contentX / getContentWidth()), 0, 0, 0));
    }

    protected void setContentY(float contentY) {
        ViewHelper.setY(mContentView, contentY);
        setBackgroundColor(Color.argb((int) (192f - 192f * contentY / getContentHeight()), 0, 0, 0));
    }
}