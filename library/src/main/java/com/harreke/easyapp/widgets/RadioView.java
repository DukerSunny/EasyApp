package com.harreke.easyapp.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 *
 * 重新封装的RadioGroup
 *
 * 支持直接以序号，而不是以Id处理其中的RadioButton
 */
public class RadioView extends RadioGroup {
    public RadioView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void checkChild(int position) {
        RadioButton child;

        if (position >= 0 && position < getChildCount()) {
            child = (RadioButton) getChildAt(position);
            check(child.getId());
        }
    }

    public RadioButton getCheckedChild() {
        int childId = getCheckedRadioButtonId();

        if (childId > 0) {
            return (RadioButton) findViewById(childId);
        } else {
            return null;
        }
    }

    public int getCheckedChildPostiion() {
        View child;
        int childId = getCheckedRadioButtonId();
        int i;

        if (childId > 0) {
            for (i = 0; i < getChildCount(); i++) {
                child = getChildAt(i);
                if (child.getId() == childId) {
                    return i;
                }
            }
        }

        return -1;
    }
}