package tv.acfun.read.parsers;

import com.harreke.easyapp.utils.GsonUtil;

import tv.acfun.read.beans.UserJson;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/10/09
 */
public class UserJsonParser {
    private UserJson userjson;

    public static UserJsonParser parse(String json) {
        UserJsonParser parser = GsonUtil.toBean(json, UserJsonParser.class);

        if (parser != null) {
            if (parser.userjson != null && parser.userjson.getUid() != 0) {
                return parser;
            }
        }

        return null;
    }

    public UserJson getUserjson() {
        return userjson;
    }
}