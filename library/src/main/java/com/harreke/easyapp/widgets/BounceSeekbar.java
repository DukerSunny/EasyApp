package com.harreke.easyapp.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.SeekBar;

import com.nineoldandroids.animation.ValueAnimator;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/03/25
 */
public class BounceSeekbar extends SeekBar implements SeekBar.OnSeekBarChangeListener, ValueAnimator.AnimatorUpdateListener {
    private ValueAnimator mAnimator = null;
    private float mCurMax = 100f;
    private float mCurProgress = 0;
    private float mLastOriProgress = 0;
    private float mMod = 0;
    private OnSeekBarChangeListener mOnSeekBarChangeListener = null;
    private float mOriMax = 0;

    public BounceSeekbar(Context context, AttributeSet attrs) {
        super(context, attrs);

        mOriMax = super.getMax();
        mMod = 1 / mOriMax;
        mLastOriProgress = super.getProgress();
        mCurProgress = calculateCurProgress(super.getProgress());

        super.setMax((int) mCurMax);
        super.setProgress((int) mCurProgress);

        super.setOnSeekBarChangeListener(this);
    }

    private float calculateCurProgress(int oriProgress) {
        return mCurMax * oriProgress / mOriMax;
    }

    private float calculateOriProgress(float curProgress) {
        return mCurProgress * mOriMax / mCurMax;
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        setProgress(((Float) animation.getAnimatedValue()).intValue());
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        float oriProgress;

        mCurProgress = progress;
        oriProgress = calculateOriProgress(mCurProgress) + 0.5f;
        Log.e(null, "last ori progress=" + mLastOriProgress + " ori progress=" + oriProgress);
        if (mOnSeekBarChangeListener != null && ((int) mLastOriProgress) != (int) oriProgress) {
            mLastOriProgress = oriProgress;
            Log.e(null, "progress changed to " + (int) oriProgress);
            mOnSeekBarChangeListener.onProgressChanged(this, (int) oriProgress, fromUser);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        if (mOnSeekBarChangeListener != null) {
            mOnSeekBarChangeListener.onStartTrackingTouch(this);
        }
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (mOnSeekBarChangeListener != null) {
            mOnSeekBarChangeListener.onStopTrackingTouch(this);
        }
        //        if (mCurProgress % mMod != 0) {
        //            startBounce();
        //        }
    }

    @Override
    public void setOnSeekBarChangeListener(OnSeekBarChangeListener onSeekBarChangeListener) {
        mOnSeekBarChangeListener = onSeekBarChangeListener;
    }

    private void startBounce() {
        float targetProgress = calculateCurProgress((int) ((mCurProgress / mCurMax) / mMod + 0.5f));

        mAnimator = ValueAnimator.ofFloat(mCurProgress, targetProgress);
        mAnimator.setDuration(300l);
        mAnimator.addUpdateListener(this);
        mAnimator.start();
    }
}