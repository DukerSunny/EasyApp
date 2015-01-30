package tv.douyu.model.bean;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/01/29
 */
public class Update {
    private String down_url;
    private String filesize;
    private int force_update;
    private String update_content;
    private String version;

    public String getDown_url() {
        return down_url;
    }

    public String getFilesize() {
        return filesize;
    }

    public int getForce_update() {
        return force_update;
    }

    public String getUpdate_content() {
        return update_content;
    }

    public String getVersion() {
        return version;
    }

    public void setDown_url(String down_url) {
        this.down_url = down_url;
    }

    public void setFilesize(String filesize) {
        this.filesize = filesize;
    }

    public void setForce_update(int force_update) {
        this.force_update = force_update;
    }

    public void setUpdate_content(String update_content) {
        this.update_content = update_content;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
