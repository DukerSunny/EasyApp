package tv.douyu.misc.danmaku.bean;

import java.io.Serializable;

/**
 * Created by neavo on 14-3-19.
 */
public class ServerBean implements Serializable {

	private String url = "";
	private String port = "";

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	@Override
	public String toString() {
		return "ServerBean{" +
				"url='" + url + '\'' +
				", port='" + port + '\'' +
				'}';
	}
}
