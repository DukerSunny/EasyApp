package air.tv.douyu.android.beans;

import java.util.List;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/12/18
 */
public class Recommend {
    private int cate_id;
    private List<Room> roomlist;
    private String title;

    public int getCate_id() {
        return cate_id;
    }

    public List<Room> getRoomlist() {
        return roomlist;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public int hashCode() {
        return cate_id;
    }

    public void setCate_id(int cate_id) {
        this.cate_id = cate_id;
    }

    public void setRoomlist(List<Room> roomlist) {
        this.roomlist = roomlist;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
