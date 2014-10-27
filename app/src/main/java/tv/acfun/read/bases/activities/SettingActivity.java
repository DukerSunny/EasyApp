package tv.acfun.read.bases.activities;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.harreke.easyapp.beans.ActionBarItem;
import com.harreke.easyapp.frameworks.bases.activity.ActivityFramework;

import tv.acfun.read.R;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/09/30
 */
public class SettingActivity extends ActivityFramework {
    private View.OnClickListener mClickListener;
    private View setting_back;

    public static Intent create(Context context) {
        return new Intent(context, SettingActivity.class);
    }

    @Override
    public void assignEvents() {
        setting_back.setOnClickListener(mClickListener);
    }

    @Override
    public void initData(Intent intent) {

    }

    @Override
    public void newEvents() {
        mClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.setting_back:
                        onBackPressed();
                        break;
                }
            }
        };
    }

    @Override
    public void onActionBarItemClick(int position, ActionBarItem item) {
    }

    @Override
    public void onActionBarMenuCreate() {
    }

    @Override
    public void onBackPressed() {
        exit(false);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void queryLayout() {
        setting_back = findViewById(R.id.setting_back);
    }

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_setting);
    }

    @Override
    public void startAction() {
    }
}