package com.harreke.utils.holders.abslistview;

/**
 ExpandableListAdapter的ChildHolder接口

 @param <CHILD>
 子项目类型
 */
public interface IExpandableListChildHolder<CHILD> {
    /**
     当设置数据时触发，用于填充数据至该子项目视图

     @param child
     子项目对象
     */
    public void setItem(CHILD child);
}