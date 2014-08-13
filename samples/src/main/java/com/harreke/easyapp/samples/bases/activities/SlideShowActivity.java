package com.harreke.easyapp.samples.bases.activities;

import android.content.Context;
import android.content.Intent;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.harreke.easyapp.beans.ActionBarItem;
import com.harreke.easyapp.frameworks.bases.activity.ActivityFramework;
import com.harreke.easyapp.samples.R;
import com.harreke.easyapp.widgets.slideshowview.SlideShowView;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/08/07
 */
public class SlideShowActivity extends ActivityFramework {
    private SlideShowView mSlideShow;

    public static Intent create(Context context) {
        return new Intent(context, SlideShowActivity.class);
    }

    @Override
    public void assignEvents() {
    }

    @Override
    public void initData(Intent intent) {
    }

    @Override
    public void newEvents() {
    }

    @Override
    public void onActionBarItemClick(int position, ActionBarItem item) {
    }

    @Override
    public void onActionBarMenuCreate() {
    }

    @Override
    public void queryLayout() {
        mSlideShow = (SlideShowView) findViewById(R.id.slideShow);
    }

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_slideshow);
    }

    @Override
    public void startAction() {
        ImageView imageView;

        imageView = new ImageView(this);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        imageView.setImageResource(R.drawable.anim_progress_radiant);
        mSlideShow.addSlideShow(imageView);

        imageView = new ImageView(this);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        imageView.setImageResource(R.drawable.anim_progress_ring);
        mSlideShow.addSlideShow(imageView);

        mSlideShow.refresh();
    }
}