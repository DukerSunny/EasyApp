package com.harreke.easyappframework.listeners;

import android.view.View;

import com.harreke.easyappframework.widgets.ListSettingView;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 *
 * 列表设置视图的监听器
 *
 * @see com.harreke.easyappframework.widgets.ListSettingView
 */
public interface OnListSettingChangeListener {
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
    public void onListSettingChange(ListSettingView parent, View view, int position);
}