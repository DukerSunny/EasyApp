package com.harreke.easyapp.widgets.slidableview;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/31
 *
 * RefreshHeader的接口
 *
 * RefreshHeader是SlidingView的一个子视图，而且必须包含于SlidingView之中
 * 用于在SlidingView触发下拉刷新时给予提示
 *
 * 下拉刷新是指，用户将SlidingView往下滑动，后者若包含有RefreshHeader，则RefreshHeader会显示提示文字“松开刷新”，这时候松手就会触发下拉刷新，告知程序清空页面并重新加载
 *
 * 想要实现RefreshHeader，必须继承RefreshHeaderView并实现IRefreshHeader的方法：
 * 通过接口函数queryLayout查询获得需要使用的视图
 * 通过其他接口函数设置这些视图的值
 */
public interface IRefreshHeader {
    /**
     * 查询布局中的视图
     *
     * 会在RefreshHeader创建的时候触发
     */
    public void queryLayout();

    /**
     * 设置RefreshHeader为结束刷新状态
     *
     * 会在SlidingView完成刷新时被设置
     */
    public void setRefreshComplete();

    /**
     * 设置RefreshHeader为开始刷新状态
     *
     * 会在SlidingView触发下拉刷新状态时被设置
     */
    public void setRefreshStart();

    /**
     * 设置RefreshHeader触发进度
     *
     * 触发进度是指RefreshHeader已经露出，但是还未完全展示的时候，已经显示部分的百分比
     *
     * @param progress
     *         已经显示部分的百分比
     */
    public void setRefreshTrigger(int progress);
}