package air.tv.douyu.android.bases.fragments;

import android.os.Bundle;
import android.view.View;

import com.harreke.easyapp.frameworks.bases.fragment.FragmentFramework;
import com.harreke.easyapp.widgets.rippleeffects.RippleDrawable;

import air.tv.douyu.android.R;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/12/18
 */
public class MoreFragment extends FragmentFramework {
    private View more_aboutus;
    private View more_checkin;
    private View more_follows;
    private View more_history;
    private View more_recommends;
    private View more_settings;
    private View more_user_info;

    public static MoreFragment create() {
        return new MoreFragment();
    }

    @Override
    protected void acquireArguments(Bundle bundle) {

    }

    @Override
    public void attachCallbacks() {

    }

    @Override
    public void enquiryViews() {
        more_user_info = findViewById(R.id.more_user_info);
        more_follows = findViewById(R.id.more_follows);
        more_history = findViewById(R.id.more_history);
        more_recommends = findViewById(R.id.more_recommends);
        more_settings = findViewById(R.id.more_settings);
        more_aboutus = findViewById(R.id.more_aboutus);
        more_checkin = findViewById(R.id.more_checkin);

        RippleDrawable.attach(more_user_info);
        RippleDrawable.attach(more_follows);
        RippleDrawable.attach(more_history);
        RippleDrawable.attach(more_recommends);
        RippleDrawable.attach(more_settings);
        RippleDrawable.attach(more_aboutus);
        RippleDrawable.attach(more_checkin, RippleDrawable.RIPPLE_STYLE_LIGHT);
    }

    @Override
    public void establishCallbacks() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_more;
    }

    @Override
    public void startAction() {

    }
}
