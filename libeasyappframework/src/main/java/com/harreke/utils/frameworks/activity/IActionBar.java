package com.harreke.utils.frameworks.activity;

import android.view.MenuItem;

/**
 Activity框架下ActionBar的接口
 */
@SuppressWarnings("UnusedDeclaration")
public interface IActionBar {
    /**
     当ActionBar的Home键被点击时触发
     */
    public void onActionBarHomeClicked();

    /**
     当ActionBar的菜单项目被点击时触发

     @param position
     菜单项目的位置
     @param item
     被点击的菜单项目
     */
    public void onActionBarItemClicked(int position, MenuItem item);
}
