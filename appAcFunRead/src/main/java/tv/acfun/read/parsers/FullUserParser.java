package tv.acfun.read.parsers;

import com.harreke.easyapp.utils.GsonUtil;
import com.harreke.easyapp.utils.NetUtil;

import java.util.HashMap;

import tv.acfun.read.beans.FullUser;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/10/09
 */
public class FullUserParser {
    private HashMap<String, FullUser> data;
    private FullUser mFullUser;
    private String msg;
    private int status;

    public static FullUserParser parse(String json) {
        FullUserParser parser = GsonUtil.toBean(json, FullUserParser.class);
        if (parser != null) {
            if (NetUtil.isStatusOk(parser.status) && parser.data != null) {
                parser.mFullUser = parser.data.get("fullUser");

                return parser;
            }
        }

        return null;
    }

    public FullUser getFullUser() {
        return mFullUser;
    }
}