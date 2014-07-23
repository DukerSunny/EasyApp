package com.harreke.utils.adapters.viewpager;

import android.view.View;

/**
 PagerAdapter的接口

 @param <ITEM>
 页面内容类型
 @param <VIEW>
 页面视图类型
 */
public interface IPageAdapter<ITEM, VIEW extends View> {
    /**
     初始化某一页面的时候触发

     @param position
     页面位置
     @param item
     页面内容

     @return 页面视图
     */
    public VIEW createPage(int position, ITEM item);
}