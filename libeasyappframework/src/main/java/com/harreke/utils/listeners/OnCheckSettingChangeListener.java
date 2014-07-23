package com.harreke.utils.listeners;

import com.harreke.utils.widgets.CheckSettingView;

/**
 {@link com.harreke.utils.widgets.CheckSettingView}复选设置视图的接口
 */
public interface OnCheckSettingChangeListener {
    /**
     当选择状态改变时触发

     @param view
     触发的视图
     @param isChecked
     是否被选中
     */
    public void onCheckSettingChange(CheckSettingView view, boolean isChecked);
}