package tv.acfun.read.beans;

import android.text.Spanned;

import com.harreke.easyapp.listeners.OnTagClickListener;

import tv.acfun.read.tools.ubb.UBBEncoder;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/09/26
 */
public class Conversion extends Comment {
    private int mQuoteCount = 0;
    private int mQuotedCount = 0;
    private transient Spanned mSpanned = null;

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

    public void parse(UBBEncoder encoder, OnTagClickListener tagClickListener) {
        mSpanned = encoder.encode(getContent(), tagClickListener);
    }
}