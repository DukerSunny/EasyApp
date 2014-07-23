package com.harreke.utils.holders.recyclerview;

/**
 RecycleHolder的接口

 @param <ITEM>
 项目类型
 */
public interface IRecyclerHolder<ITEM> {
    /**
     当设置数据时触发，用于填充数据至该项目视图

     项目视图容器

     @param item
     项目对象
     */
    public void setItem(ITEM item);
}