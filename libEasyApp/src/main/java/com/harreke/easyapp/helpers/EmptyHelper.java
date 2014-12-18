package com.harreke.easyapp.helpers;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.harreke.easyapp.R;
import com.harreke.easyapp.widgets.CircularProgressDrawable;
import com.harreke.easyapp.widgets.RippleDrawable;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/12/12
 */
public class EmptyHelper implements View.OnClickListener {
    private ImageView empty_icon;
    private View empty_infobar;
    private View empty_retry;
    private View empty_solid;
    private TextView empty_text;
    private boolean mClickableWhenIdle = true;
    private boolean mIdle = true;
    private View.OnClickListener mOnClickListener = null;
    private CircularProgressDrawable mProgressDrawable;
    private boolean mShowRetryWhenIdle = true;

    public EmptyHelper(View rootView) {
        empty_solid = rootView;
        empty_infobar = empty_solid.findViewById(R.id.empty_infobar);
        empty_icon = (ImageView) empty_solid.findViewById(R.id.empty_icon);
        empty_text = (TextView) empty_solid.findViewById(R.id.empty_text);
        empty_retry = empty_solid.findViewById(R.id.empty_retry);

        mProgressDrawable = new CircularProgressDrawable();
        RippleDrawable.attach(empty_infobar);

        empty_infobar.setOnClickListener(this);
    }

    public void hide() {
        empty_solid.setVisibility(View.GONE);
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

    public void showIdle() {
        mIdle = true;
        empty_solid.setVisibility(View.VISIBLE);
        empty_icon.setImageResource(R.drawable.image_idle);
        empty_text.setText(R.string.empty_idle);
        if (mShowRetryWhenIdle) {
            empty_retry.setVisibility(View.VISIBLE);
        } else {
            empty_retry.setVisibility(View.GONE);
        }
    }

    public void showLoading() {
        mIdle = false;
        empty_solid.setVisibility(View.VISIBLE);
        empty_icon.setImageDrawable(mProgressDrawable);
        mProgressDrawable.setProgress(-1);
        empty_text.setText(R.string.empty_loading);
        empty_retry.setVisibility(View.GONE);
    }
}