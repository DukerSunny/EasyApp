package tv.douyu.control.activity;

import android.content.Intent;

import com.harreke.easyapp.enums.ActivityAnimation;
import com.harreke.easyapp.frameworks.activity.ActivityFramework;

import tv.douyu.R;

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
    protected void createMenu() {

    }

    @Override
    public void enquiryViews() {

    }

    @Override
    public void establishCallbacks() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public void startAction() {
        //        start(PlayerActivity.create(this, 47227));
        start(MainActivity.create(this));
        exit(ActivityAnimation.None);
    }
}