package tv.acfun.read.holders;

import android.content.Context;
import android.content.res.Resources;
import android.text.method.LinkMovementMethod;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.harreke.easyapp.frameworks.recyclerview.RecyclerHolder;
import com.harreke.easyapp.helpers.ImageLoaderHelper;
import com.harreke.easyapp.widgets.rippleeffects.RippleDrawable;
import com.harreke.easyapp.widgets.rippleeffects.RippleOnClickListener;

import java.util.List;

import tv.acfun.read.R;
import tv.acfun.read.beans.Conversion;
import tv.acfun.read.beans.FullConversion;
import tv.acfun.read.helpers.ImageConnectionHelper;
import tv.acfun.read.helpers.OfflineImageLoaderHelper;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/10/05
 */
public class FullConversionHolder extends RecyclerHolder<FullConversion> {
    private TextView fullconversion_date;
    private String fullconversion_date_text;
    private View fullconversion_quote_expand;
    private String fullconversion_quote_expand_string;
    private TextView fullconversion_quote_expand_text;
    private TextView fullconversion_text;
    private ImageView fullconversion_userImg;
    private TextView fullconversion_username;
    private FullConversionQuoteHolder[] mFullConversionQuotes;
    private int mMaxQuoteCount;

    public FullConversionHolder(View itemView, int textSize, int maxQuoteCount, View.OnClickListener onAvatarClickListener,
            View.OnClickListener onQuoteExpandClickListener, View.OnClickListener onQuoteClickListener) {
        super(itemView);
        Context context = itemView.getContext();
        Resources resources = context.getResources();
        LinearLayout fullconversion_quotes;
        LayoutInflater inflater;
        View quoteView;
        int i;

        fullconversion_quote_expand = itemView.findViewById(R.id.fullconversion_quote_expand);
        fullconversion_quote_expand_text = (TextView) itemView.findViewById(R.id.fullconversion_quote_expand_text);
        fullconversion_userImg = (ImageView) itemView.findViewById(R.id.fullconversion_userImg);
        fullconversion_username = (TextView) itemView.findViewById(R.id.fullconversion_username);
        fullconversion_date = (TextView) itemView.findViewById(R.id.fullconversion_date);
        fullconversion_text = (TextView) itemView.findViewById(R.id.fullconversion_text);

        fullconversion_quote_expand_string = resources.getString(R.string.comment_quote_expand);
        fullconversion_date_text = resources.getString(R.string.comment_date);

        fullconversion_userImg.setOnClickListener(onAvatarClickListener);

        mMaxQuoteCount = maxQuoteCount;
        mFullConversionQuotes = new FullConversionQuoteHolder[mMaxQuoteCount];
        fullconversion_quotes = (LinearLayout) itemView.findViewById(R.id.fullconversion_quotes);
        inflater = LayoutInflater.from(context);
        for (i = 0; i < mMaxQuoteCount; i++) {
            quoteView = inflater.inflate(R.layout.item_fullconversion_quote, fullconversion_quotes, false);
            fullconversion_quotes.addView(quoteView);
            mFullConversionQuotes[i] = new FullConversionQuoteHolder(quoteView);
            mFullConversionQuotes[i].setTextSize(textSize);
            mFullConversionQuotes[i].setOnClickListener(onQuoteClickListener);
        }

        fullconversion_text.setMovementMethod(LinkMovementMethod.getInstance());
        fullconversion_quote_expand.setOnClickListener(onQuoteExpandClickListener);

        RippleDrawable.attach(fullconversion_quote_expand);
        RippleDrawable.attach(itemView);
    }

    @Override
    public void setItem(FullConversion fullConversion) {
        List<Conversion> quoteList = fullConversion.getQuoteList();
        Conversion conversion = fullConversion.getConversion();
        int position = getPosition();
        int floorCount = fullConversion.getFloorCount();
        int size;
        int i;

        if (ImageConnectionHelper.shouldLoadImage()) {
            ImageLoaderHelper.loadImage(fullconversion_userImg, conversion.getUserImg(), R.drawable.image_loading,
                    R.drawable.image_idle);
        } else {
            OfflineImageLoaderHelper.loadImage(fullconversion_userImg, OfflineImageLoaderHelper.OfflineImage.Avatar);
        }
        fullconversion_userImg.setTag(conversion.getUserID());
        fullconversion_username.setText("#" + conversion.getCount() + " " + conversion.getUserName());
        fullconversion_date.setText(String.format(fullconversion_date_text, conversion.getPostDate()));
        fullconversion_text.setText(conversion.getSpanned());

        if (floorCount > mMaxQuoteCount) {
            fullconversion_quote_expand.setVisibility(View.VISIBLE);
            fullconversion_quote_expand.setTag(position);
            fullconversion_quote_expand_text.setText(String.format(fullconversion_quote_expand_string, floorCount));
        } else {
            fullconversion_quote_expand.setVisibility(View.GONE);
        }
        size = quoteList.size();
        for (i = 0; i < size; i++) {
            mFullConversionQuotes[i].show();
            mFullConversionQuotes[i].setItem(quoteList.get(i));
            mFullConversionQuotes[i].itemView.setTag(R.id.comment_floor_position, getPosition());
            mFullConversionQuotes[i].itemView.setTag(R.id.comment_quote_position, i);
        }
        for (i = size; i < mMaxQuoteCount; i++) {
            mFullConversionQuotes[i].hide();
        }
    }

    @Override
    public void setOnClickListener(View.OnClickListener onClickListener) {
        RippleOnClickListener.attach(itemView, onClickListener);
    }

    public final void setTextSize(int textSize) {
        fullconversion_text.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize);
    }
}