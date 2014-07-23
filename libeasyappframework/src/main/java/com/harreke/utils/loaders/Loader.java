package com.harreke.utils.loaders;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Loader<ITEM, DATA> implements ILoader<ITEM> {
    protected HashMap<String, DATA> data;
    protected ArrayList<ITEM> mList = null;
    protected int mTotalPage = 0;
    protected String msg;
    protected int status;

    /**
     {@inheritDoc}
     */
    @Override
    public ArrayList<ITEM> getList() {
        return mList;
    }

    /**
     {@inheritDoc}
     */
    @Override
    public String getMsg() {
        return msg;
    }

    /**
     {@inheritDoc}
     */
    @Override
    public int getStatus() {
        return status;
    }

    /**
     {@inheritDoc}
     */
    @Override
    public int getTotalPage() {
        return mTotalPage;
    }

    /**
     {@inheritDoc}
     */
    @Override
    public boolean isSuccess() {
        return status >= 200 && status < 300 && data != null;
    }
}