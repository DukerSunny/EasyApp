package air.tv.douyu.android.parsers;

import com.harreke.easyapp.parsers.ListParser;
import com.harreke.easyapp.utils.StringUtil;

import java.util.List;

import air.tv.douyu.android.beans.Room;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/12/26
 */
public class RoomListParser extends ListParser<Room> {
    @Override
    public void parse(String json) {
        List<Room> list = Parser.parseList(json, Room.class);
        Room room;
        int i;

        if (list != null) {
            for (i = 0; i < list.size(); i++) {
                room = list.get(i);
                room.setRoom_name(StringUtil.escapeHtmlSymbols(room.getRoom_name()));
            }
            setList(list);
        }
    }
}