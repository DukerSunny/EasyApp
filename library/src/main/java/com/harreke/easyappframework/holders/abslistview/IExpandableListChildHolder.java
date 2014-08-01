package com.harreke.easyappframework.holders.abslistview;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 *
 * ExpandableListAdapter的ChildHolder接口
 *
 * @param <CHILD>
 *         子项目类型
 */
public interface IExpandableListChildHolder<CHILD> {
    /**
     * 当设置数据时触发，用于填充数据至该子项目视图
     *
     * @param child
     *         子项目对象
     */
    public void setItem(CHILD child);
}