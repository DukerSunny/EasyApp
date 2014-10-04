package tv.acfun.read.beans;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/09/26
 */
public class CommentListPage {
    private ArrayList<Integer> list;
    private HashMap<String, Comment> map;
    private int pageSize;
    private int totalCount;

    public ArrayList<Integer> getList() {
        return list;
    }

    public HashMap<String, Comment> getMap() {
        return map;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getTotalCount() {
        return totalCount;
    }
}