package air.tv.douyu.android.parsers;

import com.harreke.easyapp.parsers.ObjectParser;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/01/09
 */
public class StringParser extends ObjectParser<String> {
    @Override
    public void parse(String json) {
        setObject(Parser.parseObject(json, String.class));
    }
}