package com.harreke.easyappframework.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.harreke.easyappframework.R;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/29
 */
public class RefreshableSlidingView extends LinearLayout {
    private FrameLayout mContent;
    private float mDensity;
    private LinearLayout mTabMain;
    private LinearLayout mTabSub;
    private FrameLayout mTabHeader;

    public RefreshableSlidingView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.refreshableSlidingViewStyle);
    }

    public RefreshableSlidingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray style;
        int tabBackgroundId;
        int tabHeaderLayoutId;
        int tabMainLayoutId;
        int tabSubLayoutId;
        int contentBackgroundId;
        int contentLayoutId;
        int contentMargin;
        LayoutInflater inflater;
        LinearLayout tab;
        LayoutParams params;

        style = context.obtainStyledAttributes(attrs, R.styleable.RefreshableSlidingView, defStyle, 0);
        tabBackgroundId = style.getResourceId(R.styleable.RefreshableSlidingView_tabBackground, 0);
        tabHeaderLayoutId = style.getResourceId(R.styleable.RefreshableSlidingView_tabHeaderLayout, 0);
        tabMainLayoutId = style.getResourceId(R.styleable.RefreshableSlidingView_tabMainLayout, 0);
        tabSubLayoutId = style.getResourceId(R.styleable.RefreshableSlidingView_tabSubLayout, 0);
        contentBackgroundId = style.getResourceId(R.styleable.RefreshableSlidingView_contentBackground, 0);
        contentLayoutId = style.getResourceId(R.styleable.RefreshableSlidingView_contentLayout, 0);
        contentMargin = (int) style.getDimension(R.styleable.RefreshableSlidingView_contentMargin, 0);
        style.recycle();

        setOrientation(VERTICAL);

        inflater = LayoutInflater.from(context);
        mDensity = context.getResources().getDisplayMetrics().density;

        tab = new LinearLayout(context);
        tab.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tab.setOrientation(VERTICAL);
        if (tabBackgroundId > 0) {
            tab.setBackgroundResource(tabBackgroundId);
        }

        mTabHeader = new FrameLayout(context);
        mTabHeader.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        if (tabHeaderLayoutId > 0) {
            inflater.inflate(tabHeaderLayoutId, mTabHeader, true);
        }
        tab.addView(mTabHeader);

        mTabSub = new LinearLayout(context);
        mTabSub.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mTabSub.setOrientation(VERTICAL);
        if (tabMainLayoutId > 0) {
            inflater.inflate(tabSubLayoutId, mTabSub, true);
        }
        tab.addView(mTabSub);

        mTabMain = new LinearLayout(context);
        mTabMain.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mTabMain.setOrientation(VERTICAL);
        if (tabMainLayoutId > 0) {
            inflater.inflate(tabMainLayoutId, mTabMain, true);
        }
        tab.addView(mTabMain);

        addView(tab);

        params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.setMargins(0, contentMargin, 0, 0);
        mContent = new FrameLayout(context);
        mContent.setLayoutParams(params);
        if (contentBackgroundId > 0) {
            mContent.setBackgroundResource(contentBackgroundId);
        }
        if (contentLayoutId > 0) {
            inflater.inflate(contentLayoutId, mContent, true);
        }
        addView(mContent);
    }
}