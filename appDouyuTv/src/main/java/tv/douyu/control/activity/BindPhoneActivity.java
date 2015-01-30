package tv.douyu.control.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;

import com.astuetz.PagerSlidingTabStrip;
import com.harreke.easyapp.frameworks.viewpager.FragmentPageAdapter;
import com.harreke.easyapp.frameworks.base.ActivityFramework;
import com.harreke.easyapp.parsers.ObjectResult;
import com.harreke.easyapp.parsers.Parser;
import com.harreke.easyapp.requests.IRequestCallback;
import com.harreke.easyapp.utils.ResourceUtil;
import com.harreke.easyapp.widgets.transitions.SwipeToFinishLayout;

import tv.douyu.R;
import tv.douyu.misc.api.API;
import tv.douyu.control.application.DouyuTv;
import tv.douyu.model.bean.User;
import tv.douyu.callback.OnBindPhoneListener;
import tv.douyu.control.fragment.BindPhoneDomesticFragment;
import tv.douyu.control.fragment.BindPhoneForeignFragment;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/01/22
 */
public class BindPhoneActivity extends ActivityFramework implements OnBindPhoneListener {
    private ViewPager bind_phone_pager;
    private PagerSlidingTabStrip bind_phone_pager_strip;
    private IRequestCallback<String> mUserCallback;

    public static Intent create(Context context) {
        return new Intent(context, BindPhoneActivity.class);
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
    protected void createMenu() {
        setToolbarTitle(R.string.bind_phone);
        enableDefaultToolbarNavigation();
    }

    @Override
    public void enquiryViews() {
        bind_phone_pager = (ViewPager) findViewById(R.id.bind_phone_pager);
        bind_phone_pager_strip = (PagerSlidingTabStrip) findViewById(R.id.bind_phone_pager_strip);

        bind_phone_pager_strip.setTextColor(Color.WHITE);
        bind_phone_pager_strip.setTextSize((int) ResourceUtil.getDimension(this, R.dimen.Subhead));
    }

    @Override
    public void establishCallbacks() {
        mUserCallback = new IRequestCallback<String>() {
            @Override
            public void onFailure(String requestUrl) {
                onBackPressed();
            }

            @Override
            public void onSuccess(String requestUrl, String result) {
                ObjectResult<User> objectResult = Parser.parseObject(result, User.class, "error", "data", "data");
                User user;

                if (objectResult != null) {
                    user = objectResult.getObject();
                    if (user != null) {
                        DouyuTv.getInstance().updateUser(user);
                    }
                }
                setResult(RESULT_OK);
                onBackPressed();
            }
        };
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_bind_phone;
    }

    @Override
    public void onBindSuccess() {
        executeRequest(API.getUser(), mUserCallback);
    }

    @Override
    public void startAction() {
        bind_phone_pager.setAdapter(new BindPhoneAdapter(getSupportFragmentManager()));
        bind_phone_pager_strip.setViewPager(bind_phone_pager);
    }

    private class BindPhoneAdapter extends FragmentPageAdapter {
        public BindPhoneAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return BindPhoneDomesticFragment.create();
            } else {
                return BindPhoneForeignFragment.create();
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) {
                return getString(R.string.bind_phone_domestic);
            } else {
                return getString(R.string.bind_foreign);
            }
        }
    }
}
