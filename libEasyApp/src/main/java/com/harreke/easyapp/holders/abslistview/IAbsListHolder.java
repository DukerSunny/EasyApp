package com.harreke.easyapp.holders.abslistview;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 *
 * ListAdapter的Holder接口
 *
 * @param <ITEM>
 *         项目类型
 */
public interface IAbsListHolder<ITEM> {
    /**
     * 当设置数据时触发，用于填充数据至该项目视图
     *
     * @param position
     *         项目位置
     * @param item
     *         项目对象
     */
    public void setItem(int position, ITEM item);
}