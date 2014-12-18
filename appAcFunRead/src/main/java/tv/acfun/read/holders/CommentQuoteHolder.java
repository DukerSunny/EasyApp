package tv.acfun.read.holders;

import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.harreke.easyapp.holders.recycerview.RecyclerHolder;
import com.harreke.easyapp.widgets.RippleDrawable;

import tv.acfun.read.R;
import tv.acfun.read.beans.Conversion;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/10/05
 */
public class CommentQuoteHolder extends RecyclerHolder<Conversion> {
    private TextView comment_quote_reference;
    private TextView comment_quote_text;
    private TextView comment_quote_username;

    public CommentQuoteHolder(View convertView) {
        super(convertView);

        comment_quote_text = (TextView) convertView.findViewById(R.id.comment_quote_text);
        comment_quote_username = (TextView) convertView.findViewById(R.id.comment_quote_username);
        comment_quote_reference = (TextView) convertView.findViewById(R.id.comment_quote_reference);

        RippleDrawable.attach(convertView.findViewById(R.id.comment_quote));
    }

    public void hide() {
        itemView.setVisibility(View.GONE);
    }

    @Override
    public void setItem(Conversion conversion) {
        comment_quote_text.setText(conversion.getSpanned());
        comment_quote_username.setText("#" + conversion.getCount() + " " + conversion.getUserName());
        comment_quote_reference.setText(String.valueOf(conversion.getDeep() + 1));
    }

    public final void setTextSize(int textSize) {
        comment_quote_text.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize);
    }

    public void show() {
        itemView.setVisibility(View.VISIBLE);
    }
}