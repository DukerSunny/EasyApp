package com.harreke.easyapp.enums;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/01/08
 */
public enum ActivityTransition {
    None,
    /**
     * 将一个视图作为共享元素，从旧Activity变换至新Activity
     */
    Shared,
    /**
     * 将一个图像视图作为共享元素，从旧Activity变换至新Activity
     */
    SharedImage,
    Scale,
    ScaleImage,
    /**
     * 将旧视图以波纹效果变为新视图
     */
    Reveal
}