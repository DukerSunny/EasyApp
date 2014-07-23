package com.harreke.utils.pulltorefreshes.abslistview;

import android.view.View;

import com.harreke.utils.holders.abslistview.IExpandableListChildHolder;
import com.harreke.utils.holders.abslistview.IExpandableListGroupHolder;

/**
 ExpandableAdapter的接口
 */
public interface IExpandableList<GROUP, GROUPHOLDER extends IExpandableListGroupHolder<GROUP>, CHILD, CHILDHOLDER extends IExpandableListChildHolder<CHILD>> {
    /**
     通过子项目视图生成对应容器

     @param groupPosition
     父项目项目位置
     @param childPosition
     子项目位置
     @param view
     子项目视图

     @return 子项目视图容器
     */
    public CHILDHOLDER createChildHolder(int groupPosition, int childPosition, View view);

    /**
     生成子项目视图

     @param groupPosition
     府项目位置
     @param childPosition
     子项目位置
     @param child
     子项目对象

     @return 子项目视图
     */
    public View createChildView(int groupPosition, int childPosition, CHILD child);

    /**
     通过父项目视图生成对应容器

     @param groupPosition
     父项目位置
     @param view
     父项目视图

     @return 父项目视图容器
     */
    public GROUPHOLDER createGroupHolder(int groupPosition, View view);

    /**
     生成父项目视图

     @param groupPosition
     父项目位置
     @param group
     父项目对象

     @return 父项目视图
     */
    public View createGroupView(int groupPosition, GROUP group);

    /**
     设置指定位置的父项目是否可以被点击

     @param groupPosition
     父项目位置

     @return 是否可以被点击
     */
    public boolean isGroupClickable(int groupPosition);

    /**
     当列表某一子项目被点击时触发

     @param groupPosition
     父项目位置
     @param childPosition
     子项目位置
     @param child
     子项目对象
     */
    public void onChildItemClick(int groupPosition, int childPosition, CHILD child);

    /**
     当列表某一父项目被点击时触发

     @param groupPosition
     父项目位置
     @param group
     父项目对象
     */
    public void onGroupItemClick(int groupPosition, GROUP group);
}