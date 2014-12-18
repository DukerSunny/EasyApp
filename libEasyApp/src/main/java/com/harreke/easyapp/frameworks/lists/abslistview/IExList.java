package com.harreke.easyapp.frameworks.lists.abslistview;

import android.view.View;

import com.harreke.easyapp.holders.abslistview.IExListHolder;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/31
 *
 * ExpandableAdapter的接口
 *
 * @param <GROUP>
 *         父项目类型
 * @param <GROUPHOLDER>
 *         父项目Holder类型
 * @param <CHILD>
 *         子项目类型
 * @param <CHILDHOLDER>
 *         子项目Holder类型
 */
public interface IExList<GROUP, GROUPHOLDER extends IExListHolder.Group<GROUP>, CHILD, CHILDHOLDER extends IExListHolder.Child<CHILD>> {
    /**
     * 通过子项目视图生成对应容器
     *
     * @param convertView
     *         子项目视图
     *
     * @return 子项目视图容器
     */
    public CHILDHOLDER createChildHolder(View convertView);

    /**
     * 生成子项目视图
     *
     * @return 子项目视图
     */
    public View createChildView();

    /**
     * 通过父项目视图生成对应容器
     *
     * @param convertView
     *         父项目视图
     *
     * @return 父项目视图容器
     */
    public GROUPHOLDER createGroupHolder(View convertView);

    /**
     * 生成父项目视图
     *
     * @return 父项目视图
     */
    public View createGroupView();

    /**
     * 设置指定位置的父项目是否可以被点击
     *
     * @param groupPosition
     *         父项目位置
     *
     * @return 是否可以被点击
     */
    public boolean isGroupClickable(int groupPosition);
}