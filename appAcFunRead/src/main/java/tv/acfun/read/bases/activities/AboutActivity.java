package tv.acfun.read.bases.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.widget.TextView;

import com.harreke.easyapp.frameworks.bases.activity.ActivityFramework;
import com.umeng.analytics.MobclickAgent;

import tv.acfun.read.BuildConfig;
import tv.acfun.read.R;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/11/22
 */
public class AboutActivity extends ActivityFramework {
    private TextView about_version;

    public static Intent create(Context context) {
        return new Intent(context, AboutActivity.class);
    }

    @Override
    public void acquireArguments(Intent intent) {

    }

    @Override
    public void attachCallbacks() {
    }

    @Override
    public void createMenu() {
        setToolbarTitle(R.string.setting_about);
        setToolbarNavigation();
    }

    @Override
    public void enquiryViews() {
        about_version = (TextView) findViewById(R.id.about_version);
    }

    @Override
    public void establishCallbacks() {
    }

    @Override
    public void onBackPressed() {
        exit(Transition.Exit_Left);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!BuildConfig.DEBUG) {
            MobclickAgent.onPause(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!BuildConfig.DEBUG) {
            MobclickAgent.onResume(this);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_about;
    }

    @Override
    public void startAction() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            String version = info.versionName;
            about_version.setText(getString(R.string.app_name) + " v" + version);
        } catch (Exception ignored) {
        }
    }
}
