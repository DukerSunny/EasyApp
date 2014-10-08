package tv.acfun.read.bases.activities;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.harreke.easyapp.beans.ActionBarItem;
import com.harreke.easyapp.frameworks.bases.IFramework;
import com.harreke.easyapp.frameworks.bases.activity.ActivityFramework;
import com.harreke.easyapp.frameworks.list.abslistview.AbsListFramework;
import com.harreke.easyapp.helpers.PopupAbsListHelper;

import java.util.ArrayList;

import tv.acfun.read.R;
import tv.acfun.read.api.API;
import tv.acfun.read.beans.Search;
import tv.acfun.read.holders.PopupListHoler;
import tv.acfun.read.holders.SearchHolder;
import tv.acfun.read.parsers.SearchListParser;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/09/29
 */
public class SearchActivity extends ActivityFramework {
    private int mChannelId;
    private View.OnClickListener mClickListener;
    private int mOrderBy;
    private int mOrderId;
    private String mQuery;
    private boolean mReverse;
    private SearchListHelper mSearchListHelper;
    private PopupListHelper mSearchRangeHelper;
    private AdapterView.OnItemClickListener mSearchRangeItemClickListener;
    private PopupListHelper mSearchSortOrderHelper;
    private AdapterView.OnItemClickListener mSearchSortOrderItemClickListener;
    private PopupListHelper mSearchSortResultHelper;
    private AdapterView.OnItemClickListener mSearchSortResultItemClickListener;
    private PopupListHelper mSearchTargetHelper;
    private AdapterView.OnItemClickListener mSearchTargetItemClickListener;
    private View search_back;
    private View search_go;
    private EditText search_input;
    private TextView search_range;
    private TextView search_sortorder;
    private TextView search_sortresult;
    private TextView search_target;

    public static Intent create(Context context) {
        return new Intent(context, SearchActivity.class);
    }

    @Override
    public void assignEvents() {
        search_back.setOnClickListener(mClickListener);
        search_go.setOnClickListener(mClickListener);
        search_sortresult.setOnClickListener(mClickListener);
        search_target.setOnClickListener(mClickListener);
        search_range.setOnClickListener(mClickListener);
        search_sortorder.setOnClickListener(mClickListener);

        mSearchSortResultHelper.setOnItemClickListener(mSearchSortResultItemClickListener);
        mSearchTargetHelper.setOnItemClickListener(mSearchTargetItemClickListener);
        mSearchRangeHelper.setOnItemClickListener(mSearchRangeItemClickListener);
        mSearchSortOrderHelper.setOnItemClickListener(mSearchSortOrderItemClickListener);
    }

    @Override
    public void initData(Intent intent) {
        mQuery = null;
        mChannelId = 110;
        mOrderBy = 0;
        mOrderId = 0;
        mReverse = false;
    }

    @Override
    public void newEvents() {
        mClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.search_back:
                        onBackPressed();
                        break;
                    case R.id.search_go:
                        searchGo();
                        break;
                    case R.id.search_sortresult:
                        mSearchSortResultHelper.toggle();
                        break;
                    case R.id.search_target:
                        mSearchTargetHelper.toggle();
                        break;
                    case R.id.search_range:
                        mSearchRangeHelper.toggle();
                        break;
                    case R.id.search_sortorder:
                        mSearchSortOrderHelper.toggle();
                }
            }
        };
        mSearchSortResultItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSearchSortResultHelper.hide();
                search_sortresult.setText(mSearchSortResultHelper.getItem(position));
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
        };
        mSearchTargetItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSearchTargetHelper.hide();
                search_target.setText(mSearchTargetHelper.getItem(position));
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
        };
        mSearchRangeItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSearchRangeHelper.hide();
                search_range.setText(mSearchRangeHelper.getItem(position));
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
        };
        mSearchSortOrderItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSearchSortOrderHelper.hide();
                search_sortorder.setText(mSearchSortOrderHelper.getItem(position));
                mReverse = position != 0;
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
    protected void onDestroy() {
        if (mSearchSortResultHelper != null) {
            mSearchSortResultHelper.hide();
        }
        if (mSearchTargetHelper != null) {
            mSearchTargetHelper.hide();
        }
        if (mSearchRangeHelper != null) {
            mSearchRangeHelper.hide();
        }
        if (mSearchSortOrderHelper != null) {
            mSearchSortOrderHelper.hide();
        }
        super.onDestroy();
    }

    @Override
    public void queryLayout() {
        search_back = findContentView(R.id.search_back);
        search_input = (EditText) findContentView(R.id.search_input);
        search_go = findContentView(R.id.search_go);
        search_sortresult = (TextView) findContentView(R.id.search_sortresult);
        search_target = (TextView) findContentView(R.id.search_target);
        search_range = (TextView) findContentView(R.id.search_range);
        search_sortorder = (TextView) findContentView(R.id.search_sortorder);

        mSearchSortResultHelper = new PopupListHelper(this, search_sortresult);
        //        mSearchSortResultHelper.add(0, getString(R.string.search_sortresult_relative));
        mSearchSortResultHelper.add(1, getString(R.string.search_sortresult_releasedate));
        mSearchSortResultHelper.add(2, getString(R.string.search_sortresult_views));
        //        mSearchSortResultHelper.add(3, getString(R.string.search_sortresult_comments));
        mSearchSortResultHelper.add(4, getString(R.string.search_sortresult_stows));

        mSearchTargetHelper = new PopupListHelper(this, search_target);
        mSearchTargetHelper.add(0, getString(R.string.search_target_title_tag));
        mSearchTargetHelper.add(1, getString(R.string.search_target_content_description));
        mSearchTargetHelper.add(2, getString(R.string.search_target_username));

        mSearchRangeHelper = new PopupListHelper(this, search_range);
        mSearchRangeHelper.add(0, getString(R.string.search_range_misc));
        mSearchRangeHelper.add(1, getString(R.string.search_range_work_emotion));
        mSearchRangeHelper.add(2, getString(R.string.search_range_dramaculture));
        mSearchRangeHelper.add(3, getString(R.string.search_range_comic_novel));

        mSearchSortOrderHelper = new PopupListHelper(this, search_sortorder);
        mSearchSortOrderHelper.add(1, getString(R.string.search_sortorder_default));
        mSearchSortOrderHelper.add(2, getString(R.string.search_sortorder_reverse));

        mSearchListHelper = new SearchListHelper(this, R.id.search_list);
        mSearchListHelper.setLoadEnabled(true);
        mSearchListHelper.setRootView(findContentView(R.id.search_list));
        mSearchListHelper.setInfoView((com.harreke.easyapp.widgets.InfoView) findContentView(R.id.search_info));
        mSearchListHelper.bindAdapter();
    }

    private void searchGo() {
        String text = search_input.getText().toString().trim();

        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(search_input.getWindowToken(), 0);
        if (text.length() > 1) {
            mQuery = text;
            mSearchListHelper.clear();
            startAction();
        } else {
            showToast(getString(R.string.search_inputtoshort));
        }
    }

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_search);
    }

    @Override
    public void startAction() {
        if (mQuery != null) {
            mSearchListHelper
                    .from(API.getSearch(mQuery, mChannelId, mOrderBy, mOrderId, 20, mSearchListHelper.getCurrentPage()),
                            mReverse);
        }
    }

    private class PopupListHelper extends PopupAbsListHelper<String, PopupListHoler> {
        public PopupListHelper(Context context, View anchor) {
            super(context, anchor);
        }

        @Override
        public PopupListHoler createHolder(View convertView) {
            return new PopupListHoler(convertView);
        }

        @Override
        public View createView() {
            return View.inflate(getActivity(), R.layout.item_popuplist, null);
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