package tv.douyu.misc.danmaku.bean;

import java.io.Serializable;

/**
 * Created by neavo on 14-3-19.
 */

public class ErrorBean implements Serializable {

	private String code = "";

	public String getMessage() {
		String msg = "";

		switch (code) {
			case "51":
				msg = "数据传输出错，无法恢复";
				break;
			case "52":
				msg = "服务器关闭";
				break;
			case "53":
				msg = "服务器繁忙";
				break;
			case "54":
				msg = "服务器维护中";
				break;
			case "55":
				msg = "用户满员";
				break;
			case "56":
				msg = "IP封禁";
				break;
			case "57":
				msg = "帐号封禁";
				break;
			case "58":
				msg = "用户名密码错误";
				break;
			case "59":
				msg = "用户在别处登陆";
				break;
		}

		return msg;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public String toString() {
		return "ErrorBean{" +
				"code='" + code + '\'' +
				'}';
	}
}
