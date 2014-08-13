package com.harreke.easyapp.listeners;

import com.harreke.easyapp.widgets.ChildSettingView;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 *
 * 单选设置视图的监听器
 *
 * @see com.harreke.easyapp.widgets.ChildSettingView
 */
public interface OnChildSettingChangeListener {
    /**
     * 当选择状态改变时触发
     *
     * @param view
     *         触发的视图
     * @param isChecked
     *         是否被选中
     */
    public void onChildSettingChange(ChildSettingView view, boolean isChecked);
}