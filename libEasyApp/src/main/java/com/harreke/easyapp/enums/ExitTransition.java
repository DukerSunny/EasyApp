package com.harreke.easyapp.enums;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/01/08
 */
public enum ExitTransition {
    None,
    /**
     * 从左滑出
     */
    Slide_Left,
    /**
     * 从右滑出
     */
    Slide_Right,
    /**
     * 从上滑出
     */
    Slide_Top,
    /**
     * 从下滑出
     */
    Slide_Bottom,
    /**
     * 将一个视图作为焦点，从新视图变换至旧视图
     */
    Hero,
    /**
     * 将新视图以波纹收缩效果变为旧视图
     */
    Ripple
}
