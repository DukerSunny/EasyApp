package tv.acfun.read.bases.activities;

import android.content.Context;
import android.content.Intent;
import android.view.View;

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
    public void acquireArguments(Intent intent) {

    }

    @Override
    public void attachCallbacks() {
    }

    @Override
    public void enquiryViews() {
        setActionBarTitle(R.string.menu_setting);
    }

    @Override
    public void establishCallbacks() {
    }

    @Override
    public void onActionBarItemClick(int id, View item) {

    }

    @Override
    public void onBackPressed() {
        exit(false);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_setting);
    }

    @Override
    public void startAction() {
    }
}