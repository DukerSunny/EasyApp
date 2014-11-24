package com.harreke.easyapp.widgets;

import android.content.Context;
import android.widget.Checkable;
import android.widget.ImageView;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/09/16
 */
public class CheckableImageView extends ImageView implements Checkable {
    private static final int[] CHECKED_STATE_SET = {android.R.attr.state_checked};
    private boolean mChecked;

    public CheckableImageView(Context context) {
        super(context);
    }

    @Override
    public boolean isChecked() {
        return false;
    }

    @Override
    public void setChecked(boolean checked) {

    }

    @Override
    public void toggle() {

    }
}
