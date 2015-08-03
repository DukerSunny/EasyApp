package com.harreke.easyapp.parsers;

import java.util.List;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/01/07
 */
public class ListResult<ITEM> {
    private int flag = 0;
    private List<ITEM> mList = null;
    private String message = null;

    public int getFlag() {
        return flag;
    }

    public List<ITEM> getList() {
        return mList;
    }

    public String getMessage() {
        return message;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public void setList(List<ITEM> list) {
        mList = list;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
