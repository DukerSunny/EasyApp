package tv.acfun.read.holders;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.harreke.easyapp.holders.abslistview.IAbsListHolder;

import java.util.List;

import tv.acfun.read.R;
import tv.acfun.read.beans.Conversion;
import tv.acfun.read.beans.FullConversion;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/10/05
 */
public class FullConversionHolder implements IAbsListHolder<FullConversion> {
    private TextView comment_quote_expand;
    private String comment_quote_expand_text;
    private CommentFloorHolder mCommentFloor;
    private CommentQuoteHolder[] mCommentQuotes;
    private int mMaxQuoteCount;
    private View.OnClickListener mOnCloseClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (Integer) v.getTag();

            if (position == -1) {
                mCommentFloor.closeSwipe();
            } else {
                mCommentQuotes[position].closeSwipe();
            }
        }
    };
    private View.OnClickListener mOnOpenClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (Integer) v.getTag();
            int i;

            if (position == -1) {
                mCommentFloor.openSwipe();
                for (i = 0; i < mCommentQuotes.length; i++) {
                    mCommentQuotes[i].closeSwipe();
                }
            } else {
                for (i = 0; i < mCommentQuotes.length; i++) {
                    if (position != i) {
                        mCommentQuotes[i].closeSwipe();
                    }
                }
                mCommentQuotes[position].openSwipe();
            }
        }
    };

    public FullConversionHolder(View convertView, int maxQuoteCount, View.OnClickListener onUserClickListener,
            View.OnClickListener onCopyClickListener, View.OnClickListener onReplyClickListener,
            View.OnClickListener onQuoteExpandClickListener) {
        Context context = convertView.getContext();
        Resources resources = context.getResources();
        LinearLayout comment_quotes;
        View quoteView;
        int i;

        mMaxQuoteCount = maxQuoteCount;
        mCommentQuotes = new CommentQuoteHolder[mMaxQuoteCount];
        comment_quotes = (LinearLayout) convertView.findViewById(R.id.comment_quotes);
        for (i = 0; i < maxQuoteCount; i++) {
            quoteView = View.inflate(context, R.layout.item_comment_quote, null);
            comment_quotes.addView(quoteView);
            mCommentQuotes[i] = new CommentQuoteHolder(quoteView);
            mCommentQuotes[i].setOnOpenClickListener(mOnOpenClickListener);
            mCommentQuotes[i].setOnCloseClickListener(mOnCloseClickListener);
            mCommentQuotes[i].setOnUserClickListener(onUserClickListener);
            mCommentQuotes[i].setOnCopyClickListener(onCopyClickListener);
            mCommentQuotes[i].setOnReplyClickListener(onReplyClickListener);
        }

        mCommentFloor = new CommentFloorHolder(convertView);
        mCommentFloor.setOnOpenClickListener(mOnOpenClickListener);
        mCommentFloor.setOnCloseClickListener(mOnCloseClickListener);
        mCommentFloor.setOnUserClickListener(onUserClickListener);
        mCommentFloor.setOnCopyClickListener(onCopyClickListener);
        mCommentFloor.setOnReplyClickListener(onReplyClickListener);

        comment_quote_expand = (TextView) convertView.findViewById(R.id.comment_quote_expand);
        comment_quote_expand_text = resources.getString(R.string.comment_quote_expand);

        comment_quote_expand.setOnClickListener(onQuoteExpandClickListener);
    }

    @Override
    public void setItem(int position, FullConversion fullConversion) {
        List<Conversion> quoteList = fullConversion.getQuoteList();
        Conversion conversion = fullConversion.getConversion();
        int floorCount = fullConversion.getFloorCount();
        int size;
        int i;

        if (floorCount > mMaxQuoteCount) {
            comment_quote_expand.setVisibility(View.VISIBLE);
            comment_quote_expand.setTag(position);
            comment_quote_expand.setText(String.format(comment_quote_expand_text, floorCount));
        } else {
            comment_quote_expand.setVisibility(View.GONE);
        }
        size = quoteList.size();
        for (i = 0; i < size; i++) {
            mCommentQuotes[i].show();
            mCommentQuotes[i].setFloorPosition(position);
            mCommentQuotes[i].setItem(i, quoteList.get(i));
        }
        for (i = size; i < mMaxQuoteCount; i++) {
            mCommentQuotes[i].hide();
        }
        mCommentFloor.setItem(position, conversion);
    }
}