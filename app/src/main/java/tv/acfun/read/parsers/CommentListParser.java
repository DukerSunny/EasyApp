package tv.acfun.read.parsers;

import android.content.Context;

import com.harreke.easyapp.listeners.OnTagClickListener;
import com.harreke.easyapp.tools.GsonUtil;
import com.harreke.easyapp.tools.NetUtil;

import java.util.ArrayList;
import java.util.HashMap;

import tv.acfun.read.bases.application.AcFunRead;
import tv.acfun.read.beans.CommentListPage;
import tv.acfun.read.beans.Conversion;
import tv.acfun.read.beans.FullConversion;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/09/26
 */
public class CommentListParser {
    private HashMap<String, CommentListPage> data;
    private ArrayList<FullConversion> mItemList;
    private int mTotalPage;
    private String msg;
    private int status;

    public static CommentListParser parse(Context context, String json, int maxQuote, OnTagClickListener tagClickListener) {
        CommentListParser parser = GsonUtil.toBean(json, CommentListParser.class);
        CommentListPage page;

        if (parser != null) {
            if (NetUtil.isStatusOk(parser.status) && parser.data != null) {
                page = parser.data.get("page");
                if (page != null) {
                    parser.decode(context, page, maxQuote, tagClickListener);

                    return parser;
                }
            }
        }

        return null;
    }

    private void decode(Context context, CommentListPage page, int maxQuote, OnTagClickListener tagClickListener) {
        Conversion content;
        Conversion quote;
        FullConversion fullConversion;
        ArrayList<Conversion> quoteList;
        HashMap<String, Conversion> map;
        ArrayList<Integer> list;
        int floorCount;
        int commentId;
        int quoteId;
        int repeatQuoteId;
        int i;

        mTotalPage = (page.getTotalCount() - 1) / page.getPageSize() + 1;
        mItemList = new ArrayList<FullConversion>();
        map = page.getMap();
        list = page.getList();
        for (i = 0; i < list.size(); i++) {
            commentId = list.get(i);
            content = map.get("c" + commentId);
            if (content.getSpanned() == null) {
                content.parse(context, tagClickListener);
            }
            quoteList = new ArrayList<Conversion>();
            quoteId = content.getQuoteId();
            quote = content;
            floorCount = 0;
            repeatQuoteId = 0;
            while (quoteId > 0) {
                quote.newQuote();

                quote = map.get("c" + quoteId);
                if (quote.getSpanned() == null) {
                    quote.parse(context, tagClickListener);
                }
                quote.newQuoted();
                if (repeatQuoteId == 0 && quote.getQuotedCount() > 1) {
                    repeatQuoteId = quote.getId();
                }

                quoteList.add(quote);
                quoteId = quote.getQuoteId();
                floorCount++;
            }

            fullConversion = new FullConversion();
            fullConversion.setContent(content);
            fullConversion.setFloorCount(floorCount);
            fullConversion.setRepeatQuoteId(repeatQuoteId);
            if (maxQuote < 0 || quoteList.size() <= maxQuote) {
                fullConversion.setQuoteList(quoteList);
            } else {
                fullConversion.setQuoteList(quoteList.subList(0, maxQuote));
            }
            mItemList.add(fullConversion);
        }
    }

    public ArrayList<FullConversion> getItemList() {
        return mItemList;
    }

    public int getTotalPage() {
        return mTotalPage;
    }
}