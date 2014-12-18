package com.harreke.easyapp.frameworks.lists;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/10/09
 *
 * 加载状态
 */
public enum LoadStatus {
    /**
     * 空闲
     */
    Idle,
    /**
     * 正在加载
     */
    Loading,
    /**
     * 空白
     */
    Empty,
    /**
     * 没有更多
     */
    Last,
    /**
     * 错误
     */
    Error
}
