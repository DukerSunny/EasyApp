package com.harreke.easyapp.helpers;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.harreke.easyapp.R;
import com.harreke.easyapp.widgets.CircularProgressDrawable;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

import java.lang.ref.WeakReference;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/01/04
 */
public class ToastHelper {
    private Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    private CircularProgressDrawable mProgressDrawable;
    private WeakReference<Activity> mReference;
    private ViewPropertyAnimator mToastAnimator = null;
    private ImageView toast_icon;
    private View toast_root;
    private TextView toast_text;

    public ToastHelper(Activity activity) {
        ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();

        toast_root = LayoutInflater.from(activity).inflate(R.layout.widget_toast, decorView, false);
        toast_icon = (ImageView) toast_root.findViewById(R.id.toast_icon);
        toast_text = (TextView) toast_root.findViewById(R.id.toast_text);

        decorView.addView(toast_root);

        mReference = new WeakReference<Activity>(activity);
        mProgressDrawable = new CircularProgressDrawable(Color.WHITE);
        toast_icon.setImageDrawable(mProgressDrawable);

        mToastAnimator = ViewPropertyAnimator.animate(toast_root);
        ViewHelper.setAlpha(toast_root, 0f);
        setProgress(0f);
    }

    private void cancel() {
        mToastAnimator.cancel();
        toast_root.removeCallbacks(mHideRunnable);
    }

    public void destroy() {
        mProgressDrawable.setProgress(0f);
        mToastAnimator.cancel();
        if (mReference.get() != null) {
            ((ViewGroup) mReference.get().getWindow().getDecorView()).removeView(toast_root);
            mReference.clear();
        }

        toast_root = null;
        mToastAnimator = null;
        toast_icon = null;
        toast_text = null;
        mProgressDrawable = null;
    }

    public void hide() {
        if (mReference.get() != null) {
            cancel();
            ViewHelper.setY(toast_root, mReference.get().getWindow().getDecorView().getMeasuredHeight() / 2f);
            mToastAnimator.alpha(0f).start();
        }
    }

    private void setProgress(float progress) {
        mProgressDrawable.setProgress(progress);
        if (progress == 0f) {
            toast_icon.setVisibility(View.GONE);
        } else {
            toast_icon.setVisibility(View.VISIBLE);
        }
    }

    private void show(boolean indeterminate) {
        if (mReference.get() != null) {
            cancel();
            ViewHelper.setY(toast_root, mReference.get().getWindow().getDecorView().getMeasuredHeight() / 2f);
            mToastAnimator.alpha(1f).setDuration(300l).start();
            if (!indeterminate) {
                toast_root.postDelayed(mHideRunnable, 3300l);
            }
        }
    }

    public void show(String text) {
        show(text, false);
    }

    public void show(String text, boolean indeterminate) {
        if (indeterminate) {
            setProgress(-1f);
        } else {
            setProgress(0f);
        }
        toast_text.setText(text);
        show(indeterminate);
    }
}