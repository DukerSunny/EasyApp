package air.tv.douyu.android.bases.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;

import com.astuetz.PagerSlidingTabStrip;
import com.harreke.easyapp.adapters.fragment.FragmentPageAdapter;
import com.harreke.easyapp.frameworks.bases.activity.ActivityFramework;
import com.harreke.easyapp.utils.ResourceUtil;
import com.harreke.easyapp.widgets.transitions.SwipeToFinishLayout;

import air.tv.douyu.android.R;
import air.tv.douyu.android.bases.fragments.SearchFragment;
import air.tv.douyu.android.listeners.OnSearchResultListener;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/12/29
 */
public class SearchResultActivity extends ActivityFramework implements OnSearchResultListener {
    private String mQuery = null;
    private String[] mTitles = new String[2];
    private ViewPager search_result_pager;
    private PagerSlidingTabStrip search_result_pager_strip;

    public static Intent create(Context context, String query) {
        Intent intent = new Intent(context, SearchResultActivity.class);

        intent.putExtra("query", query);

        return intent;
    }

    @Override
    protected void acquireArguments(Intent intent) {
        mQuery = intent.getStringExtra("query");

        mTitles[0] = getString(R.string.search_status_online);
        mTitles[1] = getString(R.string.search_status_offline);
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
        setToolbarTitle(R.string.search_result);
        enableDefaultToolbarNavigation();
    }

    @Override
    public void enquiryViews() {
        search_result_pager_strip = (PagerSlidingTabStrip) findViewById(R.id.search_result_pager_strip);
        search_result_pager = (ViewPager) findViewById(R.id.search_result_pager);

        search_result_pager_strip.setTextColor(Color.WHITE);
        search_result_pager_strip.setTextSize((int) ResourceUtil.getDimension(this, R.dimen.Subhead));
    }

    @Override
    public void establishCallbacks() {
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_search_result;
    }

    @Override
    public void onSearchResult(int liveStatus, int count) {
        if (liveStatus == 0) {
            mTitles[1] = getString(R.string.search_status_offline) + "（" + count + "）";
        } else {
            mTitles[0] = getString(R.string.search_status_online) + "（" + count + "）";
        }
        search_result_pager_strip.notifyDataSetChanged();
    }

    @Override
    public void startAction() {
        search_result_pager.setAdapter(new SearchResultAdapter(getSupportFragmentManager()));
        search_result_pager_strip.setViewPager(search_result_pager);
    }

    private class SearchResultAdapter extends FragmentPageAdapter {
        public SearchResultAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public Fragment getItem(int position) {
            return SearchFragment.create(mQuery, 1 - position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }
    }
}