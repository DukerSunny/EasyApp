package tv.acfun.read.parsers;

import com.harreke.easyapp.utils.GsonUtil;

import tv.acfun.read.beans.Token;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/10/09
 */
public class TokenParser {
    private Token data;
    private boolean success;

    public static TokenParser parser(String json) {
        return GsonUtil.toBean(json, TokenParser.class);
    }

    public Token getData() {
        return data;
    }

    public boolean isSuccess() {
        return success;
    }
}
