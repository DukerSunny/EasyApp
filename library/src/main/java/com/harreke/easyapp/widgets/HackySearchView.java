package com.harreke.easyapp.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v7.widget.SearchView;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;

import com.harreke.easyapp.R;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/12/03
 */
public class HackySearchView extends SearchView {
    public HackySearchView(Context context) {
        this(context, null);
    }

    public HackySearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray style = context.obtainStyledAttributes(new int[]{android.R.attr.textColor});
        ((AutoCompleteTextView) findViewById(R.id.search_src_text)).setTextColor(style.getColor(0, Color.WHITE));
        style.recycle();
    }

    public HackySearchView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.searchViewStyle);
    }
}