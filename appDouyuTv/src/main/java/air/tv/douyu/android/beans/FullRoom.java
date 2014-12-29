package air.tv.douyu.android.beans;

import java.util.List;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/12/18
 */
public class FullRoom extends Room {
    private String owner_avatar;
    private String owner_weight;
    private String rtmp_cdn;
    private String rtmp_live;
    private MultiBitrate rtmp_multi_bitrate;
    private String rtmp_url;
    private List<Server> servers;
    private int use_p2p;

    public String getOwner_avatar() {
        return owner_avatar;
    }

    public String getOwner_weight() {
        return owner_weight;
    }

    public String getRtmp_cdn() {
        return rtmp_cdn;
    }

    public String getRtmp_live() {
        return rtmp_live;
    }

    public MultiBitrate getRtmp_multi_bitrate() {
        return rtmp_multi_bitrate;
    }

    public String getRtmp_url() {
        return rtmp_url;
    }

    public List<Server> getServers() {
        return servers;
    }

    public int getUse_p2p() {
        return use_p2p;
    }

    @Override
    public int hashCode() {
        return getRoom_id();
    }

    public void setOwner_avatar(String owner_avatar) {
        this.owner_avatar = owner_avatar;
    }

    public void setOwner_weight(String owner_weight) {
        this.owner_weight = owner_weight;
    }

    public void setRtmp_cdn(String rtmp_cdn) {
        this.rtmp_cdn = rtmp_cdn;
    }

    public void setRtmp_live(String rtmp_live) {
        this.rtmp_live = rtmp_live;
    }

    public void setRtmp_multi_bitrate(MultiBitrate rtmp_multi_bitrate) {
        this.rtmp_multi_bitrate = rtmp_multi_bitrate;
    }

    public void setRtmp_url(String rtmp_url) {
        this.rtmp_url = rtmp_url;
    }

    public void setServers(List<Server> servers) {
        this.servers = servers;
    }

    public void setUse_p2p(int use_p2p) {
        this.use_p2p = use_p2p;
    }
}
