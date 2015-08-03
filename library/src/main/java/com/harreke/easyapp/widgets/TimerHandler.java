package com.harreke.easyapp.widgets;

import android.os.Handler;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/03/25
 */
public class TimerHandler {
    private Handler mHandler = new Handler();
    private long mInterval = 0;
    private boolean mRunning = false;
    private Runnable mTask = new Runnable() {
        @Override
        public void run() {
            long startTime;
            long duration;

            if (mRunning) {
                startTime = System.currentTimeMillis();
                mTarget.run();
                duration = System.currentTimeMillis() - startTime;
                if (duration < mInterval) {
                    mHandler.postDelayed(this, mInterval - duration);
                } else {
                    mHandler.post(this);
                }
            }
        }
    };
    private Runnable mTarget = null;

    public TimerHandler(Runnable target, long interval) {
        if (target == null) {
            throw new IllegalArgumentException("Target must not be null!");
        }
        mTarget = target;
        if (interval == 0) {
            throw new IllegalArgumentException("Interval must be larger than 0!");
        }
        mInterval = interval;
    }

    public void setInterval(long interval) {
        if (interval > 0) {
            mHandler.removeCallbacks(mTarget);
            mInterval = interval;
            mHandler.postDelayed(mTarget, mInterval);
        }
    }

    public void start() {
        if (!mRunning) {
            mRunning = true;
            mHandler.post(mTask);
        }
    }

    public void stop() {
        if (mRunning) {
            mRunning = false;
            mHandler.removeCallbacks(mTask);
        }
    }
}