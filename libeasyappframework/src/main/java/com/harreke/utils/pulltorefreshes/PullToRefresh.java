package com.harreke.utils.pulltorefreshes;

import android.content.Context;
import android.view.View;
import android.widget.AbsListView;

import com.harreke.utils.R;
import com.harreke.utils.frameworks.IFramework;
import com.harreke.utils.loaders.ILoader;
import com.harreke.utils.requests.IRequestCallback;
import com.harreke.utils.requests.RequestBuilder;
import com.harreke.utils.widgets.InfoView;

import java.util.ArrayList;
import java.util.Comparator;

import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

public abstract class PullToRefresh<LIST extends AbsListView, ITEM, LOADER extends ILoader<ITEM>>
        implements OnRefreshListener, IPullToRefresh<ITEM>, AbsListView.OnScrollListener, View.OnClickListener, IRequestCallback<LOADER> {
    protected int mCurrentPage = 0;
    protected IFramework mFramework;
    protected InfoView mInfo;
    protected LIST mList;
    protected int mPageSize = 0;
    protected View mRoot;
    protected int mScrollState = SCROLL_STATE_IDLE;
    protected int mTotalPage = 0;
    private Comparator<ITEM> mComparator = null;
    private String mCompleteText;
    private String mErrorText;
    private String mLastText;
    private String mLoadingText;
    private PullToRefreshLayout mPullToRefresh = null;
    private boolean mReverse = false;

    /**
     初始化下拉刷新助手

     @param framework
     Activity或Fragment框架
     @param listId
     列表的视图Id
     @param pullToRefreshId
     下拉刷新的视图Id，如果不需要下拉刷新特性则传入-1
     */
    @SuppressWarnings("unchecked")
    public PullToRefresh(IFramework framework, int listId, int pullToRefreshId) {
        Context context;

        if (framework == null) {
            throw new IllegalArgumentException("Framework must not be null!");
        } else {
            mList = (LIST) framework.findViewById(listId);
            if (mList == null) {
                throw new IllegalArgumentException("Invalid listId!");
            }
            mList.setOnScrollListener(this);
            if (pullToRefreshId > 0) {
                mPullToRefresh = (PullToRefreshLayout) framework.findViewById(pullToRefreshId);
                if (mPullToRefresh == null) {
                    throw new IllegalArgumentException("Invalid layoutId!");
                }
                ActionBarPullToRefresh.from(framework.getActivity()).theseChildrenArePullable(mList).listener(this).setup(mPullToRefresh);
            }
            context = framework.getActivity();
            mCompleteText = context.getString(R.string.list_complete);
            mErrorText = context.getString(R.string.list_error);
            mLastText = context.getString(R.string.list_last);
            mLoadingText = context.getString(R.string.list_loading);
            mFramework = framework;
            setRootView(framework.getRootView());
            setInfoView(framework.getInfoView());
        }
    }

    /**
     设置内容层

     @param root
     内容层
     */
    public final void setRootView(View root) {
        mRoot = root;
    }

    /**
     设置覆盖层

     @param info
     覆盖层
     */
    public final void setInfoView(InfoView info) {
        mInfo = info;
        if (info != null) {
            info.setOnClickListener(this);
        }
    }

    /**
     销毁助手
     */
    public final void destroy() {
        cancel();
        clear();
        mFramework = null;
    }

    /**
     停止加载列表
     */
    public final void cancel() {
        mFramework.cancelRequest();
        if (mPullToRefresh != null) {
            mPullToRefresh.setRefreshComplete();
        }
    }

    /**
     {@inheritDoc}
     */
    @Override
    public void clear() {
        cancel();
        mCurrentPage = 1;
        mTotalPage = 1;
    }

    /**
     {@inheritDoc}
     */
    @Override
    public void onError() {
        hideToast(true);
        refresh();
        previousPage();
        if (isEmpty()) {
            if (mRoot != null) {
                mRoot.setVisibility(View.GONE);
            }
            if (mInfo != null) {
                mInfo.showError();
            }
        } else {
            if (mRoot != null) {
                mRoot.setVisibility(View.VISIBLE);
            }
            if (mInfo != null) {
                mInfo.hide();
            }
            mFramework.showToast(mErrorText, false);
        }
    }

    /**
     {@inheritDoc}
     */
    @Override
    public void onPostAction() {
        hideToast(true);
        refresh();
        if (isEmpty()) {
            if (mRoot != null) {
                mRoot.setVisibility(View.GONE);
            }
            if (mInfo != null) {
                mInfo.showEmpty();
            }
        } else {
            if (mRoot != null) {
                mRoot.setVisibility(View.VISIBLE);
            }
            if (mInfo != null) {
                mInfo.hide();
            }
            if (mCurrentPage > 1) {
                if (isLastPage()) {
                    showToast(mLastText, false);
                } else {
                    showToast(mCompleteText.replace("%s", String.valueOf(mPageSize)), false);
                }
            }
        }
    }

    /**
     {@inheritDoc}
     */
    @Override
    public void onPreAction() {
        hideToast(true);
        if (isEmpty()) {
            if (mRoot != null) {
                mRoot.setVisibility(View.GONE);
            }
            if (mInfo != null) {
                mInfo.showLoading();
            }
        } else {
            showToast(mLoadingText, true);
        }
    }

    /**
     填充列表视图

     @param list
     项目列表
     */
    public final void from(ArrayList<ITEM> list) {
        from(list, false);
    }

    /**
     填充列表视图

     @param list
     项目列表
     @param reverse
     是否倒转列表顺序
     */
    public final void from(ArrayList<ITEM> list, boolean reverse) {
        int i;

        if (list != null) {
            onPreAction();
            if (reverse) {
                for (i = list.size() - 1; i > -1; i--) {
                    onAdd(list.get(i));
                }
            } else {
                for (i = 0; i < list.size(); i++) {
                    onAdd(list.get(i));
                }
            }
            if (mComparator != null) {
                sort(mComparator);
            }
            onPostAction();
        }
    }

    public final void hideToast(boolean immediately) {
        mFramework.hideToast(immediately);
    }

    public final void showToast(String text, boolean progress) {
        mFramework.showToast(text, progress);
    }

    /**
     判断当前网络页面是否为最后一页

     @return 是否为最后一页
     */
    public final boolean isLastPage() {
        return mCurrentPage >= mTotalPage;
    }

    /**
     填充列表视图

     @param list
     项目列表
     */
    public final void from(ITEM[] list) {
        from(list, false);
    }

    /**
     填充列表视图

     @param list
     项目列表
     @param reverse
     是否倒转列表顺序
     */
    public final void from(ITEM[] list, boolean reverse) {
        int i;

        if (list != null) {
            onPreAction();
            if (reverse) {
                for (i = list.length - 1; i > -1; i--) {
                    onAdd(list[i]);
                }
            } else {
                for (i = 0; i < list.length; i++) {
                    onAdd(list[i]);
                }
            }
            if (mComparator != null) {
                sort(mComparator);
            }
            onPostAction();
        }
    }

    /**
     填充列表视图，从网络加载内容

     @param builder
     Http请求
     */
    public final void from(RequestBuilder builder) {
        if (builder != null) {
            onPreAction();
            mFramework.executeRequest(builder, this);
        }
    }

    /**
     获得列表当前页面序号

     @return int
     */
    public final int getCurrentPage() {
        return mCurrentPage;
    }

    /**
     获得列表上一次加载的项目数量

     @return 上一次加载的项目数量
     */
    public final int getPageSize() {
        return mPageSize;
    }

    /**
     判断当前网络页面是否为第一页

     @return boolean
     */
    public final boolean isFirstPage() {
        return mCurrentPage <= 1;
    }

    public final boolean isShowingRetry() {
        return mInfo != null && mInfo.isShowingRetry();
    }

    @Override
    public void onClick(View v) {
        onRefreshStarted(null);
    }

    @Override
    public void onRefreshStarted(View view) {
        if (!isLoading()) {
            clear();
            refresh();
            onAction();
        }
    }

    /**
     判断是否正在访问网络

     @return 是否正在访问网络
     */
    public boolean isLoading() {
        return mFramework != null && mFramework.isRequestExecuting();
    }

    @Override
    public void onFailure() {
        onError();
    }

    /**
     列表翻至前一页
     */
    public final void previousPage() {
        if (mCurrentPage > 1) {
            mCurrentPage--;
        }
    }

    /**
     下拉刷新助手获得网络数据（文本）后，会通过Gson反序列化成一个Loader对象，其包含有未经处理的原始数据，然后通过调用parse()分析数据，返回一个可以直接操作的ArrayList
     */
    @Override
    public void onSuccess(LOADER loader) {
        ArrayList<ITEM> list;
        int i;

        if (loader.isSuccess()) {
            loader.parse();
            list = loader.getList();
            if (list == null) {
                list = new ArrayList<ITEM>();
            }
            mPageSize = list.size();
            if (mPageSize == 0) {
                previousPage();
                mTotalPage = mCurrentPage;
            } else {
                for (i = 0; i < mPageSize; i++) {
                    onAdd(list.get(i));
                }
            }
            if (mComparator != null) {
                sort(mComparator);
            }
            onPostAction();
        } else {
            onError();
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        mScrollState = scrollState;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (mScrollState != SCROLL_STATE_IDLE && !isLastPage() && !isLoading()) {
            if (!mReverse && firstVisibleItem + visibleItemCount >= totalItemCount - 1) {
                nextPage();
                onAction();
            } else if (mReverse && firstVisibleItem == 0) {
                nextPage();
                onAction();
            }
        }
    }

    /**
     列表翻至下一页
     */
    public final void nextPage() {
        if (mCurrentPage < mTotalPage) {
            mCurrentPage++;
        }
    }

    /**
     设置列表的排序比较器

     @param comparator
     比较器
     */
    public final void setComparator(Comparator<ITEM> comparator) {
        mComparator = comparator;
    }

    /**
     设置是否反转“加载更多”的触发条件

     @param reverseScroll
     如果为false，则向下滑动至末尾会触发“加载更多”；否则为向上滑动至顶端会触发“加载更多”
     */
    public final void setReverseScroll(boolean reverseScroll) {
        mReverse = reverseScroll;
    }

    public final void setShowEmptyRetry(boolean showRetry) {
        if (mInfo != null) {
            mInfo.setShowEmptyRetry(showRetry);
        }
    }

    /**
     设置列表的总页数

     @param totalPage
     总页数
     */
    public final void setTotalPage(int totalPage) {
        mTotalPage = totalPage;
    }
}