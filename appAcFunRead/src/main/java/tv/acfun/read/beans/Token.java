package tv.acfun.read.beans;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/10/09
 */
public class Token {
    private String access_token;
    private long expires;
    private int userId;

    public String getAccess_token() {
        return access_token;
    }

    public long getExpires() {
        return expires;
    }

    public int getUserId() {
        return userId;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() >= expires;
    }
}