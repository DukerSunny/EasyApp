package com.harreke.easyappframework.pulltorefreshes.abslistview;

import android.view.View;

import com.harreke.easyappframework.holders.abslistview.IListHolder;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 *
 * AbsListView的Adapter接口
 */
public interface IList<ITEM, HOLDER extends IListHolder<ITEM>> {
    /**
     * 生成项目视图容器
     *
     * @param position
     *         项目位置
     *
     * @return 项目视图容器
     */
    public HOLDER createHolder(int position, View view);

    /**
     * 生成项目视图
     *
     * @param position
     *         项目位置
     * @param item
     *         项目对象
     *
     * @return 项目视图
     */
    public View createView(int position, ITEM item);

    /**
     * 当列表某一项目被点击时触发
     *
     * @param position
     *         项目位置
     * @param item
     *         项目对象
     */
    public void onListItemClick(int position, ITEM item);
}