package com.harreke.easyapp.tools;

import android.text.style.ClickableSpan;
import android.view.View;

import com.harreke.easyapp.listeners.OnTagClickListener;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 *
 * 带有标签的可点击Span
 */
public class TagClickableSpan extends ClickableSpan {
    private String mTag;
    private OnTagClickListener mTagClickListener;

    public TagClickableSpan(String tag, OnTagClickListener tagClickListener) {
        this.mTag = tag;
        this.mTagClickListener = tagClickListener;
    }

    @Override
    public void onClick(View widget) {
        mTagClickListener.onTagClick(mTag);
    }
}