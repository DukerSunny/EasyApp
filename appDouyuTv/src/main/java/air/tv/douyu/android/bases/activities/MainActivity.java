package air.tv.douyu.android.bases.activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;

import com.astuetz.PagerSlidingTabStrip;
import com.harreke.easyapp.adapters.fragment.FragmentPageAdapter;
import com.harreke.easyapp.frameworks.bases.activity.ActivityFramework;

import air.tv.douyu.android.R;
import air.tv.douyu.android.bases.application.DouyuTv;
import air.tv.douyu.android.bases.fragments.GameFragment;
import air.tv.douyu.android.bases.fragments.LiveFragment;
import air.tv.douyu.android.bases.fragments.MoreFragment;
import air.tv.douyu.android.bases.fragments.RecommendFragment;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/12/18
 */
public class MainActivity extends ActivityFramework {
    private long mExitTime = 0l;
    private ViewPager main_pager;
    private PagerSlidingTabStrip main_pager_strip;

    @Override
    protected void acquireArguments(Intent intent) {
        DouyuTv.getInstance().registerConnectionReceiver();
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

        main_pager_strip.setTextColor(Color.WHITE);
        main_pager_strip.setTextSize((int) getResources().getDimension(R.dimen.Subhead));
    }

    @Override
    public void establishCallbacks() {
    }

    @Override
    public void onBackPressed() {
        long exitTime;

        exitTime = System.currentTimeMillis();
        if (exitTime - mExitTime <= 3000) {
            exit(R.anim.none, R.anim.none);
        } else {
            mExitTime = exitTime;
            showToast(getString(R.string.app_exit));
        }
    }

    @Override
    protected void onDestroy() {
        DouyuTv.getInstance().unregisterConnectionReceiver();
        super.onDestroy();
    }

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_main);
    }

    @Override
    public void startAction() {
        main_pager.setAdapter(new MainPagerAdapter(getSupportFragmentManager()));
        main_pager_strip.setViewPager(main_pager);
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