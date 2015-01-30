package tv.acfun.read.bases.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.harreke.easyapp.bases.IFramework;
import com.harreke.easyapp.bases.activity.ActivityFramework;
import com.harreke.easyapp.frameworks.recyclerview.RecyclerFramework;
import com.harreke.easyapp.frameworks.recyclerview.RecyclerHolder;
import com.harreke.easyapp.widgets.HackySearchView;
import com.harreke.easyapp.widgets.rippleeffects.RippleDrawable;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

import tv.acfun.read.BuildConfig;
import tv.acfun.read.R;
import tv.acfun.read.api.API;
import tv.acfun.read.beans.Search;
import tv.acfun.read.holders.SearchHolder;
import tv.acfun.read.parsers.SearchListParser;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/09/29
 */
public class SearchActivity extends ActivityFramework implements SearchView.OnQueryTextListener {
    private API.ChannelId mChannelId = API.ChannelId.all;
    private API.Field mField = API.Field.title;
    private View.OnClickListener mOnClickListener;
    private String mQuery;
    private String[] mSearchRange;
    private MaterialDialog mSearchRangeDialog;
    private MaterialDialog.ListCallback mSearchRangeListCallback;
    private SearchRecyclerHelper mSearchRecyclerHelper;
    private String[] mSearchSort;
    private MaterialDialog mSearchSortDialog;
    private MaterialDialog.ListCallback mSearchSortListCallback;
    private String[] mSearchTarget;
    private MaterialDialog mSearchTargetDialog;
    private MaterialDialog.ListCallback mSearchTargetListCallback;
    private SearchView mSearchView;
    private API.SortField mSortField = API.SortField.score;
    private View search_range;
    private TextView search_range_indicator;
    private View search_sort;
    private TextView search_sort_indicator;
    private View search_target;
    private TextView search_target_indicator;

    public static Intent create(Context context) {
        return new Intent(context, SearchActivity.class);
    }

    @Override
    public void acquireArguments(Intent intent) {
        Resources resource = getResources();

        mQuery = null;

        mSearchSort = resource.getStringArray(R.array.search_sort);
        mSearchRange = resource.getStringArray(R.array.search_range);
        mSearchTarget = resource.getStringArray(R.array.search_target);
    }

    @Override
    public void attachCallbacks() {
        search_sort.setOnClickListener(mOnClickListener);
        search_range.setOnClickListener(mOnClickListener);
        search_target.setOnClickListener(mOnClickListener);
    }

    @Override
    public void createMenu() {
        setToolbarNavigation();
        setToolbarTitle(R.string.app_search);
        mSearchView = new HackySearchView(this);
        mSearchView.setOnQueryTextListener(this);
        addToolbarViewItem(0, R.string.app_search, mSearchView);
    }

    private void doSearch(String input) {
        getToolBar().requestFocus();
        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(mSearchView.getWindowToken(), 0);
        if (input.length() > 1) {
            mQuery = input;
            mSearchRecyclerHelper.clear();
            mSearchRecyclerHelper.setCanRefresh(true);
            mSearchRecyclerHelper.setCanLoad(true);
            mSearchRecyclerHelper.setShowRetryWhenEmptyIdle(true);
            mSearchRecyclerHelper.setClickableWhenEmptyIdle(true);
            startAction();
        } else {
            showToast(getString(R.string.search_tooshort));
        }
    }

    @Override
    public void enquiryViews() {
        search_sort = findViewById(R.id.search_sort);
        search_sort_indicator = (TextView) findViewById(R.id.search_sort_indicator);
        search_range = findViewById(R.id.search_range);
        search_range_indicator = (TextView) findViewById(R.id.search_range_indicator);
        search_target = findViewById(R.id.search_target);
        search_target_indicator = (TextView) findViewById(R.id.search_target_indicator);

        mSearchRecyclerHelper = new SearchRecyclerHelper(this);
        mSearchRecyclerHelper.setShowRetryWhenEmptyIdle(false);
        mSearchRecyclerHelper.setClickableWhenEmptyIdle(false);
        mSearchRecyclerHelper.setCanRefresh(false);
        mSearchRecyclerHelper.setCanLoad(false);
        mSearchRecyclerHelper.attachAdapter();
        mSearchRecyclerHelper.showEmptyIdle();

        mSearchSortDialog = new MaterialDialog.Builder(this).title(R.string.search_sort).items(mSearchSort)
                .negativeText(R.string.app_cancel).itemsCallback(mSearchSortListCallback).build();
        mSearchRangeDialog = new MaterialDialog.Builder(this).title(R.string.search_range).items(mSearchRange)
                .negativeText(R.string.app_cancel).itemsCallback(mSearchRangeListCallback).build();
        mSearchTargetDialog = new MaterialDialog.Builder(this).title(R.string.search_target).items(mSearchTarget)
                .negativeText(R.string.app_cancel).itemsCallback(mSearchTargetListCallback).build();

        RippleDrawable.attach(search_sort, RippleDrawable.RIPPLE_STYLE_LIGHT);
        RippleDrawable.attach(search_range, RippleDrawable.RIPPLE_STYLE_LIGHT);
        RippleDrawable.attach(search_target, RippleDrawable.RIPPLE_STYLE_LIGHT);
    }

    @Override
    public void establishCallbacks() {
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.search_sort:
                        mSearchSortDialog.show();
                        break;
                    case R.id.search_range:
                        mSearchRangeDialog.show();
                        break;
                    case R.id.search_target:
                        mSearchTargetDialog.show();
                }
            }
        };
        mSearchSortListCallback = new MaterialDialog.ListCallback() {
            @Override
            public void onSelection(MaterialDialog materialDialog, View view, int i, CharSequence charSequence) {
                mSortField = API.SortField.getSortFieldByIndex(i);
                search_sort_indicator.setText(mSearchSort[mSortField.getIndex()]);
            }
        };
        mSearchRangeListCallback = new MaterialDialog.ListCallback() {
            @Override
            public void onSelection(MaterialDialog materialDialog, View view, int i, CharSequence charSequence) {
                mChannelId = API.ChannelId.getChannelIdByIndex(i);
                search_range_indicator.setText(mSearchRange[mChannelId.getIndex()]);
            }
        };
        mSearchTargetListCallback = new MaterialDialog.ListCallback() {
            @Override
            public void onSelection(MaterialDialog materialDialog, View view, int i, CharSequence charSequence) {
                mField = API.Field.getFieldByIndex(i);
                search_target_indicator.setText(mSearchTarget[mField.getIndex()]);
            }
        };
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    protected void onDestroy() {
        mSearchSortDialog.dismiss();
        mSearchRangeDialog.dismiss();
        mSearchTargetDialog.dismiss();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!BuildConfig.DEBUG) {
            MobclickAgent.onPause(this);
        }
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        doSearch(s);

        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!BuildConfig.DEBUG) {
            MobclickAgent.onResume(this);
        }
    }

    @Override
    public void startAction() {
        if (!TextUtils.isEmpty(mQuery)) {
            mSearchRecyclerHelper
                    .from(API.getSearch(mQuery, mChannelId, mSortField, mField, 20, mSearchRecyclerHelper.getCurrentPage()));
        }
    }

    private class SearchRecyclerHelper extends RecyclerFramework<Search> {
        public SearchRecyclerHelper(IFramework framework) {
            super(framework);
        }

        @Override
        protected RecyclerHolder<Search> createHolder(View convertView, int viewType) {
            return new SearchHolder(convertView);
        }

        @Override
        protected View createView(LayoutInflater inflater, ViewGroup parent, int viewType) {
            return inflater.inflate(R.layout.item_search, parent, false);
        }

        @Override
        public void onItemClick(int position, Search search) {
            start(ContentActivity.create(getContext(), Integer.valueOf(search.getContentId().substring(2))));
        }

        @Override
        public List<Search> onParse(String json) {
            SearchListParser parser = SearchListParser.parse(json);

            if (parser != null) {
                return parser.getItemList();
            } else {
                return null;
            }
        }
    }
}