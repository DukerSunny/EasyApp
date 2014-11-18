package com.harreke.easyapp.frameworks.list;

import java.util.List;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/31
 */
public interface IListActionListener<ITEM> {
    /**
     * 当开始发起Http请求时触发
     */
    public void onAction();

    /**
     * 当列表加载错误时触发
     */
    public void onError();

    /**
     * 当需要解析网络数据时触发
     *
     * @param json
     *         网络json字符串
     *
     * @return 解析出的列表
     */
    public List<ITEM> onParse(String json);

    /**
     * 当列表加载完成时触发
     */
    public void onPostAction();

    /**
     * 当列表加载开始时触发
     */
    public void onPreAction();
}