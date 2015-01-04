package tv.acfun.read.parsers;

import android.text.Html;
import android.util.Log;

import com.harreke.easyapp.utils.GsonUtil;
import com.harreke.easyapp.utils.NetUtil;

import java.util.HashMap;
import java.util.List;

import tv.acfun.read.beans.Search;
import tv.acfun.read.beans.SearchListPage;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/09/24
 */
public class SearchListParser {
    private HashMap<String, SearchListPage> data;
    private List<Search> mItemList;
    private int status;

    public static SearchListParser parse(String json) {
        Log.e(null, "json=" + json);
        SearchListParser parser = GsonUtil.toBean(json, SearchListParser.class);
        SearchListPage page;

        if (parser != null) {
            Log.e(null, "list=null?" + (parser.data.get("page").getList() == null));
            if (NetUtil.isStatusOk(parser.status) && parser.data != null) {
                page = parser.data.get("page");
                if (page != null) {
                    parser.decode(page);

                    return parser;
                }
            }
        }

        return null;
    }

    private void decode(SearchListPage page) {
        Search search;
        int i;

        mItemList = page.getList();
        for (i = 0; i < mItemList.size(); i++) {
            search = mItemList.get(i);
            search.setSpanned(Html.fromHtml(search.getDescription()));
        }
    }

    public List<Search> getItemList() {
        return mItemList;
    }
}