package tv.acfun.read.beans;

import java.util.HashMap;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/09/26
 */
public class CommentListData {
    private HashMap<String, Conversion> commentContentArr;
    private int[] commentList;
    private int pageSize;
    private int totalPage;

    public HashMap<String, Conversion> getCommentContentArr() {
        return commentContentArr;
    }

    public int[] getCommentList() {
        return commentList;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getTotalPage() {
        return totalPage;
    }
}