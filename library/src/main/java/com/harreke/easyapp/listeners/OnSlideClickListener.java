package com.harreke.easyapp.listeners;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 *
 * 幻灯助手的接口
 *
 * @see com.harreke.easyapp.helpers.SlideHelper
 */
public interface OnSlideClickListener {
    /**
     * 当幻灯被点击时触发
     *
     * @param position
     *         被点击幻灯片的位置
     * @param data
     *         被点击幻灯片对应的数据
     */
    public void onSlideClick(int position, String data);
}