package com.harreke.easyapp.holders.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.harreke.easyapp.frameworks.list.recyclerview.IRecyclerItemClickListener;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 *
 * RecyclerView的Holder
 *
 * @param <ITEM>
 *         项目类型
 */
public abstract class RecyclerHolder<ITEM> extends RecyclerView.ViewHolder implements IRecyclerHolder<ITEM>, View.OnClickListener {
    private IRecyclerItemClickListener mRecyclerItemClickListener = null;

    public RecyclerHolder(View convertView) {
        super(convertView);

        convertView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (mRecyclerItemClickListener != null) {
            mRecyclerItemClickListener.onItemClick(getPosition());
        }
    }

    public final void setOnItemClickListener(IRecyclerItemClickListener recyclerItemClickListener) {
        mRecyclerItemClickListener = recyclerItemClickListener;
    }
}