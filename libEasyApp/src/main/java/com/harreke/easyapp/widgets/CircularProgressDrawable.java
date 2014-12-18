package com.harreke.easyapp.widgets;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Handler;

import com.harreke.easyapp.frameworks.bases.application.ApplicationFramework;

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
    private float mArcDegree = 0f;
    private Handler mHandler;
    private RectF mOvalBounds = new RectF();
    private Paint mPaint;
    private float mProgress = 0f;
    private int mRadius = (int) (ApplicationFramework.Density * 21);
    private float mStartArcDegree = 0f;

    public CircularProgressDrawable() {
        mHandler = new Handler();
        mPaint = new Paint();
        mPaint.setColor(Color.LTGRAY);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
        mPaint.setStrokeWidth(ARC_STROKE);
        //        mPaint.setStrokeJoin(Paint.Join.ROUND);
        //        mPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    public void draw(Canvas canvas) {
        if (mProgress > -1) {
            canvas.drawArc(mOvalBounds, 0, mProgress * 360, false, mPaint);
        } else {
            canvas.drawArc(mOvalBounds, mStartArcDegree, mArcDegree, false, mPaint);
        }
    }

    @Override
    public int getIntrinsicHeight() {
        return mRadius;
    }

    @Override
    public int getIntrinsicWidth() {
        return mRadius;
    }

    @Override
    public int getOpacity() {
        return 1;
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        mOvalBounds
                .set(bounds.left + ARC_STROKE, bounds.top + ARC_STROKE, bounds.right - ARC_STROKE, bounds.bottom - ARC_STROKE);
    }

    @Override
    public void setAlpha(int alpha) {
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
        if (progress < 0f) {
            mProgress = -1f;
            mStartArcDegree = 0f;
            mStartArcSpeed = ARC_SPEED_SLOW;
            mEndArcSpeed = ARC_SPEED_FAST;
            mArcDegree = 0f;
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
                mStartArcDegree += mStartArcSpeed;
                if (mStartArcDegree >= 360f) {
                    mStartArcDegree -= 360f;
                }
                speedGap = mEndArcSpeed - mStartArcSpeed;
                mArcDegree += speedGap;
                if (speedGap > 0f && mArcDegree >= ARC_MAX_DEGREE) {
                    swapSpeed();
                } else if (speedGap < 0f && mArcDegree <= ARC_MIN_DEGREE) {
                    swapSpeed();
                }
                invalidateSelf();
                mHandler.post(mIndeterminateRunnable);
            }
        }
    };
}