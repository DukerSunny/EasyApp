package com.harreke.easyapp.widgets.animators;

import android.view.View;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/01/26
 */
public interface IAlphaAnimation {
    public abstract float getCloseAlpha(View view);

    public abstract float getOpenAlpha(View view);
}