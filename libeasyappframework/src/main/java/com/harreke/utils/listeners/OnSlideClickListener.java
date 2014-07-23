package com.harreke.utils.listeners;

/**
 {@link com.harreke.utils.helpers.SlideHelper}幻灯助手的接口
 */
public interface OnSlideClickListener {
    /**
     当幻灯被点击时触发

     @param position
     被点击幻灯片的位置
     @param data
     被点击幻灯片对应的数据
     */
    public void onSlideClick(int position, String data);
}