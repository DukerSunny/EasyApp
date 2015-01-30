package tv.douyu.misc.danmaku.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2014/7/14.
 */
public class LiveStatusBean implements Serializable {

    private String roomID = "";
    private String liveStatus = "";

    public String getLiveStatus() {
        return liveStatus;
    }

    public void setLiveStatus(String status) {
        this.liveStatus = status;
    }


    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String id) {
        roomID = id;
    }

    @Override
    public String toString() {
        return "LiveStatusBean{" +
                "roomID='" + roomID + '\'' +"livestatus='" + liveStatus+'\''+
                '}';
    }
}
