package tv.acfun.read.bases.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.swipe.SwipeLayout;
import com.harreke.easyapp.frameworks.bases.IFramework;
import com.harreke.easyapp.frameworks.bases.activity.ActivityFramework;
import com.harreke.easyapp.frameworks.lists.recyclerview.RecyclerFramework;
import com.harreke.easyapp.helpers.DialogHelper;
import com.harreke.easyapp.holders.recycerview.RecyclerHolder;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

import tv.acfun.read.BuildConfig;
import tv.acfun.read.R;
import tv.acfun.read.bases.application.AcFunRead;
import tv.acfun.read.beans.Content;
import tv.acfun.read.holders.HistoryHolder;
import tv.acfun.read.parsers.ChannelListParser;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/09/23
 */
public class HistoryActivity extends ActivityFramework {
    private DialogHelper mClearDialog;
    private HistoryRecyclerHelper mHistoryRecyclerHelper;
    private DialogInterface.OnClickListener mOnClearDialogClickListener;
    private View.OnClickListener mOnRemoveClickListener;
    private SwipeLayout mOpenSwipeLayout = null;
    private SwipeLayout.SwipeListener mSwipeListener;

    public static Intent create(Context context) {
        return new Intent(context, HistoryActivity.class);
    }

    @Override
    public void acquireArguments(Intent intent) {
    }

    @Override
    public void attachCallbacks() {
    }

    @Override
    public void createMenu() {
        setToolbarTitle(R.string.menu_history);
        setToolbarNavigation();
        addToolbarItem(0, R.string.app_clear, R.drawable.image_clear_inverse);
    }

    @Override
    public void enquiryViews() {
        mHistoryRecyclerHelper = new HistoryRecyclerHelper(this);
        mHistoryRecyclerHelper.setHasFixedSize(true);
        mHistoryRecyclerHelper.attachAdapter();

        mClearDialog = new DialogHelper(this);
        mClearDialog.setTitle(R.string.history_clear);
        mClearDialog.setPositiveButton(R.string.app_ok);
        mClearDialog.setNegativeButton(R.string.app_cancel);
        mClearDialog.setOnClickListener(mOnClearDialogClickListener);
    }

    @Override
    public void establishCallbacks() {
        mOnRemoveClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = mHistoryRecyclerHelper.findItem((Integer) v.getTag());

                if (mOpenSwipeLayout != null) {
                    mOpenSwipeLayout.close();
                    mOpenSwipeLayout = null;
                }
                mHistoryRecyclerHelper.removeItem(position);
                AcFunRead.getInstance().writeHistory(mHistoryRecyclerHelper.getItemList());
            }
        };
        mOnClearDialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mClearDialog.hide();
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    AcFunRead.getInstance().writeHistory(null);
                    mHistoryRecyclerHelper.clear();
                }
            }
        };
        mSwipeListener = new SwipeLayout.SwipeListener() {
            @Override
            public void onClose(SwipeLayout swipeLayout) {
            }

            @Override
            public void onHandRelease(SwipeLayout swipeLayout, float v, float v2) {

            }

            @Override
            public void onOpen(SwipeLayout swipeLayout) {
                mOpenSwipeLayout = swipeLayout;
            }

            @Override
            public void onStartClose(SwipeLayout swipeLayout) {

            }

            @Override
            public void onStartOpen(SwipeLayout swipeLayout) {
                if (mOpenSwipeLayout != null && mOpenSwipeLayout != swipeLayout) {
                    mOpenSwipeLayout.close();
                }
            }

            @Override
            public void onUpdate(SwipeLayout swipeLayout, int i, int i2) {

            }
        };
    }

    @Override
    public void onBackPressed() {
        exit(R.anim.zoom_in_enter, R.anim.slide_out_left);
    }

    @Override
    protected void onDestroy() {
        mClearDialog.hide();
        super.onDestroy();
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        mClearDialog.show();

        return false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!BuildConfig.DEBUG) {
            MobclickAgent.onPause(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!BuildConfig.DEBUG) {
            MobclickAgent.onResume(this);
        }

        mHistoryRecyclerHelper.clear();
        readList();
    }

    private void readList() {
        List<Content> historyList = AcFunRead.getInstance().readHistory();

        if (historyList == null) {
            showToast(R.string.app_cannotread_cache);
        }
        mHistoryRecyclerHelper.from(historyList, true);
    }

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_history);
    }

    @Override
    public void startAction() {
    }

    private class HistoryRecyclerHelper extends RecyclerFramework<Content> {
        public HistoryRecyclerHelper(IFramework framework) {
            super(framework);
        }

        @Override
        public RecyclerHolder<Content> createHolder(View convertView, int viewType) {
            HistoryHolder holder = new HistoryHolder(convertView);

            holder.setOnRemoveClickListener(mOnRemoveClickListener);
            holder.addSwipeListener(mSwipeListener);

            return holder;
        }

        @Override
        public View createView(ViewGroup parent, int viewType) {
            return LayoutInflater.from(getActivity()).inflate(R.layout.item_history, parent, false);
        }

        @Override
        public void onRequestAction() {
        }

        @Override
        public void onItemClick(int position, Content content) {
            if (mOpenSwipeLayout != null) {
                mOpenSwipeLayout.close();
                mOpenSwipeLayout = null;
            } else {
                start(ContentActivity.create(getActivity(), content.getContentId()));
            }
        }

        @Override
        public List<Content> onParse(String json) {
            ChannelListParser listParser = ChannelListParser.parse(json);

            if (listParser != null) {
                return listParser.getItemList();
            } else {
                return null;
            }
        }
    }
}