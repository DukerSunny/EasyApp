package com.harreke.utils.holders.abslistview;

/**
 ListAdapter的Holder接口

 @param <ITEM>
 项目类型
 */
public interface IListHolder<ITEM> {
    /**
     当设置数据时触发，用于填充数据至该项目视图

     @param item
     项目对象
     */
    public void setItem(ITEM item);
}