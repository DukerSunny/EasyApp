package com.harreke.easyapp.listeners;

import com.harreke.easyapp.widgets.ChildSettingView;
import com.harreke.easyapp.widgets.GroupSettingView;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 *
 * 多选设置视图的监听器
 *
 * @see com.harreke.easyapp.widgets.GroupSettingView
 */
public interface OnGroupSettingChangeListener {
    /**
     * 当选择状态改变时触发
     *
     * @param parent
     *         触发的父视图
     * @param view
     *         触发的子视图
     * @param position
     *         触发子视图的位置
     */
    public void onGroupSettingChange(GroupSettingView parent, ChildSettingView view, int position);
}