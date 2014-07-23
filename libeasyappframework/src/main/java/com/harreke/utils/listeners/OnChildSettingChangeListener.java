package com.harreke.utils.listeners;

import com.harreke.utils.widgets.ChildSettingView;

/**
 {@link com.harreke.utils.widgets.ChildSettingView}单选设置视图的接口
 */
public interface OnChildSettingChangeListener {
    /**
     当选择状态改变时触发

     @param view
     触发的视图
     @param isChecked
     是否被选中
     */
    public void onChildSettingChange(ChildSettingView view, boolean isChecked);
}