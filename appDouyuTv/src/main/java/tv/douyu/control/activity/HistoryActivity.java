package tv.douyu.control.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.harreke.easyapp.frameworks.base.IFramework;
import com.harreke.easyapp.frameworks.base.ActivityFramework;
import com.harreke.easyapp.frameworks.recyclerview.GridItemDecoration;
import com.harreke.easyapp.frameworks.recyclerview.RecyclerFramework;
import com.harreke.easyapp.frameworks.recyclerview.RecyclerHolder;
import com.harreke.easyapp.widgets.transitions.SwipeToFinishLayout;

import tv.douyu.R;
import tv.douyu.misc.api.API;
import tv.douyu.model.bean.Room;
import tv.douyu.wrapper.holder.RoomHolder;
import tv.douyu.model.parser.RoomListParser;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/12/18
 */
public class HistoryActivity extends ActivityFramework {
    private HistoryRecyclerHelper mHistoryRecyclerHelper;

    public static Intent create(Context context) {
        return new Intent(context, HistoryActivity.class);
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
        setToolbarTitle(R.string.app_history);
        enableDefaultToolbarNavigation();
    }

    @Override
    public void enquiryViews() {
        mHistoryRecyclerHelper = new HistoryRecyclerHelper(this);
        mHistoryRecyclerHelper.setHasFixedSize(true);
        mHistoryRecyclerHelper.setCanLoad(false);
        mHistoryRecyclerHelper.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mHistoryRecyclerHelper.setItemDecoration(new GridItemDecoration(2));
        mHistoryRecyclerHelper.setListParser(new RoomListParser());
        mHistoryRecyclerHelper.attachAdapter();
    }

    @Override
    public void establishCallbacks() {
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_live;
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        start(SearchActivity.create(this));

        return false;
    }

    @Override
    public void startAction() {
        mHistoryRecyclerHelper.from(API.getHistory());
    }

    private class HistoryRecyclerHelper extends RecyclerFramework<Room> {
        public HistoryRecyclerHelper(IFramework framework) {
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
