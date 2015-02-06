package com.harreke.easyapp.widgets.animators;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.harreke.easyapp.utils.JsonUtil;
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
    private static boolean mDebug = false;
    private ValueAnimator mAnimator = null;
    private Animator.AnimatorListener mListener = null;
    private Map<String, Object> mMap = null;
    private Map<String, Object> mMapOff = new HashMap<>();
    private Map<String, Object> mMapOn = new HashMap<>();
    private Animator.AnimatorListener mOuterListener = null;
    private Animator.AnimatorListener mOuterOffListener = null;
    private Animator.AnimatorListener mOuterOnListener = null;
    private boolean mToggledOn = true;
    private View mView;
    private int mVisibilityOff = View.VISIBLE;
    private Animator.AnimatorListener mOffListener = new AnimatorListenerAdapter() {
        @Override
        public final void onAnimationEnd(Animator animation) {
            mView.setVisibility(mVisibilityOff);
        }
    };
    private int mVisibilityOn = View.VISIBLE;
    private Animator.AnimatorListener mOnListener = new AnimatorListenerAdapter() {
        @Override
        public final void onAnimationStart(Animator animation) {
            mView.setVisibility(mVisibilityOn);
        }
    };

    private ToggleViewValueAnimator(View view) {
        mView = view;
    }

    public static ToggleViewValueAnimator animate(View view) {
        return new ToggleViewValueAnimator(view);
    }

    public final ToggleViewValueAnimator alphaOff(float alpha) {
        mMapOff.put("alpha", alpha);

        return this;
    }

    public final ToggleViewValueAnimator alphaOn(float alpha) {
        mMapOn.put("alpha", alpha);

        return this;
    }

    private void animate() {
        mAnimator = makeAnimator();
        mAnimator.setDuration(mDuration);
        mAnimator.addUpdateListener(this);
        mAnimator.addListener(mListener);
        if (mOuterListener != null) {
            mAnimator.addListener(mOuterListener);
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
        mMapOn.clear();
        mMapOff.clear();
    }

    public ToggleViewValueAnimator debug(boolean debug) {
        mDebug = debug;

        return this;
    }

    public final ToggleViewValueAnimator duration(long duration) {
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

    public final float getYOff() {
        if (mMapOff.containsKey("y")) {
            return (float) mMapOff.get("y");
        } else {
            return 0f;
        }
    }

    public final float getYOn() {
        if (mMapOn.containsKey("y")) {
            return (float) mMapOn.get("y");
        } else {
            return 0f;
        }
    }

    public final ToggleViewValueAnimator heightOff(int height) {
        mMapOff.put("height", height);

        return this;
    }

    public final ToggleViewValueAnimator heightOn(int height) {
        mMapOn.put("height", height);

        return this;
    }

    public final boolean isToggledOn() {
        return mToggledOn;
    }

    public ToggleViewValueAnimator listenerOff(Animator.AnimatorListener offListener) {
        mOuterOffListener = offListener;

        return this;
    }

    public ToggleViewValueAnimator listenerOn(Animator.AnimatorListener onListener) {
        mOuterOffListener = onListener;

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

    public final ToggleViewValueAnimator rotationOff(float rotation) {
        mMapOff.put("rotation", rotation);

        return this;
    }

    public final ToggleViewValueAnimator rotationOn(float rotation) {
        mMapOn.put("rotation", rotation);

        return this;
    }

    public final ToggleViewValueAnimator scaleXOff(float scaleX) {
        mMapOff.put("scaleX", scaleX);

        return this;
    }

    public final ToggleViewValueAnimator scaleXOn(float scaleX) {
        mMapOn.put("scaleX", scaleX);

        return this;
    }

    public final ToggleViewValueAnimator scaleYOff(float scaleY) {
        mMapOff.put("scaleY", scaleY);

        return this;
    }

    public final ToggleViewValueAnimator scaleYOn(float scaleY) {
        mMapOn.put("scaleY", scaleY);

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
        //        if (mDebug) {
        //            Log.e(null, "set alpha to " + alpha);
        //        }
        ViewHelper.setAlpha(mView, alpha);
    }

    private void setHeight(int height) {
        ViewGroup.LayoutParams params;

        //        if (mDebug) {
        //            Log.e(null, "set height to " + height);
        //        }
        params = mView.getLayoutParams();
        params.height = height;
        mView.setLayoutParams(params);
    }

    private void setRotation(float rotation) {
        //        if (mDebug) {
        //            Log.e(null, "set rotation to " + rotation);
        //        }
        ViewHelper.setRotation(mView, rotation);
    }

    private void setScaleX(float scaleX) {
        //        if (mDebug) {
        //            Log.e(null, "set scaleX to " + scaleX);
        //        }
        ViewHelper.setScaleX(mView, scaleX);
    }

    private void setScaleY(float scaleY) {
        //        if (mDebug) {
        //            Log.e(null, "set scaleY to " + scaleY);
        //        }
        ViewHelper.setScaleY(mView, scaleY);
    }

    private void setWidth(int width) {
        ViewGroup.LayoutParams params;

        //        if (mDebug) {
        //            Log.e(null, "set width to " + width);
        //        }
        params = mView.getLayoutParams();
        params.width = width;
        mView.setLayoutParams(params);
    }

    private void setX(float x) {
        //        if (mDebug) {
        //            Log.e(null, "set x to " + x);
        //        }
        ViewHelper.setX(mView, x);
    }

    private void setY(float y) {
        //        if (mDebug) {
        //            Log.e(null, "set y to " + y);
        //        }
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
        if (mDebug) {
            Log.e(null, "off map=" + JsonUtil.toString(mMapOff));
        }
        cancel();
        mMap = mMapOff;
        mListener = mOffListener;
        mOuterListener = mOuterOffListener;
        if (animate) {
            animate();
        } else {
            set();
        }

        return this;
    }

    public final ToggleViewValueAnimator toggleOn(boolean animate) {
        mToggledOn = true;
        cancel();
        if (mDebug) {
            Log.e(null, "on map=" + JsonUtil.toString(mMapOn));
        }
        mMap = mMapOn;
        mListener = mOnListener;
        mOuterListener = mOuterOnListener;
        if (animate) {
            animate();
        } else {
            set();
        }

        return this;
    }

    public final ToggleViewValueAnimator visibilityOff(int visibility) {
        mVisibilityOff = visibility;

        return this;
    }

    public final ToggleViewValueAnimator visibilityOn(int visibility) {
        mVisibilityOn = visibility;

        return this;
    }

    public final ToggleViewValueAnimator widthOff(int width) {
        mMapOff.put("width", width);

        return this;
    }

    public final ToggleViewValueAnimator widthOn(int width) {
        mMapOn.put("width", width);

        return this;
    }

    public final ToggleViewValueAnimator xOff(float x) {
        mMapOff.put("x", x);

        return this;
    }

    public final ToggleViewValueAnimator xOn(float x) {
        mMapOn.put("x", x);

        return this;
    }

    public final ToggleViewValueAnimator yOff(float y) {
        mMapOff.put("y", y);

        return this;
    }

    public final ToggleViewValueAnimator yOn(float y) {
        mMapOn.put("y", y);

        return this;
    }
}