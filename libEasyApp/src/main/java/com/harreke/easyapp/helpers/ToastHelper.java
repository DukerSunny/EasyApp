package com.harreke.easyapp.helpers;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.harreke.easyapp.R;
import com.harreke.easyapp.widgets.animators.ToggleViewValueAnimator;
import com.harreke.easyapp.widgets.circluarprogresses.CircularProgressView;

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
    private ToggleViewValueAnimator mToastAnimator;
    private CircularProgressView toast_icon;
    private View toast_root;
    private TextView toast_text;

    public ToastHelper(ViewGroup rootView) {
        toast_root = LayoutInflater.from(rootView.getContext()).inflate(R.layout.widget_toast, rootView, false);
        rootView.addView(toast_root);
        toast_icon = (CircularProgressView) toast_root.findViewById(R.id.toast_icon);
        toast_text = (TextView) toast_root.findViewById(R.id.toast_text);

        mToastAnimator = ToggleViewValueAnimator.animate(toast_root).alphaOff(0f).alphaOn(1f).visibilityOff(View.GONE)
                .visibilityOn(View.VISIBLE);

        hide(false);
    }

    public void destroy() {
        Log.e(null, "destroy toast");
        ((ViewGroup) toast_root.getParent()).removeView(toast_root);
        toast_root.removeCallbacks(mHideRunnable);
        toast_root = null;
        mToastAnimator = null;
        toast_icon = null;
        toast_text = null;
    }

    public void hide() {
        hide(true);
    }

    public void hide(boolean animate) {
        mToastAnimator.toggleOff(animate);
    }

    private void setProgress(float progress) {
        toast_icon.setProgress(progress);
        if (progress == 0f) {
            toast_icon.setVisibility(View.GONE);
        } else {
            toast_icon.setVisibility(View.VISIBLE);
        }
    }

    private void show(boolean indeterminate) {
        mToastAnimator.toggleOn(true);
        if (!indeterminate) {
            toast_root.postDelayed(mHideRunnable, 3000l);
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