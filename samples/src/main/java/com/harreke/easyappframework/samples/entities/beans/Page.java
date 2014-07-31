package com.harreke.easyappframework.samples.entities.beans;

import java.util.ArrayList;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 *
 * dummy
 *
 * @param <ITEM>
 *         dummy
 */
public class Page<ITEM> {
    private ArrayList<ITEM> list;
    private int pageSize;
    private int totalCount;

    public ArrayList<ITEM> getList() {
        return list;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getTotalCount() {
        return totalCount;
    }
}
