package com.harreke.easyappframework.frameworks.list;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/31
 */
public interface IListStatusChageListener<ITEM> {
    /**
     * 当开始发起Http请求时触发
     */
    public void onAction();

    /**
     * 当列表加载错误时触发
     */
    public void onError();

    /**
     * 解析条目
     *
     * @param item
     *         条目类型
     */
    public void onParseItem(ITEM item);

    /**
     * 当列表加载完成时触发
     */
    public void onPostAction();

    /**
     * 当列表加载开始时触发
     */
    public void onPreAction();
}