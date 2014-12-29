package com.harreke.easyapp.widgets.rippleeffects;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.View;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/12/22
 */
public class RippleOnClickListener implements View.OnClickListener {
    private Handler mHandler;
    private View.OnClickListener mOnClickListener;
    private int mRippleDelay;

    private RippleOnClickListener(View.OnClickListener onClickListener, int rippleDuration) {
        mOnClickListener = onClickListener;
        mRippleDelay = (int) (rippleDuration);
        mHandler = new Handler();
    }

    public static void attach(View view, View.OnClickListener onClickListener) {
        Drawable drawable = view.getBackground();
        RippleOnClickListener rippleOnClickListener;

        if (drawable instanceof RippleDrawable) {
            rippleOnClickListener = new RippleOnClickListener(onClickListener, ((RippleDrawable) drawable).getRippleDuration());
            view.setOnClickListener(rippleOnClickListener);
        } else {
            view.setOnClickListener(onClickListener);
        }
    }

    @Override
    public void onClick(final View v) {
        if (mOnClickListener != null) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mOnClickListener.onClick(v);
                }
            }, mRippleDelay);
        }
    }
}
