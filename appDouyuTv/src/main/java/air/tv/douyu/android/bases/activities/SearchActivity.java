package air.tv.douyu.android.bases.activities;

import android.content.Context;
import android.content.Intent;

import com.harreke.easyapp.frameworks.bases.activity.ActivityFramework;
import com.harreke.easyapp.widgets.transitions.SwipeToFinishLayout;

import air.tv.douyu.android.R;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/12/29
 */
public class SearchActivity extends ActivityFramework {
    public static Intent create(Context context) {
        return new Intent(context, SearchActivity.class);
    }

    @Override
    protected void acquireArguments(Intent intent) {

    }

    @Override
    public void attachCallbacks() {

    }

    @Override
    protected void configActivity() {
        attachTransition(new SwipeToFinishLayout(this));
    }

    @Override
    public void enquiryViews() {

    }

    @Override
    public void establishCallbacks() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    public void startAction() {

    }
}
