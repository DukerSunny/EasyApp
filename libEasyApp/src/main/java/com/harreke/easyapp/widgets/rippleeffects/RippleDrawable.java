package com.harreke.easyapp.widgets.rippleeffects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;

import com.harreke.easyapp.enums.RippleStyle;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.PropertyValuesHolder;
import com.nineoldandroids.animation.TypeEvaluator;
import com.nineoldandroids.animation.ValueAnimator;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/11/28
 *
 * 模仿Material风格涟漪效果的Drawable
 */
public class RippleDrawable extends Drawable {
    private final static int RIPPLE_HOTPOT_ALPHA = 32;
    private float mBaseHotpotAlpha = RIPPLE_HOTPOT_ALPHA;
    private final static int RIPPLE_PRESSED_ALPHA = 24;
    private float mBasePressedAlpha = RIPPLE_PRESSED_ALPHA;
    private ValueAnimator mAnimator = null;
    private Animator.AnimatorListener mAnimatorListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            mAnimator = null;
        }
    };
    private Rect mBounds = new Rect(0, 0, 0, 0);
    private PointF mCenterPoint = new PointF(0f, 0f);
    private TypeEvaluator<PointF> mCoordinateTypeEvaluator = new TypeEvaluator<PointF>() {
        @Override
        public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
            return new PointF(startValue.x + fraction * (endValue.x - startValue.x),
                    startValue.y + fraction * (endValue.y - startValue.y));
            //            return new PointF(mDownPoint.x + fraction * (endValue.x - mDownPoint.x),
            //                    mDownPoint.y + fraction * (endValue.y - mDownPoint.y));
        }
    };
    private PointF mDownPoint = new PointF(0f, 0f);
    private boolean mEnabled = true;
    private float mHotpotAlpha = 0f;
    private Paint mHotpotPaint;
    private PointF mHotpotPoint = new PointF(0f, 0f);
    private float mHotpotRadius = 0;
    private ValueAnimator.AnimatorUpdateListener mUpdateSlowListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            mHotpotPoint.set((PointF) animation.getAnimatedValue("hotpotCoordinate"));
            mHotpotRadius = (float) animation.getAnimatedValue("hotpotRadius");
            invalidateSelf();
        }
    };
    private ValueAnimator.AnimatorUpdateListener mUpdateFastListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            mHotpotPoint.set((PointF) animation.getAnimatedValue("hotpotCoordinate"));
            mHotpotRadius = (float) animation.getAnimatedValue("hotpotRadius");
            mHotpotAlpha = (float) animation.getAnimatedValue("hotpotAlpha");
            invalidateSelf();
        }
    };
    private int mMaxRadius = 0;
    private boolean mPressed = false;
    private Paint mPressedPaint;
    private long mRippleDuration;
    private boolean mSquare = false;
    private Drawable mViewBackground = null;

    private RippleDrawable(View view, RippleStyle rippleStyle, long rippleDuration) {
        int baseColor = Color.BLACK;

        mRippleDuration = rippleDuration;
//        view.setClickable(true);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mAnimator == null) {
                    switch (event.getAction()) {
                        //                        case MotionEvent.ACTION_MOVE:
                        case MotionEvent.ACTION_DOWN:
                            mDownPoint.set(event.getX(), event.getY());
                            break;
                    }
                }

                return false;
            }
        });
        mViewBackground = view.getBackground();
        if (mViewBackground == null) {
            mViewBackground = new ColorDrawable(Color.TRANSPARENT);
        }
        switch (rippleStyle) {
            case Light_Square:
                mSquare = true;
            case Light:
                baseColor = Color.WHITE;
                mBaseHotpotAlpha = 128 + RIPPLE_HOTPOT_ALPHA;
                mBasePressedAlpha = 128 + RIPPLE_PRESSED_ALPHA;
                break;
            case Dark_Square:
                mSquare = true;
            case Dark:
                baseColor = Color.BLACK;
                mBaseHotpotAlpha = RIPPLE_HOTPOT_ALPHA;
                mBasePressedAlpha = RIPPLE_PRESSED_ALPHA;
        }
        mPressedPaint = new Paint();
        mPressedPaint.setColor(baseColor);
        mPressedPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
        mHotpotPaint = new Paint();
        mHotpotPaint.setColor(baseColor);
        mHotpotPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
        if (Build.VERSION.SDK_INT >= 16) {
            view.setBackground(this);
        } else {
            view.setBackgroundDrawable(this);
        }
    }

    /**
     * 为一个视图添加涟漪效果
     *
     * @param view
     *         需要添加效果的视图
     * @param rippleStyle
     *         涟漪效果风格
     * @param rippleDuration
     *         涟漪时间
     *
     *         以毫秒为单位
     *
     * @see com.harreke.easyapp.enums.RippleStyle
     */
    public static void attach(View view, RippleStyle rippleStyle, long rippleDuration) {
        new RippleDrawable(view, rippleStyle, rippleDuration);
    }

    /**
     * 为一个视图添加涟漪效果
     *
     * @param view
     *         需要添加效果的视图
     * @param rippleStyle
     *         涟漪效果风格
     *
     * @see com.harreke.easyapp.enums.RippleStyle
     */
    public static void attach(View view, RippleStyle rippleStyle) {
        attach(view, rippleStyle, 300l);
    }

    /**
     * 为一个视图添加涟漪效果
     *
     * 默认使用Dark风格
     *
     * @param view
     *         需要添加效果的视图
     */
    public static void attach(View view) {
        attach(view, RippleStyle.Dark);
    }

    private void cancel() {
        if (mAnimator != null) {
            mAnimator.cancel();
            mAnimator = null;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        mViewBackground.draw(canvas);
        if (mEnabled) {
            if (mPressed) {
                mPressedPaint.setAlpha((int) mBasePressedAlpha);
                if (mSquare) {
                    canvas.drawCircle(mCenterPoint.x, mCenterPoint.y, mMaxRadius, mPressedPaint);
                } else {
                    canvas.drawRect(mBounds, mPressedPaint);
                }
            }
            if (mAnimator != null) {
                mHotpotPaint.setAlpha((int) mHotpotAlpha);
                canvas.drawCircle(mHotpotPoint.x, mHotpotPoint.y, mHotpotRadius, mHotpotPaint);
            }
        }
    }

    @Override
    public int getOpacity() {
        return 1;
    }

    public long getRippleDuration() {
        return mRippleDuration;
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
        mViewBackground.setBounds(mBounds);
        mCenterPoint.set(mBounds.exactCenterX(), bounds.exactCenterY());
    }

    @Override
    protected boolean onStateChange(int[] stateSet) {
        boolean changed = super.onStateChange(stateSet);

        boolean enabled = false;
        boolean pressed = false;

        for (int state : stateSet) {
            if (state == android.R.attr.state_enabled) {
                enabled = true;
            }
            if (state == android.R.attr.state_pressed) {
                pressed = true;
            }
        }
        mEnabled = enabled;
        if (mEnabled) {
            if (mPressed != pressed) {
                mPressed = pressed;
                invalidateSelf();
            }
            if (mPressed) {
                startHotpotRippleSlow();
            } else {
                startHotpotRippleFast();
            }
        }

        return changed;
    }

    @Override
    public void setAlpha(int alpha) {
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
    }

    private void startHotpotRippleFast() {
        PropertyValuesHolder coordinateHolder;
        PropertyValuesHolder radiusHolder;
        PropertyValuesHolder alphaHolder;

        cancel();
        coordinateHolder =
                PropertyValuesHolder.ofObject("hotpotCoordinate", mCoordinateTypeEvaluator, mHotpotPoint, mCenterPoint);
        radiusHolder = PropertyValuesHolder.ofFloat("hotpotRadius", mHotpotRadius, mMaxRadius);
        alphaHolder = PropertyValuesHolder.ofFloat("hotpotAlpha", mHotpotAlpha, 0f);
        mAnimator = ValueAnimator.ofPropertyValuesHolder(coordinateHolder, radiusHolder, alphaHolder);
        mAnimator.setDuration(mRippleDuration);
        mAnimator.addListener(mAnimatorListener);
        mAnimator.addUpdateListener(mUpdateFastListener);
        mAnimator.start();
    }

    private void startHotpotRippleSlow() {
        PropertyValuesHolder coordinateHolder;
        PropertyValuesHolder radiusHolder;

        cancel();
        mHotpotAlpha = mBaseHotpotAlpha;
        coordinateHolder =
                PropertyValuesHolder.ofObject("hotpotCoordinate", mCoordinateTypeEvaluator, mDownPoint, mCenterPoint);
        radiusHolder = PropertyValuesHolder.ofFloat("hotpotRadius", 0f, mMaxRadius);
        mAnimator = ValueAnimator.ofPropertyValuesHolder(coordinateHolder, radiusHolder);
        mAnimator.setDuration(2000l);
        mAnimator.addListener(mAnimatorListener);
        mAnimator.addUpdateListener(mUpdateSlowListener);
        mAnimator.start();
    }
}