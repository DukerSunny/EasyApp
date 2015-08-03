package com.harreke.easyapp.helpers;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.harreke.easyapp.R;
import com.harreke.easyapp.frameworks.base.IFramework;
import com.harreke.easyapp.widgets.circluarprogresses.CircularProgressDrawable;
import com.harreke.easyapp.widgets.rippleeffects.RippleDrawable;

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
        if (empty_root != null) {
            empty_icon = (ImageView) empty_root.findViewById(R.id.empty_icon);
            empty_text = (TextView) empty_root.findViewById(R.id.empty_text);
            empty_retry = empty_root.findViewById(R.id.empty_retry);

            mProgressDrawable = new CircularProgressDrawable(framework.getContext());

            empty_root.setOnClickListener(this);
            RippleDrawable.attach(empty_root);
        }
    }

    public void hide() {
        if (empty_root != null) {
            empty_root.setVisibility(View.GONE);
            mProgressDrawable.setProgress(0);
        }
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

    public void showEmptyFailureIdle(String message) {
        if (!TextUtils.isEmpty(message)) {
            showIdle(message);
        } else {
            showIdle(R.string.empty_failure);
        }
    }

    public void showEmptyFailureIdle() {
        showEmptyFailureIdle(null);
    }

    public void showEmptyIdle() {
        showIdle(R.string.empty_idle);
    }

    private void showIdle(String message) {
        if (empty_root != null) {
            mIdle = true;
            empty_root.setVisibility(View.VISIBLE);
            if (empty_icon != null) {
                empty_icon.setImageResource(R.drawable.image_idle);
            }
            mProgressDrawable.setProgress(0f);
            empty_text.setText(message);
            if (mShowRetryWhenIdle) {
                empty_retry.setVisibility(View.VISIBLE);
            } else {
                empty_retry.setVisibility(View.GONE);
            }
        }
    }

    private void showIdle(int messageId) {
        showIdle(empty_text.getResources().getString(messageId));
    }

    public void showLoading() {
        showLoading(true);
    }

    public void showLoading(boolean animate) {
        if (empty_root != null) {
            mIdle = false;
            empty_root.setVisibility(View.VISIBLE);
            if (empty_icon != null) {
                empty_icon.setImageDrawable(mProgressDrawable);
            }
            mProgressDrawable.setProgress(-1f);
            empty_text.setText(R.string.empty_loading);
            empty_retry.setVisibility(View.GONE);
        }
    }
}