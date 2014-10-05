package tv.acfun.read.holders;

import android.view.View;
import android.widget.TextView;

import com.harreke.easyapp.holders.abslistview.IExListHolder;

import tv.acfun.read.R;
import tv.acfun.read.beans.Conversion;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/09/26
 */
public class CommentChildHolder implements IExListHolder.Child<Conversion> {
    private TextView comment_quote_text;
    private TextView comment_quote_user_name;

    public CommentChildHolder(View convertView) {
        comment_quote_text = (TextView) convertView.findViewById(R.id.comment_quote_text);
        comment_quote_user_name = (TextView) convertView.findViewById(R.id.comment_quote_username);
    }

    @Override
    public void setItem(int groupPosition, int childPosition, Conversion conversion) {
        comment_quote_user_name.setText("# " + conversion.getFloorindex() + " " + conversion.getUser().getUsername());
        comment_quote_text.setText(conversion.getSpanned());
    }
}