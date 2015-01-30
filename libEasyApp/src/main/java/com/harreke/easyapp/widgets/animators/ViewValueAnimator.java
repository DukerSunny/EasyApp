package com.harreke.easyapp.widgets.animators;

import android.view.View;
import android.view.ViewGroup;

import com.nineoldandroids.animation.Animator;
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
public class ViewValueAnimator implements ValueAnimator.AnimatorUpdateListener {
    private final static long DURATION = 300l;
    private long mDuration = DURATION;
    private ValueAnimator mAnimator = null;
    private Map<String, Object> mHolderMap = new HashMap<>();
    private Animator.AnimatorListener mListener = null;
    private ValueAnimator.AnimatorUpdateListener mUpdateListener = null;
    private View mView;

    private ViewValueAnimator(View view) {
        mView = view;
    }

    public static ViewValueAnimator animate(View view) {
        return new ViewValueAnimator(view);
    }

    public ViewValueAnimator alpha(float alpha) {
        mHolderMap.put("alpha", PropertyValuesHolder.ofFloat("alpha", getAlpha(), alpha));

        return this;
    }

    public ViewValueAnimator animate() {
        cancel();
        mAnimator = ValueAnimator.ofPropertyValuesHolder(makePropertyValeHolders());
        clear();
        mAnimator.setDuration(mDuration);
        mAnimator.addUpdateListener(this);
        if (mUpdateListener != null) {
            mAnimator.addUpdateListener(mUpdateListener);
        }
        if (mListener != null) {
            mAnimator.addListener(mListener);
        }
        mAnimator.start();

        return this;
    }

    public void cancel() {
        if (mAnimator != null) {
            mAnimator.cancel();
            mAnimator = null;
        }
    }

    public void clear() {
        mHolderMap.clear();
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

    public ViewValueAnimator height(int height) {
        mHolderMap.put("height", PropertyValuesHolder.ofFloat("height", getHeight(), height));

        return this;
    }

    private PropertyValuesHolder[] makePropertyValeHolders() {
        List<PropertyValuesHolder> holderList = new ArrayList<>();

        if (mHolderMap.containsKey("alpha")) {
            holderList.add(PropertyValuesHolder.ofFloat("alpha", getAlpha(), (Float) mHolderMap.get("alpha")));
        }
        if (mHolderMap.containsKey("width")) {
            holderList.add(PropertyValuesHolder.ofFloat("width", getWidth(), (Integer) mHolderMap.get("width")));
        }
        if (mHolderMap.containsKey("height")) {
            holderList.add(PropertyValuesHolder.ofFloat("height", getHeight(), (Integer) mHolderMap.get("height")));
        }
        if (mHolderMap.containsKey("rotation")) {
            holderList.add(PropertyValuesHolder.ofFloat("rotation", getRotation(), (Float) mHolderMap.get("rotation")));
        }
        if (mHolderMap.containsKey("scaleX")) {
            holderList.add(PropertyValuesHolder.ofFloat("scaleX", getScaleX(), (Float) mHolderMap.get("scaleX")));
        }
        if (mHolderMap.containsKey("scaleY")) {
            holderList.add(PropertyValuesHolder.ofFloat("scaleY", getScaleY(), (Float) mHolderMap.get("scaleY")));
        }
        if (mHolderMap.containsKey("x")) {
            holderList.add(PropertyValuesHolder.ofFloat("x", getX(), (Float) mHolderMap.get("x")));
        }
        if (mHolderMap.containsKey("y")) {
            holderList.add(PropertyValuesHolder.ofFloat("y", getY(), (Float) mHolderMap.get("y")));
        }

        return holderList.toArray(new PropertyValuesHolder[holderList.size()]);
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        updateAlpha((Float) animation.getAnimatedValue("alpha"));
        updateWidth((Integer) animation.getAnimatedValue("width"));
        updateHeight((Integer) animation.getAnimatedValue("height"));
        updateRotation((Float) animation.getAnimatedValue("rotation"));
        updateScaleX((Float) animation.getAnimatedValue("scaleX"));
        updateScaleY((Float) animation.getAnimatedValue("scaleY"));
        updateX((Float) animation.getAnimatedValue("x"));
        updateY((Float) animation.getAnimatedValue("y"));
    }

    public ViewValueAnimator rotation(float rotation) {
        mHolderMap.put("rotation", rotation);

        return this;
    }

    public ViewValueAnimator scaleX(float scaleX) {
        mHolderMap.put("scaleX", scaleX);

        return this;
    }

    public ViewValueAnimator scaleY(float scaleY) {
        mHolderMap.put("scaleY", scaleY);

        return this;
    }

    private void setAlpha(float alpha) {
        ViewHelper.setAlpha(mView, alpha);
    }

    public ViewValueAnimator setDuration(long duration) {
        mDuration = duration;

        return this;
    }

    private void setHeight(int height) {
        ViewGroup.LayoutParams params = mView.getLayoutParams();

        params.height = height;
        mView.setLayoutParams(params);
    }

    public final ViewValueAnimator setListener(Animator.AnimatorListener listener) {
        mListener = listener;

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

    public final ViewValueAnimator setUpdateListener(ValueAnimator.AnimatorUpdateListener updateListener) {
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

    public ViewValueAnimator width(int width) {
        mHolderMap.put("width", width);

        return this;
    }

    public ViewValueAnimator x(float x) {
        mHolderMap.put("x", x);

        return this;
    }

    public ViewValueAnimator y(float y) {
        mHolderMap.put("y", y);

        return this;
    }
}