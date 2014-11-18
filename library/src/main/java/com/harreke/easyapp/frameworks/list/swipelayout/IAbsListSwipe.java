package com.harreke.easyapp.frameworks.list.swipelayout;

import android.view.View;

import com.daimajia.swipe.SwipeLayout;
import com.harreke.easyapp.holders.abslistview.IAbsListHolder;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 *
 * AbsListView的Adapter接口
 */
public interface IAbsListSwipe<ITEM, HOLDER extends IAbsListHolder<ITEM>> {
    /**
     * 生成项目视图容器
     *
     * @param convertView
     *         项目视图
     *
     * @return 项目视图容器
     */
    public HOLDER createHolder(View convertView);

    /**
     * 生成项目视图
     *
     * @return 项目视图
     */
    public View createView();
}