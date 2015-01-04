package com.harreke.easyapp.widgets;

import android.text.style.ClickableSpan;
import android.view.View;

import com.harreke.easyapp.listeners.OnTagClickListener;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 *
 * 带有标签的可点击Span
 */
public class TagClickableSpan extends ClickableSpan {
    private String mLink;
    private String mTag;
    private OnTagClickListener mTagClickListener;

    public TagClickableSpan(String tag, String link, OnTagClickListener tagClickListener) {
        mTag = tag;
        mLink = link;
        mTagClickListener = tagClickListener;
    }

    public String getLink() {
        return mLink;
    }

    public String getTag() {
        return mTag;
    }

    @Override
    public void onClick(View widget) {
        mTagClickListener.onTagClick(mTag, mLink);
    }
}