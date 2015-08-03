package com.harreke.easyapp.helpers;

import android.view.View;

import com.harreke.easyapp.widgets.animators.IViewAnimator;
import com.harreke.easyapp.widgets.animators.ViewAnimator;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/01/08
 */
public class ViewSwitchHelper {
    private IViewAnimator[] mAnimators;
    private int mCount;
    private View[] mViews;

    public ViewSwitchHelper(View... views) {
        int i;

        if (views == null || views.length == 0) {
            throw new IllegalArgumentException("Views must not be null or empty!");
        }
        mViews = views;
        mCount = mViews.length;
        mAnimators = new ViewAnimator[mCount];
        for (i = 0; i < mCount; i++) {
            mAnimators[i] = ViewAnimator.animate(mViews[i]).scaleStart(0f, 0f).scaleEnd(1f, 1f).visibilityStart(View.VISIBLE).visibilityReverseEnd(View.GONE);
        }
    }

    public void destroy() {
        mViews = null;
        mAnimators = null;
    }

    public void hideAll(boolean animate) {
        int i;

        for (i = 0; i < mCount; i++) {
            mAnimators[i].playReverse(animate);
        }
    }

    public void showAll(boolean animate) {
        int i;

        for (i = 0; i < mCount; i++) {
            mAnimators[i].play(animate);
        }
    }

    public void switchToImageView(boolean animate, int... indexes) {
        int i;
        int j;

        if (indexes != null && indexes.length > 0) {
            for (i = 0; i < mCount; i++) {
                for (j = 0; j < indexes.length; j++) {
                    if (i == indexes[j]) {
                        mAnimators[i].play(animate);
                    } else {
                        mAnimators[i].playReverse(false);
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
                        mAnimators[i].play(animate);
                    } else {
                        mAnimators[i].playReverse(false);
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