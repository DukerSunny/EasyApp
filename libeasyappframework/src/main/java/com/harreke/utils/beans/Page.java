package com.harreke.utils.beans;

import java.util.ArrayList;

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
