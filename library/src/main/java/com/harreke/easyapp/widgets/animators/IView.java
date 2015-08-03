package com.harreke.easyapp.widgets.animators;

import android.support.annotation.NonNull;
import android.view.View;

import com.harreke.easyapp.widgets.transitions.ViewInfo;

/**
 * Created by 启圣 on 2015/6/19.
 */
public interface IView {
    IViewAnimator alpha(float alpha);

    IViewAnimator backgroundColor(int backgroundColor);

    IViewAnimator coordinate(float x, float y);

    IViewAnimator coordinate(@NonNull View view);

    IViewAnimator coordinate(@NonNull ViewInfo viewInfo);

    IViewAnimator height(int height);

    IViewAnimator pivot(float pivotX, float pivotY);

    IViewAnimator pivotX(float pivotX);

    IViewAnimator pivotY(float pivotY);

    IViewAnimator rotation(float rotation);

    IViewAnimator rotationX(float rotationX);

    IViewAnimator rotationY(float rotationY);

    IViewAnimator scale(float scaleX, float scaleY);

    IViewAnimator scaleX(float scaleX);

    IViewAnimator scaleY(float scaleY);

    IViewAnimator size(@NonNull View view);

    IViewAnimator size(int width, int height);

    IViewAnimator size(@NonNull ViewInfo viewInfo);

    IViewAnimator textColor(int textColor);

    IViewAnimator translation(float translationX, float translationY);

    IViewAnimator translationX(float translationX);

    IViewAnimator translationY(float translationY);

    IViewAnimator width(int width);

    IViewAnimator x(float x);

    IViewAnimator y(float y);
}