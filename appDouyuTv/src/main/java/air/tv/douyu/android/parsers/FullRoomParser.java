package air.tv.douyu.android.parsers;

import com.harreke.easyapp.parsers.ObjectParser;

import air.tv.douyu.android.beans.FullRoom;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/12/29
 */
public class FullRoomParser extends ObjectParser<FullRoom> {
    @Override
    public void parse(String json) {
        if (json != null) {
            json = json.replace("\"rtmp_multi_bitrate\":[],", "");
        }
        setObject(Parser.parseObject(json, FullRoom.class));
    }
}