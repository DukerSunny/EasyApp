package tv.douyu.model.parser;

import com.harreke.easyapp.parsers.IListParser;
import com.harreke.easyapp.parsers.ListResult;
import com.harreke.easyapp.parsers.Parser;

import java.util.Iterator;

import tv.douyu.model.bean.Game;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/12/26
 */
public class GameListParser implements IListParser<Game> {
    private boolean mShowEmptyCategory = false;

    public GameListParser(boolean showEmptyCategory) {
        mShowEmptyCategory = showEmptyCategory;
    }

    @Override
    public ListResult<Game> parse(String json) {
        ListResult<Game> listResult = Parser.parseList(json, Game.class, "error", "data", "data");
        Iterator<Game> iterator;
        Game game;

        if (listResult != null && listResult.getList() != null && !mShowEmptyCategory) {
            iterator = listResult.getList().iterator();
            while (iterator.hasNext()) {
                game = iterator.next();
                if (game.getOnline_room() == 0) {
                    iterator.remove();
                }
            }
        }

        return listResult;
    }
}