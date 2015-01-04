package air.tv.douyu.android.parsers;

import com.harreke.easyapp.utils.GsonUtil;
import com.harreke.easyapp.utils.StringUtil;

import java.util.List;

import air.tv.douyu.android.beans.Game;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/12/26
 */
public class GameListParser {
    private List<Game> data;
    private int error;

    public static GameListParser parse(String json) {
        GameListParser parser = GsonUtil.toBean(json, GameListParser.class);
        List<Game> list;
        Game game;
        int i;

        if (parser != null && parser.error == 0 && parser.data != null) {
            list = parser.data;
            if (list.size() > 0) {
                for (i = 0; i < list.size(); i++) {
                    game = list.get(i);
                    game.setGame_name(StringUtil.escapeHtmlSymbols(game.getGame_name()));
                }

                return parser;
            }
        }

        return null;
    }

    public List<Game> getData() {
        return data;
    }
}