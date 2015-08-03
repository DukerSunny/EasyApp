package com.harreke.easyapp.widgets.animators;

import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.Interpolator;

import com.nineoldandroids.animation.ValueAnimator;

/**
 * Created by 启圣 on 2015/6/19.
 */
public interface IViewAnimator extends IView, IStateView {
    IViewAnimator cancel();

    IViewAnimator clear();

    IViewAnimator debug(boolean debug);

    IViewAnimator delay(long delay);

    IViewAnimator duration(long duration);

    View getView();

    IViewAnimator interpolator(Interpolator interpolator);

    boolean isForward();

    IViewAnimator listener(@Nullable ValueAnimator.AnimatorListener listener);

    IViewAnimator play(boolean animate);

    IViewAnimator playReverse(boolean animate);

    IViewAnimator updateListener(@Nullable ValueAnimator.AnimatorUpdateListener updateListener);
}