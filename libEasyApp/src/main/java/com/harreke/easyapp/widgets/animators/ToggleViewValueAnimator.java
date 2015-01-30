package com.harreke.easyapp.widgets.animators;

import android.view.View;
import android.view.ViewGroup;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.PropertyValuesHolder;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.ViewHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/01/26
 */
public final class ToggleViewValueAnimator implements ValueAnimator.AnimatorUpdateListener {
    private final static long DURATION = 300l;
    private long mDuration = DURATION;
    private ValueAnimator mAnimator = null;
    private Map<String, Object> mOffHolderMap = new HashMap<>();
    private Animator.AnimatorListener mOffListener = null;
    private int mOffVisibility = View.VISIBLE;
    private Animator.AnimatorListener mInnerOffListener = new AnimatorListenerAdapter() {
        @Override
        public final void onAnimationEnd(Animator animation) {
            mView.setVisibility(mOffVisibility);
        }
    };
    private Map<String, Object> mOnHolderMap = new HashMap<>();
    private Animator.AnimatorListener mOnListener = null;
    private int mOnVisibility = View.VISIBLE;
    private Animator.AnimatorListener mInnerOnListener = new AnimatorListenerAdapter() {
        @Override
        public final void onAnimationStart(Animator animation) {
            mView.setVisibility(mOnVisibility);
        }
    };
    private boolean mToggledOn = false;
    private ValueAnimator.AnimatorUpdateListener mUpdateListener = null;
    private View mView;

    private ToggleViewValueAnimator(View view) {
        mView = view;
    }

    public static ToggleViewValueAnimator animate(View view) {
        return new ToggleViewValueAnimator(view);
    }

    public final ToggleViewValueAnimator alphaOff(float alpha) {
        mOffHolderMap.put("alpha", alpha);

        return this;
    }

    public final ToggleViewValueAnimator alphaOn(float alpha) {
        mOnHolderMap.put("alpha", alpha);

        return this;
    }

    private ToggleViewValueAnimator animate(Map<String, Object> holderMap, Animator.AnimatorListener innerListener,
            Animator.AnimatorListener outerListener, long duration) {
        cancel();
        mAnimator = ValueAnimator.ofPropertyValuesHolder(makePropertyValeHolders(holderMap));
        mAnimator.setDuration(duration);
        mAnimator.addUpdateListener(this);
        if (mUpdateListener != null) {
            mAnimator.addUpdateListener(mUpdateListener);
        }
        mAnimator.addListener(innerListener);
        if (outerListener != null) {
            mAnimator.addListener(outerListener);
        }
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

    private float getAlpha() {
        return ViewHelper.getAlpha(mView);
    }

    private int getHeight() {
        return mView.getMeasuredHeight();
    }

    private float getRotation() {
        return ViewHelper.getRotation(mView);
    }

    private float getScaleX() {
        return ViewHelper.getScaleX(mView);
    }

    private float getScaleY() {
        return ViewHelper.getScaleY(mView);
    }

    private int getWidth() {
        return mView.getMeasuredWidth();
    }

    private float getX() {
        return ViewHelper.getX(mView);
    }

    private float getY() {
        return ViewHelper.getY(mView);
    }

    public final ToggleViewValueAnimator heightOff(int height) {
        mOffHolderMap.put("height", height);

        return this;
    }

    public final ToggleViewValueAnimator heightOn(int height) {
        mOnHolderMap.put("height", height);

        return this;
    }

    public final boolean isToggledOn() {
        return mToggledOn;
    }

    private PropertyValuesHolder[] makePropertyValeHolders(Map<String, Object> holderMap) {
        List<PropertyValuesHolder> holderList = new ArrayList<>();

        if (holderMap.containsKey("alpha")) {
            holderList.add(PropertyValuesHolder.ofFloat("alpha", getAlpha(), (Float) holderMap.get("alpha")));
        }
        if (holderMap.containsKey("width")) {
            holderList.add(PropertyValuesHolder.ofFloat("width", getWidth(), (Integer) holderMap.get("width")));
        }
        if (holderMap.containsKey("height")) {
            holderList.add(PropertyValuesHolder.ofFloat("height", getHeight(), (Integer) holderMap.get("height")));
        }
        if (holderMap.containsKey("rotation")) {
            holderList.add(PropertyValuesHolder.ofFloat("rotation", getRotation(), (Float) holderMap.get("rotation")));
        }
        if (holderMap.containsKey("scaleX")) {
            holderList.add(PropertyValuesHolder.ofFloat("scaleX", getScaleX(), (Float) holderMap.get("scaleX")));
        }
        if (holderMap.containsKey("scaleY")) {
            holderList.add(PropertyValuesHolder.ofFloat("scaleY", getScaleY(), (Float) holderMap.get("scaleY")));
        }
        if (holderMap.containsKey("x")) {
            holderList.add(PropertyValuesHolder.ofFloat("x", getX(), (Float) holderMap.get("x")));
        }
        if (holderMap.containsKey("y")) {
            holderList.add(PropertyValuesHolder.ofFloat("y", getY(), (Float) holderMap.get("y")));
        }

        return holderList.toArray(new PropertyValuesHolder[holderList.size()]);
    }

    @Override
    public final void onAnimationUpdate(ValueAnimator animation) {
        updateAlpha((Float) animation.getAnimatedValue("alpha"));
        updateWidth((Integer) animation.getAnimatedValue("width"));
        updateHeight((Integer) animation.getAnimatedValue("height"));
        updateRotation((Float) animation.getAnimatedValue("rotation"));
        updateScaleX((Float) animation.getAnimatedValue("scaleX"));
        updateScaleY((Float) animation.getAnimatedValue("scaleY"));
        updateX((Float) animation.getAnimatedValue("x"));
        updateY((Float) animation.getAnimatedValue("y"));
    }

    public final ToggleViewValueAnimator rotationOff(float rotation) {
        mOffHolderMap.put("rotation", rotation);

        return this;
    }

    public final ToggleViewValueAnimator rotationOn(float rotation) {
        mOnHolderMap.put("rotation", rotation);

        return this;
    }

    public final ToggleViewValueAnimator scaleXOff(float scaleX) {
        mOffHolderMap.put("scaleX", scaleX);

        return this;
    }

    public final ToggleViewValueAnimator scaleXOn(float scaleX) {
        mOnHolderMap.put("scaleX", scaleX);

        return this;
    }

    public final ToggleViewValueAnimator scaleYOff(float scaleY) {
        mOffHolderMap.put("scaleY", scaleY);

        return this;
    }

    public final ToggleViewValueAnimator scaleYOn(float scaleY) {
        mOnHolderMap.put("scaleY", scaleY);

        return this;
    }

    private void setAlpha(float alpha) {
        ViewHelper.setAlpha(mView, alpha);
    }

    public final ToggleViewValueAnimator setDuration(long duration) {
        mDuration = duration;

        return this;
    }

    private void setHeight(int height) {
        ViewGroup.LayoutParams params = mView.getLayoutParams();

        params.height = height;
        mView.setLayoutParams(params);
    }

    public final ToggleViewValueAnimator setOffListener(Animator.AnimatorListener offListener) {
        mOffListener = offListener;

        return this;
    }

    public final ToggleViewValueAnimator setOnListener(Animator.AnimatorListener offListener) {
        mOnListener = offListener;

        return this;
    }

    private void setRotation(float rotation) {
        ViewHelper.setRotation(mView, rotation);
    }

    private void setScaleX(float scaleX) {
        ViewHelper.setScaleX(mView, scaleX);
    }

    private void setScaleY(float scaleY) {
        ViewHelper.setScaleY(mView, scaleY);
    }

    public final ToggleViewValueAnimator setUpdateListener(ValueAnimator.AnimatorUpdateListener updateListener) {
        mUpdateListener = updateListener;

        return this;
    }

    private void setWidth(int width) {
        ViewGroup.LayoutParams params = mView.getLayoutParams();

        params.width = width;
        mView.setLayoutParams(params);
    }

    private void setX(float x) {
        ViewHelper.setX(mView, x);
    }

    private void setY(float y) {
        ViewHelper.setY(mView, y);
    }

    public final ToggleViewValueAnimator toggle(boolean animate) {
        if (isToggledOn()) {
            toggleOff(animate);
        } else {
            toggleOn(animate);
        }

        return this;
    }

    public final ToggleViewValueAnimator toggleOff(boolean animate) {
        mToggledOn = false;

        return animate(mOffHolderMap, mInnerOffListener, mOffListener, animate ? mDuration : 0l);
    }

    public final ToggleViewValueAnimator toggleOn(boolean animate) {
        mToggledOn = true;

        return animate(mOnHolderMap, mInnerOnListener, mOnListener, animate ? mDuration : 0l);
    }

    private void updateAlpha(Float alpha) {
        if (alpha != null) {
            setAlpha(alpha);
        }
    }

    private void updateHeight(Integer height) {
        if (height != null) {
            setHeight(height);
        }
    }

    private void updateRotation(Float rotation) {
        if (rotation != null) {
            setRotation(rotation);
        }
    }

    private void updateScaleX(Float scaleX) {
        if (scaleX != null) {
            setScaleX(scaleX);
        }
    }

    private void updateScaleY(Float scaleY) {
        if (scaleY != null) {
            setScaleY(scaleY);
        }
    }

    private void updateWidth(Integer width) {
        if (width != null) {
            setWidth(width);
        }
    }

    private void updateX(Float x) {
        if (x != null) {
            setX(x);
        }
    }

    private void updateY(Float y) {
        if (y != null) {
            setY(y);
        }
    }

    public final ToggleViewValueAnimator visibilityOff(int visibility) {
        mOffVisibility = visibility;

        return this;
    }

    public final ToggleViewValueAnimator visibilityOn(int visibility) {
        mOnVisibility = visibility;

        return this;
    }

    public final ToggleViewValueAnimator widthOff(int width) {
        mOffHolderMap.put("width", width);

        return this;
    }

    public final ToggleViewValueAnimator widthOn(int width) {
        mOnHolderMap.put("width", width);

        return this;
    }

    public final ToggleViewValueAnimator xOff(float x) {
        mOffHolderMap.put("x", x);

        return this;
    }

    public final ToggleViewValueAnimator xOn(float x) {
        mOnHolderMap.put("x", x);

        return this;
    }

    public final ToggleViewValueAnimator yOff(float y) {
        mOffHolderMap.put("y", y);

        return this;
    }

    public final ToggleViewValueAnimator yOn(float y) {
        mOnHolderMap.put("y", y);

        return this;
    }
}