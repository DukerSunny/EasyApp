package com.harreke.easyapp.holders.abslistview;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/08/05
 */
public class IExListHolder {
    /**
     * ExListAdapter的ChildHolder接口
     *
     * @param <CHILD>
     *         子条目类型
     */
    public interface Child<CHILD> {
        /**
         * 当设置数据时触发，用于填充数据至该子条目视图
         *
         * @param child
         *         子条目对象
         */
        public void setItem(CHILD child);
    }

    /**
     * ExListAdapter的GroupHolder接口
     *
     * @param <GROUP>
     *         条目类型
     */
    public interface Group<GROUP> {
        /**
         * 当设置数据时触发，用于填充数据至该父条目视图
         *
         * @param group
         *         父条目对象
         */
        public void setItem(GROUP group);
    }
}