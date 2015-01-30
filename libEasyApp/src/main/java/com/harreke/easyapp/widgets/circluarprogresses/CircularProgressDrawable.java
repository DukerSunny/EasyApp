package com.harreke.easyapp.widgets.circluarprogresses;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Handler;

import com.harreke.easyapp.frameworks.base.ApplicationFramework;
import com.harreke.easyapp.utils.ResourceUtil;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/12/10
 */
public class CircularProgressDrawable extends Drawable {
    private final static float ARC_MIN_DEGREE = 45f;
    private final static float ARC_MAX_DEGREE = 360f - ARC_MIN_DEGREE;
    private final static float ARC_SPEED_FAST = 10.8f;
    private float mEndArcSpeed = ARC_SPEED_FAST;
    private final static float ARC_SPEED_SLOW = 3.6f;
    private float mStartArcSpeed = ARC_SPEED_SLOW;
    private final static float ARC_STROKE = ApplicationFramework.Density * 3;
    private float mArc = 0f;
    private Paint mBottomPaint;
    private RectF mDrawBounds = new RectF();
    private Handler mHandler;
    private int mIntrinsicHeight = (int) (ApplicationFramework.Density * 24);
    private int mIntrinsicWidth = (int) (ApplicationFramework.Density * 24);
    private float mProgress = 0f;
    private float mStartArc = 0f;
    private Paint mTopPaint;

    public CircularProgressDrawable(Context context) {
        this(ResourceUtil.obtainThemeColor(context)[0]);
    }

    public CircularProgressDrawable(int color) {
        mHandler = new Handler();

        mTopPaint = new Paint();
        mTopPaint.setColor(color);
        mTopPaint.setStyle(Paint.Style.STROKE);
        mTopPaint.setStrokeWidth(ARC_STROKE);
        mTopPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));

        mBottomPaint = new Paint();
        mBottomPaint.setColor((color & 0x00ffffff) | 0x40000000);
        mBottomPaint.setStyle(Paint.Style.STROKE);
        mBottomPaint.setStrokeWidth(ARC_STROKE);
        mBottomPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
    }

    @Override
    public void draw(Canvas canvas) {
        if (mProgress >= 0f) {
            canvas.drawArc(mDrawBounds, 0, 360, false, mBottomPaint);
            canvas.drawArc(mDrawBounds, 0, mProgress * 360, false, mTopPaint);
        } else {
            canvas.drawArc(mDrawBounds, mStartArc, mArc, false, mTopPaint);
        }
    }

    @Override
    public int getIntrinsicHeight() {
        return mIntrinsicHeight;
    }

    @Override
    public int getIntrinsicWidth() {
        return mIntrinsicWidth;
    }

    @Override
    public int getOpacity() {
        return 1;
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        mIntrinsicWidth = bounds.width();
        mIntrinsicHeight = bounds.height();
        mDrawBounds
                .set(bounds.left + ARC_STROKE, bounds.top + ARC_STROKE, bounds.right - ARC_STROKE, bounds.bottom - ARC_STROKE);
    }

    @Override
    public void setAlpha(int alpha) {
        mTopPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
    }

    /**
     * 设置进度
     *
     * @param progress
     *         进度
     *
     *         0f~1f，表示从0%到100%
     *         -1f，表示一直转动
     */
    public void setProgress(float progress) {
        mHandler.removeCallbacks(mIndeterminateRunnable);
        if (progress < 0f) {
            mProgress = -1f;
            mStartArc = 0f;
            mStartArcSpeed = ARC_SPEED_SLOW;
            mEndArcSpeed = ARC_SPEED_FAST;
            mArc = 0f;
            mHandler.post(mIndeterminateRunnable);
        } else if (progress > 1f) {
            mProgress = 1f;
        } else {
            mProgress = progress;
        }
        invalidateSelf();
    }

    private void swapSpeed() {
        float temp;

        temp = mStartArcSpeed;
        mStartArcSpeed = mEndArcSpeed;
        mEndArcSpeed = temp;
    }

    private Runnable mIndeterminateRunnable = new Runnable() {
        @Override
        public void run() {
            float speedGap;

            if (mProgress == -1) {
                mStartArc += mStartArcSpeed;
                if (mStartArc >= 360f) {
                    mStartArc -= 360f;
                }
                speedGap = mEndArcSpeed - mStartArcSpeed;
                mArc += speedGap;
                if (speedGap > 0f && mArc >= ARC_MAX_DEGREE) {
                    swapSpeed();
                } else if (speedGap < 0f && mArc <= ARC_MIN_DEGREE) {
                    swapSpeed();
                }
                invalidateSelf();
                mHandler.post(mIndeterminateRunnable);
            }
        }
    };
}