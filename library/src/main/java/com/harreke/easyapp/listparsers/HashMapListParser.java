package com.harreke.easyapp.listparsers;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 *
 * 列表框架的HashMap网络数据解析器
 *
 * 用于解析以HashMap方式封包的数据
 *
 * @param <ITEM>
 *         列表框架的条目类型
 * @param <DATA>
 *         封包类型
 */
public abstract class HashMapListParser<ITEM, DATA> implements IListParser<ITEM> {
    private HashMap<String, DATA> data;
    private ArrayList<ITEM> mList = null;
    private int mTotalPage = 1;
    private String msg;
    private int status;

    /**
     * 获得数据
     *
     * @param key
     *         数据主键
     *
     * @return 原始数据结构
     */
    protected DATA getData(String key) {
        return data.get(key);
    }

    /**
     * 获得处理过、可直接操作的条目列表
     *
     * @return 条目列表
     */
    @Override
    public ArrayList<ITEM> getList() {
        return mList;
    }

    /**
     * 获得消息
     *
     * @return 消息
     */
    @Override
    public String getMsg() {
        return msg;
    }

    /**
     * 获得状态码
     *
     * @return 状态码
     */
    @Override
    public int getStatus() {
        return status;
    }

    /**
     * 获得条目总页数
     *
     * @return 条目总页数
     */
    @Override
    public int getTotalPage() {
        return mTotalPage;
    }

    /**
     * 判断反序列化是否成功（是否获得正确的原始数据）
     *
     * @return 反序列化是否成功
     */
    @Override
    public boolean isSuccess() {
        return status >= 200 && status < 300 && data != null;
    }

    /**
     * 设置解析列表
     *
     * @param list
     *         解析出的列表
     */
    protected void setList(ArrayList<ITEM> list) {
        mList = list;
        if (mList != null && mList.size() > 0 && mTotalPage == 0) {
            mTotalPage = 1;
        }
    }

    /**
     * 设置解析总页数
     *
     * @param totalPage
     *         解析出的总页数
     */
    protected void setTotalPage(int totalPage) {
        mTotalPage = totalPage;
    }
}