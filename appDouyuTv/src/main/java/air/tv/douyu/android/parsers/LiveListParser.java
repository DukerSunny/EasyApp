package air.tv.douyu.android.parsers;

import com.harreke.easyapp.tools.GsonUtil;
import com.harreke.easyapp.tools.StringUtil;

import java.util.List;

import air.tv.douyu.android.beans.Room;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/12/26
 */
public class LiveListParser {
    private List<Room> data;
    private int error;

    public static LiveListParser parse(String json) {
        LiveListParser parser = GsonUtil.toBean(json, LiveListParser.class);
        List<Room> list;
        Room game;
        int i;

        if (parser != null && parser.error == 0 && parser.data != null) {
            list = parser.data;
            if (list.size() > 0) {
                for (i = 0; i < list.size(); i++) {
                    game = list.get(i);
                    game.setRoom_name(StringUtil.escapeHtmlSymbols(game.getRoom_name()));
                }

                return parser;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public List<Room> getData() {
        return data;
    }
}
