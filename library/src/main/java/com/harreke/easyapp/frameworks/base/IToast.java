package com.harreke.easyapp.frameworks.base;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/04/10
 * <p>
 * Toast接口
 * <p>
 * Toast是悬浮于Activity页面之上的一个临时性文本框，用于提示一些状态消息
 * <p>
 * Toast分为计时和持久两种显示模式：
 * 1.计时模式下，Toast将持续显示指定的时间，然后自动隐藏
 * 2.持久模式下，Toast将持续显示，直到程序主动调用hideToast函数隐藏该Toast
 * <p>
 * 计时模式又分为长短两种时间：
 * 1.长时间默认为3秒
 * 2.短时间默认为1.5秒
 * <p>
 * Toast默认以长计时模式显示
 */
public interface IToast {
    /**
     * 隐藏Toast
     */
    void hideToast();

    /**
     * 设置Toast显示时间
     * <p>
     * 只有在计时模式下该时间才有效果
     *
     * @param duration Toast显示时间
     */
    void setToastDuration(long duration);

    /**
     * 显示计时模式Toast
     *
     * @param textId Toast文本Id
     */
    void showToast(int textId);

    /**
     * 显示计时模式Toast
     *
     * @param text Toast文本
     */
    void showToast(String text);

    /**
     * 显示Toast
     *
     * @param textId        Toast文本Id
     * @param indeterminate 是否以持久模式显示
     *                      true表示以持久模式显示，false表示以计时模式显示
     */
    void showToast(int textId, boolean indeterminate);

    /**
     * 显示Toast
     *
     * @param text          Toast文本
     * @param indeterminate 是否以持久模式显示
     *                      true表示以持久模式显示，false表示以计时模式显示
     */
    void showToast(String text, boolean indeterminate);
}
