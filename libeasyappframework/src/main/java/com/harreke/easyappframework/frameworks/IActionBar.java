package com.harreke.easyappframework.frameworks;

import android.view.View;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 *
 * 框架下ActionBar的接口
 */
public interface IActionBar {
    /**
     * 为ActionBar添加一个菜单选项
     *
     * @param title
     *         选项标题
     * @param imageId
     *         选项的图标Id
     */
    public void addActionBarItem(String title, int imageId);

    /**
     * 为ActionBar添加一个菜单选项
     *
     * @param title
     *         选项标题
     * @param imageId
     *         选项的图标Id
     * @param flag
     *         选项的Flag
     */
    public void addActionBarItem(String title, int imageId, int flag);

    /**
     * 为ActionBar添加一个菜单选项
     *
     * @param title
     *         选项标题
     * @param view
     *         选项的视图
     */
    public void addActionBarItem(String title, View view);

    /**
     * 为ActionBar添加一个菜单选项
     *
     * @param title
     *         选项标题
     * @param view
     *         选项的视图
     * @param flag
     *         选项的Flag
     */
    public void addActionBarItem(String title, View view, int flag);

    /**
     * 为ActionBar添加一个菜单选项
     *
     * @param title
     *         选项标题 选项的图标Id 选项的视图
     * @param view
     *         选项的视图
     * @param items
     *         选项的内容数组
     */
    public void addActionBarItem(String title, View view, String[] items);

    /**
     * 为ActionBar添加一个菜单选项
     *
     * @param title
     *         选项标题
     * @param view
     *         选项的视图
     * @param items
     *         选项的内容数组
     * @param flag
     *         选项的Flag
     */
    public void addActionBarItem(String title, View view, String[] items, int flag);

    /**
     * 为ActionBar添加一个菜单选项
     *
     * @param title
     *         选项标题
     * @param imageId
     *         选项的图标Id
     * @param items
     *         选项的内容数组
     */
    public void addActionBarItem(String title, int imageId, String[] items);

    /**
     * 为ActionBar添加一个菜单选项
     *
     * @param title
     *         选项标题
     * @param imageId
     *         选项的图标Id
     * @param items
     *         选项的内容数组
     * @param flag
     *         选项的Flag
     */
    public void addActionBarItem(String title, int imageId, String[] items, int flag);

    /**
     * 禁用ActionBar的Home键上的图标
     */
    public void disableActionBarHomeIcon();

    /**
     * 启用ActionBar的Home键左边的返回箭头
     */
    public void enableActionBarHomeBack();

    /**
     * 隐藏ActoinBar上的指定菜单选项
     *
     * @param position
     *         菜单选项的序号
     */
    public void hideActionBarItem(int position);

    /**
     * 判断ActionB上的某个菜单选项是否正在显示
     *
     * @param position
     *         菜单选项位置
     *
     * @return 菜单选项是否正在显示
     */
    public boolean isActionBarItemShowing(int position);

    /**
     * 判断ActionBar是否正在显示
     *
     * @return ActionBar是否正在显示
     */
    public boolean isActionBarShowing();

    /**
     * 刷新ActionBar上的菜单选项
     */
    public void refreshActionBarItems();

    /**
     * 设置ActionBar的Home键是否可点击
     *
     * @param clickable
     *         是否可点击
     */
    public void setActionBarHomeClickable(boolean clickable);

    /**
     * 设置ActionBar的Home键上的图标
     *
     * @param iconId
     *         图标Id
     */
    public void setActionBarHomeIcon(int iconId);

    /**
     * 设置ActionBar的Home键上的标题
     *
     * @param titleId
     *         标题Id
     */
    public void setActionBarHomeTitle(int titleId);

    /**
     * 设置ActionBar的Home键上的标题
     *
     * @param title
     *         标题
     */
    public void setActionBarHomeTitle(String title);

    /**
     * 设置ActionBar的Home键上的标题是否可见
     *
     * @param visible
     *         是否可见
     */
    public void setActionBarHomeTitleVisible(boolean visible);

    /**
     * 设置ActionBar的Home键是否可见
     *
     * @param visible
     *         是否可见
     */
    public void setActionBarHomeVisible(boolean visible);

    /**
     * 设置ActionBar上的所有菜单选项是否可见
     *
     * @param visible
     *         是否可见
     */
    public void setActionBarItemsVisible(boolean visible);

    /**
     * 设置ActionBar的视图
     *
     * @param view
     *         ActionBar的视图
     */
    public void setActionBarView(View view);

    /**
     * 设置ActionBar的视图
     *
     * @param viewId
     *         ActionBar的视图Id
     */
    public void setActionBarView(int viewId);

    /**
     * 设置ActionBar是否可见
     *
     * @param visible
     *         是否可见
     */
    public void setActionBarVisible(boolean visible);

    /**
     * 显示ActionBar上的指定菜单选项
     *
     * @param position
     *         菜单选项位置
     */
    public void showActionBarItem(int position);
}