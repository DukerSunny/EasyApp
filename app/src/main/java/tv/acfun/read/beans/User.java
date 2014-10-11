package tv.acfun.read.beans;

import tv.acfun.read.bases.application.AcFunRead;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/09/24
 */
public class User {
    private int userId;
    private String userImg;
    private String username;

    public int getUserId() {
        return userId;
    }

    public String getUserImg() {
        if (userImg == null) {
            userImg = AcFunRead.CacheDir + "/" + AcFunRead.DIR_ASSETS + "/avatar";
        }

        return userImg;
    }

    public String getUsername() {
        return username;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}