package tv.acfun.read.bases.activities;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.daimajia.swipe.SwipeLayout;
import com.harreke.easyapp.frameworks.bases.IFramework;
import com.harreke.easyapp.frameworks.bases.activity.ActivityFramework;
import com.harreke.easyapp.frameworks.list.abslistview.FooterLoadStatus;
import com.harreke.easyapp.frameworks.list.swipelayout.AbsListSwipeFramework;

import java.util.ArrayList;
import java.util.List;

import tv.acfun.read.R;
import tv.acfun.read.bases.application.AcFunRead;
import tv.acfun.read.beans.Content;
import tv.acfun.read.holders.HistoryHolder;
import tv.acfun.read.parsers.ChannelListParser;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/09/23
 */
public class HistoryActivity extends ActivityFramework {
    private HistoryListHelper mHistoryListHelper;
    private SwipeLayout mOpenSwipeLayout = null;
    private View.OnClickListener mRemoveClickListener;

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
    public void enquiryViews() {
        View footer_loadmore = View.inflate(getActivity(), R.layout.footer_loadmore, null);

        setActionBarTitle(R.string.menu_history);
        addActionBarImageItem(0, R.drawable.image_clear);

        mHistoryListHelper = new HistoryListHelper(this, R.id.history_list, R.id.history_swipe);
        mHistoryListHelper.addFooterView(footer_loadmore);
        mHistoryListHelper.setLoadMore(new FooterLoadStatus(footer_loadmore));
        mHistoryListHelper.setShowRetryHintWhenEmpty(false);
        mHistoryListHelper.bindAdapter();
    }

    @Override
    public void establishCallbacks() {
        mRemoveClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (Integer) v.getTag();

                mOpenSwipeLayout.close();
                mOpenSwipeLayout = null;
                mHistoryListHelper.removeItem(position);
                AcFunRead.getInstance().writeHistory(mHistoryListHelper.getItemList());
                mHistoryListHelper.clear();
                readList();
            }
        };
    }

    @Override
    public void onActionBarItemClick(int id, View item) {
        AcFunRead.getInstance().writeHistory(null);
        mHistoryListHelper.clear();
        mHistoryListHelper.refresh();
    }

    @Override
    public void onBackPressed() {
        exit(false);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mHistoryListHelper.clear();
        readList();
    }

    private void readList() {
        List<Content> historyList = AcFunRead.getInstance().readHistory();

        if (historyList == null) {
            showToast(R.string.app_cannotread_cache);
        }
        mHistoryListHelper.from(historyList);
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