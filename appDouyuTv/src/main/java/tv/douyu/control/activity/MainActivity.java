package tv.douyu.control.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.harreke.easyapp.adapters.fragment.FragmentPageAdapter;
import com.harreke.easyapp.frameworks.activity.ActivityFramework;
import com.harreke.easyapp.requests.IRequestCallback;
import com.harreke.easyapp.utils.JsonUtil;
import com.harreke.easyapp.utils.ResourceUtil;

import tv.douyu.R;
import tv.douyu.control.application.DouyuTv;
import tv.douyu.control.fragment.GameFragment;
import tv.douyu.control.fragment.LiveFragment;
import tv.douyu.control.fragment.MoreFragment;
import tv.douyu.control.fragment.RecommendFragment;
import tv.douyu.misc.api.API;
import tv.douyu.model.bean.Update;
import tv.douyu.wrapper.helper.UpdateHelper;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/12/18
 */
public class MainActivity extends ActivityFramework {
    private long mExitTime = 0l;
    private IRequestCallback<String> mUpdateCallback;
    private UpdateHelper mUpdateHelper;
    private ViewPager main_pager;
    private PagerSlidingTabStrip main_pager_strip;
    private String mVersion;

    public static Intent create(Context context) {
        return new Intent(context, MainActivity.class);
    }

    @Override
    protected void acquireArguments(Intent intent) {
    }

    @Override
    public void attachCallbacks() {
    }

    @Override
    protected void createMenu() {
        addToolbarItem(0, R.string.app_search, R.drawable.image_toolbar_search);
    }

    @Override
    public void enquiryViews() {
        main_pager = (ViewPager) findViewById(R.id.main_pager);
        main_pager_strip = (PagerSlidingTabStrip) findViewById(R.id.main_pager_strip);

        mUpdateHelper = new UpdateHelper(getFramework());

        main_pager_strip.setTextColor(Color.WHITE);
        main_pager_strip.setTextSize((int) ResourceUtil.getDimension(this, R.dimen.Subhead));
    }

    @Override
    public void establishCallbacks() {
        mUpdateCallback = new IRequestCallback<String>() {
            @Override
            public void onFailure(String requestUrl) {
            }

            @Override
            public void onSuccess(String requestUrl, String result) {
                Update update = JsonUtil.toObject(result, Update.class);

                if (update != null && !TextUtils.isEmpty(mVersion) && !TextUtils.isEmpty(update.getVersion()) &&
                        !mVersion.equals(update.getVersion())) {
                    mUpdateHelper.show(update);
                }
            }
        };
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void onBackPressed() {
        long exitTime;

        exitTime = System.currentTimeMillis();
        if (exitTime - mExitTime <= 3000) {
            exit();
        } else {
            mExitTime = exitTime;
            showToast(getString(R.string.app_exit));
        }
    }

    @Override
    protected void onDestroy() {
        mUpdateHelper.destroy();
        super.onDestroy();
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        start(SearchActivity.create(this));

        return false;
    }

    @Override
    public void startAction() {
        main_pager.setAdapter(new MainPagerAdapter(getSupportFragmentManager()));
        main_pager_strip.setViewPager(main_pager);
        executeRequest(API.getCheckUpdate(), mUpdateCallback);
    }

    private class MainPagerAdapter extends FragmentPageAdapter {
        public MainPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return RecommendFragment.create();
                case 1:
                    return GameFragment.create();
                case 2:
                    return LiveFragment.create();
                case 3:
                    return MoreFragment.create();
                default:
                    return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.app_recommend);
                case 1:
                    return getString(R.string.app_game);
                case 2:
                    return getString(R.string.app_live);
                case 3:
                    return getString(R.string.app_more);
                default:
                    return "";
            }
        }
    }
}