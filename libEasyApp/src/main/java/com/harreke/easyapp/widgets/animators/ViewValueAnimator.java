package com.harreke.easyapp.widgets.animators;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.harreke.easyapp.utils.JsonUtil;
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
public class ViewValueAnimator implements ValueAnimator.AnimatorUpdateListener, Animator.AnimatorListener {
    private final static long DURATION = 300l;
    private long mDuration = DURATION;
    private static boolean mDebug = false;
    private ValueAnimator mAnimator = null;
    private Animator.AnimatorListener mListener = null;
    private Map<String, Object> mMap = new HashMap<>();
    private ValueAnimator.AnimatorUpdateListener mUpdateListener = null;
    private View mView;
    private int mVisibilityEnd = View.VISIBLE;
    private int mVisibilityStart = View.VISIBLE;

    private ViewValueAnimator(View view) {
        mView = view;
    }

    public static ViewValueAnimator animate(View view) {
        return new ViewValueAnimator(view);
    }

    public final ViewValueAnimator alpha(float alpha) {
        mMap.put("alpha", alpha);

        return this;
    }

    private void animate() {
        mAnimator = makeAnimator();
        mAnimator.setDuration(mDuration);
        mAnimator.addUpdateListener(this);
        if (mUpdateListener != null) {
            mAnimator.addUpdateListener(mUpdateListener);
        }
        mAnimator.addListener(this);
        if (mListener != null) {
            mAnimator.addListener(mListener);
        }
        mAnimator.start();
    }

    public final void cancel() {
        if (mAnimator != null) {
            mAnimator.cancel();
            mAnimator = null;
        }
    }

    public final void clear() {
        mMap.clear();
    }

    public ViewValueAnimator debug(boolean debug) {
        mDebug = debug;

        return this;
    }

    public final ViewValueAnimator duration(long duration) {
        mDuration = duration;

        return this;
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

    public final ViewValueAnimator height(int height) {
        mMap.put("height", height);

        return this;
    }

    public ViewValueAnimator listener(Animator.AnimatorListener listener) {
        mListener = listener;

        return this;
    }

    private ValueAnimator makeAnimator() {
        List<PropertyValuesHolder> holderList = new ArrayList<>();

        if (mMap.containsKey("alpha")) {
            holderList.add(PropertyValuesHolder.ofFloat("alpha", getAlpha(), (float) mMap.get("alpha")));
        }
        if (mMap.containsKey("width")) {
            holderList.add(PropertyValuesHolder.ofInt("width", getWidth(), (int) mMap.get("width")));
        }
        if (mMap.containsKey("height")) {
            holderList.add(PropertyValuesHolder.ofInt("height", getHeight(), (int) mMap.get("height")));
        }
        if (mMap.containsKey("rotation")) {
            holderList.add(PropertyValuesHolder.ofFloat("rotation", getRotation(), (float) mMap.get("rotation")));
        }
        if (mMap.containsKey("scaleX")) {
            holderList.add(PropertyValuesHolder.ofFloat("scaleX", getScaleX(), (float) mMap.get("scaleX")));
        }
        if (mMap.containsKey("scaleY")) {
            holderList.add(PropertyValuesHolder.ofFloat("scaleY", getScaleY(), (float) mMap.get("scaleY")));
        }
        if (mMap.containsKey("x")) {
            holderList.add(PropertyValuesHolder.ofFloat("x", getX(), (float) mMap.get("x")));
        }
        if (mMap.containsKey("y")) {
            holderList.add(PropertyValuesHolder.ofFloat("y", getY(), (float) mMap.get("y")));
        }

        return ValueAnimator.ofPropertyValuesHolder(holderList.toArray(new PropertyValuesHolder[holderList.size()]));
    }

    @Override
    public void onAnimationCancel(Animator animation) {
    }

    @Override
    public final void onAnimationEnd(Animator animation) {
        mView.setVisibility(mVisibilityEnd);
    }

    @Override
    public void onAnimationRepeat(Animator animation) {
    }

    @Override
    public final void onAnimationStart(Animator animation) {
        mView.setVisibility(mVisibilityStart);
    }

    @Override
    public final void onAnimationUpdate(ValueAnimator animation) {
        if (mMap.containsKey("alpha")) {
            setAlpha((float) animation.getAnimatedValue("alpha"));
        }
        if (mMap.containsKey("width")) {
            setWidth((int) animation.getAnimatedValue("width"));
        }
        if (mMap.containsKey("height")) {
            setHeight((int) animation.getAnimatedValue("height"));
        }
        if (mMap.containsKey("rotation")) {
            setRotation((float) animation.getAnimatedValue("rotation"));
        }
        if (mMap.containsKey("scaleX")) {
            setScaleX((float) animation.getAnimatedValue("scaleX"));
        }
        if (mMap.containsKey("scaleY")) {
            setScaleY((float) animation.getAnimatedValue("scaleY"));
        }
        if (mMap.containsKey("x")) {
            setX((float) animation.getAnimatedValue("x"));
        }
        if (mMap.containsKey("y")) {
            setY((float) animation.getAnimatedValue("y"));
        }
    }

    public final ViewValueAnimator rotation(float rotation) {
        mMap.put("rotation", rotation);

        return this;
    }

    public final ViewValueAnimator scaleX(float scaleX) {
        mMap.put("scaleX", scaleX);

        return this;
    }

    public final ViewValueAnimator scaleY(float scaleY) {
        mMap.put("scaleY", scaleY);

        return this;
    }

    private void set() {
        if (mMap.containsKey("alpha")) {
            setAlpha((float) mMap.get("alpha"));
        }
        if (mMap.containsKey("width")) {
            setWidth((int) mMap.get("width"));
        }
        if (mMap.containsKey("height")) {
            setHeight((int) mMap.get("height"));
        }
        if (mMap.containsKey("rotation")) {
            setRotation((float) mMap.get("rotation"));
        }
        if (mMap.containsKey("scaleX")) {
            setScaleX((float) mMap.get("scaleX"));
        }
        if (mMap.containsKey("scaleY")) {
            setScaleY((float) mMap.get("scaleY"));
        }
        if (mMap.containsKey("x")) {
            setX((float) mMap.get("x"));
        }
        if (mMap.containsKey("y")) {
            setY((float) mMap.get("y"));
        }
    }

    private void setAlpha(float alpha) {
        if (mDebug) {
            Log.e(null, "set alpha to " + alpha);
        }
        ViewHelper.setAlpha(mView, alpha);
    }

    private void setHeight(int height) {
        ViewGroup.LayoutParams params;

        if (mDebug) {
            Log.e(null, "set height to " + height);
        }
        params = mView.getLayoutParams();
        params.height = height;
        mView.setLayoutParams(params);
    }

    private void setRotation(float rotation) {
        if (mDebug) {
            Log.e(null, "set rotation to " + rotation);
        }
        ViewHelper.setRotation(mView, rotation);
    }

    private void setScaleX(float scaleX) {
        if (mDebug) {
            Log.e(null, "set scaleX to " + scaleX);
        }
        ViewHelper.setScaleX(mView, scaleX);
    }

    private void setScaleY(float scaleY) {
        if (mDebug) {
            Log.e(null, "set scaleY to " + scaleY);
        }
        ViewHelper.setScaleY(mView, scaleY);
    }

    private void setWidth(int width) {
        ViewGroup.LayoutParams params;

        if (mDebug) {
            Log.e(null, "set width to " + width);
        }
        params = mView.getLayoutParams();
        params.width = width;
        mView.setLayoutParams(params);
    }

    private void setX(float x) {
        if (mDebug) {
            Log.e(null, "set x to " + x);
        }
        ViewHelper.setX(mView, x);
    }

    private void setY(float y) {
        if (mDebug) {
            Log.e(null, "set y to " + y);
        }
        ViewHelper.setY(mView, y);
    }

    public final ViewValueAnimator start(boolean animate) {
        if (mDebug) {
            Log.e(null, "map=" + JsonUtil.toString(mMap));
        }
        cancel();
        if (animate) {
            animate();
        } else {
            set();
        }

        return this;
    }

    public ViewValueAnimator updateListener(ValueAnimator.AnimatorUpdateListener updateListener) {
        mUpdateListener = updateListener;

        return this;
    }

    public final ViewValueAnimator visibilityEnd(int visibility) {
        mVisibilityEnd = visibility;

        return this;
    }

    public final ViewValueAnimator visibilityStart(int visibility) {
        mVisibilityStart = visibility;

        return this;
    }

    public final ViewValueAnimator width(int width) {
        mMap.put("width", width);

        return this;
    }

    public final ViewValueAnimator x(float x) {
        mMap.put("x", x);

        return this;
    }

    public final ViewValueAnimator y(float y) {
        mMap.put("y", y);

        return this;
    }
}