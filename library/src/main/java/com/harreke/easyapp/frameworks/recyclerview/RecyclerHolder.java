package com.harreke.easyapp.frameworks.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.harreke.easyapp.frameworks.base.IFramework;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/12/01
 */
public abstract class RecyclerHolder<ITEM> extends RecyclerView.ViewHolder {
    public RecyclerHolder(View itemView) {
        super(itemView);
    }

    protected View findViewById(int viewId) {
        return itemView.findViewById(viewId);
    }

    public abstract void loadImage(IFramework framework, ITEM item);

    public abstract void setItem(ITEM item);
}
