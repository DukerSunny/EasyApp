package com.harreke.easyappframework.listparsers;

import java.util.ArrayList;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 *
 * 列表框架的网络数据解析器接口
 *
 * @param <ITEM>
 *         列表框架的条目类型
 *
 * @see com.harreke.easyappframework.frameworks.list.ListFramework
 */
public interface IListParser<ITEM> {
    /**
     * 获得处理过、可直接操作的条目列表
     *
     * @return 条目列表
     */
    public ArrayList<ITEM> getList();

    /**
     * 获得消息
     *
     * @return 消息
     */
    public String getMsg();

    /**
     * 获得状态码
     *
     * @return 状态码
     */
    public int getStatus();

    /**
     * 获得条目总页数
     *
     * @return 条目总页数
     */
    public int getTotalPage();

    /**
     * 判断反序列化是否成功（是否获得正确的原始数据）
     *
     * @return 反序列化是否成功
     */
    public boolean isSuccess();

    /**
     * 开始解析
     */
    public void parse(String json);
}