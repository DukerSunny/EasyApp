package com.harreke.easyappframework.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.harreke.easyappframework.R;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 *
 * 文本设置视图
 *
 * 最基本的设置视图，没有任何其他功能，只用来显示文字
 */
public class TextSettingView extends LinearLayout {
    private TextView text_summary;

    public TextSettingView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.textSettingViewStyle);
    }

    public TextSettingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray style;
        TextView textView;
        String summary;
        int summaryColor;
        int summarySize;
        String text;
        int textColor;
        int textSize;

        style = context.obtainStyledAttributes(attrs, R.styleable.TextSettingView, defStyle, 0);
        summary = style.getString(R.styleable.TextSettingView_summary);
        summaryColor = style.getColor(R.styleable.TextSettingView_summaryColor, 0);
        summarySize = (int) style.getDimension(R.styleable.TextSettingView_summarySize, 0);
        text = style.getString(R.styleable.TextSettingView_text);
        textColor = style.getColor(R.styleable.TextSettingView_textColor, 0);
        textSize = (int) style.getDimension(R.styleable.TextSettingView_textSize, 0);
        style.recycle();

        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER_VERTICAL);

        textView = new TextView(context);
        textView.setGravity(Gravity.CENTER_VERTICAL);
        textView.setLayoutParams(new LayoutParams(-1, -2));
        textView.setText(text);
        textView.setTextColor(textColor);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        addView(textView);

        text_summary = new TextView(context);
        text_summary.setLayoutParams(new LayoutParams(-1, -2));
        setSummary(summary);
        text_summary.setTextColor(summaryColor);
        text_summary.setTextSize(TypedValue.COMPLEX_UNIT_PX, summarySize);
        addView(text_summary);
    }

    /**
     * 设置描述
     *
     * @param summary
     *         描述
     */
    public final void setSummary(String summary) {
        if (summary != null) {
            text_summary.setVisibility(VISIBLE);
            text_summary.setText(summary);
        } else {
            text_summary.setVisibility(GONE);
        }
    }
}