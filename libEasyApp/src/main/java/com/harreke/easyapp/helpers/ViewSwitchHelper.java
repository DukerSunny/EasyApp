package com.harreke.easyapp.helpers;

import android.view.View;

import com.harreke.easyapp.widgets.animators.ToggleViewValueAnimator;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/01/08
 */
public class ViewSwitchHelper {
    private ToggleViewValueAnimator[] mAnimators;
    private int mCount;
    private View[] mViews;

    public ViewSwitchHelper(View... views) {
        int i;

        if (views == null || views.length == 0) {
            throw new IllegalArgumentException("Views must not be null or empty!");
        }
        mViews = views;
        mCount = mViews.length;
        mAnimators = new ToggleViewValueAnimator[mCount];
        for (i = 0; i < mCount; i++) {
            mAnimators[i] =
                    ToggleViewValueAnimator.animate(mViews[i]).visibilityOff(View.GONE).visibilityOn(View.VISIBLE).scaleXOff(0f)
                            .scaleXOn(1f).scaleYOff(0f).scaleYOn(1f);
        }
    }

    public void destroy() {
        mViews = null;
        mAnimators = null;
    }

    public void hideAll(boolean animate) {
        int i;

        for (i = 0; i < mCount; i++) {
            mAnimators[i].toggleOff(false);
        }
    }

    public void showAll(boolean animate) {
        int i;

        for (i = 0; i < mCount; i++) {
            mAnimators[i].toggleOn(animate);
        }
    }

    public void switchToImageView(boolean animate, int... indexes) {
        int i;
        int j;

        if (indexes != null && indexes.length > 0) {
            for (i = 0; i < mCount; i++) {
                for (j = 0; j < indexes.length; j++) {
                    if (i == indexes[j]) {
                        mAnimators[i].toggleOn(animate);
                    } else {
                        mAnimators[i].toggleOff(false);
                    }
                }
            }
        }
    }

    public void switchToView(boolean animate, View... views) {
        int i;
        int j;

        if (views != null && views.length > 0) {
            for (i = 0; i < mCount; i++) {
                for (j = 0; j < views.length; j++) {
                    if (mViews[i] == views[j]) {
                        mAnimators[i].toggleOn(animate);
                    } else {
                        mAnimators[i].toggleOff(false);
                    }
                }
            }
        }
    }

    public void switchToView(View... views) {
        switchToView(true, views);
    }

    public void switchToView(int... indexes) {
        switchToImageView(true, indexes);
    }
}