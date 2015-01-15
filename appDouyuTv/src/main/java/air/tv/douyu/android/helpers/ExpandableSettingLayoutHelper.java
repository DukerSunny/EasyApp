package air.tv.douyu.android.helpers;

import android.view.View;
import android.view.ViewGroup;

import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/01/08
 */
public class ExpandableSettingLayoutHelper {
    private View mController;
    private ViewPropertyAnimator mControllerAnimator;
    private View mLayout;
    private ValueAnimator mLayoutAnimator = null;
    private int mLayoutMaxHeight;
    private boolean mShowing = true;
    private ValueAnimator.AnimatorUpdateListener mUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            setLayoutHeight((Integer) animation.getAnimatedValue());
        }
    };

    public ExpandableSettingLayoutHelper(View controller, View layout, int layoutMaxHeight) {
        mController = controller;
        mLayout = layout;
        mLayoutMaxHeight = layoutMaxHeight;

        mControllerAnimator = ViewPropertyAnimator.animate(mController);
    }

    private void animateTo(float rotation, int height) {
        mControllerAnimator.cancel();
        mControllerAnimator.rotation(rotation).setDuration(300l).start();
        if (mLayoutAnimator != null) {
            mLayoutAnimator.cancel();
        }
        mLayoutAnimator = ValueAnimator.ofInt(mLayout.getMeasuredHeight(), height);
        mLayoutAnimator.setDuration(300l);
        mLayoutAnimator.addUpdateListener(mUpdateListener);
        mLayoutAnimator.start();
    }

    public void hide(boolean animate) {
        mShowing = false;
        if (animate) {
            animateTo(-90f, 0);
        } else {
            ViewHelper.setRotation(mController, -90f);
            setLayoutHeight(0);
        }
    }

    public void hide() {
        hide(true);
    }

    public boolean isShowing() {
        return mShowing;
    }

    private void setLayoutHeight(int height) {
        ViewGroup.LayoutParams params = mLayout.getLayoutParams();

        params.height = height;
        mLayout.setLayoutParams(params);
    }

    public void show() {
        show(true);
    }

    public void show(boolean animate) {
        mShowing = true;
        if (animate) {
            animateTo(0f, mLayoutMaxHeight);
        } else {
            ViewHelper.setRotation(mController, 0f);
            setLayoutHeight(mLayoutMaxHeight);
        }
    }

    public void toggle(boolean animate) {
        if (mShowing) {
            hide(animate);
        } else {
            show(animate);
        }
    }

    public void toggle() {
        toggle(true);
    }
}