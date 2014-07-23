package com.harreke.utils.listeners;

import android.view.View;

import com.harreke.utils.widgets.ListSettingView;

/**
 {@link com.harreke.utils.widgets.ListSettingView}列表设置视图的接口
 */
public interface OnListSettingChangeListener {
    /**
     当选择状态改变时触发

     @param parent
     触发的父视图
     @param view
     触发的子视图
     @param position
     触发子视图的位置
     */
    public void onListSettingChange(ListSettingView parent, View view, int position);
}