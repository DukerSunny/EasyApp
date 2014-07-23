package com.harreke.utils.tools;

import android.text.style.ClickableSpan;
import android.view.View;

import com.harreke.utils.listeners.OnTagClickListener;

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