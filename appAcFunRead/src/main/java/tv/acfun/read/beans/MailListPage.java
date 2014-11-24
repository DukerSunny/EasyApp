package tv.acfun.read.beans;

import java.util.ArrayList;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/09/24
 */
public class MailListPage {
    private ArrayList<String> isFriendList;
    private ArrayList<String> mailList;
    private int totalPage;
    private ArrayList<String> unReadList;

    public ArrayList<String> getIsFriendList() {
        return isFriendList;
    }

    public ArrayList<String> getMailList() {
        return mailList;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public ArrayList<String> getUnReadList() {
        return unReadList;
    }
}