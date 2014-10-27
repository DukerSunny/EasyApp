package tv.acfun.read.beans;

import android.text.Spanned;

import com.google.gson.annotations.Expose;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/10/27
 */
public class Mail {
    private int fromuId;
    private String fromusername;
    private String lastMessage;
    @Expose(serialize = false, deserialize = false)
    private Spanned mSpanned;
    private int mailGroupId;
    private String p2p;
    private long postTime;
    private String user_img;

    public int getFromuId() {
        return fromuId;
    }

    public String getFromusername() {
        return fromusername;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public int getMailGroupId() {
        return mailGroupId;
    }

    public String getP2p() {
        return p2p;
    }

    public long getPostTime() {
        return postTime;
    }

    public Spanned getSpanned() {
        return mSpanned;
    }

    public String getUser_img() {
        return user_img;
    }

    public void setSpanned(Spanned spanned) {
        mSpanned = spanned;
    }
}