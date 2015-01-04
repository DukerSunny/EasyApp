package tv.acfun.read.parsers;

import android.text.Html;

import com.harreke.easyapp.utils.GsonUtil;

import java.util.List;

import tv.acfun.read.beans.Search;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/09/30
 */
public class SearchListParserBak {
    private List<Search> list;
    private boolean success;

    public static SearchListParserBak parse(String json) {
        SearchListParserBak parser = GsonUtil.toBean(json, SearchListParserBak.class);

        if (parser != null && parser.success && parser.list != null) {
            parser.decode();

            return parser;
        } else {
            return null;
        }
    }

    private void decode() {
        Search search;
        int i;

        for (i = 0; i < list.size(); i++) {
            search = list.get(i);
            search.setSpanned(Html.fromHtml(search.getDescription()));
        }
    }

    public List<Search> getList() {
        return list;
    }
}