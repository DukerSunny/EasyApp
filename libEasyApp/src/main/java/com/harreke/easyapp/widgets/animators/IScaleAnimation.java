package com.harreke.easyapp.widgets.animators;

import android.view.View;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/01/26
 */
public interface IScaleAnimation {
    public abstract float getCloseScaleX(View view);

    public abstract float getCloseScaleY(View view);

    public abstract float getOpenScaleX(View view);

    public abstract float getOpenScaleY(View view);
}