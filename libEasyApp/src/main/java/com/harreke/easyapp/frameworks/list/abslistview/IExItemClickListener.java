package com.harreke.easyapp.frameworks.list.abslistview;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/31
 */
public interface IExItemClickListener<GROUP, CHILD> {
    /**
     * 当列表某一子条目被点击时触发
     *
     * @param groupPosition
     *         父条目位置
     * @param childPosition
     *         子条目位置
     * @param child
     *         子条目对象
     */
    public void onChildItemClick(int groupPosition, int childPosition, CHILD child);

    /**
     * 当列表某一父条目被点击时触发
     *
     * @param groupPosition
     *         父条目位置
     * @param group
     *         父条目对象
     */
    public void onGroupItemClick(int groupPosition, GROUP group);
}