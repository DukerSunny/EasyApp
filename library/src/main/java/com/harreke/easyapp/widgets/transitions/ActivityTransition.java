package com.harreke.easyapp.widgets.transitions;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/01/08
 */
public enum ActivityTransition {
    None,
    Animation,
    /**
     * 将一个视图作为共享元素，从旧Activity变换至新Activity
     */
    Shared,
    Scale,
    Reveal
}