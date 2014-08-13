package com.harreke.easyapp.frameworks.list.recyclerview;

import android.view.View;

import com.harreke.easyapp.holders.recyclerview.IRecyclerHolder;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 *
 * RecyclerView的Adapter接口
 */
@Deprecated
public interface IRecycler<ITEM, HOLDER extends IRecyclerHolder<ITEM>> {
    /**
     * 生成条目视图容器
     *
     * @param position
     *         条目位置
     * @param convertView
     *         条目视图
     *
     * @return 条目视图容器
     */
    public HOLDER createHolder(int position, View convertView);

    /**
     * 生成条目视图
     *
     * @param position
     *         条目位置
     * @param item
     *         条目对象
     *
     * @return 条目视图
     */
    public View createView(int position, ITEM item);
}