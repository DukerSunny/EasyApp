package com.harreke.easyapp.frameworks.recyclerview.itemanimators;

import android.support.v7.widget.RecyclerView;

import com.harreke.easyapp.widgets.animators.ViewAnimator;

import java.lang.ref.WeakReference;
import java.util.LinkedList;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/04/28
 */
public class SlideUpItemAnimator extends RecyclerView.ItemAnimator {
    private LinkedList<ViewAnimator> mPendingAddAnimatorList = new LinkedList<>();
    private LinkedList<ViewAnimator> mPendingChangeAnimatorList = new LinkedList<>();
    private LinkedList<ViewAnimator> mPendingMoveAnimatorList = new LinkedList<>();
    private LinkedList<ViewAnimator> mPendingRemoveAnimatorList = new LinkedList<>();
    private final WeakReference<RecyclerView> mRecyclerViewRef;

    public SlideUpItemAnimator(RecyclerView recyclerView) {
        mRecyclerViewRef = new WeakReference<RecyclerView>(recyclerView);
    }

    @Override
    public boolean animateAdd(RecyclerView.ViewHolder holder) {
        RecyclerView recyclerView = mRecyclerViewRef.get();

        if (recyclerView != null) {
            endAnimation(holder);

        }

        return false;
    }

    @Override
    public boolean animateChange(RecyclerView.ViewHolder oldHolder, RecyclerView.ViewHolder newHolder, int fromLeft, int fromTop, int toLeft, int toTop) {
        return false;
    }

    @Override
    public boolean animateMove(RecyclerView.ViewHolder holder, int fromX, int fromY, int toX, int toY) {
        return false;
    }

    @Override
    public boolean animateRemove(RecyclerView.ViewHolder holder) {

        return false;
    }

    @Override
    public void endAnimation(RecyclerView.ViewHolder item) {

    }

    @Override
    public void endAnimations() {

    }

    @Override
    public boolean isRunning() {
        return false;
    }

    @Override
    public void runPendingAnimations() {

    }

    private class PendingAnimator {
        private ViewAnimator mAnimator;
        private WeakReference<RecyclerView.ViewHolder> mHolderRef;

        public PendingAnimator(RecyclerView.ViewHolder holder, ViewAnimator animator) {
            mHolderRef = new WeakReference<RecyclerView.ViewHolder>(holder);
            mAnimator = animator;
        }

        public void cancel() {
            mHolderRef.clear();
            mAnimator.cancel();
        }

        public ViewAnimator getAnimator() {
            return mAnimator;
        }

        public RecyclerView.ViewHolder getHolder() {
            return mHolderRef.get();
        }
    }
}
