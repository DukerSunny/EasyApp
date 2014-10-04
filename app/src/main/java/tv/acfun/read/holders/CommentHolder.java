package tv.acfun.read.holders;

import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.harreke.easyapp.helpers.ImageLoaderHelper;
import com.harreke.easyapp.holders.abslistview.IAbsListHolder;

import tv.acfun.read.R;
import tv.acfun.read.bases.application.AcFunRead;
import tv.acfun.read.beans.ConvertedComment;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/09/26
 */
public class CommentHolder implements IAbsListHolder<ConvertedComment> {
    private View comment;
    private View comment_quote;
    private TextView comment_quote_text;
    private TextView comment_quote_user_name;
    private TextView comment_repeat;
    private TextView comment_text;
    private ImageView comment_user_avatar;
    private TextView comment_user_name;

    public CommentHolder(View convertView) {
        comment_quote = convertView.findViewById(R.id.comment_quote);
        comment_quote_text = (TextView) convertView.findViewById(R.id.comment_quote_text);
        comment_quote_user_name = (TextView) convertView.findViewById(R.id.comment_quote_user_name);

        comment = convertView.findViewById(R.id.comment);
        comment_repeat = (TextView) convertView.findViewById(R.id.comment_repeat);
        comment_user_avatar = (ImageView) convertView.findViewById(R.id.comment_user_avatar);
        comment_user_name = (TextView) convertView.findViewById(R.id.comment_user_name);
        comment_text = (TextView) convertView.findViewById(R.id.comment_text);
    }

    @Override
    public void setItem(int position, ConvertedComment convertedComment) {
        Html.ImageGetter imageGetter = ((AcFunRead) AcFunRead.getInstance()).emotGetter;

        if (convertedComment.isInQuoteMode()) {
            if (!convertedComment.hasRepeatQuote()) {
                comment_quote.setVisibility(View.VISIBLE);
                comment.setVisibility(View.GONE);
                comment_quote_user_name.setText("# " + convertedComment.getFloorindex() + " " + convertedComment.getUsername());
                comment_quote_text.setText(Html.fromHtml(convertedComment.getText(), imageGetter, null));
            }
        } else {
            comment_quote.setVisibility(View.GONE);
            comment.setVisibility(View.VISIBLE);
            if (!convertedComment.hasRepeatQuote()) {
                comment_repeat.setVisibility(View.GONE);
            } else {
                comment_repeat.setVisibility(View.VISIBLE);
            }
            ImageLoaderHelper.loadImage(comment_user_avatar, convertedComment.getUserImg());
            comment_user_name.setText("# " + convertedComment.getFloorindex() + " " + convertedComment.getUsername());
            comment_text.setText(Html.fromHtml(convertedComment.getText(), imageGetter, null));
        }
    }
}