package com.harreke.easyappframework.samples.widgets;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.harreke.easyappframework.samples.R;
import com.harreke.easyappframework.widgets.slidableview.LoadOverlayView;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/31
 */
public class LoadOverlay extends LoadOverlayView {
    private Drawable mLoadComplete;
    private String mLoadCompletePageSize;
    private ImageView mLoadOverlayImage;
    private TextView mLoadOverlayText;
    private AnimationDrawable mLoading;

    public LoadOverlay(Context context, AttributeSet attrs) {
        super(context, attrs);

        Resources resources = getResources();

        mLoadComplete = resources.getDrawable(R.drawable.image_load_complete);
        mLoading = (AnimationDrawable) resources.getDrawable(R.drawable.anim_progress_ring);

        mLoadCompletePageSize = resources.getString(R.string.loadoverlay_loadcomplete_pagesize);
    }

    @Override
    public void queryLayout() {
        mLoadOverlayImage = (ImageView) findViewById(R.id.loadoverlay_image);
        mLoadOverlayText = (TextView) findViewById(R.id.loadoverlay_text);
    }

    @Override
    public void setLoadComplete(int pageSize) {
        mLoading.stop();
        mLoadOverlayImage.setImageDrawable(mLoadComplete);
        if (pageSize > -1) {
            mLoadOverlayText.setText(String.format(mLoadCompletePageSize, pageSize));
        } else {
            mLoadOverlayText.setText(R.string.loadoverlay_loadcomplete);
        }
    }

    @Override
    public void setLoadStart() {
        mLoadOverlayImage.setImageDrawable(mLoading);
        mLoading.start();
        mLoadOverlayText.setText(R.string.loadoverlay_loadinging);
    }
}