package com.harreke.easyapp.frameworks.list;

import android.view.View;

import java.util.Comparator;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 *
 * 列表助手的接口
 */

public interface IList<ITEM> {
    /**
     * 添加一个条目
     *
     * @param itemId
     *         条目Id，大于等于0，用于检测是否有重复条目
     *         若为-1，则不检测重复条目
     * @param item
     *         条目对象
     *
     * @return 如果添加成功，返回true，否则返回false
     */
    public boolean addItem(int itemId, ITEM item);

    /**
     * 设置数据适配器
     */
    public void bindAdapter();

    /**
     * 清空列表
     */
    public void clear();

    /**
     * 获得指定条目
     *
     * @param position
     *         条目位置
     *
     * @return 指定条目
     */
    public ITEM getItem(int position);

    /**
     * 获得条目总数
     *
     * @return int
     */
    public int getItemCount();

    /**
     * 判断列表是否为空
     *
     * @return 列表是否为空
     */
    public boolean isEmpty();

    /**
     * 解析条目Id
     *
     * @param item
     *         条目对象
     *
     * @return 条目Id
     *
     * 条目Id是用来标识是否重复的依据
     *
     * @see #addItem(int, ITEM)
     */
    public int parseItemId(ITEM item);

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
     * 设置加载更多功能接口
     *
     * 为列表添加加载更多功能
     *
     * @param loadMore
     *         加载更多功能接口
     */
    public void setLoadMore(ILoadStatus loadMore);

    /**
     * 排序列表条目
     *
     * @param comparator
     *         比较器
     */
    public void sort(Comparator<ITEM> comparator);
}