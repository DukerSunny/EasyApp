package com.harreke.easyappframework.frameworks.list.abslistview;

import android.view.View;

import com.harreke.easyappframework.holders.abslistview.IExpandableListChildHolder;
import com.harreke.easyappframework.holders.abslistview.IExpandableListGroupHolder;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/31
 *
 * ExpandableAdapter的接口
 *
 * @param <GROUP>
 *         父条目类型
 * @param <GROUPHOLDER>
 *         父条目Holder类型
 * @param <CHILD>
 *         子条目类型
 * @param <CHILDHOLDER>
 *         子条目Holder类型
 */
public interface IExpandableList<GROUP, GROUPHOLDER extends IExpandableListGroupHolder<GROUP>, CHILD, CHILDHOLDER extends IExpandableListChildHolder<CHILD>> {
    /**
     * 通过子条目视图生成对应容器
     *
     * @param groupPosition
     *         父条目条目位置
     * @param childPosition
     *         子条目位置
     * @param view
     *         子条目视图
     *
     * @return 子条目视图容器
     */
    public CHILDHOLDER createChildHolder(int groupPosition, int childPosition, View view);

    /**
     * 生成子条目视图
     *
     * @param groupPosition
     *         府条目位置
     * @param childPosition
     *         子条目位置
     * @param child
     *         子条目对象
     *
     * @return 子条目视图
     */
    public View createChildView(int groupPosition, int childPosition, CHILD child);

    /**
     * 通过父条目视图生成对应容器
     *
     * @param groupPosition
     *         父条目位置
     * @param view
     *         父条目视图
     *
     * @return 父条目视图容器
     */
    public GROUPHOLDER createGroupHolder(int groupPosition, View view);

    /**
     * 生成父条目视图
     *
     * @param groupPosition
     *         父条目位置
     * @param group
     *         父条目对象
     *
     * @return 父条目视图
     */
    public View createGroupView(int groupPosition, GROUP group);

    /**
     * 设置指定位置的父条目是否可以被点击
     *
     * @param groupPosition
     *         父条目位置
     *
     * @return 是否可以被点击
     */
    public boolean isGroupClickable(int groupPosition);
}