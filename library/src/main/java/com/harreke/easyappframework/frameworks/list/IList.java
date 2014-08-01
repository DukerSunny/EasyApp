package com.harreke.easyappframework.frameworks.list;

import android.view.View;

import java.util.Comparator;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 *
 * 列表助手的接口
 */

public interface IList<ITEM> {
    /**
     * 清空列表
     */
    public void clear();

    /**
     * 获取条目总数
     *
     * @return int
     */
    public int getItemCount();

    /**
     * 判断列表是否为空
     *
     * @return boolean
     */
    public boolean isEmpty();

    /**
     * 刷新列表
     */
    public void refresh();

    /**
     * 设置列表视图
     *
     * @param listView
     *         列表视图
     */
    public void setListView(View listView);

    /**
     * 排序列表条目
     *
     * @param comparator
     *         比较器
     */
    public void sort(Comparator<ITEM> comparator);
}