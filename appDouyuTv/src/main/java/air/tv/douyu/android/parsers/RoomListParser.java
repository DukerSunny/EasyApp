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
        Room game;
        int i;

        if (list != null) {
            for (i = 0; i < list.size(); i++) {
                game = list.get(i);
                game.setRoom_name(StringUtil.escapeHtmlSymbols(game.getRoom_name()));
            }
            setList(list);
        }
    }
}