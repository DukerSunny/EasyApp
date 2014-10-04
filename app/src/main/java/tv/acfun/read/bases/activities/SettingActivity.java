package tv.acfun.read.bases.activities;

import android.content.Context;
import android.content.Intent;

import com.harreke.easyapp.beans.ActionBarItem;
import com.harreke.easyapp.frameworks.bases.activity.ActivityFramework;

import tv.acfun.read.R;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/09/30
 */
public class SettingActivity extends ActivityFramework {
    public static Intent create(Context context) {
        return new Intent(context, SettingActivity.class);
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
        setContentView(R.layout.activity_setting);
    }

    @Override
    public void startAction() {

    }
}
