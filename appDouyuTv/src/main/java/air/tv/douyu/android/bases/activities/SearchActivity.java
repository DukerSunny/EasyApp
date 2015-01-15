package air.tv.douyu.android.bases.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.harreke.easyapp.frameworks.bases.IFramework;
import com.harreke.easyapp.frameworks.bases.activity.ActivityFramework;
import com.harreke.easyapp.frameworks.recyclerview.RecyclerFramework;
import com.harreke.easyapp.frameworks.recyclerview.RecyclerHolder;
import com.harreke.easyapp.widgets.transitions.SwipeToFinishLayout;

import air.tv.douyu.android.R;
import air.tv.douyu.android.beans.Room;
import air.tv.douyu.android.holders.RoomHolder;
import air.tv.douyu.android.parsers.RoomListParser;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/12/29
 */
public class SearchActivity extends ActivityFramework {
    private String mQuery = null;
    private SearchRecyclerHelper mSearchRecyclerHelper;
    private EditText search_input;

    public static Intent create(Context context) {
        return new Intent(context, SearchActivity.class);
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
        enableDefaultToolbarNavigation();
        addToolbarItem(0, R.string.search_action, R.drawable.image_toolbar_search);
    }

    private void doSearch() {
        String query = search_input.getText().toString();

        if (TextUtils.isEmpty(query)) {
            showToast(R.string.search_empty);
        } else {
            mQuery = query;
            startAction();
        }
    }

    @Override
    public void enquiryViews() {
        search_input = (EditText) findViewById(R.id.search_input);

        mSearchRecyclerHelper = new SearchRecyclerHelper(this);
        mSearchRecyclerHelper.setHasFixedSize(true);
        mSearchRecyclerHelper.setShowRetryWhenEmptyIdle(false);
        mSearchRecyclerHelper.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mSearchRecyclerHelper.setItemDecoration();
        mSearchRecyclerHelper.setListParser(new RoomListParser());
        mSearchRecyclerHelper.attachAdapter();
        mSearchRecyclerHelper.showEmptyIdle();
    }

    @Override
    public void establishCallbacks() {
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        doSearch();

        return false;
    }

    @Override
    public void startAction() {
        if (!TextUtils.isEmpty(mQuery)) {
            start(SearchResultActivity.create(this, mQuery));
        }
    }

    private class SearchRecyclerHelper extends RecyclerFramework<Room> {
        public SearchRecyclerHelper(IFramework framework) {
            super(framework);
        }

        @Override
        protected RecyclerHolder<Room> createHolder(View itemView, int viewType) {
            return new RoomHolder(itemView);
        }

        @Override
        protected View createView(LayoutInflater inflater, ViewGroup parent, int viewType) {
            return inflater.inflate(R.layout.item_room, parent, false);
        }

        @Override
        public void onItemClick(int position, Room room) {
            start(RoomActivity.create(getContext(), room.getRoom_id()));
        }
    }
}