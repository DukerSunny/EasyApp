package tv.acfun.read.holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.harreke.easyapp.holders.abslistview.IAbsListHolder;

import tv.acfun.read.R;
import tv.acfun.read.beans.Conversion;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/09/26
 */
public class CommentHolder implements IAbsListHolder<Conversion> {
    private View comment;
    private View comment_quote;
    private TextView comment_quote_text;
    private TextView comment_quote_user_name;
    private TextView comment_repeat;
    private TextView comment_text;
    private ImageView comment_user_avatar;
    private TextView comment_user_name;

    public CommentHolder(View convertView) {
//        comment_quote = convertView.findViewById(R.id.comment_quote);
        //        comment_quote_text = (TextView) convertView.findViewById(R.id.comment_quote_text);
        //        comment_quote_user_name = (TextView) convertView.findViewById(R.id.comment_quote_username);
        //
        //        comment = convertView.findViewById(R.id.comment);
        //        comment_repeat = (TextView) convertView.findViewById(R.id.comment_repeat);
        //        comment_user_avatar = (ImageView) convertView.findViewById(R.id.comment_userImg);
        //        comment_user_name = (TextView) convertView.findViewById(R.id.comment_username);
        //        comment_text = (TextView) convertView.findViewById(R.id.comment_text);
    }

    @Override
    public void setItem(int position, Conversion conversion) {
        //        Html.ImageGetter imageGetter = ((AcFunRead) AcFunRead.getInstance()).emotGetter;
        //
        //        if (conversion.isInQuoteMode()) {
        //            if (!conversion.hasRepeatQuote()) {
        //                comment_quote.setVisibility(View.VISIBLE);
        //                comment.setVisibility(View.GONE);
        //                comment_quote_user_name.setText("# " + conversion.getFloorindex() + " " + conversion.getUsername());
        //                comment_quote_text.setText(Html.fromHtml(conversion.getText(), imageGetter, null));
        //            }
        //        } else {
        //            comment_quote.setVisibility(View.GONE);
        //            comment.setVisibility(View.VISIBLE);
        //            if (!conversion.hasRepeatQuote()) {
        //                comment_repeat.setVisibility(View.GONE);
        //            } else {
        //                comment_repeat.setVisibility(View.VISIBLE);
        //            }
        //            ImageLoaderHelper.loadImage(comment_user_avatar, conversion.getUserImg());
        //            comment_user_name.setText("# " + conversion.getFloorindex() + " " + conversion.getUsername());
        //            comment_text.setText(Html.fromHtml(conversion.getText(), imageGetter, null));
        //        }
    }
}