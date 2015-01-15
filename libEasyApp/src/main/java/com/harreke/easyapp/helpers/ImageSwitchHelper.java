package com.harreke.easyapp.helpers;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;

import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/01/08
 */
public class ImageSwitchHelper {
    private ViewPropertyAnimator[] mAnimators;
    private int mCount;
    private ImageView[] mImageViews;

    public ImageSwitchHelper(@NonNull ImageView... imageViews) {
        int i;

        mImageViews = imageViews;
        mCount = mImageViews.length;
        mAnimators = new ViewPropertyAnimator[mCount];
        for (i = 0; i < mCount; i++) {
            mAnimators[i] = ViewPropertyAnimator.animate(mImageViews[i]);
        }
    }

    private void cancel(int index) {
        mAnimators[index].cancel();
        scale(index, 1f);
    }

    public void clear() {
        int i;

        for (i = 0; i < mCount; i++) {
            cancel(i);
        }
        mImageViews = null;
        mAnimators = null;
    }

    public void hideAll() {
        int i;

        for (i = 0; i < mCount; i++) {
            cancel(i);
            mImageViews[i].setVisibility(View.GONE);
        }
    }

    private void scale(int index, float scale) {
        ViewHelper.setScaleX(mImageViews[index], scale);
        ViewHelper.setScaleY(mImageViews[index], scale);
    }

    public void showAll() {
        int i;

        for (i = 0; i < mCount; i++) {
            cancel(i);
            mImageViews[i].setVisibility(View.VISIBLE);
        }
    }

    private void startShow(int index) {
        mAnimators[index].cancel();
        mAnimators[index].scaleX(1f).scaleY(1f);
        mAnimators[index].setDuration(300l);
        mAnimators[index].start();
    }

    public void switchToImageView(@NonNull int... indexes) {
        switchToImageView(true, indexes);
    }

    public void switchToImageView(@NonNull ImageView... imageViews) {
        switchToImageView(true, imageViews);
    }

    public void switchToImageView(boolean animate, @NonNull ImageView... imageVies) {
        int i;
        int j;

        for (i = 0; i < mCount; i++) {
            for (j = 0; j < imageVies.length; j++) {
                if (mImageViews[i] == imageVies[j]) {
                    mImageViews[i].setVisibility(View.VISIBLE);
                    if (animate) {
                        scale(i, 0f);
                        startShow(i);
                    } else {
                        scale(i, 1f);
                    }
                } else {
                    scale(i, 1f);
                    mImageViews[i].setVisibility(View.GONE);
                }
            }
        }
    }

    public void switchToImageView(boolean animate, @NonNull int... indexes) {
        int i;
        int j;

        for (i = 0; i < mCount; i++) {
            for (j = 0; j < indexes.length; j++) {
                if (i == indexes[j]) {
                    mImageViews[i].setVisibility(View.VISIBLE);
                    if (animate) {
                        scale(i, 0f);
                        startShow(i);
                    } else {
                        scale(i, 1f);
                    }
                } else {
                    scale(i, 1f);
                    mImageViews[i].setVisibility(View.GONE);
                }
            }
        }
    }
}