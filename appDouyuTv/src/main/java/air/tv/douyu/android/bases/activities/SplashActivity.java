package air.tv.douyu.android.bases.activities;

import android.content.Intent;

import com.harreke.easyapp.frameworks.bases.activity.ActivityFramework;

import air.tv.douyu.android.R;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/01/15
 */
public class SplashActivity extends ActivityFramework {
    @Override
    protected void acquireArguments(Intent intent) {

    }

    @Override
    public void attachCallbacks() {

    }

    @Override
    public void enquiryViews() {

    }

    @Override
    public void establishCallbacks() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_player;
    }

    @Override
    public void startAction() {
        start(PlayerActivity.create(this, 0));
    }
}
