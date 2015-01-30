package com.harreke.easyapp.widgets.animators;

import android.view.View;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/01/26
 */
public interface IExpandAnimation {
    public abstract int getCloseHeight(View view);

    public abstract int getCloseWidth(View view);

    public abstract int getOpenHeight(View view);

    public abstract int getOpenWidth(View view);
}