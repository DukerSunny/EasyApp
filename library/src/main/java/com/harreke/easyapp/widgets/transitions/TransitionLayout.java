package com.harreke.easyapp.widgets.transitions;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.harreke.easyapp.frameworks.base.ActivityFramework;
import com.harreke.easyapp.utils.ViewUtil;
import com.harreke.easyapp.widgets.animators.IViewAnimator;
import com.harreke.easyapp.widgets.animators.ViewAnimator;
import com.harreke.easyapp.widgets.animators.ViewAnimatorSet;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.view.ViewHelper;

import java.util.HashSet;
import java.util.Iterator;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/12/29
 */
@SuppressLint("ViewConstructor")
public class TransitionLayout extends FrameLayout {
    private FrameLayout mContentView;
    private ViewAnimator mContentViewAnimator = null;
    private ViewAnimatorSet mContentViewAnimatorSet;
    private View mMaskView;
    private ViewAnimator mMaskViewAnimator;
    private View mMeasureView = null;
    private OnTransitionListener mOnTransitionListener = null;
    private HashSet<SharedTransition> mSharedTransitionSet = new HashSet<>();
    private TransitionOptions mTransitionOptions = null;
    private Animator.AnimatorListener mTransitionListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            if (mMaskViewAnimator.isForward()) {
                if (!validateContent(true)) {
                    onPostEnter();
                }
            } else {
                onPostExit();
            }
        }
    };
    private Animator.AnimatorListener mContentViewAnimatorListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            if (mContentViewAnimatorSet.isForward()) {
                if (mOnTransitionListener != null) {
                    mOnTransitionListener.onPostEnter();
                }
            } else {
                setSharedPreExit();
                startExitTransition();
            }
        }
    };

    public TransitionLayout(ActivityFramework activity) {
        super(activity);

        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mMaskView = new View(activity);
        mMaskView.setLayoutParams(
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mContentView = new FrameLayout(activity);
        mContentView.setLayoutParams(
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        addView(mMaskView);
        addView(mContentView);

        mMaskViewAnimator = ViewAnimator.animate(mMaskView);
        mMaskViewAnimator.backgroundColorStart(Color.argb(0, 0, 0, 0)).backgroundColorEnd(Color.argb(192, 0, 0, 0))
                .listener(mTransitionListener);

        mContentViewAnimatorSet = new ViewAnimatorSet();
    }

    private void cancelContent() {
        if (mContentViewAnimator != null) {
            mContentViewAnimator.cancel();
            mContentViewAnimator = null;
        }
    }

    private void cancelShared() {
        Iterator<SharedTransition> iterator = mSharedTransitionSet.iterator();
        SharedTransition sharedTransition;

        while (iterator.hasNext()) {
            sharedTransition = iterator.next();
            sharedTransition.sharedViewAnimator.cancel();
            iterator.remove();
        }
    }

    protected int getContentHeight() {
        return mContentView.getMeasuredHeight();
    }

    public View getContentView() {
        return mContentView;
    }

    protected int getContentWidth() {
        return mContentView.getMeasuredWidth();
    }

    protected float getContentX() {
        return ViewHelper.getX(mContentView);
    }

    protected float getContentY() {
        return ViewHelper.getY(mContentView);
    }

    private void makeAnimationAnimator(TransitionOptions options) {
        int width = mContentView.getMeasuredWidth();
        int height = mContentView.getMeasuredHeight();

        cancelContent();
        mContentViewAnimator = ViewAnimator.animate(mContentView);
        switch (options.animation) {
            case Slide_Left:
                mContentViewAnimator.xStart(-width).xEnd(0f);
                break;
            case Slide_Top:
                mContentViewAnimator.yStart(-height).yEnd(0f);
                break;
            case Slide_Right:
                mContentViewAnimator.xStart(width).xEnd(0f);
                break;
            case Slide_Bottom:
                mContentViewAnimator.yStart(height).yEnd(0f);
                break;
        }
        mContentViewAnimator.visibilityStart(View.VISIBLE).visibilityReverseEnd(View.GONE);
    }

    private IViewAnimator makeSharedViewAnimator(View view, ViewInfo start, ViewInfo end) {
        return ViewAnimator.animate(view).coordinateStart(start).coordinateEnd(end).sizeStart(start).sizeEnd(end)
                .visibilityStart(VISIBLE).visibilityReverseStart(VISIBLE).visibilityReverseEnd(View.GONE);
    }

    private boolean needTransition() {
        return mTransitionOptions != null && mTransitionOptions.transition != ActivityTransition.None &&
                mTransitionOptions.transition != ActivityTransition.Animation;
    }

    protected void onPostEnter() {
        if (mOnTransitionListener != null) {
            mOnTransitionListener.onPostEnter();
        }
    }

    protected void onPostExit() {
        if (mOnTransitionListener != null) {
            mOnTransitionListener.onPostExit();
        }
    }

    public void release() {
        cancelContent();
        cancelShared();
    }

    public void setContentView(int layoutId) {
        View contentView;

        mContentView.removeAllViews();
        contentView = LayoutInflater.from(getContext()).inflate(layoutId, mContentView, false);
        mContentView.addView(contentView);
    }

    public void setContentVisibility(int visibility) {
        mContentView.setVisibility(visibility);
    }

    protected void setContentX(float contentX) {
        ViewHelper.setX(mContentView, contentX);
    }

    protected void setContentY(float contentY) {
        ViewHelper.setY(mContentView, contentY);
    }

    /**
     * 设置遮罩视图的透明度
     *
     * @param alpha 透明度（0-100%）
     */
    protected void setMaskAlpha(float alpha) {
        mMaskView.setBackgroundColor(Color.argb((int) (alpha * 192), 0, 0, 0));
    }

    public void setMaskVisibility(int visibility) {
        mMaskView.setVisibility(visibility);
    }

    public void setMeasureViewId(int layoutId) {
        setContentVisibility(View.INVISIBLE);
        mMeasureView = LayoutInflater.from(getContext()).inflate(layoutId, this, false);
        mMeasureView.setVisibility(INVISIBLE);
        addView(mMeasureView);
    }

    public void setOnTransitionListener(OnTransitionListener onTransitionListener) {
        mOnTransitionListener = onTransitionListener;
    }

    private void setSharedPostEnter() {
        Iterator<SharedTransition> iterator;
        SharedTransition sharedTransition;
        View sharedView;
        View endView;

        iterator = mSharedTransitionSet.iterator();
        while (iterator.hasNext()) {
            sharedTransition = iterator.next();
            sharedView = sharedTransition.sharedView;
            sharedView.setVisibility(GONE);
            endView = sharedTransition.endView;
            endView.setVisibility(VISIBLE);
        }
    }

    private void setSharedPreExit() {
        Iterator<SharedTransition> iterator;
        SharedTransition sharedTransition;
        View sharedView;
        View endView;

        iterator = mSharedTransitionSet.iterator();
        while (iterator.hasNext()) {
            sharedTransition = iterator.next();
            sharedView = sharedTransition.sharedView;
            sharedView.setVisibility(VISIBLE);
            endView = sharedTransition.endView;
            endView.setVisibility(GONE);
        }
    }

    public final void startEnterTransition(@Nullable TransitionOptions transitionOptions) {
        mTransitionOptions = transitionOptions;
        if (!validateTransition(true)) {
            onPostEnter();
        }
    }

    public void startExitTransition(@Nullable TransitionOptions options) {
        mTransitionOptions = options;
        if (!validateContent(false)) {
            startExitTransition();
        }
    }

    private void startExitTransition() {
        if (!validateTransition(false)) {
            onPostExit();
        }
    }

    private void startSharedViewEnterTransition() {
        Iterator<SharedViewInfo> viewInfoIterator;
        SharedViewInfo viewInfoEntry;
        ViewInfo startViewInfo;
        ViewInfo endViewInfo;
        View sharedView;
        View endView;
        int sharedViewId;
        IViewAnimator sharedViewAnimator;
        SharedTransition sharedTransition;

        if (mMeasureView != null) {
            viewInfoIterator = mTransitionOptions.viewInfoSet.iterator();
            while (viewInfoIterator.hasNext()) {
                viewInfoEntry = viewInfoIterator.next();
                sharedViewId = viewInfoEntry.endViewId;
                startViewInfo = viewInfoEntry.startViewInfo;
                sharedView = mMeasureView.findViewById(sharedViewId);
                sharedView.setVisibility(INVISIBLE);
                endViewInfo = ViewUtil.getViewInfo(sharedView, viewInfoEntry.endViewWithStatusBarHeight);
                ((ViewGroup) sharedView.getParent()).removeView(sharedView);
                addView(sharedView);
                ViewHelper.setX(sharedView, startViewInfo.x);
                ViewHelper.setY(sharedView, startViewInfo.y);
                endView = mContentView.findViewById(sharedViewId);
                endView.setVisibility(INVISIBLE);
                if (sharedView instanceof ImageView && endView instanceof ImageView) {
                    ((ImageView) sharedView).setImageBitmap(ViewUtil.bytes2Bitmap(viewInfoEntry.bitmap));
                    ((ImageView) endView).setImageBitmap(ViewUtil.bytes2Bitmap(viewInfoEntry.bitmap));
                } else if (sharedView instanceof TextView && endView instanceof TextView) {
                    ((TextView) sharedView).setText(viewInfoEntry.text);
                    ((TextView) endView).setText(viewInfoEntry.text);
                }
                if (mOnTransitionListener != null) {
                    mOnTransitionListener.onShared(sharedViewId, sharedView, endView, viewInfoEntry);
                }
                sharedViewAnimator = makeSharedViewAnimator(sharedView, startViewInfo, endViewInfo);
                sharedViewAnimator.play(true);
                sharedTransition = new SharedTransition(sharedViewId, sharedView, sharedViewAnimator, endView);
                mSharedTransitionSet.add(sharedTransition);
            }
            removeView(mMeasureView);
            mMeasureView = null;
        }
    }

    private void startSharedViewExitTransition() {
        Iterator<SharedTransition> iterator;
        SharedTransition sharedTransition;
        IViewAnimator sharedViewAnimator;

        iterator = mSharedTransitionSet.iterator();
        while (iterator.hasNext()) {
            sharedTransition = iterator.next();
            sharedViewAnimator = sharedTransition.sharedViewAnimator;
            sharedViewAnimator.playReverse(true);
        }
    }

    private boolean validateContent(boolean forward) {
        if (needTransition()) {
            switch (mTransitionOptions.transition) {
                case Shared:
                    if (forward) {
                        setSharedPostEnter();
                    }
                    break;
            }
        }
        if (mOnTransitionListener != null) {
            mContentViewAnimatorSet.listener(mContentViewAnimatorListener);
            if (forward) {
                mOnTransitionListener.onContentEnter(mContentView, mContentViewAnimatorSet);
                mContentViewAnimatorSet.play();
            } else {
                mOnTransitionListener.onContentExit(mContentView, mContentViewAnimatorSet);
                mContentViewAnimatorSet.playReverse();
            }

            return true;
        } else {
            return false;
        }
    }

    private boolean validateTransition(boolean forward) {
        if (needTransition()) {
            switch (mTransitionOptions.transition) {
                case Shared:
                    if (forward) {
                        startSharedViewEnterTransition();
                    } else {
                        startSharedViewExitTransition();
                    }
                    break;
            }
            if (forward) {
                mMaskViewAnimator.play(true);
            } else {
                mMaskViewAnimator.playReverse(true);
            }

            return true;
        } else {
            return false;
        }
    }
}