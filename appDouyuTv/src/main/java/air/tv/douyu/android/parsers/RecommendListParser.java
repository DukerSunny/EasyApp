package air.tv.douyu.android.parsers;

import com.harreke.easyapp.parsers.ListParser;
import com.harreke.easyapp.utils.StringUtil;

import java.util.List;

import air.tv.douyu.android.beans.Recommend;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/12/18
 */
public class RecommendListParser extends ListParser<Recommend> {
    @Override
    public void parse(String json) {
        List<Recommend> list = Parser.parseList(json, Recommend.class);
        Recommend recommend;
        int i;

        if (list != null) {
            for (i = 0; i < list.size(); i++) {
                recommend = list.get(i);
                recommend.setTitle(StringUtil.escapeHtmlSymbols(recommend.getTitle()));
            }
            setList(list);
        }
    }
}