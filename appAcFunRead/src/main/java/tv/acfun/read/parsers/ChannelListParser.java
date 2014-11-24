package tv.acfun.read.parsers;

import android.text.Html;

import com.harreke.easyapp.tools.GsonUtil;
import com.harreke.easyapp.tools.NetUtil;

import java.util.ArrayList;
import java.util.HashMap;

import tv.acfun.read.beans.ChannelListPage;
import tv.acfun.read.beans.Content;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/09/24
 */
public class ChannelListParser {
    private HashMap<String, ChannelListPage> data;
    private ArrayList<Content> mItemList;
    private int mTotalPage;
    private String msg;
    private int status;


    public static ChannelListParser parse(String json) {
        ChannelListParser parser = GsonUtil.toBean(json, ChannelListParser.class);
        ChannelListPage page;

        if (parser != null) {
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

    private void decode(ChannelListPage page) {
        Content content;
        int i;

        mTotalPage = (page.getTotalCount() - 1) / page.getPageSize() + 1;
        mItemList = page.getList();
        for (i = 0; i < mItemList.size(); i++) {
            content = mItemList.get(i);
            content.setSpanned(Html.fromHtml(content.getDescription()));
        }
    }

    public ArrayList<Content> getItemList() {
        return mItemList;
    }

    public int getTotalPage() {
        return mTotalPage;
    }
}