package tv.douyu.misc.danmaku.bean;

import java.io.Serializable;

/**
 * Created by neavo on 14-3-20.
 */
public class DanmakuBean implements Serializable {

	private String Content = "";
    private String resCode = "";
    private String nickName;

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}

    public String getResCode() {
        return resCode;
    }

    public void setResCode(String resCode) {
        this.resCode = resCode;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

	@Override
    public String toString() {
        return "DanmakuBean{" +
                "Content='" + Content + '\'' +
                "resCode='" + resCode + '\'' +
                '}';
    }
}
