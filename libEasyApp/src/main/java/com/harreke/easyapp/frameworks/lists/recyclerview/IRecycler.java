package com.harreke.easyapp.frameworks.lists.recyclerview;

import android.view.View;
import android.view.ViewGroup;

import com.harreke.easyapp.frameworks.lists.ILoadStatus;
import com.harreke.easyapp.holders.recycerview.RecyclerHolder;

import java.util.Comparator;
import java.util.List;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/12/01
 */
public interface IRecycler<ITEM> {
    /**
     * 添加一个条目，并通知视图刷新
     *
     * @param item
     *         条目
     *
     * @return 是否添加成功
     *
     * 注：
     * 如果设置丢弃重复条目，则会判断该条目是否重复，若重复，则丢弃该条目并导致添加失败
     */
    public boolean addItem(ITEM item);

    /**
     * 添加一组条目，并通知视图刷新
     *
     * @param itemList
     *         条目列表
     *
     * @return 成功添加的条目数量
     */
    public boolean addItem(List<ITEM> itemList);

    /**
     * 添加一组条目，并通知视图刷新
     *
     * @param items
     *         条目数组
     *
     * @return 成功添加的条目数量
     */
    public boolean addItem(ITEM[] items);

    /**
     * 绑定数据适配器
     */
    public void bindAdapter();

    /**
     * 清空列表
     */
    public void clear();

    /**
     * 生成项目视图容器
     *
     * @param convertView
     *         项目视图
     * @param viewType
     *         项目类型
     *
     * @return 项目视图容器
     */
    public RecyclerHolder<ITEM> createHolder(View convertView, int viewType);

    /**
     * 生成项目视图
     *
     * @param viewType
     *         项目类型
     *
     * @return 项目视图
     */
    public View createView(ViewGroup parent, int viewType);

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

    public List<ITEM> getItemList();

    /**
     * 判断列表是否为空
     *
     * @return 列表是否为空
     */
    public boolean isEmpty();

    /**
     * 移除指定条目，并通知视图刷新
     *
     * @param start
     *         条目起始位置
     * @param end
     *         条目终止位置
     *
     * @return 是否移除成功
     */
    public boolean removeItem(int start, int end);

    /**
     * 移除一个条目，并通知视图刷新
     *
     * @param item
     *         条目
     *
     * @return 是否移除成功
     */
    public boolean removeItem(ITEM item);

    /**
     * 移除一个条目，并通知视图刷新
     *
     * @param position
     *         条目位置
     *
     * @return 是否移除成功
     */
    public boolean removeItem(int position);

    /**
     * 将视图滑动至顶部
     */
    public void scrollToTop();

    /**
     * 填充数据
     *
     * @param holder
     *         项目视图容器
     * @param item
     *         项目
     */
    public void setItem(RecyclerHolder<ITEM> holder, ITEM item);

    /**
     * 设置加载更多功能接口
     *
     * 为列表添加加载更多功能
     *
     * @param loadMore
     *         加载更多功能接口
     */
    public void setLoadMore(ILoadStatus loadMore);

    public void setOnClickListener(RecyclerHolder<ITEM> holder, View.OnClickListener onItemClickListener);

    /**
     * 排序列表条目
     *
     * @param comparator
     *         比较器
     */
    public void sort(Comparator<ITEM> comparator);
}
