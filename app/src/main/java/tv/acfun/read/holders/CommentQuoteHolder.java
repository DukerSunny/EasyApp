package tv.acfun.read.holders;

import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.harreke.easyapp.holders.abslistview.IAbsListHolder;

import tv.acfun.read.R;
import tv.acfun.read.beans.Conversion;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/10/05
 */
public class CommentQuoteHolder implements IAbsListHolder<Conversion> {
    private TextView comment_quote_reference;
    private TextView comment_quote_text;
    private TextView comment_quote_username;
    private View mConvertView;

    public CommentQuoteHolder(View convertView, View.OnClickListener onQuoteClickListener) {
        int[] info = new int[2];

        mConvertView = convertView;
        comment_quote_text = (TextView) convertView.findViewById(R.id.comment_quote_text);
        comment_quote_username = (TextView) convertView.findViewById(R.id.comment_quote_username);
        comment_quote_reference = (TextView) convertView.findViewById(R.id.comment_quote_reference);

        comment_quote_text.setMovementMethod(LinkMovementMethod.getInstance());

        mConvertView.setTag(info);
        mConvertView.setOnClickListener(onQuoteClickListener);
    }

    @Override
    public void setItem(int position, Conversion conversion) {
        ((int[]) mConvertView.getTag())[1] = position;
        comment_quote_text.setText(conversion.getSpanned());
        comment_quote_username.setText("#" + conversion.getCount() + " " + conversion.getUserName());
        comment_quote_reference.setText(String.valueOf(conversion.getDeep() + 1));
    }

    public void setParentPosition(int parentPosition) {
        ((int[]) mConvertView.getTag())[0] = parentPosition;
    }

    public void setVisibility(int visibility) {
        mConvertView.setVisibility(visibility);
    }
}