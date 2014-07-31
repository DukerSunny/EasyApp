package com.harreke.easyappframework.frameworks.list;

import android.content.Context;
import android.view.View;
import android.widget.AbsListView;

import com.harreke.easyappframework.R;
import com.harreke.easyappframework.frameworks.bases.IFramework;
import com.harreke.easyappframework.listeners.OnSlidableTriggerListener;
import com.harreke.easyappframework.loaders.ILoader;
import com.harreke.easyappframework.requests.IRequestCallback;
import com.harreke.easyappframework.requests.RequestBuilder;
import com.harreke.easyappframework.widgets.InfoView;
import com.harreke.easyappframework.widgets.slidableview.SlidableView;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/31
 *
 * 列表框架
 *
 * 对SlidableView进行封装的列表框架，支持任意列表视图
 * 这是一个抽象类，对于不同的AbsListView（ListView、GridView等），需要继承这个类并实现对应的抽象方法
 *
 * 所谓的列表视图，是指必须依靠Adapter管理数据的视图，如AbsListView、RecyclerView等
 * 支持ListView、GridView，以及其他衍生于AbsListView的视图
 * 不支持不依靠Adapter管理数据的视图
 *
 * 注：
 * ListView和GridView架构相近（视图单元是一个简单单元），GridView可以直接使用ListView的实现类；
 * 虽然ExpandableListView同样衍生于AbsListView，但是由于架构的不同（视图单元是一个复合单元，由一个父单元和多个子单元复合而成），不能直接使用ListView的实现类，需要另外实现ExpandableListView的方法
 * 其他架构有别于ListView的视图，也需要另外实现对应的方法
 *
 * @param <ITEM>
 *         列表条目类型
 *
 *         列表视图单元的类型，是列表的最基本组成
 * @param <LOADER>
 *         列表Loader类型
 *         列表填充数据时，数据的结构类型
 *
 *         待填充进列表的数据需按一定结构储存，以便被框架识别与解析
 */
public abstract class ListFramework<ITEM, LOADER extends ILoader<ITEM>>
        implements IList<ITEM>, IListStatusChageListener<ITEM>, IRequestCallback<LOADER>, OnSlidableTriggerListener, View.OnClickListener {
    protected int mCurrentPage = 0;
    protected IFramework mFramework;
    protected InfoView mInfo;
    protected boolean mLoadEnabled = false;
    protected int mPageSize = 0;
    protected boolean mReverse = false;
    protected View mRoot;
    protected int mTotalPage = 0;
    private Comparator<ITEM> mComparator = null;
    private String mCompleteText;
    private String mErrorText;
    private String mLastText;
    private String mLoadingText;
    private SlidableView mSlidableView = null;

    public ListFramework(IFramework framework, int listId, int slidableViewId) {
        Context context;
        View listView;
        View slidableView;

        if (framework == null) {
            throw new IllegalArgumentException("Framework must not be null!");
        } else {
            listView = framework.queryContent(listId);
            if (listView == null || !(listView instanceof AbsListView)) {
                throw new IllegalArgumentException("Invalid listId!");
            }
            setListView(listView);
            if (slidableViewId > 0) {
                slidableView = framework.queryContent(slidableViewId);
                if (slidableView == null || !(slidableView instanceof SlidableView)) {
                    throw new IllegalArgumentException("Invalid slidableViewId!");
                }
                mSlidableView = (SlidableView) slidableView;
                mSlidableView.setSlidableView(listId);
            }
            context = framework.getActivity();
            mCompleteText = context.getString(R.string.list_complete);
            mErrorText = context.getString(R.string.info_retry);
            mLastText = context.getString(R.string.list_last);
            mLoadingText = context.getString(R.string.info_loading);
            mFramework = framework;
            setRootView(framework.getContent());
            setInfoView(framework.getInfo());
        }
    }

    /**
     * 停止加载列表
     */
    public final void cancel() {
        mFramework.cancelRequest();
        if (mSlidableView != null) {
            mSlidableView.setRefreshComplete();
        }
    }

    /**
     * 清空列表
     */
    @Override
    public void clear() {
        cancel();
        mCurrentPage = 1;
        mTotalPage = 1;
    }

    /**
     * 销毁框架
     */
    public final void destroy() {
        cancel();
        clear();
        mFramework = null;
    }

    /**
     * 填充列表视图
     *
     * @param list
     *         项目列表
     */
    public final void from(ArrayList<ITEM> list) {
        from(list, false);
    }

    /**
     * 填充列表视图
     *
     * @param list
     *         项目列表
     * @param reverse
     *         是否倒转列表顺序
     */
    public final void from(ArrayList<ITEM> list, boolean reverse) {
        int i;

        if (list != null) {
            onPreAction();
            if (reverse) {
                for (i = list.size() - 1; i > -1; i--) {
                    onParseItem(list.get(i));
                }
            } else {
                for (i = 0; i < list.size(); i++) {
                    onParseItem(list.get(i));
                }
            }
            if (mComparator != null) {
                sort(mComparator);
            }
            onPostAction();
        }
    }

    /**
     * 填充列表视图
     *
     * @param list
     *         项目列表
     */
    public final void from(ITEM[] list) {
        from(list, false);
    }

    /**
     * 填充列表视图
     *
     * @param list
     *         项目列表
     * @param reverse
     *         是否倒转列表顺序
     */
    public final void from(ITEM[] list, boolean reverse) {
        int i;

        if (list != null) {
            onPreAction();
            if (reverse) {
                for (i = list.length - 1; i > -1; i--) {
                    onParseItem(list[i]);
                }
            } else {
                for (i = 0; i < list.length; i++) {
                    onParseItem(list[i]);
                }
            }
            if (mComparator != null) {
                sort(mComparator);
            }
            onPostAction();
        }
    }

    /**
     * 填充列表视图，从网络加载内容
     *
     * @param builder
     *         Http请求
     */
    public final void from(RequestBuilder builder) {
        if (builder != null) {
            onPreAction();
            mFramework.executeRequest(builder, this);
        }
    }

    /**
     * 获得列表当前页面序号
     *
     * @return int
     */
    public final int getCurrentPage() {
        return mCurrentPage;
    }

    /**
     * 获得列表上一次加载的项目数量
     *
     * @return 上一次加载的项目数量
     */
    public final int getPageSize() {
        return mPageSize;
    }

    public final void hideToast(boolean immediately) {
        mFramework.hideToast(immediately);
    }

    /**
     * 判断当前网络页面是否为第一页
     *
     * @return boolean
     */
    public final boolean isFirstPage() {
        return mCurrentPage <= 1;
    }

    /**
     * 判断当前网络页面是否为最后一页
     *
     * @return 是否为最后一页
     */
    public final boolean isLastPage() {
        return mCurrentPage >= mTotalPage;
    }

    /**
     * 判断是否正在访问网络
     *
     * @return 是否正在访问网络
     */
    public boolean isLoading() {
        return mFramework != null && mFramework.isRequestExecuting();
    }

    public final boolean isShowingRetry() {
        return mInfo != null && mInfo.isShowingRetry();
    }

    /**
     * 列表翻至下一页
     */
    public final void nextPage() {
        if (mCurrentPage < mTotalPage) {
            mCurrentPage++;
        }
    }

    @Override
    public void onClick(View v) {
        mSlidableView.setRefreshStart();
    }

    /**
     * 当列表加载错误时触发
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
                mInfo.setInfoVisibility(InfoView.INFO_ERROR);
            }
        } else {
            if (mRoot != null) {
                mRoot.setVisibility(View.VISIBLE);
            }
            if (mInfo != null) {
                mInfo.setInfoVisibility(InfoView.INFO_HIDE);
            }
            mFramework.showToast(mErrorText, false);
        }
    }

    /**
     * 解析条目
     *
     * @param item
     *         条目类型
     */
    @Override
    public void onParseItem(ITEM item) {
    }

    /**
     * 当列表加载完成时触发
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
                mInfo.setInfoVisibility(InfoView.INFO_EMPTY);
            }
        } else {
            if (mRoot != null) {
                mRoot.setVisibility(View.VISIBLE);
            }
            if (mInfo != null) {
                mInfo.setInfoVisibility(InfoView.INFO_HIDE);
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
     * 当列表加载开始时触发
     */
    @Override
    public void onPreAction() {
        hideToast(true);
        if (isEmpty()) {
            if (mRoot != null) {
                mRoot.setVisibility(View.GONE);
            }
            if (mInfo != null) {
                mInfo.setInfoVisibility(InfoView.INFO_LOADING);
            }
        } else {
            showToast(mLoadingText, true);
        }
    }

    @Override
    public void onFailure() {
        onError();
    }

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
                    onParseItem(list.get(i));
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

    /**
     * 触发开始加载
     */
    @Override
    public void onLoadStart() {
        if (!isLoading()) {
            nextPage();
            onAction();
        }
    }

    /**
     * 触发开始刷新
     */
    @Override
    public void onRefreshStart() {
        if (!isLoading()) {
            clear();
            refresh();
            onAction();
        }
    }

    /**
     * 列表翻至前一页
     */
    public final void previousPage() {
        if (mCurrentPage > 1) {
            mCurrentPage--;
        }
    }

    /**
     * 设置列表的排序比较器
     *
     * @param comparator
     *         比较器
     */
    public final void setComparator(Comparator<ITEM> comparator) {
        mComparator = comparator;
    }

    /**
     * 设置覆盖层
     *
     * @param info
     *         覆盖层
     */
    public final void setInfoView(InfoView info) {
        mInfo = info;
        if (info != null) {
            info.setOnClickListener(this);
        }
    }

    /**
     * 设置是否启用加载更多功能
     *
     * 当列表滑动至底部时，是否触发加载更多状态，以便自动加载下一页
     *
     * @param loadEnabled
     *         是否启用加载更多功能
     */
    public final void setLoadEnabled(boolean loadEnabled) {
        mLoadEnabled = loadEnabled;
    }

    /**
     * 设置是否反转“加载更多”的触发条件
     *
     * @param reverseScroll
     *         如果为false，则向下滑动至末尾会触发“加载更多”；否则为向上滑动至顶端会触发“加载更多”
     */
    public final void setReverseScroll(boolean reverseScroll) {
        mReverse = reverseScroll;
    }

    /**
     * 设置内容层
     *
     * @param root
     *         内容层
     */
    public final void setRootView(View root) {
        mRoot = root;
    }

    public final void setShowRetryWhenEmpty(boolean showRetryWhenEmpty) {
        if (mInfo != null) {
            mInfo.setShowRetryWhenEmpty(showRetryWhenEmpty);
        }
    }

    /**
     * 设置列表的总页数
     *
     * @param totalPage
     *         总页数
     */
    public final void setTotalPage(int totalPage) {
        mTotalPage = totalPage;
    }

    public final void showToast(String text, boolean progress) {
        mFramework.showToast(text, progress);
    }
}