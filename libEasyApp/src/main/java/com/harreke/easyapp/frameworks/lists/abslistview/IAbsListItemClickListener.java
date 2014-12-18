package com.harreke.easyapp.frameworks.lists.abslistview;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/31
 */
public interface IAbsListItemClickListener<ITEM> {
    /**
     * 当列表某一条目被点击时触发
     *
     * @param position
     *         条目位置
     * @param item
     *         条目对象
     */
    public void onItemClick(int position, ITEM item);
}