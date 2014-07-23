package com.harreke.utils.listeners;

/**
 {@link com.harreke.utils.helpers.DialogHelper}对话框助手的接口
 */
public interface OnDialogButtonClickListener {
    /**
     当对话框上按钮被点击时触发

     @param which
     被点击的按钮Id
     */
    public void onDialogButtonClick(int which);
}