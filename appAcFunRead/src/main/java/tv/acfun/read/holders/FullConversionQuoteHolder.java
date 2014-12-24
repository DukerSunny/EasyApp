package tv.acfun.read.holders;

import android.text.method.LinkMovementMethod;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.harreke.easyapp.frameworks.recyclerview.RecyclerHolder;
import com.harreke.easyapp.widgets.rippleeffects.RippleDrawable;

import tv.acfun.read.R;
import tv.acfun.read.beans.Conversion;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/10/05
 */
public class FullConversionQuoteHolder extends RecyclerHolder<Conversion> {
    private TextView fullconversion_quote_reference;
    private TextView fullconversion_quote_text;
    private TextView fullconversion_quote_username;

    public FullConversionQuoteHolder(View convertView) {
        super(convertView);

        fullconversion_quote_text = (TextView) convertView.findViewById(R.id.fullconversion_quote_text);
        fullconversion_quote_username = (TextView) convertView.findViewById(R.id.fullconversion_quote_username);
        fullconversion_quote_reference = (TextView) convertView.findViewById(R.id.fullconversion_quote_reference);

        fullconversion_quote_text.setMovementMethod(LinkMovementMethod.getInstance());

        RippleDrawable.attach(convertView.findViewById(R.id.fullconversion_quote));
    }

    public void hide() {
        itemView.setVisibility(View.GONE);
    }

    @Override
    public void setItem(Conversion conversion) {
        fullconversion_quote_text.setText(conversion.getSpanned());
        fullconversion_quote_username.setText("#" + conversion.getCount() + " " + conversion.getUserName());
        fullconversion_quote_reference.setText(String.valueOf(conversion.getDeep() + 1));
    }

    public final void setTextSize(int textSize) {
        fullconversion_quote_text.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize);
    }

    public void show() {
        itemView.setVisibility(View.VISIBLE);
    }
}