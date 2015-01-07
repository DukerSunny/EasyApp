package com.harreke.easyapp.parsers;

import java.util.List;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/01/07
 */
public abstract class ListParser<ITEM> {
    private List<ITEM> mList = null;

    public List<ITEM> getList() {
        return mList;
    }

    public abstract void parse(String json);

    protected void setList(List<ITEM> list) {
        mList = list;
    }
}
