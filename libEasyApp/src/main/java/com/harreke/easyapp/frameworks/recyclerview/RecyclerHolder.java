package com.harreke.easyapp.frameworks.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;

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

    public abstract void setItem(ITEM item);

    public void setOnClickListener(View.OnClickListener onClickListener) {
        itemView.setOnClickListener(onClickListener);
    }
}
