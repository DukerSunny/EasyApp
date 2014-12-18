package tv.acfun.read.holders;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.harreke.easyapp.holders.recycerview.RecyclerHolder;
import com.harreke.easyapp.widgets.RippleDrawable;

import java.util.List;

import tv.acfun.read.R;
import tv.acfun.read.beans.Conversion;
import tv.acfun.read.beans.FullConversion;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/10/05
 */
public class FullConversionHolder extends RecyclerHolder<FullConversion> {
    private View comment_quote_expand;
    private String comment_quote_expand_string;
    private TextView comment_quote_expand_text;
    private CommentFloorHolder mCommentFloor;
    private View[] mCommentQuoteDividers;
    private CommentQuoteHolder[] mCommentQuotes;
    private int mMaxQuoteCount;

    public FullConversionHolder(View convertView, int textSize, int maxQuoteCount, View.OnClickListener onAvatarClickListener,
            View.OnClickListener onQuoteExpandClickListener, View.OnClickListener onQuoteClickListener) {
        super(convertView);
        Context context = convertView.getContext();
        Resources resources = context.getResources();
        LinearLayout comment_quotes;
        LayoutInflater inflater;
        View quoteView;
        View dividerView;
        int i;

        mMaxQuoteCount = maxQuoteCount;
        mCommentQuotes = new CommentQuoteHolder[mMaxQuoteCount];
        mCommentQuoteDividers = new View[mMaxQuoteCount];
        comment_quotes = (LinearLayout) convertView.findViewById(R.id.comment_quotes);
        inflater = LayoutInflater.from(context);
        for (i = 0; i < mMaxQuoteCount; i++) {
            quoteView = inflater.inflate(R.layout.item_comment_quote, comment_quotes, false);
            dividerView = inflater.inflate(R.layout.widget_divider_horizontal, comment_quotes, false);
            comment_quotes.addView(quoteView);
            comment_quotes.addView(dividerView);
            mCommentQuotes[i] = new CommentQuoteHolder(quoteView);
            mCommentQuotes[i].setTextSize(textSize);
            mCommentQuotes[i].setOnClickListener(onQuoteClickListener);
            mCommentQuoteDividers[i] = dividerView;
        }

        mCommentFloor = new CommentFloorHolder(convertView);
        mCommentFloor.setTextSize(textSize);
        mCommentFloor.setOnAvatarClickListener(onAvatarClickListener);

        comment_quote_expand = convertView.findViewById(R.id.comment_quote_expand);
        comment_quote_expand_text = (TextView) convertView.findViewById(R.id.comment_quote_expand_text);
        comment_quote_expand_string = resources.getString(R.string.comment_quote_expand);

        comment_quote_expand.setOnClickListener(onQuoteExpandClickListener);

        RippleDrawable.attach(comment_quote_expand);
    }

    @Override
    public void setItem(FullConversion fullConversion) {
        List<Conversion> quoteList = fullConversion.getQuoteList();
        Conversion conversion = fullConversion.getConversion();
        int position = getPosition();
        int floorCount = fullConversion.getFloorCount();
        int size;
        int i;

        if (floorCount > mMaxQuoteCount) {
            comment_quote_expand.setVisibility(View.VISIBLE);
            comment_quote_expand.setTag(position);
            comment_quote_expand_text.setText(String.format(comment_quote_expand_string, floorCount));
        } else {
            comment_quote_expand.setVisibility(View.GONE);
        }
        size = quoteList.size();
        for (i = 0; i < size; i++) {
            mCommentQuotes[i].show();
            mCommentQuotes[i].setItem(quoteList.get(i));
            mCommentQuotes[i].itemView.setTag(R.id.comment_floor_position, getPosition());
            mCommentQuotes[i].itemView.setTag(R.id.comment_quote_position, i);
            mCommentQuoteDividers[i].setVisibility(View.VISIBLE);
        }
        for (i = size; i < mMaxQuoteCount; i++) {
            mCommentQuotes[i].hide();
            mCommentQuoteDividers[i].setVisibility(View.GONE);
        }
        mCommentFloor.setItem(conversion);
    }
}