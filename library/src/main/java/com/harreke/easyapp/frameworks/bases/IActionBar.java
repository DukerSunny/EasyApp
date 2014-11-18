package com.harreke.easyapp.frameworks.bases;

import android.view.View;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/11/18
 */
public interface IActionBar {
    public void addActionBarImageItem(int id, int imageId);

    public void addActionBarViewItem(int id, int layoutId, boolean clickable);

    public void addActionBarViewItem(int id, View item, boolean clickable);

    public void hideActionbarItem(int id);

    public void setActionBarTitle(String text);

    public void setActionBarTitle(int textId);

    public void showActionBarHome(boolean show);

    public void showActionBarItem(int id);

    public void showActionBarTitle(boolean show);
}