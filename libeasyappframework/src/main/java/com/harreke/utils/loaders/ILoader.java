package com.harreke.utils.loaders;

import java.util.ArrayList;

/**
 {@link com.harreke.utils.pulltorefreshes.PullToRefresh}下拉刷新列表助手的Loader接口

 @param <ITEM>
 下拉刷新列表的项目类型
 */
public interface ILoader<ITEM> {
    /**
     获得处理过、可直接操作的项目列表

     @return 项目列表
     */
    public ArrayList<ITEM> getList();

    /**
     获得消息

     @return 消息
     */
    public String getMsg();

    /**
     获得状态码

     @return 状态码
     */
    public int getStatus();

    /**
     获得项目总页数

     @return 项目总页数
     */
    public int getTotalPage();

    /**
     判断反序列化是否成功（是否获得正确的原始数据）

     @return 反序列化是否成功
     */
    public boolean isSuccess();

    /**
     处理Loader里的原始数据
     */
    public void parse();
}