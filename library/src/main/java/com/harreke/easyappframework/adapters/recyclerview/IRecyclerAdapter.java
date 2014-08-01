package com.harreke.easyappframework.adapters.recyclerview;

import com.harreke.easyappframework.holders.recyclerview.RecyclerHolder;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 *
 * RecyclerAdapter的接口
 */
public interface IRecyclerAdapter<I, H extends RecyclerHolder<I>> {
    /**
     * 生成项目视图容器
     *
     * @param position
     *         项目位置
     *
     * @return 项目视图容器
     */
    public H createHolder(int position);
}