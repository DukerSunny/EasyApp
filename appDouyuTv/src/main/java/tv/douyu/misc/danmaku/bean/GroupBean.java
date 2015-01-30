package tv.douyu.misc.danmaku.bean;

/**
 * Created by neavo on 14-3-20.
 */
public class GroupBean {

	private String roomID = "";
	private String groupID = "";

	public String getRoomID() {
		return roomID;
	}

	public void setRoomID(String roomID) {
		this.roomID = roomID;
	}

	public String getGroupID() {
		return groupID;
	}

	public void setGroupID(String groupID) {
		this.groupID = groupID;
	}

	@Override
	public String toString() {
		return "GroupBean{" +
				"roomID='" + roomID + '\'' +
				", groupID='" + groupID + '\'' +
				'}';
	}
}
