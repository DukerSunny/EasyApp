package tv.acfun.read.parsers;

import android.content.Context;
import android.util.SparseIntArray;

import com.harreke.easyapp.tools.GsonUtil;
import com.harreke.easyapp.tools.NetUtil;

import java.util.ArrayList;
import java.util.HashMap;

import tv.acfun.read.beans.Comment;
import tv.acfun.read.beans.CommentListPage;
import tv.acfun.read.beans.ConvertedComment;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/09/26
 */
public class CommentListParser {
    private HashMap<String, CommentListPage> data;
    private ArrayList<ConvertedComment> mItemList;
    private int mTotalPage;
    private String msg;
    private int status;

    public static CommentListParser parse(Context context, String json) {
        CommentListParser parser = GsonUtil.toBean(json, CommentListParser.class);
        CommentListPage page;

        if (parser != null) {
            if (NetUtil.isStatusOk(parser.status) && parser.data != null) {
                page = parser.data.get("page");
                if (page != null) {
                    parser.decode(context, page);

                    return parser;
                }
            }
        }

        return null;
    }

    private void decode(Context context, CommentListPage page) {
        SparseIntArray quotedCountList = new SparseIntArray();
        Comment comment;
        ConvertedComment convertedComment;
        boolean inQuotedMode;
        int commentId;
        int quotedCount;
        int quoteId;
        int quoteCount;
        int i;

        mTotalPage = (page.getTotalCount() - 1) / page.getPageSize() + 1;
        mItemList = new ArrayList<ConvertedComment>();
        for (i = 0; i < page.getList().size(); i++) {
            commentId = page.getList().get(i);
            inQuotedMode = false;
            quoteCount = 0;
            while (commentId > 0) {
                comment = page.getMap().get("c" + commentId);
                convertedComment = new ConvertedComment();
                convertedComment.setInQuoteMode(inQuotedMode);
                convertedComment.parse(context, comment);
//                quoteId = comment.getQuoteId();
                quoteId = 0;
                if (quoteId > 0) {
                    quotedCount = quotedCountList.get(quoteId);
                    if (quotedCount == 0) {
                        convertedComment.setHasRepeatQuote(false);
                    } else {
                        convertedComment.setHasRepeatQuote(true);
                    }
                    quotedCount++;
                    quotedCountList.put(quoteId, quotedCount);
                    quoteCount++;
                } else {
                    convertedComment.setHasRepeatQuote(false);
                }
                if (quoteCount == 0) {
                    mItemList.add(convertedComment);
                } else {
                    mItemList.add(i, convertedComment);
                }
                inQuotedMode = true;
                commentId = quoteId;
            }
        }
    }

    public ArrayList<ConvertedComment> getItemList() {
        return mItemList;
    }

    public int getTotalPage() {
        return mTotalPage;
    }
}