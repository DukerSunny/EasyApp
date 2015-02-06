package tv.douyu.control.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.harreke.easyapp.frameworks.base.IFramework;
import com.harreke.easyapp.frameworks.base.ActivityFramework;
import com.harreke.easyapp.frameworks.recyclerview.RecyclerFramework;
import com.harreke.easyapp.frameworks.recyclerview.RecyclerHolder;
import com.harreke.easyapp.widgets.transitions.SwipeToFinishLayout;

import java.util.ArrayList;
import java.util.List;

import tv.douyu.R;
import tv.douyu.control.application.DouyuTv;
import tv.douyu.wrapper.holder.SearchKeywordHolder;

import static android.widget.TextView.OnEditorActionListener;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/12/29
 */
public class SearchActivity extends ActivityFramework {
    private MaterialDialog.ButtonCallback mClearCallback;
    private MaterialDialog mClearDialog;
    private View.OnClickListener mOnClickListener;
    private OnEditorActionListener mOnEditorActionListener;
    private String mQuery = null;
    private List<String> mSearchKeywordList;
    private SearchKeywordRecyclerHelper mSearchKeywordRecyclerHelper;
    private View search_clear;
    private EditText search_input;

    public static Intent create(Context context) {
        return new Intent(context, SearchActivity.class);
    }

    @Override
    protected void acquireArguments(Intent intent) {
        mSearchKeywordList = DouyuTv.getInstance().readSearchKeywordList();
        if (mSearchKeywordList == null) {
            mSearchKeywordList = new ArrayList<String>();
        }
    }

    @Override
    public void attachCallbacks() {
        search_input.setOnEditorActionListener(mOnEditorActionListener);
        search_clear.setOnClickListener(mOnClickListener);
    }

    private void checkAndSearch(String query) {
        mQuery = query;
        if (mSearchKeywordList.contains(mQuery)) {
            mSearchKeywordList.remove(mQuery);
        }
        mSearchKeywordList.add(0, mQuery);
        DouyuTv.getInstance().writeSearchKeywordList(mSearchKeywordList);
        startAction();
    }

    @Override
    protected void createMenu() {
        enableDefaultToolbarNavigation();
        addToolbarItem(0, R.string.search_action, R.drawable.image_toolbar_search);
    }

    private void doSearch() {
        String query = search_input.getText().toString();

        if (query.length() == 0) {
            showToast(R.string.search_empty);
        } else if (query.length() <= 2) {
            showToast(R.string.search_tooshort);
        } else {
            checkAndSearch(query);
        }
    }

    @Override
    public void enquiryViews() {
        search_input = (EditText) findViewById(R.id.search_input);
        search_clear = findViewById(R.id.search_clear);

        mSearchKeywordRecyclerHelper = new SearchKeywordRecyclerHelper(this);
        mSearchKeywordRecyclerHelper.setHasFixedSize(true);
        mSearchKeywordRecyclerHelper.setShowRetryWhenEmptyIdle(false);
        mSearchKeywordRecyclerHelper.attachAdapter();
        mSearchKeywordRecyclerHelper.showEmptyIdle();

        mClearDialog = new MaterialDialog.Builder(this).title(R.string.search_clear_sure).positiveText(R.string.app_ok)
                .negativeText(R.string.app_cancel).callback(mClearCallback).build();
    }

    @Override
    public void establishCallbacks() {
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClearDialog.show();
            }
        };
        mClearCallback = new MaterialDialog.ButtonCallback() {
            @Override
            public void onPositive(MaterialDialog materialDialog) {
                DouyuTv.getInstance().clearSearchKeywordList();
                mSearchKeywordList.clear();
                updateList();
            }
        };
        mOnEditorActionListener = new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    doSearch();

                    return true;
                } else {
                    return false;
                }
            }
        };
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_search;
    }

    private void hideSearchClear() {
        search_clear.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        mClearDialog.dismiss();
        super.onDestroy();
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        doSearch();

        return false;
    }

    private void showSearchClear() {
        search_clear.setVisibility(View.VISIBLE);
    }

    @Override
    public void startAction() {
        updateList();
        if (!TextUtils.isEmpty(mQuery)) {
            start(SearchResultActivity.create(this, mQuery));
        }
    }

    private void updateList() {
        if (mSearchKeywordList.size() > 0) {
            showSearchClear();
        } else {
            hideSearchClear();
        }
        mSearchKeywordRecyclerHelper.from(mSearchKeywordList);
    }

    private class SearchKeywordRecyclerHelper extends RecyclerFramework<String> {
        public SearchKeywordRecyclerHelper(IFramework framework) {
            super(framework);
        }

        @Override
        protected RecyclerHolder<String> createHolder(View itemView, int viewType) {
            return new SearchKeywordHolder(itemView);
        }

        @Override
        protected View createView(LayoutInflater inflater, ViewGroup parent, int viewType) {
            return inflater.inflate(R.layout.item_searchkeyword, parent, false);
        }

        @Override
        public void onItemClick(int position, String keyword) {
            checkAndSearch(keyword);
        }
    }
}