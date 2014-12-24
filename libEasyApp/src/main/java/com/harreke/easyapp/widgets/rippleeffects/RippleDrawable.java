package com.harreke.easyapp.widgets.rippleeffects;

import android.annotation.TargetApi;
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

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.PropertyValuesHolder;
import com.nineoldandroids.animation.TypeEvaluator;
import com.nineoldandroids.animation.ValueAnimator;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/11/28
 *
 * 模仿Material风格涟漪效果的Drawable
 */
public class RippleDrawable extends Drawable {
    public final static int RIPPLE_STYLE_DARK = 0;
    public final static int RIPPLE_STYLE_DARK_SQUARE = 2;
    public final static int RIPPLE_STYLE_LIGHT = 1;
    public final static int RIPPLE_STYLE_LIGHT_SQUARE = 3;
    private final static int RIPPLE_DURATION = 300;
    private final static int RIPPLE_HOTPOT_ALPHA = 32;
    private float mBaseHotpotAlpha = RIPPLE_HOTPOT_ALPHA;
    private final static int RIPPLE_PRESSED_ALPHA = 24;
    private float mBasePressedAlpha = RIPPLE_PRESSED_ALPHA;
    private ObjectAnimator mAnimator = null;
    private Animator.AnimatorListener mFadeListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            mAnimator = null;
        }
    };
    private PointF mCenterPoint = new PointF(0f, 0f);
    private TypeEvaluator<PointF> mCoordinateTypeEvaluator = new TypeEvaluator<PointF>() {
        @Override
        public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
            return new PointF(startValue.x + fraction * (endValue.x - startValue.x),
                    startValue.y + fraction * (endValue.y - startValue.y));
        }
    };
    private PointF mDownPoint = new PointF(0f, 0f);
    private boolean mEnabled = true;
    private float mHotpotAlpha = 0f;
    private Paint mHotpotPaint;
    private PointF mHotpotPoint = new PointF(0f, 0f);
    private float mHotpotRadius = 0;
    private int mMaxRadius = 0;
    private boolean mPressed = false;
    private Paint mPressedPaint;
    private int mRippleDuration;
    private Animator.AnimatorListener mRippleListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            startHotpotFade();
        }
    };
    private boolean mSquare = false;
    private ValueAnimator.AnimatorUpdateListener mUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            invalidateSelf();
        }
    };
    private Drawable mViewBackground = null;

    private RippleDrawable(View view, int rippleStyle, int rippleDuration) {
        int baseColor = Color.BLACK;

        mRippleDuration = rippleDuration;
        view.setClickable(true);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mAnimator == null) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            mDownPoint.set(event.getX(), event.getY());
                            startHotpotRipple();
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
            case RIPPLE_STYLE_LIGHT_SQUARE:
                mSquare = true;
            case RIPPLE_STYLE_LIGHT:
                baseColor = Color.WHITE;
                mBaseHotpotAlpha = 128 + RIPPLE_HOTPOT_ALPHA;
                mBasePressedAlpha = 128 + RIPPLE_PRESSED_ALPHA;
                break;
            case RIPPLE_STYLE_DARK_SQUARE:
                mSquare = true;
            case RIPPLE_STYLE_DARK:
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
     *
     *         {@link #RIPPLE_STYLE_DARK}
     *         暗色矩形风格
     *         {@link #RIPPLE_STYLE_DARK_SQUARE}
     *         暗色正方形风格
     *         {@link #RIPPLE_STYLE_LIGHT}
     *         亮色矩形风格
     *         {@link #RIPPLE_STYLE_LIGHT_SQUARE}
     *         亮色正方形风格
     * @param rippleDuration
     *         涟漪时间
     *
     *         以毫秒为单位
     */
    @TargetApi(16)
    public static void attach(View view, int rippleStyle, int rippleDuration) {
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
     *         {@link #RIPPLE_STYLE_DARK}
     *         暗色矩形风格
     *         {@link #RIPPLE_STYLE_DARK_SQUARE}
     *         暗色正方形风格
     *         {@link #RIPPLE_STYLE_LIGHT}
     *         亮色矩形风格
     *         {@link #RIPPLE_STYLE_LIGHT_SQUARE}
     *         亮色正方形风格
     */
    public static void attach(View view, int rippleStyle) {
        attach(view, rippleStyle, RIPPLE_DURATION);
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
        attach(view, RIPPLE_STYLE_DARK);
    }

    @Override
    public void draw(Canvas canvas) {
        mViewBackground.draw(canvas);
        if (mEnabled) {
            if (mPressed && !mSquare) {
                mPressedPaint.setAlpha((int) mBasePressedAlpha);
                canvas.drawCircle(mCenterPoint.x, mCenterPoint.y, mMaxRadius, mPressedPaint);
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

    public int getRippleDuration() {
        return mRippleDuration;
    }

    @Override
    public boolean isStateful() {
        return true;
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        int width = bounds.width() / 2;
        int height = bounds.height() / 2;

        if (mSquare) {
            mMaxRadius = Math.min(width, height);
        } else {
            mMaxRadius = (int) Math.sqrt(width * width + height * height);
        }
        mViewBackground.setBounds(bounds);
        mCenterPoint.set(bounds.exactCenterX(), bounds.exactCenterY());
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
        } else {
            invalidateSelf();
        }

        return changed;
    }

    @Override
    public void setAlpha(int alpha) {
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
    }

    private void setHotpotAlpha(float value) {
        mHotpotAlpha = value;
    }

    private void setHotpotCoordinate(PointF value) {
        mHotpotPoint.set(value);
    }

    private void setHotpotRadius(float value) {
        mHotpotRadius = value;
    }

    private void startHotpotFade() {
        PropertyValuesHolder alphaHolder;

        mHotpotAlpha = mBaseHotpotAlpha;
        alphaHolder = PropertyValuesHolder.ofFloat("hotpotAlpha", mBaseHotpotAlpha, 0f);
        mAnimator = ObjectAnimator.ofPropertyValuesHolder(this, alphaHolder).setDuration(mRippleDuration);
        mAnimator.addListener(mFadeListener);
        mAnimator.addUpdateListener(mUpdateListener);
        mAnimator.start();
    }

    private void startHotpotRipple() {
        PropertyValuesHolder coordinateHolder;
        PropertyValuesHolder radiusHolder;

        mHotpotAlpha = mBaseHotpotAlpha;
        coordinateHolder =
                PropertyValuesHolder.ofObject("hotpotCoordinate", mCoordinateTypeEvaluator, mDownPoint, mCenterPoint);
        radiusHolder = PropertyValuesHolder.ofFloat("hotpotRadius", 0f, mMaxRadius);
        mAnimator = ObjectAnimator.ofPropertyValuesHolder(this, coordinateHolder, radiusHolder).setDuration(mRippleDuration);
        mAnimator.addListener(mRippleListener);
        mAnimator.addUpdateListener(mUpdateListener);
        mAnimator.start();
    }
}