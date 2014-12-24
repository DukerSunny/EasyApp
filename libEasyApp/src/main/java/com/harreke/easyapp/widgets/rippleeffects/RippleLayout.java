package com.harreke.easyapp.widgets.rippleeffects;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import com.harreke.easyapp.R;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/11/28
 */
public class RippleLayout extends FrameLayout {
    private final static int RIPPLE_FRAME_RATE = 40;
    private final static String TAG = "RippleLayout";
    private boolean mCollapse = false;
    private float mCollapseAlpha;
    private float mCollapseAlphaStep;
    private Paint mCollapsePaint;
    private int mCount;
    private boolean mDelayed = false;
    private boolean mExpand = false;
    private Paint mExpandPaint;
    private int mExpandRadius = 48;
    private int mExpandStep = 0;
    private float mExpandX = 0f;
    private float mExpandY = 0f;
    private float mHotpotAlpha = 255f;
    private float mHotpotAlphaStep = 0f;
    private Paint mHotpotPaint;
    private int mMaxRadius = 0;

    public RippleLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray style;
        int backgroundColor = getBackgroundColor();
        int rippleColor;
        int rippleDuration;

        style = context.obtainStyledAttributes(attrs, R.styleable.RippleLayout);
        rippleColor = style.getColor(R.styleable.RippleLayout_rippleBackgroundColor, makeRippleColor(backgroundColor));
        rippleDuration = style.getInt(R.styleable.app_rippleDuration, 600);
        style.recycle();

        mHotpotPaint = new Paint();
        mHotpotPaint.setColor(makeHotpotColor(backgroundColor));
        mHotpotPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
        mExpandPaint = new Paint();
        mExpandPaint.setColor(rippleColor);
        mExpandPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
        mCollapsePaint = new Paint();
        mCollapsePaint.setColor(rippleColor);
        mCollapsePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));

        mCount = rippleDuration * RIPPLE_FRAME_RATE / 1000;
        mHotpotAlphaStep = 255f / mCount;
        mCollapseAlphaStep = 128f / (mCount / 2);
        setClickable(true);
    }

    private void collapseRipple() {
        mCollapse = true;
        mCollapseAlpha = 128f;
        if (mHotpotAlpha == 0) {
            mDelayed = false;
            post(mRippleCollapseRunnable);
        } else {
            mDelayed = true;
        }
    }

    private void expandRipple() {
        mHotpotAlpha = 255f;
        mExpand = true;
        mExpandRadius = 48;
        post(mRippleExpandRunnable);
    }

    private int getBackgroundColor() {
        Drawable drawable = getBackground();

        if (drawable != null && drawable instanceof ColorDrawable) {
            return ((ColorDrawable) drawable).getColor();
        } else {
            setBackgroundColor(Color.TRANSPARENT);
            return Color.parseColor("#e2e2e2");
        }
    }

    private int makeHotpotColor(int backgroundColor) {
        int r = (backgroundColor >> 16) & 0xFF;
        int g = (backgroundColor >> 8) & 0xFF;
        int b = (backgroundColor) & 0xFF;

        r = (r - 16 < 0) ? 0 : r - 16;
        g = (g - 16 < 0) ? 0 : g - 16;
        b = (b - 16 < 0) ? 0 : b - 16;

        return Color.argb(128, r, g, b);
        //        return Color.rgb(r, g, b);
    }

    private int makeRippleColor(int backgroundColor) {
        int r = (backgroundColor >> 16) & 0xFF;
        int g = (backgroundColor >> 8) & 0xFF;
        int b = (backgroundColor) & 0xFF;

        //        r = (r - 8 < 0) ? 0 : r - 4;
        //        g = (g - 8 < 0) ? 0 : g - 4;
        //        b = (b - 8 < 0) ? 0 : b - 4;
        //
        //        return Color.rgb(r, g, b);

        return Color.argb(128, r, g, b);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mExpand) {
            if (!mCollapse) {
                canvas.drawCircle(mExpandX, mExpandY, mMaxRadius, mExpandPaint);
            }
            mHotpotPaint.setAlpha((int) mHotpotAlpha);
            canvas.drawCircle(mExpandX, mExpandY, mExpandRadius, mHotpotPaint);
        }
        if (mCollapse) {
            mCollapsePaint.setAlpha((int) mCollapseAlpha);
            canvas.drawCircle(mExpandX, mExpandY, mMaxRadius, mCollapsePaint);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!mExpand) {
                    mExpandX = event.getX();
                    mExpandY = event.getY();
                    expandRipple();
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (!mCollapse) {
                    collapseRipple();
                }
        }

        return false;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        mMaxRadius = (int) Math.sqrt(width * width + height * height);
        mExpandStep = (mMaxRadius - 48) / mCount;
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (!mCollapse) {
                    collapseRipple();
                }
        }

        return super.onTouchEvent(event);
    }

    private Runnable mRippleExpandRunnable = new Runnable() {
        @Override
        public void run() {
            if (mExpand) {
                if (mExpandRadius == mMaxRadius) {
                    if (mHotpotAlpha > 0f) {
                        mHotpotAlpha -= mHotpotAlphaStep;
                    } else if (mHotpotAlpha < 0f) {
                        mHotpotAlpha = 0f;
                    }
                }
                if (mExpandRadius < mMaxRadius) {
                    mExpandRadius += mExpandStep;
                } else if (mExpandRadius > mMaxRadius) {
                    mExpandRadius = mMaxRadius;
                }
                if (mHotpotAlpha > 0 || mExpandRadius < mMaxRadius) {
                    post(mRippleExpandRunnable);
                } else if (mDelayed) {
                    mDelayed = false;
                    post(mRippleCollapseRunnable);
                }
                invalidate();
            }
        }
    };

    private Runnable mRippleCollapseRunnable = new Runnable() {
        @Override
        public void run() {
            if (mCollapse) {
                if (mCollapseAlpha > 0f) {
                    mCollapseAlpha -= mCollapseAlphaStep;
                    post(mRippleCollapseRunnable);
                } else {
                    mExpand = false;
                    mCollapse = false;
                }
                invalidate();
            }
        }
    };
}