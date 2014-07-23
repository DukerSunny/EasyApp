package com.harreke.utils.holders.abslistview;

/**
 ExpandableListAdapter的GroupHolder接口

 @param <GROUP>
 项目类型
 */
public interface IExpandableListGroupHolder<GROUP> {
    /**
     当设置数据时触发，用于填充数据至该父项目视图

     @param group
     父项目对象
     */
    public void setItem(GROUP group);
}