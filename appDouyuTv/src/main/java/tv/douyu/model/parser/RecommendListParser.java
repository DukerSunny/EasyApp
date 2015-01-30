package tv.douyu.model.parser;

import com.harreke.easyapp.parsers.IListParser;
import com.harreke.easyapp.parsers.ListResult;
import com.harreke.easyapp.parsers.Parser;
import com.harreke.easyapp.utils.StringUtil;

import java.util.List;

import tv.douyu.model.bean.Recommend;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/12/18
 */
public class RecommendListParser implements IListParser<Recommend> {
    @Override
    public ListResult<Recommend> parse(String json) {
        ListResult<Recommend> listResult = Parser.parseList(json, Recommend.class, "error", "data", "data");
        List<Recommend> list;
        Recommend recommend;
        int i;

        if (listResult != null && listResult.getList() != null) {
            list = listResult.getList();
            for (i = 0; i < list.size(); i++) {
                recommend = list.get(i);
                recommend.setTitle(StringUtil.escapeHtmlSymbols(recommend.getTitle()));
            }
        }

        return listResult;
    }
}