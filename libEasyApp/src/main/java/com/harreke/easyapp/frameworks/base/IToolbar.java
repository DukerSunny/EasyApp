package com.harreke.easyapp.frameworks.base;

import android.view.View;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/11/18
 */
public interface IToolbar {
    public void addToolbarItem(int id, int titleId, int imageId);

    public void addToolbarViewItem(int id, int titleId, View view);

    public void hideToolbar();

    public void hideToolbarItem(int id);

    public void enableDefaultToolbarNavigation();

    public void setToolbarNavigation(int imageId);

    public void setToolbarTitle(String text);

    public void setToolbarTitle(int textId);

    public void showToolbar();

    public boolean isToolbarShowing();

    public void showToolbarItem(int id);
}