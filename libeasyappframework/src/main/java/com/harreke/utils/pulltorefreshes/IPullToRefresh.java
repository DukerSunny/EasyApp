package com.harreke.utils.pulltorefreshes;

import java.util.Comparator;

/**
 下拉刷新助手的接口
 */

public interface IPullToRefresh<ITEM> {
    /**
     清空列表
     */
    public void clear();

    /**
     获取项目总数

     @return int
     */
    public int getItemCount();

    /**
     判断列表是否为空

     @return boolean
     */
    public boolean isEmpty();

    /**
     当开始发起Http请求时触发
     */
    public void onAction();

    /**
     添加项目

     @param item
     项目类型
     */
    public void onAdd(ITEM item);

    /**
     当列表加载错误时触发
     */
    public void onError();

    /**
     当列表加载完成时触发
     */
    public void onPostAction();

    /**
     当列表加载开始时触发
     */
    public void onPreAction();

    /**
     刷新列表
     */
    public void refresh();

    /**
     排序列表项目

     @param comparator
     比较器
     */
    public void sort(Comparator<ITEM> comparator);
}