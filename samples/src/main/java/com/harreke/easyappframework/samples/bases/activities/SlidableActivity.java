package com.harreke.easyappframework.samples.bases.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import com.harreke.easyappframework.beans.ActionBarItem;
import com.harreke.easyappframework.frameworks.bases.activity.ActivityFramework;
import com.harreke.easyappframework.listeners.OnSlidableTriggerListener;
import com.harreke.easyappframework.samples.R;
import com.harreke.easyappframework.widgets.slidableview.SlidableView;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/31
 *
 * SlidableView的示例Activity
 */
public class SlidableActivity extends ActivityFramework implements OnSlidableTriggerListener {
    private SlidableView mSlidableView;

    public static Intent create(Context context) {
        return new Intent(context, SlidableActivity.class);
    }

    @Override
    public void assignEvents() {
        mSlidableView.setOnSlidableTriggerListener(this);
    }

    @Override
    public void newEvents() {
    }

    @Override
    public void queryLayout() {
        mSlidableView = (SlidableView) findViewById(R.id.slidingView);
    }

    @Override
    public void setLayout() {
        setContent(R.layout.activity_slidable);
    }

    @Override
    public void startAction() {
    }

    @Override
    public void initData(Intent intent) {
    }

    @Override
    public void onActionBarMenuCreate() {
    }

    @Override
    public void onActionBarItemClick(int position, ActionBarItem item) {
    }

    @Override
    public void onLoadStart() {
        mSlidableView.setLoadStart();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSlidableView.setLoadComplete(-1);
            }
        }, 3000);
    }

    @Override
    public void onRefreshStart() {
        mSlidableView.setRefreshStart();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSlidableView.setRefreshComplete();
            }
        }, 3000);
    }
}