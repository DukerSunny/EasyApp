package air.tv.douyu.android.parsers;

import com.harreke.easyapp.parsers.ListParser;
import com.harreke.easyapp.utils.StringUtil;

import java.util.List;

import air.tv.douyu.android.beans.Game;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/12/26
 */
public class GameListParser extends ListParser<Game> {
    @Override
    public void parse(String json) {
        List<Game> list = Parser.parseList(json, Game.class);
        Game game;
        int i;

        if (list != null) {
//            for (i = 0; i < list.size(); i++) {
//                game = list.get(i);
//                game.setGame_name(StringUtil.escapeHtmlSymbols(game.getGame_name()));
//            }
            setList(list);
        }
    }
}