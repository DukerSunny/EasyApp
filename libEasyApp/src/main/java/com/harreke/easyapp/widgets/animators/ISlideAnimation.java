package com.harreke.easyapp.widgets.animators;

import android.view.View;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/01/26
 */
public interface ISlideAnimation {
    public abstract float getCloseX(View view);

    public abstract float getCloseY(View view);

    public abstract float getOpenX(View view);

    public abstract float getOpenY(View view);
}