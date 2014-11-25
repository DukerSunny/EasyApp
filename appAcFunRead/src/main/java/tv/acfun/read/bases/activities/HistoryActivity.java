package tv.acfun.read.bases.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;

import com.daimajia.swipe.SwipeLayout;
import com.harreke.easyapp.frameworks.bases.IFramework;
import com.harreke.easyapp.frameworks.bases.activity.ActivityFramework;
import com.harreke.easyapp.frameworks.list.abslistview.FooterLoadStatus;
import com.harreke.easyapp.frameworks.list.swipelayout.AbsListSwipeFramework;
import com.harreke.easyapp.helpers.DialogHelper;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
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
    private HistoryListHelper mHistoryList;
    private SwipeLayout mOpenSwipeLayout = null;
    private View.OnClickListener mRemoveClickListener;
    private DialogHelper mRemoveDialog;
    private DialogInterface.OnClickListener mRemoveDialogClickListener;

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
        setToolbarNavigation(R.drawable.image_back_inverse);
        addToolbarItem(0, R.string.app_clear, R.drawable.image_clear);
    }

    @Override
    public void enquiryViews() {
        View footer_loadmore = View.inflate(getActivity(), R.layout.footer_loadmore, null);

        mHistoryList = new HistoryListHelper(this, R.id.history_list, R.id.history_swipe);
        mHistoryList.addFooterView(footer_loadmore);
        mHistoryList.setLoadMore(new FooterLoadStatus(footer_loadmore));
        mHistoryList.setShowRetryHintWhenEmpty(false);
        mHistoryList.bindAdapter();

        mRemoveDialog = new DialogHelper(this);
        mRemoveDialog.setTitle(R.string.history_clear);
        mRemoveDialog.setPositiveButton(R.string.app_ok);
        mRemoveDialog.setNegativeButton(R.string.app_cancel);
        mRemoveDialog.setOnClickListener(mRemoveDialogClickListener);
    }

    @Override
    public void establishCallbacks() {
        mRemoveClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (Integer) v.getTag();

                if (mOpenSwipeLayout != null) {
                    mOpenSwipeLayout.close();
                    mOpenSwipeLayout = null;
                }
                mHistoryList.removeItem(position);
                AcFunRead.getInstance().writeHistory(mHistoryList.getItemList());
                mHistoryList.clear();
                readList();
            }
        };
        mRemoveDialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mRemoveDialog.hide();
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    AcFunRead.getInstance().writeHistory(null);
                    mHistoryList.clear();
                    mHistoryList.refresh();
                }
            }
        };
    }

    @Override
    protected void onDestroy() {
        mRemoveDialog.hide();
        super.onDestroy();
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        mRemoveDialog.show();

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

        mHistoryList.clear();
        readList();
    }

    private void readList() {
        List<Content> historyList = AcFunRead.getInstance().readHistory();

        if (historyList == null) {
            showToast(R.string.app_cannotread_cache);
        }
        mHistoryList.from(historyList);
    }

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_history);
    }

    @Override
    public void startAction() {
    }

    private class HistoryListHelper extends AbsListSwipeFramework<Content, HistoryHolder> {
        public HistoryListHelper(IFramework framework, int listId, int swipeLayoutId) {
            super(framework, listId, swipeLayoutId);
        }

        @Override
        public HistoryHolder createHolder(View convertView) {
            return new HistoryHolder(convertView, mRemoveClickListener);
        }

        @Override
        public View createView() {
            return View.inflate(getActivity(), R.layout.item_history, null);
        }

        @Override
        public void onAction() {
        }

        @Override
        public void onClose(SwipeLayout swipeLayout) {
        }

        @Override
        public void onHandRelease(SwipeLayout swipeLayout, float v, float v2) {
        }

        @Override
        public void onItemClick(int position, Content content) {
            if (mOpenSwipeLayout != null) {
                mOpenSwipeLayout.close();
                mOpenSwipeLayout = null;
            } else {
                if (content != null) {
                    start(ContentActivity.create(getActivity(), content.getContentId()));
                } else {
                    start(SearchActivity.create(getActivity()));
                }
            }
        }

        @Override
        public void onOpen(SwipeLayout swipeLayout) {
            mOpenSwipeLayout = swipeLayout;
        }

        @Override
        public ArrayList<Content> onParse(String json) {
            ChannelListParser listParser = ChannelListParser.parse(json);

            if (listParser != null) {
                setTotalPage(listParser.getTotalPage());

                return listParser.getItemList();
            } else {
                return null;
            }
        }

        @Override
        public void onStartClose(SwipeLayout swipeLayout) {
        }

        @Override
        public void onStartOpen(SwipeLayout swipeLayout) {
        }

        @Override
        public void onUpdate(SwipeLayout swipeLayout, int i, int i2) {
        }

        @Override
        public int parseItemId(Content content) {
            return content.getContentId();
        }
    }
}