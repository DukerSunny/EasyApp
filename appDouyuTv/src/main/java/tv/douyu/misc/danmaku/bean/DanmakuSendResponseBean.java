package tv.douyu.misc.danmaku.bean;

import java.io.Serializable;

/**
 * Created by wm on 2014/11/12.
 */
public class DanmakuSendResponseBean implements Serializable{
    //cd maxl
    private String cdtime = "";
    private String maxlength= "";

    public void setCdtime(String time) {
            cdtime = time;
    }

    public void setMaxlength(String length) {
        maxlength = length;
    }

    public String getCdtime() {
        return cdtime;
    }

    public String getMaxlength() {
        return maxlength;
    }



    @Override
    public String toString() {
        return "DanmuSendResponseBean{" +
                "cdtime='" + cdtime + '\'' +
                ", maxlength='" + maxlength + '\'' +
                '}';
    }

}
