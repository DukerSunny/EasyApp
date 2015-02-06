package com.harreke.easyapp.widgets.animators;

import android.view.View;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.view.ViewPropertyAnimator;

import java.util.HashMap;
import java.util.Map;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/01/26
 */
public final class ToggleViewPropertyAnimator {
    private final static long DURATION = 300l;
    private long mDuration = DURATION;
    private ViewPropertyAnimator mAnimator = null;
    private Map<String, Object> mOffHolderMap = new HashMap<>();
    private int mOffVisibility = View.VISIBLE;
    private Animator.AnimatorListener mInnerOffListener = new AnimatorListenerAdapter() {
        @Override
        public final void onAnimationEnd(Animator animation) {
            mView.setVisibility(mOffVisibility);
        }
    };
    private Map<String, Object> mOnHolderMap = new HashMap<>();
    private int mOnVisibility = View.VISIBLE;
    private Animator.AnimatorListener mInnerOnListener = new AnimatorListenerAdapter() {
        @Override
        public final void onAnimationStart(Animator animation) {
            mView.setVisibility(mOnVisibility);
        }
    };
    private boolean mToggledOn = false;
    private View mView;

    private ToggleViewPropertyAnimator(View view) {
        mView = view;
    }

    public static ToggleViewPropertyAnimator animate(View view) {
        return new ToggleViewPropertyAnimator(view);
    }

    public final ToggleViewPropertyAnimator alphaOff(float alpha) {
        mOffHolderMap.put("alpha", alpha);

        return this;
    }

    public final ToggleViewPropertyAnimator alphaOn(float alpha) {
        mOnHolderMap.put("alpha", alpha);

        return this;
    }

    private ToggleViewPropertyAnimator animate(Map<String, Object> holderMap, Animator.AnimatorListener innerListener,
            long duration) {
        cancel();
        mAnimator = makeAnimator(holderMap);
        mAnimator.setDuration(duration);
        mAnimator.setListener(innerListener);
        mAnimator.start();

        return this;
    }

    public final void cancel() {
        if (mAnimator != null) {
            mAnimator.cancel();
            mAnimator = null;
        }
    }

    public final void clear() {
        mOnHolderMap.clear();
        mOffHolderMap.clear();
    }

    public final boolean isToggledOn() {
        return mToggledOn;
    }

    private ViewPropertyAnimator makeAnimator(Map<String, Object> holderMap) {
        ViewPropertyAnimator animator = ViewPropertyAnimator.animate(mView);

        if (holderMap.containsKey("alpha")) {
            animator.alpha((float) holderMap.get("alpha"));
        }
        if (holderMap.containsKey("rotation")) {
            animator.rotation((float) holderMap.get("rotation"));
        }
        if (holderMap.containsKey("scaleX")) {
            animator.scaleX((float) holderMap.get("scaleX"));
        }
        if (holderMap.containsKey("scaleY")) {
            animator.scaleY((float) holderMap.get("scaleY"));
        }
        if (holderMap.containsKey("x")) {
            animator.x((float) holderMap.get("x"));
        }
        if (holderMap.containsKey("y")) {
            animator.y((float) holderMap.get("y"));
        }

        return animator;
    }

    public final ToggleViewPropertyAnimator rotationOff(float rotation) {
        mOffHolderMap.put("rotation", rotation);

        return this;
    }

    public final ToggleViewPropertyAnimator rotationOn(float rotation) {
        mOnHolderMap.put("rotation", rotation);

        return this;
    }

    public final ToggleViewPropertyAnimator scaleXOff(float scaleX) {
        mOffHolderMap.put("scaleX", scaleX);

        return this;
    }

    public final ToggleViewPropertyAnimator scaleXOn(float scaleX) {
        mOnHolderMap.put("scaleX", scaleX);

        return this;
    }

    public final ToggleViewPropertyAnimator scaleYOff(float scaleY) {
        mOffHolderMap.put("scaleY", scaleY);

        return this;
    }

    public final ToggleViewPropertyAnimator scaleYOn(float scaleY) {
        mOnHolderMap.put("scaleY", scaleY);

        return this;
    }

    public final ToggleViewPropertyAnimator setDuration(long duration) {
        mDuration = duration;

        return this;
    }

    public final ToggleViewPropertyAnimator toggle(boolean animate) {
        if (isToggledOn()) {
            toggleOff(animate);
        } else {
            toggleOn(animate);
        }

        return this;
    }

    public final ToggleViewPropertyAnimator toggleOff(boolean animate) {
        mToggledOn = false;
        //        return animate(mOnHolderMap, mInnerOnListener, mOnListener, animate ? mDuration : 0l);
        //        Log.e(null, "toggle off as " + JsonUtil.toString(mOffHolderMap));
        return animate(mOffHolderMap, mInnerOffListener, mDuration);
    }

    public final ToggleViewPropertyAnimator toggleOn(boolean animate) {
        mToggledOn = true;
        //        return animate(mOnHolderMap, mInnerOnListener, mOnListener, animate ? mDuration : 0l);
        //        Log.e(null, "toggle on as " + JsonUtil.toString(mOnHolderMap));
        return animate(mOnHolderMap, mInnerOnListener, mDuration);
    }

    public final ToggleViewPropertyAnimator visibilityOff(int visibility) {
        mOffVisibility = visibility;

        return this;
    }

    public final ToggleViewPropertyAnimator visibilityOn(int visibility) {
        mOnVisibility = visibility;

        return this;
    }

    public final ToggleViewPropertyAnimator widthOff(int width) {
        mOffHolderMap.put("width", width);

        return this;
    }

    public final ToggleViewPropertyAnimator widthOn(int width) {
        mOnHolderMap.put("width", width);

        return this;
    }

    public final ToggleViewPropertyAnimator xOff(float x) {
        mOffHolderMap.put("x", x);

        return this;
    }

    public final ToggleViewPropertyAnimator xOn(float x) {
        mOnHolderMap.put("x", x);

        return this;
    }

    public final ToggleViewPropertyAnimator yOff(float y) {
        mOffHolderMap.put("y", y);

        return this;
    }

    public final ToggleViewPropertyAnimator yOn(float y) {
        mOnHolderMap.put("y", y);

        return this;
    }
}