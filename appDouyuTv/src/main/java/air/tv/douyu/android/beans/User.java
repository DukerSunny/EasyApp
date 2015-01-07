package air.tv.douyu.android.beans;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/01/04
 */
public class User {
    private Avatar avatar;
    private String email;
    private int email_status;
    private int follow;
    private int gold1;
    private long lastlogin;
    private String mobile_phone;
    private String nickname;
    private int phone_status;
    private String qq;
    private int score;
    private String token;
    private long token_exp;
    private int uid;
    private String username;

    public Avatar getAvatar() {
        return avatar;
    }

    public String getEmail() {
        return email;
    }

    public int getEmail_status() {
        return email_status;
    }

    public int getFollow() {
        return follow;
    }

    public int getGold1() {
        return gold1;
    }

    public long getLastlogin() {
        return lastlogin;
    }

    public String getMobile_phone() {
        return mobile_phone;
    }

    public String getNickname() {
        return nickname;
    }

    public int getPhone_status() {
        return phone_status;
    }

    public String getQq() {
        return qq;
    }

    public int getScore() {
        return score;
    }

    public String getToken() {
        return token;
    }

    public long getToken_exp() {
        return token_exp;
    }

    public int getUid() {
        return uid;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public int hashCode() {
        return uid;
    }

    public void setAvatar(Avatar avatar) {
        this.avatar = avatar;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setEmail_status(int email_status) {
        this.email_status = email_status;
    }

    public void setFollow(int follow) {
        this.follow = follow;
    }

    public void setGold1(int gold1) {
        this.gold1 = gold1;
    }

    public void setLastlogin(long lastlogin) {
        this.lastlogin = lastlogin;
    }

    public void setMobile_phone(String mobile_phone) {
        this.mobile_phone = mobile_phone;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setPhone_status(int phone_status) {
        this.phone_status = phone_status;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setToken_exp(long token_exp) {
        this.token_exp = token_exp;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
