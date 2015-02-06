package com.harreke.easyapp.widgets.transitions;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.harreke.easyapp.frameworks.base.ActivityFramework;
import com.harreke.easyapp.helpers.ImageLoaderHelper;
import com.harreke.easyapp.listeners.OnTransitionListener;
import com.harreke.easyapp.widgets.animators.ToggleViewValueAnimator;
import com.harreke.easyapp.widgets.animators.ViewValueAnimator;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.ViewHelper;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/12/29
 */
public class TransitionLayout extends FrameLayout implements ValueAnimator.AnimatorUpdateListener {
    private ViewValueAnimator mContentAnimator;
    private Runnable mContentRunnable = null;
    private ViewGroup mContentView;
    private Animator.AnimatorListener mEnterListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            clearTransition();
            onEnter();
        }
    };
    private Animator.AnimatorListener mExitListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            clearTransition();
            onExit();
        }
    };
    private OnTransitionListener mOnTransitionListener = null;
    private View mView;
    private ToggleViewValueAnimator mViewAnimator;

    public TransitionLayout(Context context) {
        super(context);

        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mContentView = new FrameLayout(context);
        mContentView.setLayoutParams(
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        addView(mContentView);

        mContentAnimator = ViewValueAnimator.animate(mContentView);
    }

    private void clearTransition() {
        mContentAnimator.cancel();
        mContentAnimator.clear();
        removeView(mView);
    }

    public void destroy() {
        mContentAnimator.cancel();
        removeCallbacks(mContentRunnable);
        mContentAnimator = null;
        mContentRunnable = null;
        mContentView = null;
    }

    public ViewValueAnimator getContentAnimator() {
        return mContentAnimator;
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

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
    }

    protected void onEnter() {
        Log.e(null, "transition enter");
        if (mOnTransitionListener != null) {
            mOnTransitionListener.onEnter();
        }
    }

    protected void onExit() {
        Log.e(null, "transition exit");
        if (mOnTransitionListener != null) {
            mOnTransitionListener.onExit();
        }
    }

    public void setContentView(int layoutId) {
        mContentView.removeAllViews();
        mContentView.addView(LayoutInflater.from(getContext()).inflate(layoutId, mContentView, false));
    }

    public void setContentView(View view) {
        mContentView.removeAllViews();
        mContentView.addView(view);
    }

    public void setContentView(View view, ViewGroup.LayoutParams params) {
        mContentView.removeAllViews();
        mContentView.addView(view, params);
    }

    public void setContentVisibility(int visibility) {
        mContentView.setVisibility(visibility);
    }

    protected void setContentX(float contentX) {
        ViewHelper.setX(mContentView, contentX);
    }

    protected void setContentY(float contentY) {
        ViewHelper.setY(mContentView, contentY);
    }

    public void setOnTransitionListener(OnTransitionListener onTransitionListener) {
        mOnTransitionListener = onTransitionListener;
    }

    public void startEnterTransition(ActivityFramework framework, final TransitionOptions options) {
        final View targetView;
        View originalView;

        mContentAnimator.cancel();
        mContentAnimator.clear();
        switch (options.transition) {
            case None:
                onEnter();
                break;
            case Shared:
                originalView = LayoutInflater.from(framework).inflate(framework.getLayoutId(), mContentView, false);
                mView = originalView.findViewById(options.targetViewId);
                ((ViewGroup) mView.getParent()).removeView(mView);
                addView(mView);
                mContentView.setVisibility(INVISIBLE);
                ViewHelper.setAlpha(mContentView, 0f);
                targetView = mContentView.findViewById(options.targetViewId);
                targetView.post(new Runnable() {
                    @Override
                    public void run() {
                        mViewAnimator = ToggleViewValueAnimator.animate(mView).xOff(options.viewRect.left)
                                .xOn(ViewHelper.getX(targetView)).yOff(options.viewRect.top).yOn(ViewHelper.getY(targetView))
                                .widthOff((int) options.viewRect.width()).widthOn(targetView.getMeasuredWidth())
                                .heightOff((int) options.viewRect.height()).heightOn(targetView.getMeasuredHeight())
                                .listenerOn(mEnterListener).listenerOff(mExitListener).debug(true).toggleOn(true);
                        mContentAnimator.alpha(1f).visibilityStart(VISIBLE).start(true);
                    }
                });
                break;
            case SharedImage:
                originalView = LayoutInflater.from(framework).inflate(framework.getLayoutId(), mContentView, false);
                mView = originalView.findViewById(options.targetViewId);
                ((ViewGroup) mView.getParent()).removeView(mView);
                addView(mView);
                mContentView.setVisibility(INVISIBLE);
                ViewHelper.setAlpha(mContentView, 0f);
                targetView = mContentView.findViewById(options.targetViewId);
                targetView.post(new Runnable() {
                    @Override
                    public void run() {
                        ImageLoaderHelper.loadImage((ImageView) mView, options.imageViewUrl, 0, 0);
                        mViewAnimator = ToggleViewValueAnimator.animate(mView).xOff(options.viewRect.left)
                                .xOn(ViewHelper.getX(targetView)).yOff(options.viewRect.top).yOn(ViewHelper.getY(targetView))
                                .widthOff((int) options.viewRect.width()).widthOn(targetView.getMeasuredWidth())
                                .heightOff((int) options.viewRect.height()).heightOn(targetView.getMeasuredHeight())
                                .listenerOn(mEnterListener).listenerOff(mExitListener).debug(true).toggleOn(true);
                        mContentAnimator.alpha(1f).visibilityStart(VISIBLE).start(true);
                    }
                });
                break;
        }
    }

    public void startExitTransition(TransitionOptions options) {
        mContentAnimator.cancel();
        mContentAnimator.clear();
        switch (options.transition) {
            case None:
                onExit();
                break;
            case Shared:
            case SharedImage:
                mViewAnimator.toggleOff(true);
                mContentAnimator.alpha(0f).visibilityEnd(INVISIBLE).start(true);
                break;
        }
    }
}