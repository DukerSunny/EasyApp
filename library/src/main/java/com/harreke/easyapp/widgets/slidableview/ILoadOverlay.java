package com.harreke.easyapp.widgets.slidableview;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/31
 *
 * LoadOverlay的接口
 *
 * LoadOverlay是SlidableView的一个子视图，而且必须包含于SlidableView之中
 * 用于在SlidableView加载更多状态时给予提示
 *
 * 加载更多是指，用户将SlidableView往上滑动，后者包含的可滑动页面（如ListView）达到底部，则会触发加载更多，告知程序加载下一个页面
 *
 * 想要实现LoadOverlay，必须继承LoadOverlayView并实现ILoadOverlay的方法：
 * 通过接口函数queryLayout查询获得需要使用的视图
 * 通过其他接口函数设置这些视图的值
 */
public interface ILoadOverlay {
    /**
     * 查询布局中的视图
     *
     * 会在LoadOverlay创建的时候触发
     */
    public void queryLayout();

    /**
     * 设置LoadOverlay为结束加载状态
     *
     * 会在SlidableView完成加载时被设置
     *
     * @param pageSize
     *         页面加载的条目数
     *
     *         -1为不需要显示加载条目数
     */
    public void setLoadComplete(int pageSize);

    /**
     * 设置LoadOverlay为开始加载状态
     *
     * 会在SlidableView触发加载更多状态时被设置
     */
    public void setLoadStart();
}