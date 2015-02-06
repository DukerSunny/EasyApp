package com.harreke.easyapp.widgets.animators;

import android.view.View;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/01/31
 */
public class RotateToExpandAnimator {
    public ToggleViewValueAnimator mControllerAnimator;
    public ToggleViewValueAnimator mLayoutAnimator;
    private boolean mToggledOn = false;

    public RotateToExpandAnimator(View controller, View layout) {
        mControllerAnimator = ToggleViewValueAnimator.animate(controller);
        mLayoutAnimator = ToggleViewValueAnimator.animate(layout);
    }

    public RotateToExpandAnimator controllerOffRotation(float rotation) {
        mControllerAnimator.rotationOff(rotation);

        return this;
    }

    public RotateToExpandAnimator controllerOnRotation(float rotation) {
        mControllerAnimator.rotationOn(rotation);

        return this;
    }

    public boolean isToggledOn() {
        return mToggledOn;
    }

    public RotateToExpandAnimator layoutOffHeight(int height) {
        mLayoutAnimator.heightOff(height);

        return this;
    }

    public RotateToExpandAnimator layoutOffWidth(int width) {
        mLayoutAnimator.widthOff(width);

        return this;
    }

    public RotateToExpandAnimator layoutOnHeight(int height) {
        mLayoutAnimator.heightOn(height);

        return this;
    }

    public RotateToExpandAnimator layoutOnWidth(int width) {
        mLayoutAnimator.widthOn(width);

        return this;
    }

    public void toggle(boolean animate) {
        if (isToggledOn()) {
            toggleOff(animate);
        } else {
            toggleOn(animate);
        }
    }

    public void toggleOff(boolean animate) {
        mToggledOn = false;
        mControllerAnimator.toggleOff(animate);
        mLayoutAnimator.toggleOff(animate);
    }

    public void toggleOn(boolean animate) {
        mToggledOn = true;
        mControllerAnimator.toggleOn(animate);
        mLayoutAnimator.toggleOn(animate);
    }
}