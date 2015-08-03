package com.harreke.easyapp.helpers;

import android.graphics.PixelFormat;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.harreke.easyapp.R;
import com.harreke.easyapp.frameworks.base.ApplicationFramework;
import com.harreke.easyapp.frameworks.base.IFramework;
import com.harreke.easyapp.frameworks.base.IToast;
import com.harreke.easyapp.widgets.animators.ViewAnimator;
import com.harreke.easyapp.widgets.circluarprogresses.CircularProgressView;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;

import java.lang.ref.WeakReference;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/01/04
 */
public class ToastHelper implements IToast {
    public final static long DURATION_LONG = 3000l;
    public final static long DURATION_SHORT = 1500l;
    private boolean mAttached = false;
    private long mDuration = DURATION_LONG;
    private WeakReference<IFramework> mFrameworkRef = null;
    private Handler mHandler = new Handler();
    private ViewAnimator mToastAnimator = null;
    private Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hideToast();
        }
    };
    private WindowManager.LayoutParams mToastParams;
    private CircularProgressView toast_icon;
    private View toast_root;
    private final Animator.AnimatorListener mListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            if (mToastAnimator != null && !mToastAnimator.isForward()) {
                removeToast();
            }
        }

        @Override
        public void onAnimationStart(Animator animation) {
            if (mToastAnimator != null && mToastAnimator.isForward()) {
                addToast();
            }
        }
    };
    private TextView toast_text;

    public ToastHelper(@NonNull IFramework framework) {
        mFrameworkRef = new WeakReference<>(framework);
        toast_root = LayoutInflater.from(framework.getContext()).inflate(R.layout.widget_toast, null);
        mToastParams = new WindowManager.LayoutParams();
        mToastParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        mToastParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        mToastParams.gravity = Gravity.CENTER;
        mToastParams.y = (int) (ApplicationFramework.Density * 64);
        mToastParams.flags =
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        mToastParams.format = PixelFormat.TRANSLUCENT;
        mToastParams.windowAnimations = android.R.style.Animation_Toast;
        mToastParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        toast_icon = (CircularProgressView) toast_root.findViewById(R.id.toast_icon);
        toast_text = (TextView) toast_root.findViewById(R.id.toast_text);

        mToastAnimator = ViewAnimator.animate(toast_root);
        mToastAnimator.alphaStart(0f).alphaEnd(1f).visibilityStart(View.VISIBLE).visibilityReverseEnd(View.GONE).listener(mListener);
    }

    private void addToast() {
        IFramework framework = getFramework();

        if (framework != null && !mAttached) {
            mAttached = true;
            framework.getActivityFramework().getWindowManager().addView(toast_root, mToastParams);
        }
    }

    private void clearFramework() {
        if (mFrameworkRef != null) {
            mFrameworkRef.clear();
            mFrameworkRef = null;
        }
    }

    private IFramework getFramework() {
        return mFrameworkRef == null ? null : mFrameworkRef.get();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void hideToast() {
        hideToast(true);
    }

    private void hideToast(boolean animate) {
        if (mToastAnimator != null) {
            mToastAnimator.playReverse(animate);
        }
    }

    public void release() {
        removeToast();
        mHandler.removeCallbacks(mHideRunnable);
        clearFramework();
        toast_root = null;
        mToastAnimator = null;
    }

    private void removeToast() {
        IFramework framework = getFramework();

        if (framework != null && mAttached) {
            mAttached = false;
            toast_icon.setProgress(0f);
            try {
                framework.getActivityFramework().getWindowManager().removeViewImmediate(toast_root);
            } catch (IllegalArgumentException ignored) {
            }
        }
    }

    private void setProgress(float progress) {
        toast_icon.setProgress(progress);
        if (progress == 0f) {
            toast_icon.setVisibility(View.GONE);
        } else {
            toast_icon.setVisibility(View.VISIBLE);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setToastDuration(long duration) {
        mDuration = duration;
    }

    private void showToast(boolean indeterminate) {
        removeToast();
        if (mToastAnimator != null) {
            mToastAnimator.play(true);
            if (!indeterminate) {
                mHandler.postDelayed(mHideRunnable, mDuration);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showToast(int textId) {
        showToast(textId, false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showToast(String text) {
        showToast(text, false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showToast(int textId, boolean indeterminate) {
        if (indeterminate) {
            setProgress(-1f);
        } else {
            setProgress(0f);
        }
        toast_text.setText(toast_text.getResources().getText(textId));
        showToast(indeterminate);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showToast(String text, boolean indeterminate) {
        if (indeterminate) {
            setProgress(-1f);
        } else {
            setProgress(0f);
        }
        toast_text.setText(text);
        showToast(indeterminate);
    }
}