package tv.douyu.misc.danmaku.bean;

import java.io.Serializable;

/**
 * Created by neavo on 14-3-20.
 */

public class RoomBean implements Serializable {

    private String useName = "";
    private String roomGroup = "";
    private String roomPassword = "1234567890123456";
    private String keyName = "";
    private String npv = "";

    public String getNpv() {
        return npv;
    }

    public void setNpv(String npv) {
        this.npv = npv;
    }


    public String getUseName() {
        return useName;
    }

    public void setUseName(String useName) {
        this.useName = useName;
    }

    public void setkeyName(String keyName) {
        this.keyName = keyName;
    }

    public String getKeyName() {
        return keyName;
    }

    public String getRoomGroup() {
        return roomGroup;
    }

    public void setRoomGroup(String roomGroup) {
        this.roomGroup = roomGroup;
    }

    public String getRoomPassword() {
        return roomPassword;
    }

    public void setRoomPassword(String roomPassword) {
        this.roomPassword = roomPassword;
    }

    @Override
    public String toString() {
        return "RoomBean{" +
                "useName='" + useName + '\'' +
                ", roomGroup='" + roomGroup + '\'' +
                ", roomPassword='" + roomPassword + '\'' +
                ", npv='" + npv + '\'' +
                '}';
    }
}
