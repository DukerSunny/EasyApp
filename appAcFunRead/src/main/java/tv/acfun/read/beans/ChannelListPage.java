package tv.acfun.read.beans;

import java.util.ArrayList;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/09/24
 */
public class ChannelListPage {
    private ArrayList<Content> list;
    private int pageSize;
    private int totalCount;

    public ArrayList<Content> getList() {
        return list;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getTotalCount() {
        return totalCount;
    }
}