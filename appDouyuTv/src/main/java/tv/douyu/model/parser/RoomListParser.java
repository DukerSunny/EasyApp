package tv.douyu.model.parser;

import com.harreke.easyapp.parsers.IListParser;
import com.harreke.easyapp.parsers.ListResult;
import com.harreke.easyapp.parsers.Parser;
import com.harreke.easyapp.utils.StringUtil;

import java.util.List;

import tv.douyu.model.bean.Room;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/12/26
 */
public class RoomListParser implements IListParser<Room> {
    @Override
    public ListResult<Room> parse(String json) {
        ListResult<Room> listResult = Parser.parseList(json, Room.class, "error", "data", "data");
        List<Room> list;
        Room room;
        int i;

        if (listResult != null && listResult.getList() != null) {
            list = listResult.getList();
            for (i = 0; i < list.size(); i++) {
                room = list.get(i);
                room.setRoom_name(StringUtil.escapeHtmlSymbols(room.getRoom_name()));
            }
        }

        return listResult;
    }
}