package com.harreke.easyappframework.samples.widgets;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.harreke.easyappframework.samples.R;
import com.harreke.easyappframework.tools.DevUtil;
import com.harreke.easyappframework.widgets.slidableview.RefreshHeaderView;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/31
 */
public class RefreshHeader extends RefreshHeaderView {
    private Drawable mRefreshComplete;
    private ImageView mRefreshHeaderImage;
    private TextView mRefreshHeaderText;
    private AnimationDrawable mRefreshProgress;
    private AnimationDrawable mRefreshing;

    public RefreshHeader(Context context, AttributeSet attrs) {
        super(context, attrs);

        Resources resources = getResources();

        mRefreshComplete = resources.getDrawable(R.drawable.image_load_complete);
        mRefreshing = (AnimationDrawable) resources.getDrawable(R.drawable.anim_progressloading);
        mRefreshProgress = (AnimationDrawable) resources.getDrawable(R.drawable.anim_refreshprogress);
    }

    @Override
    public void queryLayout() {
        mRefreshHeaderImage = (ImageView) findViewById(R.id.refreshheader_image);
        mRefreshHeaderText = (TextView) findViewById(R.id.refreshheader_text);
    }

    @Override
    public void setRefreshComplete() {
        mRefreshing.stop();
        mRefreshHeaderImage.setImageDrawable(mRefreshComplete);
        mRefreshHeaderText.setText(R.string.refreshheader_refreshcomplete);
    }

    @Override
    public void setRefreshStart() {
        mRefreshHeaderImage.setImageDrawable(mRefreshing);
        mRefreshing.start();
        mRefreshHeaderText.setText(R.string.refreshheader_refreshing);
    }

    @Override
    public void setRefreshTrigger(int progress) {
        if (progress <= 0) {
            mRefreshHeaderImage.setImageDrawable(mRefreshProgress.getFrame(0));
            mRefreshHeaderText.setText(R.string.refreshheader_pulltorefresh);
        } else if (progress > 0 && progress < 25) {
            mRefreshHeaderImage.setImageDrawable(mRefreshProgress.getFrame(1));
            mRefreshHeaderText.setText(R.string.refreshheader_pulltorefresh);
        } else if (progress >= 25 && progress < 50) {
            mRefreshHeaderImage.setImageDrawable(mRefreshProgress.getFrame(2));
            mRefreshHeaderText.setText(R.string.refreshheader_pulltorefresh);
        } else if (progress >= 50 && progress < 75) {
            mRefreshHeaderImage.setImageDrawable(mRefreshProgress.getFrame(3));
            mRefreshHeaderText.setText(R.string.refreshheader_pulltorefresh);
        } else if (progress >= 75 && progress < 100) {
            mRefreshHeaderImage.setImageDrawable(mRefreshProgress.getFrame(4));
            mRefreshHeaderText.setText(R.string.refreshheader_pulltorefresh);
        } else if (progress >= 100) {
            mRefreshHeaderImage.setImageDrawable(mRefreshProgress.getFrame(5));
            mRefreshHeaderText.setText(R.string.refreshheader_releasetorefresh);
        }
    }
}