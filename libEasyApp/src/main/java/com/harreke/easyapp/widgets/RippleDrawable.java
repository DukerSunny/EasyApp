package com.harreke.easyapp.widgets;

import android.annotation.TargetApi;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/11/28
 *
 * 模仿Material风格涟漪效果的Drawable
 */
public class RippleDrawable extends Drawable {
    public final static int RIPPLE_STYLE_DARK = 0;
    public final static int RIPPLE_STYLE_LIGHT = 1;
    private final static int RIPPLE_DURATION = 600;
    private final static int RIPPLE_FRAME_RATE = 40;
    private final static int RIPPLE_HOTPOT_ALPHA = 32;
    private float mBaseHotpotAlpha = RIPPLE_HOTPOT_ALPHA;
    private final static int RIPPLE_PRESSED_ALPHA = 24;
    private float mBasePressedAlpha = RIPPLE_PRESSED_ALPHA;
    private float mCenterX = 0f;
    private float mCenterY = 0f;
    private Runnable mDelayClickRunnable = new Runnable() {
        @Override
        public void run() {
            mView.setPressed(false);
            mView.performClick();
        }
    };

    private boolean mEnabled = true;
    private int mFrameCount;
    private float mHotpotAlpha = 0f;
    private float mHotpotAlphaStep = 0f;
    private boolean mHotpotFading = false;
    private Paint mHotpotPaint;
    private float mHotpotRadius = 0;
    private float mHotpotRadiusStep = 0;
    private float mHotpotX = 0f;
    private float mHotpotXStep = 0f;
    private float mHotpotY = 0f;
    private float mHotpotYStep = 0f;
    private int mMaxRadius = 0;
    private boolean mPressed = false;
    private float mPressedAlpha = 0f;
    private Paint mPressedPaint;
    private int mRippleDuration;
    private boolean mRippling = false;
    private View mView;
    private Drawable mViewBackground = null;

    public RippleDrawable(View view, int rippleStyle, int rippleDuration) {
        int baseColor = Color.BLACK;

        mRippleDuration = rippleDuration;
        mView = view;
        mView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mHotpotX = event.getX();
                        mHotpotY = event.getY();
                        mHotpotXStep = (mCenterX - mHotpotX) / mFrameCount;
                        mHotpotYStep = (mCenterY - mHotpotY) / mFrameCount;
                        break;
                    case MotionEvent.ACTION_UP:
                        if (mPressed) {
                            return false;
                        } else {
                            mView.postDelayed(mDelayClickRunnable, mRippleDuration);
                        }

                        return true;
                }

                return false;
            }
        });
        mViewBackground = view.getBackground();
        if (mViewBackground == null) {
            mViewBackground = new ColorDrawable(Color.TRANSPARENT);
        }
        switch (rippleStyle) {
            case RIPPLE_STYLE_LIGHT:
                baseColor = Color.WHITE;
                mBaseHotpotAlpha = 128 + RIPPLE_HOTPOT_ALPHA;
                mBasePressedAlpha = 128 + RIPPLE_PRESSED_ALPHA;
                break;
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

        mFrameCount = mRippleDuration * RIPPLE_FRAME_RATE / 1000;
        mHotpotAlphaStep = mBaseHotpotAlpha / mFrameCount;
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
     *         暗色风格
     *         {@link #RIPPLE_STYLE_LIGHT}
     *         亮色风格
     * @param rippleDuration
     *         涟漪时间
     *
     *         以毫秒为单位
     */
    @TargetApi(16)
    public static void attach(View view, int rippleStyle, int rippleDuration) {
        RippleDrawable drawable = new RippleDrawable(view, rippleStyle, rippleDuration);

        if (Build.VERSION.SDK_INT >= 16) {
            view.setBackground(drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }
        view.setClickable(true);
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
     *         暗色风格
     *         {@link #RIPPLE_STYLE_LIGHT}
     *         亮色风格
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
            if (mPressed) {
                mPressedPaint.setAlpha((int) mPressedAlpha);
                canvas.drawCircle(mCenterX, mCenterY, mMaxRadius, mPressedPaint);
            }
            if (mRippling || mHotpotFading) {
                mHotpotPaint.setAlpha((int) mHotpotAlpha);
                canvas.drawCircle(mHotpotX, mHotpotY, mHotpotRadius, mHotpotPaint);
            }
        }
    }

    @Override
    public int getOpacity() {
        return 1;
    }

    @Override
    public boolean isStateful() {
        return true;
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        int width = bounds.width() / 2;
        int height = bounds.height() / 2;

        mMaxRadius = (int) Math.sqrt(width * width + height * height);
        mViewBackground.setBounds(bounds);
        mCenterX = bounds.centerX();
        mCenterY = bounds.centerY();
        mHotpotRadiusStep = mMaxRadius / mFrameCount;
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
            if (!mPressed && pressed) {
                mPressed = true;
                startRipple();
            } else if (mPressed && !pressed) {
                mPressed = false;
                invalidateSelf();
            }
        } else {
            mRippling = false;
            mHotpotFading = false;
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

    private void startHotpotFade() {
        mView.post(mHotpotFadeRunnable);
    }

    private void startRipple() {
        mHotpotAlpha = mBaseHotpotAlpha;
        mPressedAlpha = mBasePressedAlpha;
        mHotpotRadius = 0;
        mRippling = true;
        mHotpotFading = true;
        mView.removeCallbacks(mRippleRunnable);
        mView.removeCallbacks(mHotpotFadeRunnable);
        mView.post(mRippleRunnable);
        invalidateSelf();
    }

    private Runnable mHotpotFadeRunnable = new Runnable() {
        @Override
        public void run() {
            if (mEnabled && mHotpotFading) {
                mHotpotAlpha -= mHotpotAlphaStep;
                if (mHotpotAlpha < 0f) {
                    mHotpotAlpha = 0f;
                    mHotpotFading = false;
                } else {
                    mView.post(mHotpotFadeRunnable);
                }
                invalidateSelf();
            }
        }
    };

    private Runnable mRippleRunnable = new Runnable() {
        @Override
        public void run() {
            if (mEnabled && mRippling) {
                mHotpotRadius += mHotpotRadiusStep;
                mHotpotX += mHotpotXStep;
                mHotpotY += mHotpotYStep;
                if (mHotpotRadius > mMaxRadius) {
                    mHotpotRadius = mMaxRadius;
                    mRippling = false;
                    startHotpotFade();
                } else {
                    mView.post(mRippleRunnable);
                }
                invalidateSelf();
            }
        }
    };
}