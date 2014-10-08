package tv.acfun.read.holders;

import android.content.res.Resources;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.harreke.easyapp.helpers.ImageLoaderHelper;
import com.harreke.easyapp.holders.abslistview.IAbsListHolder;

import java.util.List;

import tv.acfun.read.R;
import tv.acfun.read.beans.Conversion;
import tv.acfun.read.beans.FullConversion;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/10/05
 */
public class FullConversionHolder implements IAbsListHolder<FullConversion> {
    private TextView comment_date;
    private View comment_options;
    private TextView comment_quote_expand;
    private String comment_quote_expand_text;
    private View comment_quote_repeat;
    private TextView comment_text;
    private ImageView comment_userImg;
    private TextView comment_username;
    private CommentQuoteHolder[] mCommentQuotes;

    public FullConversionHolder(View convertView) {
        Resources resources = convertView.getResources();

        mCommentQuotes = new CommentQuoteHolder[3];
        mCommentQuotes[0] = new CommentQuoteHolder(convertView.findViewById(R.id.comment_quote1));
        mCommentQuotes[1] = new CommentQuoteHolder(convertView.findViewById(R.id.comment_quote2));
        mCommentQuotes[2] = new CommentQuoteHolder(convertView.findViewById(R.id.comment_quote3));
        comment_quote_expand = (TextView) convertView.findViewById(R.id.comment_quote_expand);
        comment_quote_repeat = convertView.findViewById(R.id.comment_quote_repeat);
        comment_quote_expand_text = resources.getString(R.string.comment_quote_expand);

        comment_userImg = (ImageView) convertView.findViewById(R.id.comment_userImg);
        comment_username = (TextView) convertView.findViewById(R.id.comment_username);
        comment_date = (TextView) convertView.findViewById(R.id.comment_date);
        comment_options = convertView.findViewById(R.id.comment_options);
        comment_text = (TextView) convertView.findViewById(R.id.comment_text);

        comment_text.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public void setItem(int position, FullConversion fullConversion) {
        List<Conversion> quoteList = fullConversion.getQuoteList();
        Conversion content = fullConversion.getContent();
        Conversion quote;
        boolean repeat;
        int floorCount = fullConversion.getFloorCount();
        int size;
        int i;

        comment_options.setTag(position);
        if (floorCount > 3) {
            comment_quote_expand.setVisibility(View.VISIBLE);
            comment_quote_expand.setTag(position);
            comment_quote_expand.setText(String.format(comment_quote_expand_text, floorCount));
        } else {
            comment_quote_expand.setVisibility(View.GONE);
        }
        size = quoteList.size();
        repeat = false;
        for (i = 0; i < 3; i++) {
            if (i < size && !repeat) {
                quote = quoteList.get(size - i - 1);
                if (fullConversion.getRepeatQuoteId() == quote.getId()) {
                    comment_quote_repeat.setVisibility(View.VISIBLE);
                    mCommentQuotes[i].setVisibility(View.GONE);
                    repeat = true;
                } else {
                    comment_quote_repeat.setVisibility(View.GONE);
                    mCommentQuotes[i].setVisibility(View.VISIBLE);
                    mCommentQuotes[i].setItem(i, quote);
                }
            } else {
                comment_quote_repeat.setVisibility(View.GONE);
                mCommentQuotes[i].setVisibility(View.GONE);
            }
        }
        ImageLoaderHelper.loadImage(comment_userImg, content.getUser().getUserImg());
        comment_username.setText("#" + content.getFloorindex() + " " + content.getUser().getUsername());
        comment_date.setText(content.getDate());
        comment_text.setText(content.getSpanned());
    }

    public void setOnOptionsClickListener(View.OnClickListener clickListener) {
        comment_options.setOnClickListener(clickListener);
    }

    public void setOnQuoteExpandClickListener(View.OnClickListener clickListener) {
        comment_quote_expand.setOnClickListener(clickListener);
    }
}