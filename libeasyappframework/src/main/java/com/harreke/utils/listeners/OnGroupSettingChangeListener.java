package com.harreke.utils.listeners;

import com.harreke.utils.widgets.ChildSettingView;
import com.harreke.utils.widgets.GroupSettingView;

/**
 {@link com.harreke.utils.widgets.GroupSettingView} 多选设置视图的接口
 */
public interface OnGroupSettingChangeListener {
    /**
     当选择状态改变时触发

     @param parent
     触发的父视图
     @param view
     触发的子视图
     @param position
     触发子视图的位置
     */
    public void onGroupSettingChange(GroupSettingView parent, ChildSettingView view, int position);
}