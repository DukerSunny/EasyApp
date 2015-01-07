package air.tv.douyu.android.parsers;

import com.harreke.easyapp.parsers.ObjectParser;

import air.tv.douyu.android.beans.User;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/01/04
 */
public class LoginParser extends ObjectParser<User> {
    @Override
    public void parse(String json) {
        setObject(Parser.parseObject(json, User.class));
    }
}