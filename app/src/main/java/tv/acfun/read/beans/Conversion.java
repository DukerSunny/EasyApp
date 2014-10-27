package tv.acfun.read.beans;

import android.content.Context;
import android.text.Spanned;

import com.google.gson.annotations.Expose;
import com.harreke.easyapp.listeners.OnTagClickListener;

import java.text.SimpleDateFormat;
import java.util.Date;

import tv.acfun.read.R;
import tv.acfun.read.tools.ubb.UBBEncoder;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/09/26
 */
public class Conversion extends Comment {
    private String mDate;
    private int mQuoteCount = 0;
    private int mQuotedCount = 0;
    @Expose(serialize = false, deserialize = false)
    private Spanned mSpanned = null;

    public String getDate() {
        return mDate;
    }

    public int getQuoteCount() {
        return mQuoteCount;
    }

    public int getQuotedCount() {
        return mQuotedCount;
    }

    public Spanned getSpanned() {
        return mSpanned;
    }

    public void newQuote() {
        mQuoteCount++;
    }

    public void newQuoted() {
        mQuotedCount++;
    }

    public void parse(Context context, UBBEncoder encoder, OnTagClickListener tagClickListener) {
        mDate = new SimpleDateFormat(context.getString(R.string.comment_date)).format(new Date(getTime()));
        mSpanned = encoder.encode(getContent(), tagClickListener);
    }
}