package com.harreke.easyappframework.beans;

import android.view.View;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 *
 * ActionBarItem是Menu的菜单选项，应用于IActionBar接口
 *
 * 菜单选项是ActionBar右侧的一个个按钮，按钮上可以显示文字、图标或自定义视图
 */
public class ActionBarItem {
    private int mFlag;
    private int mImageId;
    private int mIndex;
    private int mSubItemCount;
    private String mTitle;
    private View mView;
    private boolean mVisible;

    public ActionBarItem(String title, int index) {
        mTitle = title;
        mIndex = index;
        mSubItemCount = 0;
        mFlag = -1;
        mImageId = 0;
        mView = null;
        mVisible = true;
    }

    /**
     * 获得菜单选项的状态
     *
     * @return 状态，-1表示无状态
     *
     * @see android.view.MenuItem
     */
    public final int getFlag() {
        return mFlag;
    }

    /**
     * 获得菜单选项图标
     *
     * @return 图标Id
     */
    public final int getImageId() {
        return mImageId;
    }

    /**
     * 获得菜单选项在ActionBar上的序号
     *
     * @return 序号
     */
    public final int getIndex() {
        return mIndex;
    }

    /**
     * 获得菜单选项包含子菜单数量
     *
     * @return 包含子菜单数量
     */
    public final int getSubItemCount() {
        return mSubItemCount;
    }

    /**
     * 获得菜单选项的标题
     *
     * @return 标题
     */
    public final String getTitle() {
        return mTitle;
    }

    /**
     * 设置菜单选项的视图
     *
     * @return 视图
     */
    public final View getView() {
        return mView;
    }

    /**
     * 判断菜单选项是否可见
     *
     * @return 是否可见
     */
    public final boolean isVisible() {
        return mVisible;
    }

    /**
     * 设置菜单选项的状态
     *
     * @param flag
     *         状态，-1表示无状态
     *
     * @see android.view.MenuItem
     */
    public final void setFlag(int flag) {
        mFlag = flag;
    }

    /**
     * 设置菜单选项的图标
     *
     * @param imageId
     *         图标Id
     */
    public final void setImageId(int imageId) {
        mImageId = imageId;
    }

    /**
     * 设置菜单选项包含子菜单数量
     */
    public final void setSubItemCount(int subItemCount) {
        mSubItemCount = subItemCount;
    }

    /**
     * 设置菜单选项的标题
     *
     * @param title
     *         标题
     */
    public final void setTitle(String title) {
        mTitle = title;
    }

    /**
     * 设置菜单选项的视图
     *
     * @param view
     *         视图
     */
    public final void setView(View view) {
        mView = view;
    }

    /**
     * 设置菜单选项是否可见
     *
     * @param visible
     *         是否可见
     */
    public final void setVisible(boolean visible) {
        mVisible = visible;
    }
}