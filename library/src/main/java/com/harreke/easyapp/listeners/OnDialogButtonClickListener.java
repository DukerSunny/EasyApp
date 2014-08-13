package com.harreke.easyapp.listeners;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 *
 * 对话框助手的监听器
 *
 * @see com.harreke.easyapp.helpers.DialogHelper
 */
public interface OnDialogButtonClickListener {
    /**
     * 当对话框上按钮被点击时触发
     *
     * @param which
     *         被点击的按钮Id
     */
    public void onDialogButtonClick(int which);
}