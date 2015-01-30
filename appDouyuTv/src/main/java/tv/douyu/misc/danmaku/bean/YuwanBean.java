package tv.douyu.misc.danmaku.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2014/11/11.
 */

//
//r:int,操作结果，0-成功，非零失败，283-钱不足，284-内部异常，285-操作超时，服务器繁忙
//        ms:int,鱼丸数量
//        sb:int64,鱼丸余额
//        strength:int64,赠送者战斗力余额
//        如果操作完成后服务器发送给客户端
//ben:YuwanBean{yuwan_r='0', yuwan_ms='100', yuwan_sb='900', yuwan_strength='300'}
public class YuwanBean implements Serializable {
    private String yuwan_r = null;
    private String yuwan_ms = null;
    private String yuwan_sb = null;
    private String yuwan_strength = null;


    public String getYuwan_r() {
        return yuwan_r;
    }

    public void setYuwan_r(String r) {
        yuwan_r = r;
    }

    public String getYuwan_ms() {
        return yuwan_ms;
    }

    public void setYuwan_ms(String ms) {
        yuwan_ms = ms;
    }

    public String getYuwan_sb() {
        return yuwan_sb;
    }

    public void setYuwan_sb(String sb) {
        yuwan_sb = sb;
    }

    public String getYuwan_strength() {
        return yuwan_strength;
    }

    public void setYuwan_strength(String strengths) {
        yuwan_strength = strengths;
    }

    @Override
    public String toString() {
        return "YuwanBean{" +
                "yuwan_r='" + yuwan_r + '\'' +
                ", yuwan_ms='" + yuwan_ms + '\'' +
                ", yuwan_sb='" + yuwan_sb + '\'' +
                ", yuwan_strength='" + yuwan_strength + '\'' +
                '}';
    }
}
