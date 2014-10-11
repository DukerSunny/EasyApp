package tv.acfun.read.beans;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/10/09
 */
public class FullUser extends User {
    private int currExp;
    private int exp;
    private int followed;
    private int following;
    private int gender;
    private long lastLoginDate;
    private int level;
    private String location;
    private int nextLevelNeed;
    private String phone;
    private String qq;
    private String signature;

    public int getCurrExp() {
        return currExp;
    }

    public int getExp() {
        return exp;
    }

    public int getFollowed() {
        return followed;
    }

    public int getFollowing() {
        return following;
    }

    public int getGender() {
        return gender;
    }

    public long getLastLoginDate() {
        return lastLoginDate;
    }

    public int getLevel() {
        return level;
    }

    public String getLocation() {
        return location;
    }

    public int getNextLevelNeed() {
        return nextLevelNeed;
    }

    public String getPhone() {
        return phone;
    }

    public String getQq() {
        return qq;
    }

    public String getSignature() {
        return signature;
    }
}