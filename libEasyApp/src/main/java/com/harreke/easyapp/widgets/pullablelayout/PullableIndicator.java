package com.harreke.easyapp.widgets.pullablelayout;

import android.content.Context;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.harreke.easyapp.R;
import com.harreke.easyapp.frameworks.application.ApplicationFramework;
import com.harreke.easyapp.widgets.circluarprogresses.CircularProgressView;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/12/12
 */
public class PullableIndicator extends LinearLayout {
    private CircularProgressView mProgressView;
    private TextView mToastView;

    public PullableIndicator(Context context) {
        super(context);
        LayoutParams params;
        int padding = (int) (ApplicationFramework.Density * 8);
        int margin = (int) (ApplicationFramework.Density * 8);

        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);
        setPadding(padding, padding, padding, padding);

        mProgressView = new CircularProgressView(context);
        mProgressView.setLayoutParams(
                new LayoutParams((int) (ApplicationFramework.Density * 32), (int) (ApplicationFramework.Density * 32)));
        addView(mProgressView);

        params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.leftMargin = margin;
        mToastView = new TextView(context);
        mToastView.setLayoutParams(params);
        mToastView.setTextAppearance(context, R.style.Subhead);
        addView(mToastView);
    }

    public void setProgress(float progress) {
        mProgressView.setProgress(progress);
    }

    public void setToast(String toast) {
        mToastView.setText(toast);
    }

    public void setToast(int toastId) {
        mToastView.setText(toastId);
    }
}