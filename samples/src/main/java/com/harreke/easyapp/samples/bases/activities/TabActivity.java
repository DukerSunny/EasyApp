package com.harreke.easyapp.samples.bases.activities;

import android.content.Context;
import android.content.Intent;

import com.harreke.easyapp.beans.ActionBarItem;
import com.harreke.easyapp.frameworks.bases.activity.ActivityFramework;
import com.harreke.easyapp.samples.R;
import com.harreke.easyapp.widgets.ScrollLayout;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/09/16
 */
public class TabActivity extends ActivityFramework {
    public static Intent create(Context context) {
        return new Intent(context, TabActivity.class);
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
    }

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_tab);
    }

    @Override
    public void startAction() {

    }
}
