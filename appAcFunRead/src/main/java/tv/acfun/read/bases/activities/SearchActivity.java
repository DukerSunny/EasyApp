package tv.acfun.read.bases.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.SearchView;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

import com.harreke.easyapp.frameworks.bases.IFramework;
import com.harreke.easyapp.frameworks.bases.activity.ActivityFramework;
import com.harreke.easyapp.frameworks.list.abslistview.AbsListFramework;
import com.harreke.easyapp.frameworks.list.abslistview.FooterLoadStatus;
import com.harreke.easyapp.widgets.InfoView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

import tv.acfun.read.R;
import tv.acfun.read.api.API;
import tv.acfun.read.beans.Search;
import tv.acfun.read.holders.SearchHolder;
import tv.acfun.read.parsers.SearchListParser;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/09/29
 */
public class SearchActivity extends ActivityFramework {
    private int mChannelId;
    private int mOrderBy;
    private int mOrderId;
    private String mQuery;
    private SearchListHelper mSearchListHelper;
    private AdapterView.OnItemSelectedListener mSearchRangeItemClickListener;
    private AdapterView.OnItemSelectedListener mSearchSortResultItemClickListener;
    private AdapterView.OnItemSelectedListener mSearchTargetItemClickListener;
    private EditText search_input;
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
        search_sortresult.setOnItemSelectedListener(mSearchSortResultItemClickListener);
        search_target.setOnItemSelectedListener(mSearchTargetItemClickListener);
        search_range.setOnItemSelectedListener(mSearchRangeItemClickListener);
    }

    @Override
    public void createMenu() {
        setToolbarTitle(R.string.app_search);
        setToolbarNavigation(R.drawable.image_back_inverse);
        addToolbarViewItem(0, R.string.app_search, new SearchView(this));
    }

    private void doSearch() {
        String text = search_input.getText().toString().trim();

        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(search_input.getWindowToken(), 0);
        if (text.length() > 1) {
            mQuery = text;
            mSearchListHelper.clear();
            startAction();
        } else {
            showToast(getString(R.string.search_tooshort));
        }
    }

    @Override
    public void enquiryViews() {
        View footer_loadmore = View.inflate(getActivity(), R.layout.footer_loadmore, null);

        search_input = (EditText) findViewById(R.id.search_input);
        search_sortresult = (Spinner) findViewById(R.id.search_sortresult);
        search_target = (Spinner) findViewById(R.id.search_target);
        search_range = (Spinner) findViewById(R.id.search_range);

        mSearchListHelper = new SearchListHelper(this, R.id.search_list);
        mSearchListHelper.addFooterView(footer_loadmore);
        mSearchListHelper.setLoadMore(new FooterLoadStatus(footer_loadmore));
        mSearchListHelper.setRootView(findViewById(R.id.search_list));
        mSearchListHelper.setInfoView((InfoView) findViewById(R.id.search_info));
        mSearchListHelper.bindAdapter();
        mSearchListHelper.onPostAction();
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
    public boolean onMenuItemClick(MenuItem menuItem) {
        doSearch();

        return false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_search);
    }

    @Override
    public void startAction() {
        if (mQuery != null) {
            mSearchListHelper
                    .from(API.getSearch(mQuery, mChannelId, mOrderBy, mOrderId, 20, mSearchListHelper.getCurrentPage()));
        }
    }

    private class SearchListHelper extends AbsListFramework<Search, SearchHolder> {
        public SearchListHelper(IFramework framework, int listId) {
            super(framework, listId);
        }

        @Override
        public SearchHolder createHolder(View convertView) {
            return new SearchHolder(convertView);
        }

        @Override
        public View createView() {
            return View.inflate(getActivity(), R.layout.item_search, null);
        }

        @Override
        public void onAction() {
        }

        @Override
        public void onItemClick(int position, Search search) {
            start(ContentActivity.create(getActivity(), search.getId()));
        }

        @Override
        public ArrayList<Search> onParse(String json) {
            SearchListParser parser = SearchListParser.parse(json);

            if (parser != null) {
                setTotalPage(parser.getTotalPage());

                return parser.getContents();
            } else {
                return null;
            }
        }

        @Override
        public int parseItemId(Search search) {
            return search.getId();
        }
    }
}