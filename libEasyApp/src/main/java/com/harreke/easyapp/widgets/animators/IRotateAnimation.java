package com.harreke.easyapp.widgets.animators;

import android.view.View;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/01/26
 */
public interface IRotateAnimation {
    public abstract float getCloseRotation(View view);

    public abstract float getOpenRotation(View view);
}