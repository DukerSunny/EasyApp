package com.harreke.easyapp.helpers;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.harreke.easyapp.R;
import com.harreke.easyapp.frameworks.base.ActivityFramework;
import com.harreke.easyapp.frameworks.base.FragmentFramework;
import com.harreke.easyapp.frameworks.base.IFramework;
import com.harreke.easyapp.frameworks.base.IIntent;
import com.harreke.easyapp.widgets.transitions.ActivityTransition;
import com.harreke.easyapp.widgets.transitions.TransitionOptions;

import java.lang.ref.WeakReference;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/04/09
 */
public class IntentHelper implements IIntent {
    private DelayRunnable mDelayRunnable = null;
    private WeakReference<IFramework> mFrameworkRef = null;
    private Handler mHandler = new Handler();

    public IntentHelper(IFramework framework) {
        mFrameworkRef = new WeakReference<>(framework);
    }

    private IFramework getFramework() {
        return mFrameworkRef == null ? null : mFrameworkRef.get();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void start(@NonNull Intent intent, @Nullable TransitionOptions options) {
        start(intent, 0, options);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void start(@NonNull Intent intent, int requestCode) {
        start(intent, requestCode, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void start(@NonNull Intent intent, int requestCode, @Nullable TransitionOptions options) {
        IFramework framework;
        ActivityFramework activity;
        FragmentFramework fragment;

        framework = getFramework();
        if (framework != null) {
            activity = framework.getActivityFramework();
            activity.hideToast();
            if (framework instanceof ActivityFramework) {
                if (options != null) {
                    intent.putExtra("transitionOptions", options.toBundle());
                    activity.startActivityForResult(intent, requestCode);
                    if (options.transition == ActivityTransition.Animation) {
                        activity.overridePendingTransition(options.animation.getEnterAnimationId(), R.anim.none);
                    } else {
                        activity.overridePendingTransition(R.anim.none, R.anim.none);
                    }
                } else {
                    activity.startActivityForResult(intent, requestCode);
                }
            } else {
                fragment = (FragmentFramework) framework;

                if (options != null) {
                    intent.putExtra("transitionOptions", options.toBundle());
                    fragment.startActivityForResult(intent, requestCode);
                    if (options.transition == ActivityTransition.Animation) {
                        activity.overridePendingTransition(options.animation.getEnterAnimationId(), R.anim.none);
                    } else {
                        activity.overridePendingTransition(R.anim.none, R.anim.none);
                    }
                } else {
                    fragment.startActivityForResult(intent, requestCode);
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void start(@NonNull Intent intent) {
        start(intent, 0);
    }

    @Override
    public void startDelay(@NonNull Intent intent, @Nullable TransitionOptions options, long delay) {
        startDelay(intent, 0, options, delay);
    }

    @Override
    public void startDelay(@NonNull Intent intent, int requestCode, @Nullable TransitionOptions options, long delay) {
        if (mDelayRunnable != null) {
            mHandler.removeCallbacks(mDelayRunnable);
        }
        mDelayRunnable = new DelayRunnable(intent, requestCode, options);
        mHandler.postDelayed(mDelayRunnable, delay);
    }

    @Override
    public void startDelay(@NonNull Intent intent, long delay) {
        startDelay(intent, null, delay);
    }

    private class DelayRunnable implements Runnable {
        private Intent mIntent;
        private TransitionOptions mOptions;
        private int mRequestCode;

        public DelayRunnable(Intent intent, int requestCode, TransitionOptions options) {
            mIntent = intent;
            mRequestCode = requestCode;
            mOptions = options;
        }

        @Override
        public void run() {
            mDelayRunnable = null;
            start(mIntent, mRequestCode, mOptions);
        }
    }
}