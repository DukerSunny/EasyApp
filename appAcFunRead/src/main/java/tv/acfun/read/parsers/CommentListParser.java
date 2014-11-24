package tv.acfun.read.parsers;

import com.harreke.easyapp.listeners.OnTagClickListener;
import com.harreke.easyapp.tools.GsonUtil;
import com.harreke.easyapp.tools.NetUtil;

import java.util.ArrayList;
import java.util.HashMap;

import tv.acfun.read.beans.CommentListData;
import tv.acfun.read.beans.Conversion;
import tv.acfun.read.beans.FullConversion;
import tv.acfun.read.tools.ubb.UBBEncoder;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/09/26
 */
public class CommentListParser {
    private CommentListData data;
    private ArrayList<FullConversion> mItemList;
    private int mTotalPage;
    private String msg;
    private int status;

    public static CommentListParser parse(String json, int maxQuoteCount, int cid, OnTagClickListener tagClickListener) {
        CommentListParser parser = GsonUtil.toBean(json, CommentListParser.class);

        if (parser != null) {
            if (NetUtil.isStatusOk(parser.status) && parser.data != null && parser.data.getCommentList() != null &&
                    parser.data.getCommentContentArr() != null) {
                parser.decode(parser.data, maxQuoteCount, cid, tagClickListener);

                return parser;
            }
        }

        return null;
    }

    private void decode(CommentListData page, int maxQuoteCount, int cid, OnTagClickListener tagClickListener) {
        Conversion conversion;
        Conversion quote;
        FullConversion fullConversion;
        ArrayList<Conversion> quoteList;
        HashMap<String, Conversion> commentContentArr;
        int[] commentList;
        UBBEncoder encoder;
        int floorCount;
        int commentId;
        int quoteId;
        int i;

        encoder = new UBBEncoder();
        mTotalPage = page.getTotalPage();
        mItemList = new ArrayList<FullConversion>();
        commentContentArr = page.getCommentContentArr();
        commentList = page.getCommentList();
        if (cid > 0) {
            commentList = new int[]{cid};
        }
        for (i = 0; i < commentList.length; i++) {
            commentId = commentList[i];
            conversion = commentContentArr.get("c" + commentId);
            if (conversion.getSpanned() == null) {
                conversion.parse(encoder, tagClickListener);
            }
            quoteList = new ArrayList<Conversion>();
            quoteId = conversion.getQuoteId();
            quote = conversion;
            floorCount = 0;
            while (quoteId > 0) {
                quote.newQuote();
                quote = commentContentArr.get("c" + quoteId);
                if (quote.getSpanned() == null) {
                    quote.parse(encoder, tagClickListener);
                }
                quote.newQuoted();
                if (cid > 0 || quote.getQuotedCount() < 2 && quoteList.size() < maxQuoteCount) {
                    quoteList.add(0, quote);
                }
                quoteId = quote.getQuoteId();
                floorCount++;
            }
            fullConversion = new FullConversion();
            fullConversion.setConversion(conversion);
            fullConversion.setFloorCount(floorCount);
            fullConversion.setQuoteList(quoteList);
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