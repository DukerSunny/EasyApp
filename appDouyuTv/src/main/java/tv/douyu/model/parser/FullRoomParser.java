package tv.douyu.model.parser;

import com.harreke.easyapp.parsers.IObjectParser;
import com.harreke.easyapp.parsers.ObjectResult;
import com.harreke.easyapp.parsers.Parser;

import tv.douyu.model.bean.FullRoom;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/12/29
 */
public class FullRoomParser implements IObjectParser<FullRoom> {
    @Override
    public ObjectResult<FullRoom> parse(String json) {
        if (json != null) {
            json = json.replace("\"rtmp_multi_bitrate\":[],", "");
        }
        return Parser.parseObject(json, FullRoom.class, "error", "data", "data");
    }
}