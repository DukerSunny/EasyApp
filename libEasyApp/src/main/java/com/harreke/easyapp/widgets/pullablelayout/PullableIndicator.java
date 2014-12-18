package com.harreke.easyapp.widgets.pullablelayout;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.harreke.easyapp.frameworks.bases.application.ApplicationFramework;
import com.harreke.easyapp.widgets.CircularProgressDrawable;
import com.melnykov.fab.FloatingActionButton;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.PropertyValuesHolder;
import com.nineoldandroids.view.ViewHelper;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/12/12
 */
public class PullableIndicator extends FrameLayout {
    public final static long DURATION_MORPH = 300;
    private final static int COLLAPSE_DIAMETER = (int) (ApplicationFramework.Density * 72);
    private final static int DRAWABLE_DIAMETER = (int) (ApplicationFramework.Density * 24);
    private final static int EXPAND_WIDTH = (int) (ApplicationFramework.Density * 144);
    private PropertyValuesHolder mExpandButtonWidthHolder =
            PropertyValuesHolder.ofInt("buttonWidth", COLLAPSE_DIAMETER, EXPAND_WIDTH);
    private PropertyValuesHolder mCollapseButtonWidthHolder =
            PropertyValuesHolder.ofInt("buttonWidth", EXPAND_WIDTH, COLLAPSE_DIAMETER);
    private PropertyValuesHolder mCollapseButtonColorHolder = PropertyValuesHolder.ofInt("buttonColor", 0, 255);
    private PropertyValuesHolder mCollapseButtonScaleHolder = PropertyValuesHolder.ofFloat("buttonScale", 4f, 1f);
    private PropertyValuesHolder mCollapseProgressAlphaHolder = PropertyValuesHolder.ofFloat("progressAlpha", 0f, 1f);
    private PropertyValuesHolder mCollapseToastAlphaHolder = PropertyValuesHolder.ofFloat("toastAlpha", 1f, 0f);
    private PropertyValuesHolder mExpandButtonColorHolder = PropertyValuesHolder.ofInt("buttonColor", 255, 0);
    private PropertyValuesHolder mExpandButtonScaleHolder = PropertyValuesHolder.ofFloat("buttonScale", 1f, 4f);
    private PropertyValuesHolder mExpandProgressAlphaHolder = PropertyValuesHolder.ofFloat("progressAlpha", 1f, 0f);
    private PropertyValuesHolder mExpandToastAlphaHolder = PropertyValuesHolder.ofFloat("toastAlpha", 0f, 1f);
    private FloatingActionButton mFloatingActionButton;
    private CircularProgressDrawable mProgressDrawable;
    private ImageView mProgressView;
    private TextView mToastView;

    public PullableIndicator(Context context) {
        super(context);
        LayoutParams params;

        params = new LayoutParams(EXPAND_WIDTH, COLLAPSE_DIAMETER);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        setLayoutParams(params);

        params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        mFloatingActionButton = new FloatingActionButton(context);
        mFloatingActionButton.setLayoutParams(params);
        mFloatingActionButton.setColorNormal(Color.WHITE);
        addView(mFloatingActionButton);

        mProgressDrawable = new CircularProgressDrawable();
        params = new LayoutParams(DRAWABLE_DIAMETER, DRAWABLE_DIAMETER);
        params.gravity = Gravity.CENTER;
        mProgressView = new ImageView(context);
        mProgressView.setLayoutParams(params);
        mProgressView.setImageDrawable(mProgressDrawable);
        addView(mProgressView);

        params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        mToastView = new TextView(context);
        mToastView.setLayoutParams(params);
        mToastView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        mToastView.setTextColor(Color.WHITE);
        ViewHelper.setAlpha(mToastView, 0f);
        addView(mToastView);
    }

    public void collapse() {
        ObjectAnimator animator;

        animator = ObjectAnimator.ofPropertyValuesHolder(this, mCollapseButtonScaleHolder, mCollapseButtonColorHolder,
                mCollapseProgressAlphaHolder, mCollapseToastAlphaHolder);
        animator.setDuration(DURATION_MORPH);
        animator.start();
    }

    public void expand(String toast) {
        ObjectAnimator animator;

        setProgress(0f);
        mToastView.setText(toast);
        animator = ObjectAnimator
                .ofPropertyValuesHolder(this, mExpandButtonScaleHolder, mExpandButtonColorHolder, mExpandProgressAlphaHolder,
                        mExpandToastAlphaHolder);
        animator.setDuration(DURATION_MORPH);
        animator.start();
    }

    private void setButtonColor(int value) {
        mFloatingActionButton.setColorNormal(Color.argb(128 + value / 2, value, value, value));
    }

    private void setButtonScale(float scale) {
        ViewHelper.setScaleX(mFloatingActionButton, scale);
        ViewHelper.setScaleY(mFloatingActionButton, scale);
    }

    private void setButtonWidth(int width) {
        LayoutParams params = (LayoutParams) mFloatingActionButton.getLayoutParams();

        params.width = width;
        mFloatingActionButton.setLayoutParams(params);
        mFloatingActionButton.requestLayout();
    }

    public void setProgress(float progress) {
        mProgressDrawable.setProgress(progress);
    }

    private void setProgressAlpha(float alpha) {
        ViewHelper.setAlpha(mProgressView, alpha);
    }

    private void setToastAlpha(float alpha) {
        ViewHelper.setAlpha(mToastView, alpha);
    }
}