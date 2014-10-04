package com.harreke.easyapp.frameworks.list.abslistview;

import android.view.View;

import com.harreke.easyapp.holders.abslistview.IAbsListHolder;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 *
 * AbsListView的Adapter接口
 */
public interface IAbsList<ITEM, HOLDER extends IAbsListHolder<ITEM>> {
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
     * @param item
     *         项目对象
     *
     * @return 项目视图
     */
    public View createView(ITEM item);
}