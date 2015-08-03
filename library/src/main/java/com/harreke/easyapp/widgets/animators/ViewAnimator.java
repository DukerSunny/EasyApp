package com.harreke.easyapp.widgets.animators;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

import com.harreke.easyapp.utils.ViewUtil;
import com.harreke.easyapp.widgets.transitions.ViewInfo;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.ValueAnimator;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/01/26
 */
public class ViewAnimator implements IViewAnimator {
    private ValueAnimator mAnimator = null;
    private boolean mDebug = false;
    private long mDelay = 0l;
    private long mDuration = ViewAnimatorUtil.DURATION;
    private Map<String, Object> mEndMap = new HashMap<>();
    private boolean mForward = true;
    private Interpolator mInterpolator = new AccelerateDecelerateInterpolator();
    private Animator.AnimatorListener mOuterListener = null;
    private boolean mPlayedToEnd = false;
    private Map<String, Object> mStartMap = new HashMap<>();
    private ValueAnimator.AnimatorUpdateListener mUpdateOuterListener = null;
    private WeakReference<View> mViewRef = null;
    private Animator.AnimatorListener mInnerListener = new AnimatorListenerAdapter() {
        @SuppressWarnings("ResourceType")
        @Override
        public void onAnimationEnd(Animator animation) {
            View view = getView();

            if (view != null) {
                if (mForward) {
                    if (ViewAnimatorUtil.containsVisibility(mEndMap)) {
                        view.setVisibility(ViewAnimatorUtil.getVisibility(mEndMap));
                    }
                } else {
                    if (ViewAnimatorUtil.containsVisibilityReverse(mEndMap)) {
                        view.setVisibility(ViewAnimatorUtil.getVisibilityReverse(mEndMap));
                    }
                }
                mPlayedToEnd = mForward;
            }
        }

        @SuppressWarnings("ResourceType")
        @Override
        public void onAnimationStart(Animator animation) {
            View view = getView();

            if (view != null) {
                if (mForward) {
                    if (ViewAnimatorUtil.containsVisibility(mStartMap)) {
                        view.setVisibility(ViewAnimatorUtil.getVisibility(mStartMap));
                    }
                } else {
                    if (ViewAnimatorUtil.containsVisibilityReverse(mStartMap)) {
                        view.setVisibility(ViewAnimatorUtil.getVisibilityReverse(mStartMap));
                    }
                }
            }
        }
    };
    private ValueAnimator.AnimatorUpdateListener mUpdateInnerListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            View view = getView();

            if (view != null) {
                ViewAnimatorUtil.update(view, animation);
            }
        }
    };

    private ViewAnimator(@NonNull View view) {
        mViewRef = new WeakReference<>(view);
    }

    public static ViewAnimator animate(@NonNull View view) {
        return new ViewAnimator(view);
    }

    @Override
    public IViewAnimator alpha(float alpha) {
        ViewAnimatorUtil.setAlpha(mEndMap, alpha);

        return this;
    }

    @Override
    public IViewAnimator alphaEnd(float alpha) {
        return alpha(alpha);
    }

    @Override
    public IViewAnimator alphaStart(float alpha) {
        ViewAnimatorUtil.setAlpha(mStartMap, alpha);

        return this;
    }

    private void animate() {
        View view = getView();

        if (view != null) {
            if (mForward) {
                mAnimator = ViewAnimatorUtil.make(view, mStartMap, mEndMap, mDebug);
            } else {
                mAnimator = ViewAnimatorUtil.make(view, mEndMap, mStartMap, mDebug);
            }
            mAnimator.setDuration(mDuration);
            mAnimator.setStartDelay(mDelay);
            mAnimator.addUpdateListener(mUpdateInnerListener);
            if (mUpdateOuterListener != null) {
                mAnimator.addUpdateListener(mUpdateOuterListener);
            }
            mAnimator.addListener(mInnerListener);
            if (mOuterListener != null) {
                mAnimator.addListener(mOuterListener);
            }
            if (mInterpolator != null) {
                mAnimator.setInterpolator(mInterpolator);
            }
            mAnimator.start();
        }
    }

    @Override
    public IViewAnimator backgroundColor(int backgroundColor) {
        ViewAnimatorUtil.setBackgroundColor(mEndMap, backgroundColor);

        return this;
    }

    @Override
    public IViewAnimator backgroundColorEnd(int backgroundColor) {
        return backgroundColor(backgroundColor);
    }

    @Override
    public IViewAnimator backgroundColorStart(int backgroundColor) {
        ViewAnimatorUtil.setBackgroundColor(mStartMap, backgroundColor);

        return this;
    }

    @Override
    public IViewAnimator cancel() {
        if (mAnimator != null) {
            mAnimator.cancel();
            mAnimator = null;
        }

        return this;
    }

    @Override
    public IViewAnimator clear() {
        mEndMap.clear();
        mUpdateOuterListener = null;
        mOuterListener = null;
        mDelay = 0l;
        mDuration = ViewAnimatorUtil.DURATION;

        return this;
    }

    @Override
    public IViewAnimator coordinate(float x, float y) {
        ViewAnimatorUtil.setCoordinate(mEndMap, x, y);

        return this;
    }

    @Override
    public IViewAnimator coordinate(@NonNull View view) {
        return coordinate(ViewUtil.getViewInfo(view));
    }

    @Override
    public IViewAnimator coordinate(@NonNull ViewInfo viewInfo) {
        return coordinate(viewInfo.x, viewInfo.y);
    }

    @Override
    public IViewAnimator coordinateEnd(float x, float y) {
        return coordinate(x, y);
    }

    @Override
    public IViewAnimator coordinateEnd(@NonNull ViewInfo viewInfo) {
        return coordinate(viewInfo);
    }

    @Override
    public IViewAnimator coordinateEnd(@NonNull View view) {
        return coordinate(view);
    }

    @Override
    public IViewAnimator coordinateStart(@NonNull ViewInfo viewInfo) {
        return coordinateStart(viewInfo.x, viewInfo.y);
    }

    @Override
    public IViewAnimator coordinateStart(@NonNull View view) {
        return coordinateStart(ViewUtil.getViewInfo(view));
    }

    @Override
    public IViewAnimator coordinateStart(float x, float y) {
        ViewAnimatorUtil.setCoordinate(mStartMap, x, y);

        return this;
    }

    @Override
    public IViewAnimator debug(boolean debug) {
        mDebug = debug;

        return this;
    }

    @Override
    public IViewAnimator delay(long delay) {
        mDelay = delay;

        return this;
    }

    @Override
    public IViewAnimator duration(long duration) {
        mDuration = duration;

        return this;
    }

    @Override
    public View getView() {
        View view = null;

        if (mViewRef != null) {
            view = mViewRef.get();
        }
        if (view == null) {
            cancel();
            clear();
        }

        return view;
    }

    @Override
    public IViewAnimator height(int height) {
        ViewAnimatorUtil.setHeight(mEndMap, height);

        return this;
    }

    @Override
    public IViewAnimator heightEnd(int height) {
        return height(height);
    }

    @Override
    public IViewAnimator heightStart(int height) {
        ViewAnimatorUtil.setHeight(mStartMap, height);

        return this;
    }

    @Override
    public IViewAnimator interpolator(Interpolator interpolator) {
        mInterpolator = interpolator;

        return this;
    }

    @Override
    public boolean isForward() {
        return mForward;
    }

    public boolean isPlayedToEnd() {
        return mPlayedToEnd;
    }

    @Override
    public IViewAnimator listener(@Nullable Animator.AnimatorListener listener) {
        mOuterListener = listener;

        return this;
    }

    @Override
    public IViewAnimator pivot(float pivotX, float pivotY) {
        ViewAnimatorUtil.setPivot(mEndMap, pivotX, pivotY);

        return this;
    }

    @Override
    public IViewAnimator pivotEnd(float pivotX, float pivotY) {
        return pivot(pivotX, pivotY);
    }

    @Override
    public IViewAnimator pivotStart(float pivotX, float pivotY) {
        ViewAnimatorUtil.setPivot(mStartMap, pivotX, pivotY);

        return this;
    }

    @Override
    public IViewAnimator pivotX(float pivotX) {
        ViewAnimatorUtil.setPivotX(mEndMap, pivotX);

        return this;
    }

    @Override
    public IViewAnimator pivotXEnd(float pivotX) {
        return pivotX(pivotX);
    }

    @Override
    public IViewAnimator pivotXStart(float pivotX) {
        ViewAnimatorUtil.setPivotX(mStartMap, pivotX);

        return this;
    }

    @Override
    public IViewAnimator pivotY(float pivotY) {
        ViewAnimatorUtil.setPivotY(mEndMap, pivotY);

        return this;
    }

    @Override
    public IViewAnimator pivotYEnd(float pivotY) {
        return pivotY(pivotY);
    }

    @Override
    public IViewAnimator pivotYStart(float pivotY) {
        ViewAnimatorUtil.setPivotY(mStartMap, pivotY);

        return this;
    }

    @Override
    public IViewAnimator play(boolean animate) {
        playInner(true, animate);

        return this;
    }

    private void playInner(boolean forward, boolean animate) {
        View view = getView();

        if (view != null) {
            mForward = forward;
            cancel();
            if (animate) {
                animate();
            } else {
                if (mForward) {
                    ViewAnimatorUtil.set(view, mStartMap);
                } else {
                    ViewAnimatorUtil.set(view, mEndMap);
                }
                mPlayedToEnd = mForward;
                mInnerListener.onAnimationEnd(null);
                if (mOuterListener != null) {
                    mOuterListener.onAnimationEnd(null);
                }
            }
        }
    }

    @Override
    public IViewAnimator playReverse(boolean animate) {
        playInner(false, animate);

        return this;
    }

    @Override
    public IViewAnimator rotation(float rotation) {
        ViewAnimatorUtil.setRotation(mEndMap, rotation);

        return this;
    }

    @Override
    public IViewAnimator rotationEnd(float rotation) {
        return rotation(rotation);
    }

    @Override
    public IViewAnimator rotationStart(float rotation) {
        ViewAnimatorUtil.setRotation(mStartMap, rotation);

        return this;
    }

    @Override
    public IViewAnimator rotationX(float rotationX) {
        ViewAnimatorUtil.setRotationX(mEndMap, rotationX);

        return this;
    }

    @Override
    public IViewAnimator rotationXEnd(float rotationX) {
        return rotationX(rotationX);
    }

    @Override
    public IViewAnimator rotationXStart(float rotationX) {
        ViewAnimatorUtil.setRotationX(mStartMap, rotationX);

        return this;
    }

    @Override
    public IViewAnimator rotationY(float rotationY) {
        ViewAnimatorUtil.setRotationY(mEndMap, rotationY);

        return this;
    }

    @Override
    public IViewAnimator rotationYEnd(float rotationY) {
        return rotationY(rotationY);
    }

    @Override
    public IViewAnimator rotationYStart(float rotationY) {
        ViewAnimatorUtil.setRotationY(mStartMap, rotationY);

        return this;
    }

    @Override
    public IViewAnimator scale(float scaleX, float scaleY) {
        ViewAnimatorUtil.setScale(mEndMap, scaleX, scaleY);

        return this;
    }

    @Override
    public IViewAnimator scaleEnd(float scaleX, float scaleY) {
        return scale(scaleX, scaleY);
    }

    @Override
    public IViewAnimator scaleStart(float scaleX, float scaleY) {
        ViewAnimatorUtil.setScale(mStartMap, scaleX, scaleY);

        return this;
    }

    @Override
    public IViewAnimator scaleX(float scaleX) {
        ViewAnimatorUtil.setScaleX(mEndMap, scaleX);

        return this;
    }

    @Override
    public IViewAnimator scaleXEnd(float scaleX) {
        return scaleX(scaleX);
    }

    @Override
    public IViewAnimator scaleXStart(float scaleX) {
        ViewAnimatorUtil.setScaleX(mStartMap, scaleX);

        return this;
    }

    @Override
    public IViewAnimator scaleY(float scaleY) {
        ViewAnimatorUtil.setScaleY(mEndMap, scaleY);

        return this;
    }

    @Override
    public IViewAnimator scaleYEnd(float scaleY) {
        return scaleY(scaleY);
    }

    @Override
    public IViewAnimator scaleYStart(float scaleY) {
        ViewAnimatorUtil.setScaleY(mStartMap, scaleY);

        return this;
    }

    @Override
    public IViewAnimator size(@NonNull View view) {
        return sizeEnd(view);
    }

    @Override
    public IViewAnimator size(int width, int height) {
        ViewAnimatorUtil.setSize(mEndMap, width, height);

        return this;
    }

    @Override
    public IViewAnimator size(@NonNull ViewInfo viewInfo) {
        return size(viewInfo.width, viewInfo.height);
    }

    @Override
    public IViewAnimator sizeEnd(@NonNull ViewInfo viewInfo) {
        return size(viewInfo);
    }

    @Override
    public IViewAnimator sizeEnd(int width, int height) {
        return size(width, height);
    }

    @Override
    public IViewAnimator sizeEnd(@NonNull View view) {
        return size(view.getMeasuredWidth(), view.getMeasuredHeight());
    }

    @Override
    public IViewAnimator sizeStart(@NonNull ViewInfo viewInfo) {
        return sizeStart(viewInfo.width, viewInfo.height);
    }

    @Override
    public IViewAnimator sizeStart(@NonNull View view) {
        return sizeStart(view.getMeasuredWidth(), view.getMeasuredHeight());
    }

    @Override
    public IViewAnimator sizeStart(int width, int height) {
        ViewAnimatorUtil.setSize(mStartMap, width, height);

        return this;
    }

    @Override
    public IViewAnimator textColor(int textColor) {
        ViewAnimatorUtil.setTextColor(mEndMap, textColor);

        return this;
    }

    @Override
    public IViewAnimator textColorEnd(int textColor) {
        return textColor(textColor);
    }

    @Override
    public IViewAnimator textColorStart(int textColor) {
        ViewAnimatorUtil.setTextColor(mStartMap, textColor);

        return this;
    }

    @Override
    public IViewAnimator translation(float translationX, float translationY) {
        ViewAnimatorUtil.setTranslation(mEndMap, translationX, translationY);

        return this;
    }

    @Override
    public IViewAnimator translationEnd(float translationX, float translationY) {
        return translation(translationX, translationY);
    }

    @Override
    public IViewAnimator translationStart(float translationX, float translationY) {
        ViewAnimatorUtil.setTranslation(mStartMap, translationX, translationY);

        return this;
    }

    @Override
    public IViewAnimator translationX(float translationX) {
        ViewAnimatorUtil.setTranslationX(mEndMap, translationX);

        return this;
    }

    @Override
    public IViewAnimator translationXEnd(float translationX) {
        return translationX(translationX);
    }

    @Override
    public IViewAnimator translationXStart(float translationX) {
        ViewAnimatorUtil.setTranslationX(mStartMap, translationX);

        return this;
    }

    @Override
    public IViewAnimator translationY(float translationY) {
        ViewAnimatorUtil.setTranslationY(mEndMap, translationY);

        return this;
    }

    @Override
    public IViewAnimator translationYEnd(float translationY) {
        return translationY(translationY);
    }

    @Override
    public IViewAnimator translationYStart(float translationY) {
        ViewAnimatorUtil.setTranslationY(mStartMap, translationY);

        return this;
    }

    @Override
    public IViewAnimator updateListener(ValueAnimator.AnimatorUpdateListener updateListener) {
        mUpdateOuterListener = updateListener;

        return this;
    }

    @Override
    public IViewAnimator visibilityEnd(int visibilityOff) {
        ViewAnimatorUtil.setVisibility(mEndMap, visibilityOff);

        return this;
    }

    @Override
    public IViewAnimator visibilityReverseEnd(int visibilityReverse) {
        ViewAnimatorUtil.setVisibilityReverse(mEndMap, visibilityReverse);

        return this;
    }

    @Override
    public IViewAnimator visibilityReverseStart(int visibilityReverse) {
        ViewAnimatorUtil.setVisibilityReverse(mStartMap, visibilityReverse);

        return this;
    }

    @Override
    public IViewAnimator visibilityStart(int visibility) {
        ViewAnimatorUtil.setVisibility(mStartMap, visibility);

        return this;
    }

    public ViewAnimator width(int width) {
        ViewAnimatorUtil.setWidth(mEndMap, width);

        return this;
    }

    @Override
    public IViewAnimator widthEnd(int width) {
        return width(width);
    }

    @Override
    public IViewAnimator widthStart(int width) {
        ViewAnimatorUtil.setWidth(mStartMap, width);

        return this;
    }

    @Override
    public IViewAnimator x(float x) {
        ViewAnimatorUtil.setX(mEndMap, x);

        return this;
    }

    @Override
    public IViewAnimator xEnd(float x) {
        return x(x);
    }

    @Override
    public IViewAnimator xStart(float x) {
        ViewAnimatorUtil.setX(mStartMap, x);

        return this;
    }

    @Override
    public IViewAnimator y(float y) {
        ViewAnimatorUtil.setY(mEndMap, y);

        return this;
    }

    @Override
    public IViewAnimator yEnd(float y) {
        return y(y);
    }

    @Override
    public IViewAnimator yStart(float y) {
        ViewAnimatorUtil.setY(mStartMap, y);

        return this;
    }
}