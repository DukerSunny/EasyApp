package tv.acfun.read.bases.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.TextView;

import com.harreke.easyapp.frameworks.bases.activity.ActivityFramework;

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
    public void enquiryViews() {
        setActionBarTitle(R.string.setting_about);

        about_version = (TextView) findViewById(R.id.about_version);
    }

    @Override
    public void establishCallbacks() {

    }

    @Override
    public void onBackPressed() {
        exit(false);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void onActionBarItemClick(int id, View item) {

    }

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_about);
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
