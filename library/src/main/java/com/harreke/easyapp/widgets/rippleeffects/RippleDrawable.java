package com.harreke.easyapp.widgets.rippleeffects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;

import com.harreke.easyapp.R;
import com.harreke.easyapp.utils.ResourceUtil;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.PropertyValuesHolder;
import com.nineoldandroids.animation.ValueAnimator;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/11/28
 *
 * 模仿Material风格涟漪效果的Drawable
 */
public class RippleDrawable extends Drawable implements View.OnTouchListener {
    private int mAlpha = 0;
    private ValueAnimator mAnimator = null;
    private Drawable mBackground = null;
    private int mBaseAlpha = 64;
    private int mBaseColor = Color.BLACK;
    private Rect mBounds = new Rect(0, 0, 0, 0);
    private float mCenterX = 0f;
    private float mCenterY = 0f;
    private float mDownX = 0f;
    private float mDownY = 0f;
    private boolean mEnabled = true;
    private float mHotpotX = 0f;
    private float mHotpotY = 0f;
    private int mMaxRadius = 0;
    private Paint mPaint;
    private boolean mPressed = false;
    private float mRadius = 0;
    private ValueAnimator.AnimatorUpdateListener mUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            Object value;

            value = animation.getAnimatedValue("hotpotX");
            if (value != null) {
                mHotpotX = (float) value;
            }
            value = animation.getAnimatedValue("hotpotY");
            if (value != null) {
                mHotpotY = (float) value;
            }
            value = animation.getAnimatedValue("alpha");
            if (value != null) {
                mAlpha = (int) value;
            }
            value = animation.getAnimatedValue("radius");
            if (value != null) {
                mRadius = (float) value;
            }
            invalidateSelf();
        }
    };
    private boolean mRippling = false;
    private Animator.AnimatorListener mListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationCancel(Animator animation) {
            mRippling = false;
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            mRippling = false;
        }
    };
    private boolean mSquare = false;
    private View mView;

    private RippleDrawable(View view, RippleStyle style) {
        mView = view;
        mBackground = mView.getBackground();
        switch (style) {
            case Light_Square:
                mSquare = true;
                mBaseColor = Color.WHITE;
                mBaseAlpha = 192;
                if (mBackground == null) {
                    mBackground = ResourceUtil.getDrawable(view.getContext(), R.drawable.selector_button_light_square);
                }
                break;
            case Light:
                mSquare = false;
                mBaseColor = Color.WHITE;
                mBaseAlpha = 192;
                if (mBackground == null) {
                    mBackground = ResourceUtil.getDrawable(view.getContext(), R.drawable.selector_button_light);
                }
                break;
            case Dark_Square:
                mSquare = true;
                mBaseColor = Color.BLACK;
                mBaseAlpha = 64;
                if (mBackground == null) {
                    mBackground = ResourceUtil.getDrawable(view.getContext(), R.drawable.selector_button_dark_square);
                }
                break;
            case Dark:
                mSquare = false;
                mBaseColor = Color.BLACK;
                mBaseAlpha = 64;
                if (mBackground == null) {
                    mBackground = ResourceUtil.getDrawable(view.getContext(), R.drawable.selector_button_dark);
                }
                break;
        }
        mPaint = new Paint();
        mPaint.setColor(mBaseColor);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
        mView.setOnTouchListener(this);
        if (Build.VERSION.SDK_INT >= 16) {
            mView.setBackground(this);
        } else {
            mView.setBackgroundDrawable(this);
        }
    }

    public static RippleDrawable attach(View view) {
        return new RippleDrawable(view, RippleStyle.Dark);
    }

    public static RippleDrawable attach(View view, RippleStyle style) {
        return new RippleDrawable(view, style);
    }

    private void cancel() {
        if (mAnimator != null) {
            mAnimator.cancel();
            mAnimator = null;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        mBackground.draw(canvas);
        if (mEnabled || mRippling) {
            mPaint.setAlpha(mAlpha);
            canvas.drawCircle(mHotpotX, mHotpotY, mRadius, mPaint);
        }
    }

    @Override
    public int getOpacity() {
        return 1;
    }

    public boolean isEnabled() {
        return mEnabled;
    }

    public boolean isRippling() {
        return mRippling;
    }

    @Override
    public boolean isStateful() {
        return true;
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        mBounds.set(bounds);
        if (mSquare) {
            mMaxRadius = Math.max(mBounds.width() / 2, mBounds.height() / 2);
        } else {
            mMaxRadius = (int) Math.sqrt(mBounds.width() * mBounds.width() + mBounds.height() * mBounds.height());
        }
        mBackground.setBounds(mBounds);
        mCenterX = mBounds.exactCenterX();
        mCenterY = mBounds.exactCenterY();
    }

    @Override
    protected boolean onStateChange(int[] stateSet) {
        boolean enabled = false;
        boolean pressed = false;
        int i;

        mBackground.setState(stateSet);
        for (i = 0; i < stateSet.length; i++) {
            if (stateSet[i] == android.R.attr.state_enabled) {
                enabled = true;
            }
            if (stateSet[i] == android.R.attr.state_pressed) {
                pressed = true;
            }
        }
        if (mEnabled && !enabled) {
            mEnabled = false;

            return true;
        } else {
            if (enabled) {
                mEnabled = true;
            }
            if (mPressed && !pressed) {
                mPressed = false;
                startRipple(mHotpotX, mCenterX, mHotpotY, mCenterY, mBaseColor, mRadius, mMaxRadius, mBaseAlpha, 0, 300l);

                return true;
            } else if (!mPressed && pressed) {
                mPressed = true;
                startRipple(mDownX, mCenterX, mDownY, mCenterY, mBaseColor, 0, mMaxRadius, mBaseAlpha, mBaseAlpha, 2000l);

                return true;
            }
        }

        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (isEnabled() && !isRippling()) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                mDownX = event.getX();
                mDownY = event.getY();
            }
        }

        return false;
    }

    @Override
    public void setAlpha(int alpha) {
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
    }

    public void startRipple(float x, float y, int color, long duration) {
        startRipple(x, x, y, y, color, 0, mMaxRadius, 255, 255, duration);
    }

    public void startRipple(float fromX, float toX, float fromY, float toY, int color, float fromRadius, float toRadius,
            int fromAlpha, int toAlpha, long duration) {
        PropertyValuesHolder hotpotXHolder;
        PropertyValuesHolder hotpotYHolder;
        PropertyValuesHolder radiusHolder;
        PropertyValuesHolder alphaHolder;

        cancel();
        mRippling = true;
        hotpotXHolder = PropertyValuesHolder.ofFloat("hotpotX", fromX, toX);
        hotpotYHolder = PropertyValuesHolder.ofFloat("hotpotY", fromY, toY);
        radiusHolder = PropertyValuesHolder.ofFloat("radius", fromRadius, toRadius);
        alphaHolder = PropertyValuesHolder.ofInt("alpha", fromAlpha, toAlpha);
        mPaint.setColor(color);
        mAnimator = ValueAnimator.ofPropertyValuesHolder(hotpotXHolder, hotpotYHolder, radiusHolder, alphaHolder);
        mAnimator.setDuration(duration);
        mAnimator.addListener(mListener);
        mAnimator.addUpdateListener(mUpdateListener);
        mAnimator.start();
    }
}