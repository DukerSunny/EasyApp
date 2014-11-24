package tv.acfun.read.parsers;

import android.text.Html;

import com.harreke.easyapp.tools.GsonUtil;

import java.util.ArrayList;

import tv.acfun.read.beans.Search;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/09/30
 */
public class SearchListParser {
    private ArrayList<Search> contents;
    private boolean success;
    private int totalPage;

    public static SearchListParser parse(String json) {
        SearchListParser parser = GsonUtil.toBean(json, SearchListParser.class);

        if (parser != null && parser.success && parser.contents != null) {
            parser.decode();

            return parser;
        } else {
            return null;
        }
    }

    private void decode() {
        Search search;
        int i;

        for (i = 0; i < contents.size(); i++) {
            search = contents.get(i);
            search.setSpanned(Html.fromHtml(search.getDescription()));
        }
    }

    public ArrayList<Search> getContents() {
        return contents;
    }

    public int getTotalPage() {
        return totalPage;
    }
}