package com.harreke.easyapp.frameworks.list;

import android.content.Context;
import android.view.View;

import com.harreke.easyapp.R;
import com.harreke.easyapp.frameworks.bases.IFramework;
import com.harreke.easyapp.requests.IRequestCallback;
import com.harreke.easyapp.requests.RequestBuilder;
import com.harreke.easyapp.widgets.InfoView;

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
 * 1.ListView和GridView架构相近（视图单元是一个简单单元），GridView可以直接使用ListView的实现类；
 * 虽然ExpandableListView同样衍生于AbsListView，但是由于架构的不同（视图单元是一个复合单元，由一个父单元和多个子单元复合而成），不能直接使用ListView的实现类，需要另外实现ExpandableListView的方法
 * 其他架构有别于ListView的视图，也需要另外实现对应的方法
 *
 * 2.发起网络请求时，必须先设置网络数据解析器，以便解析识别从网络获得的数据
 *
 * @param <ITEM>
 *         列表条目类型
 *
 *         列表视图单元的类型，是列表的最基本组成
 *
 * @see com.harreke.easyapp.listparsers.IListParser
 */
public abstract class ListFramework<ITEM> implements IList<ITEM>, IListActionListener<ITEM>, IRequestCallback<String>, View.OnClickListener {
    private Comparator<ITEM> mComparator = null;
    private String mCompleteText;
    private int mCurrentPage = 1;
    private String mErrorText;
    private IFramework mFramework;
    private InfoView mInfo = null;
    private String mLastText;
    private boolean mLoadEnabled = false;
    private int mPageSize = 0;
    private boolean mReverseScroll = false;
    private View mRoot = null;
    private boolean mSortReverse = false;
    private int mTotalPage = 1;

    public ListFramework(IFramework framework, int listId) {
        Context context;
        View listView;

        if (framework == null) {
            throw new IllegalArgumentException("Framework must not be null!");
        } else {
            listView = framework.findContentView(listId);
            if (listView == null) {
                throw new IllegalArgumentException("Invalid listId!");
            }
            setListView(listView);
            context = framework.getActivity();
            mErrorText = context.getString(R.string.info_retry);
            mLastText = context.getString(R.string.list_last);
            mCompleteText = context.getString(R.string.list_complete);
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
    }

    /**
     * 清空列表
     */
    @Override
    public void clear() {
        cancel();
        mCurrentPage = 1;
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
     *         条目列表
     */
    public final void from(ArrayList<ITEM> list) {
        from(list, false);
    }

    /**
     * 填充列表视图
     *
     * @param list
     *         条目列表
     * @param reverse
     *         是否倒转列表顺序
     */
    @SuppressWarnings("unchecked")
    public final void from(ArrayList<ITEM> list, boolean reverse) {
        ITEM[] items = null;

        if (list != null) {
            items = (ITEM[]) new Object[list.size()];
            list.toArray(items);
        }
        from(items, reverse);
    }

    /**
     * 填充列表视图
     *
     * @param list
     *         条目列表
     */
    public final void from(ITEM[] list) {
        from(list, false);
    }

    /**
     * 填充列表视图
     *
     * @param items
     *         条目列表
     * @param reverse
     *         是否倒转列表顺序
     */
    public final void from(ITEM[] items, boolean reverse) {
        ITEM item;
        int size;
        int i;

        if (items != null) {
            onPreAction();
            mPageSize = 0;
            size = items.length;
            if (reverse) {
                for (i = size - 1; i > -1; i--) {
                    item = items[i];
                    if (addItem(parseItemId(item), item)) {
                        mPageSize++;
                    }
                }
            } else {
                for (i = 0; i < size; i++) {
                    item = items[i];
                    if (addItem(parseItemId(item), item)) {
                        mPageSize++;
                    }
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
     * 填充列表视图，从网络加载内容
     *
     * @param builder
     *         Http请求
     */
    public final void from(RequestBuilder builder) {
        from(builder, false);
    }

    /**
     * 填充列表视图，从网络加载内容
     *
     * @param builder
     *         Http请求
     * @param reverse
     *         是否倒转列表顺序
     */
    public final void from(RequestBuilder builder, boolean reverse) {
        if (builder == null) {
            throw new IllegalArgumentException("Request builder must not be null!");
        }
        onPreAction();
        mSortReverse = reverse;
        mFramework.executeRequest(builder, this);
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
     * 获得列表上一次加载的条目数量
     *
     * @return 上一次加载的条目数量
     */
    public final int getPageSize() {
        return mPageSize;
    }

    private void hideToast(boolean animate) {
        mFramework.hideToast(animate);
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
     * 判断是否启用加载更多功能
     *
     * @return 是否启用加载更多功能
     */
    public final boolean isLoadEnabled() {
        return mLoadEnabled;
    }

    /**
     * 判断是否正在访问网络
     *
     * @return 是否正在访问网络
     */
    public boolean isLoading() {
        return mFramework != null && mFramework.isRequestExecuting();
    }

    /**
     * 判断是否反转“加载更多”的触发条件
     *
     * @return 如果为false，则向下滑动至末尾会触发“加载更多”；否则为向上滑动至顶端会触发“加载更多”
     */
    public final boolean isReverseScroll() {
        return mReverseScroll;
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
        clear();
        onAction();
    }

    /**
     * 当列表加载错误时触发
     */
    @Override
    public void onError() {
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

    @Override
    public void onFailure() {
        onError();
    }

    /**
     * 当列表加载完成时触发
     */
    @Override
    public void onPostAction() {
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
            if (mCurrentPage > 1 && isLastPage()) {
                showToast(mLastText);
            }
        }
    }

    /**
     * 当列表加载开始时触发
     */
    @Override
    public void onPreAction() {
        if (isEmpty()) {
            if (mRoot != null) {
                mRoot.setVisibility(View.GONE);
            }
            if (mInfo != null) {
                mInfo.setInfoVisibility(InfoView.INFO_LOADING);
            }
        }
    }

    @Override
    public void onSuccess(String result) {
        ArrayList<ITEM> list = onParse(result);

        if (list != null) {
            if (mSortReverse) {
                from(list, true);
                mSortReverse = false;
            } else {
                from(list);
            }
        } else {
            onError();
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
        mReverseScroll = reverseScroll;
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

    /**
     * 设置“空内容”时是否显示“重试”按钮
     *
     * @param showRetryHintWhenEmpty
     *         “空内容”时是否需要显示“重试”按钮
     */
    public final void setShowRetryHintWhenEmpty(boolean showRetryHintWhenEmpty) {
        if (mInfo != null) {
            mInfo.setShowRetryWhenEmpty(showRetryHintWhenEmpty);
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

    private void showToast(String text) {
        mFramework.showToast(text);
    }
}