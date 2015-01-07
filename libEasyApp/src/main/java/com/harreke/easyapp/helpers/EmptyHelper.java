package com.harreke.easyapp.helpers;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.harreke.easyapp.R;
import com.harreke.easyapp.frameworks.bases.IFramework;
import com.harreke.easyapp.widgets.CircularProgressDrawable;
import com.harreke.easyapp.widgets.rippleeffects.RippleDrawable;
import com.harreke.easyapp.widgets.rippleeffects.RippleOnClickListener;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.view.ViewPropertyAnimator;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/12/12
 */
public class EmptyHelper implements View.OnClickListener {
    private ImageView empty_icon;
    private View empty_retry;
    private View empty_root;
    private TextView empty_text;
    private boolean mClickableWhenIdle = true;
    private Animator.AnimatorListener mHideListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            empty_root.setVisibility(View.GONE);
            empty_root.setClickable(true);
        }

        @Override
        public void onAnimationStart(Animator animation) {
            empty_root.setClickable(false);
        }
    };
    private boolean mIdle = true;
    private View.OnClickListener mOnClickListener = null;
    private CircularProgressDrawable mProgressDrawable;
    private ViewPropertyAnimator mRootAnimator;
    private Animator.AnimatorListener mShowListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            empty_root.setClickable(true);
        }

        @Override
        public void onAnimationStart(Animator animation) {
            empty_root.setVisibility(View.VISIBLE);
            empty_root.setClickable(false);
        }
    };
    private boolean mShowRetryWhenIdle = true;

    public EmptyHelper(IFramework framework) {
        this(framework, R.id.empty_root);
    }

    public EmptyHelper(IFramework framework, int emptyRootId) {
        empty_root = framework.findViewById(emptyRootId);
        empty_icon = (ImageView) empty_root.findViewById(R.id.empty_icon);
        empty_text = (TextView) empty_root.findViewById(R.id.empty_text);
        empty_retry = empty_root.findViewById(R.id.empty_retry);

        mProgressDrawable = new CircularProgressDrawable(framework.getContext());

        mRootAnimator = ViewPropertyAnimator.animate(empty_root);

        RippleDrawable.attach(empty_root);
        RippleOnClickListener.attach(empty_root, this);
    }

    public void hide() {
        hide(true);
    }

    public void hide(boolean animate) {
        if (animate) {
            mRootAnimator.cancel();
            mRootAnimator.alpha(0f).setDuration(300l).setListener(mHideListener).start();
        } else {
            empty_root.setVisibility(View.GONE);
        }
        mProgressDrawable.setProgress(0);
    }

    public boolean isEmptyIdle() {
        return mIdle;
    }

    @Override
    public void onClick(View v) {
        if (mOnClickListener != null && mClickableWhenIdle && isEmptyIdle()) {
            mOnClickListener.onClick(v);
        }
    }

    public void setClickableWhenIdle(boolean clickableWhenIdle) {
        mClickableWhenIdle = clickableWhenIdle;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    public void setShowRetryWhenIdle(boolean showRetryWhenIdle) {
        mShowRetryWhenIdle = showRetryWhenIdle;
    }

    public void showEmptyFailureIdle() {
        showEmptyFailureIdle(true);
    }

    public void showEmptyFailureIdle(boolean animate) {
        showIdle(R.string.empty_failure, animate);
    }

    public void showEmptyIdle(boolean animate) {
        showIdle(R.string.empty_idle, animate);
    }

    public void showEmptyIdle() {
        showEmptyIdle(true);
    }

    private void showIdle(int toastId, boolean animate) {
        mIdle = true;
        if (animate) {
            mRootAnimator.cancel();
            mRootAnimator.alpha(1f).setDuration(300l).setListener(mShowListener).start();
        } else {
            empty_root.setVisibility(View.VISIBLE);
        }
        empty_icon.setImageResource(R.drawable.image_idle);
        mProgressDrawable.setProgress(0);
        empty_text.setText(toastId);
        if (mShowRetryWhenIdle) {
            empty_retry.setVisibility(View.VISIBLE);
        } else {
            empty_retry.setVisibility(View.GONE);
        }
    }

    public void showLoading() {
        showLoading(true);
    }

    public void showLoading(boolean animate) {
        mIdle = false;
        if (animate) {
            mRootAnimator.cancel();
            mRootAnimator.alpha(1f).setDuration(300l).setListener(mShowListener).start();
        } else {
            empty_root.setVisibility(View.VISIBLE);
        }
        empty_icon.setImageDrawable(mProgressDrawable);
        mProgressDrawable.setProgress(-1);
        empty_text.setText(R.string.empty_loading);
        empty_retry.setVisibility(View.GONE);
    }
}