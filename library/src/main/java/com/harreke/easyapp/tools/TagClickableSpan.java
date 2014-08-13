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
    private String tag;
    private OnTagClickListener tagClickEvent;

    public TagClickableSpan(String tag, OnTagClickListener tagClickEvent) {
        this.tag = tag;
        this.tagClickEvent = tagClickEvent;
    }

    @Override
    public void onClick(View widget) {
        tagClickEvent.onTagClick(tag);
    }
}