package com.harreke.easyapp.listeners;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/31
 *
 * SlidableView的监听器
 */
public interface OnSlidableTriggerListener {
    /**
     * 触发开始加载
     */
    public void onLoadTrigger();

    /**
     * 触发开始刷新
     */
    public void onRefreshTrigger();
}