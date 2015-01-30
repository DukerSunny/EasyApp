package tv.douyu.misc.danmaku.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by neavo on 14-3-19.
 */

public class ServerGroupBean implements Serializable {

	private String roomID = "";
	private ArrayList<ServerBean> serverArray = new ArrayList<>();

	public String getRoomID() {
		return roomID;
	}

	public void setRoomID(String roomID) {
		this.roomID = roomID;
	}

	public ArrayList<ServerBean> getServerArray() {
		return serverArray;
	}

	public void setServerArray(ArrayList<ServerBean> serverArray) {
		this.serverArray = serverArray;
	}

	@Override
	public String toString() {
		return "ServerGroupBean{" +
				"roomID='" + roomID + '\'' +
				", serverArray=" + serverArray +
				'}';
	}
}
