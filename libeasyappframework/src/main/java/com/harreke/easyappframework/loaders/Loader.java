package com.harreke.easyappframework.loaders;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 *
 * 下拉刷新列表助手的Loader
 *
 * @param <ITEM>
 *         下拉刷新列表的项目类型
 * @param <DATA>
 *         项目封包类型
 */
public abstract class Loader<ITEM, DATA> implements ILoader<ITEM> {
    protected HashMap<String, DATA> data;
    protected ArrayList<ITEM> mList = null;
    protected int mTotalPage = 0;
    protected String msg;
    protected int status;

    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayList<ITEM> getList() {
        return mList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMsg() {
        return msg;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getStatus() {
        return status;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getTotalPage() {
        return mTotalPage;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSuccess() {
        return status >= 200 && status < 300 && data != null;
    }
}