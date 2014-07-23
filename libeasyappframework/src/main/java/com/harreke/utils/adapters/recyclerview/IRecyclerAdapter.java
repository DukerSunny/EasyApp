package com.harreke.utils.adapters.recyclerview;

import com.harreke.utils.holders.recyclerview.RecyclerHolder;

/**
 RecyclerAdapter的接口
 */
public interface IRecyclerAdapter<I, H extends RecyclerHolder<I>> {
    /**
     生成项目视图容器

     @param position
     项目位置

     @return 项目视图容器
     */
    public H createHolder(int position);
}