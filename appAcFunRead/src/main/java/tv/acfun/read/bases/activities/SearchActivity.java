package tv.acfun.read.bases.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.harreke.easyapp.frameworks.bases.IFramework;
import com.harreke.easyapp.frameworks.bases.activity.ActivityFramework;
import com.harreke.easyapp.frameworks.lists.recyclerview.RecyclerFramework;
import com.harreke.easyapp.holders.recycerview.RecyclerHolder;
import com.harreke.easyapp.widgets.HackySearchView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

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
    private int mChannelId;
    private boolean mFirstSearch = true;
    private int mOrderBy;
    private int mOrderId;
    private String mQuery;
    private AdapterView.OnItemSelectedListener mSearchRangeItemClickListener;
    private SearchRecyclerHelper mSearchRecyclerHelper;
    private AdapterView.OnItemSelectedListener mSearchSortResultItemClickListener;
    private AdapterView.OnItemSelectedListener mSearchTargetItemClickListener;
    private SearchView mSearchView;
    private Spinner search_range;
    private Spinner search_sortresult;
    private Spinner search_target;

    public static Intent create(Context context) {
        return new Intent(context, SearchActivity.class);
    }

    @Override
    public void acquireArguments(Intent intent) {
        mQuery = null;
        mChannelId = 110;
        mOrderBy = 0;
        mOrderId = 0;
    }

    @Override
    public void attachCallbacks() {
        //        search_sortresult.setOnItemSelectedListener(mSearchSortResultItemClickListener);
        //        search_target.setOnItemSelectedListener(mSearchTargetItemClickListener);
        //        search_range.setOnItemSelectedListener(mSearchRangeItemClickListener);
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
        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(mSearchView.getWindowToken(), 0);
        if (input.length() > 1) {
            mQuery = input;
            mSearchRecyclerHelper.clear();
            mSearchRecyclerHelper.setCanRefresh(true);
            mSearchRecyclerHelper.setCanLoad(true);
            startAction();
        } else {
            showToast(getString(R.string.search_tooshort));
        }
    }

    @Override
    public void enquiryViews() {
        //        search_sortresult = (Spinner) findViewById(R.id.search_sortresult);
        //        search_target = (Spinner) findViewById(R.id.search_target);
        //        search_range = (Spinner) findViewById(R.id.search_range);
        //        search_ptr = (PtrFrameLayout) findViewById(R.id.search_ptr);

        mSearchRecyclerHelper = new SearchRecyclerHelper(this);
        mSearchRecyclerHelper.showRetryWhenEmptyIdle(false);
        mSearchRecyclerHelper.setClickableWhenEmptyIdle(false);
        mSearchRecyclerHelper.hideEmpty();
        mSearchRecyclerHelper.setCanRefresh(false);
        mSearchRecyclerHelper.setCanLoad(false);
        mSearchRecyclerHelper.attachAdapter();
    }

    @Override
    public void establishCallbacks() {
        mSearchSortResultItemClickListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        mOrderId = 2;
                        break;
                    case 1:
                        mOrderId = 1;
                        break;
                    case 2:
                        mOrderId = 4;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        mSearchTargetItemClickListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        mOrderBy = 1;
                        break;
                    case 1:
                        mOrderBy = 3;
                        break;
                    case 2:
                        mOrderBy = 2;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        mSearchRangeItemClickListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        mChannelId = 110;
                        break;
                    case 1:
                        mChannelId = 73;
                        break;
                    case 2:
                        mChannelId = 74;
                        break;
                    case 3:
                        mChannelId = 75;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
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
    public void setLayout() {
        setContentView(R.layout.activity_search);
    }

    @Override
    public void startAction() {
        mSearchRecyclerHelper
                .from(API.getSearch(mQuery, mChannelId, mOrderBy, mOrderId, 20, mSearchRecyclerHelper.getCurrentPage()));
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
        protected View createView(ViewGroup parent, int viewType) {
            return LayoutInflater.from(getActivity()).inflate(R.layout.item_search, parent, false);
        }

        @Override
        public void onRequestAction() {
            startAction();
        }

        @Override
        public void onItemClick(int position, Search search) {
            start(ContentActivity.create(getActivity(), search.getId()));
        }

        @Override
        public ArrayList<Search> onParse(String json) {
            SearchListParser parser = SearchListParser.parse(json);

            if (parser != null) {
                return parser.getContents();
            } else {
                return null;
            }
        }
    }
}