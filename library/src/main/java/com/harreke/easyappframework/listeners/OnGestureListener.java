package com.harreke.easyappframework.listeners;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 *
 * 手势监听器
 * 注：
 * 支持最多2个点的手势
 *
 * @see com.harreke.easyappframework.helpers.GestureHelper
 */
public interface OnGestureListener {
    /**
     * 当单点按下时时触发
     *
     * @return 如果需要消费该事件，返回true；否则返回false
     */
    public boolean onDown();

    /**
     * 当多点按下时时触发
     *
     * 注：
     * 仅在已开启多点触摸功能时可用
     *
     * @return 如果需要消费该事件，返回true；否则返回false
     */
    public boolean onPointerDown();

    /**
     * 当多点抬起时触发
     *
     * 注：
     * 仅在已开启多点触摸功能时可用
     *
     * @return 如果需要消费该事件，返回true；否则返回false
     */
    public boolean onPointerUp();

    /**
     * 当缩放时触发
     *
     * 注：
     * 仅在已开启多点触摸功能时可用
     *
     * @param scale
     *         缩放的总倍率（从单点按下时开始计算）
     * @param scaleX
     *         缩放中心x坐标
     * @param scaleY
     *         缩放中心y坐标
     * @param duration
     *         缩放总时长（从单点按下时开始计算）
     *
     * @return 如果需要消费该事件，返回true；否则返回false
     */
    public boolean onScale(float scale, float scaleX, float scaleY, long duration);

    /**
     * 当滑动时触发
     *
     * 注：
     * 仅在未开启多点触摸功能时可用
     *
     * @param scrollX
     *         滑动的横向总距离（从单点按下时开始计算）
     * @param scrollY
     *         滑动的纵向总距离（从单点按下时开始计算）
     * @param duration
     *         滑动的总时长（从单点按下时开始计算）
     *
     * @return 如果需要消费该事件，返回true；否则返回false
     */
    public boolean onScroll(float scrollX, float scrollY, long duration);

    /**
     * 当点击屏幕时触发
     *
     * @param x
     *         点击的x坐标
     * @param y
     *         点击的y坐标
     * @param tapCount
     *         连续点击的次数
     */
    public void onTaps(float x, float y, int tapCount);

    /**
     * 当位移时触发
     *
     * 注：
     * 仅在已开启多点触摸功能时，并且缩放倍率大于最小倍率时可用
     *
     * @param translateX
     *         位移的横向总距离（从单点按下时开始计算）
     * @param translateY
     *         位移的纵向总距离（从单点按下时开始计算）
     * @param duration
     *         位移的总时长（从单点按下时开始计算）
     *
     * @return 如果需要消费该事件，返回true；否则返回false
     */
    public boolean onTranslate(float translateX, float translateY, long duration);

    /**
     * 当单点抬起时触发
     *
     * @return 如果需要消费该事件，返回true；否则返回false
     */
    public boolean onUp();
}