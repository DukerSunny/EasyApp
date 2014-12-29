package air.tv.douyu.android.beans;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/12/18
 */
public class SlideShow {
    private int id;
    private String pic_url;
    private Room room;
    private String title;

    public int getId() {
        return id;
    }

    public String getPic_url() {
        return pic_url;
    }

    public Room getRoom() {
        return room;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public int hashCode() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
    }

    public void setRoom(FullRoom room) {
        this.room = room;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
