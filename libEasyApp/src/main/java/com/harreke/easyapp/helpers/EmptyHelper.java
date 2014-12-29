package com.harreke.easyapp.helpers;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.harreke.easyapp.R;
import com.harreke.easyapp.frameworks.bases.IFramework;
import com.harreke.easyapp.widgets.CircularProgressDrawable;
import com.harreke.easyapp.widgets.rippleeffects.RippleDrawable;
import com.harreke.easyapp.widgets.rippleeffects.RippleOnClickListener;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/12/12
 */
public class EmptyHelper implements View.OnClickListener {
    private ImageView empty_icon;
    private View empty_retry;
    private View empty_root;
    private TextView empty_text;
    private boolean mClickableWhenIdle = true;
    private boolean mIdle = true;
    private View.OnClickListener mOnClickListener = null;
    private CircularProgressDrawable mProgressDrawable;
    private boolean mShowRetryWhenIdle = true;

    public EmptyHelper(IFramework framework) {
        this(framework, R.id.empty_root);
    }

    public EmptyHelper(IFramework framework, int emptyRootId) {
        empty_root = framework.findViewById(emptyRootId);
        empty_icon = (ImageView) empty_root.findViewById(R.id.empty_icon);
        empty_text = (TextView) empty_root.findViewById(R.id.empty_text);
        empty_retry = empty_root.findViewById(R.id.empty_retry);

        mProgressDrawable = new CircularProgressDrawable();

        RippleDrawable.attach(empty_root);
        RippleOnClickListener.attach(empty_root, this);
    }

    public EmptyHelper(View rootView) {
        empty_root = rootView;
        empty_icon = (ImageView) empty_root.findViewById(R.id.empty_icon);
        empty_text = (TextView) empty_root.findViewById(R.id.empty_text);
        empty_retry = empty_root.findViewById(R.id.empty_retry);

        mProgressDrawable = new CircularProgressDrawable();

        RippleDrawable.attach(rootView);
        RippleOnClickListener.attach(rootView, this);
    }

    public void hide() {
        empty_root.setVisibility(View.GONE);
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

    public void showEmptyIdle() {
        showIdle(R.string.empty_idle);
    }

    public void showEmptyFailureIdle() {
        showIdle(R.string.empty_failure);
    }

    private void showIdle(int toastId) {
        mIdle = true;
        empty_root.setVisibility(View.VISIBLE);
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
        mIdle = false;
        empty_root.setVisibility(View.VISIBLE);
        empty_icon.setImageDrawable(mProgressDrawable);
        mProgressDrawable.setProgress(-1);
        empty_text.setText(R.string.empty_loading);
        empty_retry.setVisibility(View.GONE);
    }
}