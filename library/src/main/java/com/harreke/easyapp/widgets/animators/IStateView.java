package com.harreke.easyapp.widgets.animators;

import android.support.annotation.NonNull;
import android.view.View;

import com.harreke.easyapp.widgets.transitions.ViewInfo;

/**
 * Created by 启圣 on 2015/6/19.
 */
public interface IStateView {
    IViewAnimator alphaEnd(float alpha);

    IViewAnimator alphaStart(float alpha);

    IViewAnimator backgroundColorEnd(int backgroundColor);

    IViewAnimator backgroundColorStart(int backgroundColor);

    IViewAnimator coordinateEnd(float x, float y);

    IViewAnimator coordinateEnd(@NonNull ViewInfo viewInfo);

    IViewAnimator coordinateEnd(@NonNull View view);

    IViewAnimator coordinateStart(float x, float y);

    IViewAnimator coordinateStart(@NonNull ViewInfo viewInfo);

    IViewAnimator coordinateStart(@NonNull View view);

    IViewAnimator heightEnd(int height);

    IViewAnimator heightStart(int height);

    IViewAnimator pivotEnd(float pivotX, float pivotY);

    IViewAnimator pivotStart(float pivotX, float pivotY);

    IViewAnimator pivotXEnd(float pivotX);

    IViewAnimator pivotXStart(float pivotX);

    IViewAnimator pivotYEnd(float pivotY);

    IViewAnimator pivotYStart(float pivotY);

    IViewAnimator rotationEnd(float rotation);

    IViewAnimator rotationStart(float rotation);

    IViewAnimator rotationXEnd(float rotationX);

    IViewAnimator rotationXStart(float rotationX);

    IViewAnimator rotationYEnd(float rotationY);

    IViewAnimator rotationYStart(float rotationY);

    IViewAnimator scaleEnd(float scaleX, float scaleY);

    IViewAnimator scaleStart(float scaleX, float scaleY);

    IViewAnimator scaleXEnd(float scaleX);

    IViewAnimator scaleXStart(float scaleX);

    IViewAnimator scaleYEnd(float scaleY);

    IViewAnimator scaleYStart(float scaleY);

    IViewAnimator sizeEnd(@NonNull ViewInfo viewInfo);

    IViewAnimator sizeEnd(int width, int height);

    IViewAnimator sizeEnd(@NonNull View view);

    IViewAnimator sizeStart(@NonNull ViewInfo viewInfo);

    IViewAnimator sizeStart(@NonNull View view);

    IViewAnimator sizeStart(int width, int height);

    IViewAnimator textColorEnd(int textColor);

    IViewAnimator textColorStart(int textColor);

    IViewAnimator translationEnd(float translationX, float translationY);

    IViewAnimator translationStart(float translationX, float translationY);

    IViewAnimator translationXEnd(float translationX);

    IViewAnimator translationXStart(float translationX);

    IViewAnimator translationYEnd(float translationY);

    IViewAnimator translationYStart(float translationY);

    IViewAnimator visibilityEnd(int visibilityOff);

    IViewAnimator visibilityReverseEnd(int visibilityReverse);

    IViewAnimator visibilityReverseStart(int visibilityReverse);

    IViewAnimator visibilityStart(int visibilityOff);

    IViewAnimator widthEnd(int width);

    IViewAnimator widthStart(int width);

    IViewAnimator xEnd(float x);

    IViewAnimator xStart(float x);

    IViewAnimator yEnd(float y);

    IViewAnimator yStart(float y);
}