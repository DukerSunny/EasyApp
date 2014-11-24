package com.harreke.easyapp.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CompoundButton;

import com.harreke.easyapp.R;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/09/16
 *
 * 子标签视图
 *
 * 子标签视图是按钮，可以点击切换开/关状态，默认处于多选模式
 *
 * 将多个子标签视图放入同一个标签组视图{@link com.harreke.easyapp.widgets.GroupTabView}，将使这些子标签视图成为互斥状态，即单选模式
 */
public class ChildTabView extends CompoundButton {
    public ChildTabView(Context context) {
        this(context, null);
    }

    public ChildTabView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.childTabViewStyle);
    }

    public ChildTabView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
}