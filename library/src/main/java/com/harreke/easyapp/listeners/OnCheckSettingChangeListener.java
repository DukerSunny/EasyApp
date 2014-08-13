package com.harreke.easyapp.listeners;

import com.harreke.easyapp.widgets.CheckSettingView;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 *
 * 复选设置视图的监听器
 *
 * @see com.harreke.easyapp.widgets.CheckSettingView
 */
public interface OnCheckSettingChangeListener {
    /**
     * 当选择状态改变时触发
     *
     * @param view
     *         触发的视图
     * @param isChecked
     *         是否被选中
     */
    public void onCheckSettingChange(CheckSettingView view, boolean isChecked);
}