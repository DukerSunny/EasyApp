package com.harreke.easyapp.frameworks.bases;

import com.harreke.easyapp.beans.ActionBarItem;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 *
 * ActionBar点击事件的接口
 */
public interface IActionBarClickListener {
    /**
     * 当ActionBar的Home键被点击时触发
     */
    public void onActionBarHomeClick();

    /**
     * 当ActionBar的菜单选项被点击时触发
     *
     * @param position
     *         菜单选项的位置
     * @param item
     *         被点击的菜单选项
     */
    public void onActionBarItemClick(int position, ActionBarItem item);
}