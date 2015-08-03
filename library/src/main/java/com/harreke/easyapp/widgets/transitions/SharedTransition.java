package com.harreke.easyapp.widgets.transitions;

import android.support.annotation.NonNull;
import android.view.View;

import com.harreke.easyapp.widgets.animators.IViewAnimator;

/**
 * Created by 启圣 on 2015/6/18.
 */
public class SharedTransition {
    public View endView;
    public View sharedView;
    public IViewAnimator sharedViewAnimator;
    public int sharedViewId;

    public SharedTransition(int sharedViewId, @NonNull View sharedView, @NonNull IViewAnimator sharedViewAnimator, @NonNull View endView) {
        this.sharedViewId = sharedViewId;
        this.sharedView = sharedView;
        this.sharedViewAnimator = sharedViewAnimator;
        this.endView = endView;
    }
}