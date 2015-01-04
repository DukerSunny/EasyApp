package air.tv.douyu.android.parsers;

import com.harreke.easyapp.utils.GsonUtil;

import air.tv.douyu.android.beans.FullRoom;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/12/29
 */
public class FullRoomParser {
    private FullRoom data;
    private int error;

    public static FullRoomParser parse(String json) {
        if (json != null) {
            json = json.replace("\"rtmp_multi_bitrate\":[],", "");
        }
        FullRoomParser parser = GsonUtil.toBean(json, FullRoomParser.class);

        if (parser != null && parser.error == 0 && parser.data != null) {
            return parser;
        }

        return null;
    }

    public FullRoom getData() {
        return data;
    }
}