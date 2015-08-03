package com.harreke.easyapp.widgets.animators;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/04/14
 */
public class ViewAnimatorSet {
    private List<IViewAnimator> mAnimatorList = new ArrayList<>();
    private WeakReference<IViewAnimator> mAnimatorRef = null;
    private boolean mForward = true;
    private Animator.AnimatorListener mOuterListener = null;
    private boolean mPlaying = false;
    private int mPlayingIndex = -1;
    private Animator.AnimatorListener mInnerListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            if (mPlaying) {
                if (mForward) {
                    if (mPlayingIndex < mAnimatorList.size() - 1) {
                        mPlayingIndex++;
                        playInner();
                    } else {
                        mPlaying = false;
                        if (mOuterListener != null) {
                            mOuterListener.onAnimationEnd(animation);
                        }
                    }
                } else {
                    if (mPlayingIndex > 0) {
                        mPlayingIndex--;
                        playInner();
                    } else {
                        mPlaying = false;
                        if (mOuterListener != null) {
                            mOuterListener.onAnimationEnd(animation);
                        }
                    }
                }
            }
        }

        @Override
        public void onAnimationStart(Animator animation) {
            if (mPlaying && mOuterListener != null) {
                if (mForward) {
                    if (mPlayingIndex == 0) {
                        mOuterListener.onAnimationStart(animation);
                    }
                } else {
                    if (mPlayingIndex == mAnimatorList.size() - 1) {
                        mOuterListener.onAnimationStart(animation);
                    }
                }
            }
        }
    };

    public ViewAnimatorSet() {
    }

    public static ViewAnimatorSet collect(@NonNull IViewAnimator... animators) {
        ViewAnimatorSet animatorSet = new ViewAnimatorSet();
        int i;

        for (i = 0; i < animators.length; i++) {
            animatorSet.add(animators[i]);
        }

        return animatorSet;
    }

    public ViewAnimatorSet add(@NonNull IViewAnimator animator) {
        mAnimatorList.add(animator);

        return this;
    }

    public ViewAnimatorSet addAll(@Nullable IViewAnimator... animators) {
        int i;

        if (animators != null) {
            for (i = 0; i < animators.length; i++) {
                add(animators[i]);
            }
        }

        return this;
    }

    public ViewAnimatorSet cancel() {
        IViewAnimator animator = getAnimator();

        mPlaying = false;
        if (animator != null) {
            animator.cancel();
        }
        clearAnimatorRef();

        return this;
    }

    private void clearAnimatorRef() {
        if (mAnimatorRef != null) {
            mAnimatorRef.clear();
            mAnimatorRef = null;
        }
    }

    private IViewAnimator getAnimator() {
        return mAnimatorRef != null ? mAnimatorRef.get() : null;
    }

    public boolean isForward() {
        return mForward;
    }

    public ViewAnimatorSet listener(@Nullable Animator.AnimatorListener listener) {
        mOuterListener = listener;

        return this;
    }

    public void play() {
        mForward = true;
        if (mAnimatorList.size() > 0) {
            mPlaying = true;
            mPlayingIndex = 0;
            playInner();
        } else {
            if (mOuterListener != null) {
                mOuterListener.onAnimationEnd(null);
            }
        }
    }

    private void playInner() {
        IViewAnimator animator = mAnimatorList.get(mPlayingIndex);

        clearAnimatorRef();
        mAnimatorRef = new WeakReference<>(animator);
        animator.listener(mInnerListener);
        if (mForward) {
            animator.play(true);
        } else {
            animator.playReverse(true);
        }
    }

    public void playReverse() {
        mForward = false;
        if (mAnimatorList.size() > 0) {
            mPlaying = true;
            mPlayingIndex = mAnimatorList.size() - 1;
            playInner();
        } else {
            if (mOuterListener != null) {
                mOuterListener.onAnimationEnd(null);
            }
        }
    }
}