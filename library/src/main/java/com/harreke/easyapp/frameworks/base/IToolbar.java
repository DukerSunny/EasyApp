package com.harreke.easyapp.frameworks.base;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/04/10
 * <p/>
 * Toolbar接口
 * <p/>
 * Toolbar接口统一管理Activity中对Toolbar的调用，使用全新的Toolbar取代传统的ActionBar
 */
public interface IToolbar {
    /**
     * 在Toolbar上启用默认返回按钮
     */
    void enableDefaultToolbarNavigation();

    /**
     * 隐藏Toolbar
     */
    void hideToolbar();

    /**
     * 隐藏Toolbar上指定按钮
     *
     * @param id 按钮Id
     */
    void hideToolbarItem(int id);

    /**
     * 判断Toolbar是否可见
     *
     * @return Toolbar是否可见
     */
    boolean isToolbarShowing();

    void patchToolbarTopPadding();

    /**
     * 设置Toolbar返回按钮图像
     *
     * @param imageId 返回按钮图像Id
     */
    void setToolbarNavigation(int imageId);

    /**
     * 设置Toolbar副标题文本
     *
     * @param titleId 副标题文本Id
     */
    void setToolbarSubTitle(int titleId);

    /**
     * 设置Toolbar副标题文本
     *
     * @param title 副标题文本
     */
    void setToolbarSubTitle(String title);

    /**
     * 设置Toolbar标题文本
     *
     * @param textId 标题文本Id
     */
    void setToolbarTitle(int textId);

    /**
     * 设置Toolbar标题文本
     *
     * @param text 标题文本
     */
    void setToolbarTitle(String text);

    /**
     * 显示Toolbar
     */
    void showToolbar();

    /**
     * 显示Toolbar上指定按钮
     *
     * @param id 按钮Id
     */
    void showToolbarItem(int id);
}