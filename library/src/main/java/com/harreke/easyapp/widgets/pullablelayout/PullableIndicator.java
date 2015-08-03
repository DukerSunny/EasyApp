package com.harreke.easyapp.widgets.pullablelayout;

import android.content.Context;

import com.harreke.easyapp.R;
import com.harreke.easyapp.utils.ResourceUtil;
import com.harreke.easyapp.widgets.circluarprogresses.CircularProgressDrawable;
import com.melnykov.fab.FloatingActionButton;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/12/12
 */
public class PullableIndicator extends FloatingActionButton {
    private CircularProgressDrawable mDrawable;

    public PullableIndicator(Context context) {
        super(context);

        mDrawable = new CircularProgressDrawable(ResourceUtil.obtainThemeColor(context)[0]);
        setImageDrawable(mDrawable);
        setColorNormalResId(R.color.white);
        setClickable(false);
    }

    public void setProgress(float progress) {
        mDrawable.setProgress(progress);
    }
}