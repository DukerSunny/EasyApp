package com.harreke.easyapp.frameworks.recyclerview;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.harreke.easyapp.R;
import com.harreke.easyapp.adapters.recyclerview.RecyclerAdapter;
import com.harreke.easyapp.frameworks.bases.IFramework;
import com.harreke.easyapp.helpers.EmptyHelper;
import com.harreke.easyapp.requests.IRequestCallback;
import com.harreke.easyapp.requests.RequestBuilder;
import com.harreke.easyapp.widgets.pullablelayout.PullableLayout;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/12/01
 */
public abstract class RecyclerFramework<ITEM> implements PullableLayout.OnPullableListener {
    private final static int ACTION_LOAD = 2;
    private final static int ACTION_NONE = 0;
    private int mAction = ACTION_NONE;
    private final static int ACTION_REFRESH = 1;
    private RecyclerAdapter<ITEM> mAdapter;
    private Comparator<ITEM> mComparator = null;
    private int mCurrentPage = 1;
    private EmptyHelper mEmptyHelper = null;
    private IFramework mFramework = null;
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            onHandleMessage(message);
            return false;
        }
    });
    private int mLastItemAddedCount = 0;
    private View.OnClickListener mOnEmptyClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onRequestAction();
        }
    };
    private View.OnClickListener mOnItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Object tag = v.getTag();
            int position;

            if (tag != null && tag instanceof Integer) {
                position = (Integer) tag;
            } else {
                position = mRecyclerView.getChildPosition(v);
            }

            onItemClick(position, getItem(position));
        }
    };
    private ParseTask mParseTask = null;
    private IRequestCallback<String> mRequestCallback = new IRequestCallback<String>() {
        @Override
        public void onFailure(String requestUrl) {
            onPostAction(false);
        }

        @Override
        public void onSuccess(String requestUrl, String result) {
            mParseTask = new ParseTask();
            mParseTask.execute(result);
        }
    };
    private PullableLayout mPullableLayout = null;
    private RecyclerView mRecyclerView;
    private boolean mSortReverse = false;

    public RecyclerFramework(IFramework framework) {
        View empty_root;

        mFramework = framework;
        mRecyclerView = (RecyclerView) mFramework.findViewById(getRecyclerViewId());
        setLayoutManager(makeDefaultLayoutManager());
        setItemAnimator(new DefaultItemAnimator());
        setItemDecoration();
        setHasFixedSize(true);
        setCanLoad(true);
        setCanRefresh(true);
        mPullableLayout = (PullableLayout) mFramework.findViewById(getPullableLayoutId());
        if (mPullableLayout != null) {
            mPullableLayout.setOnPullableListener(this);
        }
        empty_root = mFramework.findViewById(R.id.empty_root);
        if (empty_root != null) {
            mEmptyHelper = new EmptyHelper(empty_root);
            mEmptyHelper.setOnClickListener(mOnEmptyClickListener);
        }
    }

    public boolean addItem(ITEM item) {
        return mAdapter.addItem(item);
    }

    public boolean addItem(int position, ITEM item) {
        return mAdapter.addItem(position, item);
    }

    /**
     * 添加一组条目，并通知视图刷新
     *
     * @param itemList
     *         条目列表
     *
     * @return 成功添加的条目数量
     */
    public int addItem(List<ITEM> itemList) {
        return mAdapter.addItem(itemList);
    }

    /**
     * 添加一组条目，并通知视图刷新
     *
     * @param items
     *         条目数组
     *
     * @return 成功添加的条目数量
     */
    public int addItem(ITEM[] items) {
        return mAdapter.addItem(items);
    }

    public void addItemDecoration(RecyclerView.ItemDecoration decoration) {
        mRecyclerView.addItemDecoration(decoration);
    }

    public void attachAdapter() {
        mAdapter = new Adapter();
        mAdapter.setHasStableIds(true);
        mRecyclerView.setAdapter(mAdapter);
    }

    public void attachAdapter(RecyclerAdapter<ITEM> adapter) {
        mAdapter = adapter;
        mRecyclerView.setAdapter(mAdapter);
    }

    /**
     * 停止加载列表
     */
    public final void cancel() {
        cancelParseTask();
        mFramework.cancelRequest();
    }

    private void cancelParseTask() {
        if (mParseTask != null) {
            mParseTask.cancel(true);
        }
    }

    /**
     * 清空列表
     */
    public void clear() {
        cancel();
        mCurrentPage = 1;
        mAdapter.clear();
    }

    protected abstract RecyclerHolder<ITEM> createHolder(View itemView, int viewType);

    protected abstract View createView(LayoutInflater inflater, ViewGroup parent, int viewType);

    /**
     * 销毁框架
     */
    public final void destroy() {
        clear();
        mFramework = null;
    }

    /**
     * 查询条目位置
     *
     * @param hashCode
     *         条目哈希码
     *
     * @return 条目位置
     */
    public int findItem(int hashCode) {
        return mAdapter.findItem(hashCode);
    }

    /**
     * 填充列表视图
     *
     * @param itemList
     *         条目列表
     */
    public final void from(List<ITEM> itemList) {
        from(itemList, false);
    }

    /**
     * 填充列表视图
     *
     * @param itemList
     *         条目列表
     * @param reverse
     *         是否倒转列表顺序
     */
    public final void from(List<ITEM> itemList, boolean reverse) {
        from(itemList, reverse, true);
    }

    private void from(List<ITEM> itemList, boolean reverse, boolean withPreAction) {
        if (itemList != null) {
            if (withPreAction) {
                onPreAction();
            }
            if (reverse) {
                Collections.reverse(itemList);
            }
            mLastItemAddedCount = addItem(itemList);
            if (mComparator != null) {
                sort(mComparator);
            }
            onPostAction(true);
        } else {
            onPostAction(false);
        }
    }

    /**
     * 填充列表视图
     *
     * @param items
     *         条目数组
     */
    public final void from(ITEM[] items) {
        from(items, false);
    }

    /**
     * 填充列表视图
     *
     * @param items
     *         条目数组
     * @param reverse
     *         是否倒转列表顺序
     */
    public final void from(ITEM[] items, boolean reverse) {
        if (items != null) {
            onPreAction();
            if (reverse) {
                Arrays.sort(items, Collections.reverseOrder());
            }
            mLastItemAddedCount = addItem(items);
            if (mComparator != null) {
                sort(mComparator);
            }
            onPostAction(true);
        } else {
            onPostAction(false);
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
     * 执行新的网络请求会取消之前未完成的请求
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
        cancelParseTask();
        mFramework.executeRequest(builder, mRequestCallback);
    }

    public RecyclerAdapter<ITEM> getAdapter() {
        return mAdapter;
    }

    /**
     * 获得列表当前页面序号
     *
     * @return int
     */
    public final int getCurrentPage() {
        return mAction == ACTION_REFRESH ? 1 : mCurrentPage;
    }

    /**
     * 获得条目
     *
     * @param position
     *         条目位置
     *
     * @return 条目对象
     */
    public final ITEM getItem(int position) {
        return mAdapter.getItem(position);
    }

    /**
     * 获得条目总数
     *
     * @return 条目总数
     */
    public int getItemCount() {
        return mAdapter.getItemCount();
    }

    @SuppressWarnings("unchecked")
    public final RecyclerHolder<ITEM> getItemHolder(int position) {
        return (RecyclerHolder<ITEM>) mRecyclerView.findViewHolderForPosition(position);
    }

    protected long getItemId(int position) {
        return mAdapter.getItemId(position);
    }

    public List<ITEM> getItemList() {
        return mAdapter.getItemList();
    }

    public View getItemView(int position) {
        return getItemHolder(position).itemView;
    }

    /**
     * 获得列表上一次加载的条目数量
     *
     * @return 上一次加载的条目数量
     */
    public final int getLastItemAddedCount() {
        return mLastItemAddedCount;
    }

    protected int getPullableLayoutId() {
        return R.id.recycler_pullable;
    }

    protected int getRecyclerViewId() {
        return R.id.recycler_solid;
    }

    public RecyclerView getReyclerView() {
        return mRecyclerView;
    }

    protected int getViewType(int position) {
        return 0;
    }

    public void hideEmpty() {
        if (mEmptyHelper != null) {
            mEmptyHelper.hide();
        }
    }

    /**
     * 判断是否正在访问网络
     *
     * @return 是否正在访问网络
     */
    public boolean isActing() {
        return mParseTask != null || mFramework != null && mFramework.isRequestExecuting();
    }

    public boolean isEmpty() {
        return mAdapter.getItemCount() == 0;
    }

    /**
     * 判断当前网络页面是否为第一页
     *
     * @return boolean
     */
    public final boolean isFirstPage() {
        return mCurrentPage <= 1;
    }

    private RecyclerView.LayoutManager makeDefaultLayoutManager() {
        LinearLayoutManager manager = new LinearLayoutManager(mFramework.getContext());

        manager.setOrientation(LinearLayoutManager.VERTICAL);

        return manager;
    }

    /**
     * 列表翻至下一页
     */
    public final void nextPage() {
        mCurrentPage++;
    }

    /**
     * 处理从背景线程发送至主线程的消息
     *
     * @param message
     *         消息
     */
    protected void onHandleMessage(Message message) {
    }

    public abstract void onItemClick(int position, ITEM item);

    /**
     * 解析从网络加载的json数据，转换成可供操作的列表
     *
     * 注：该函数运行于背景线程，进行解析时不会阻塞主线程
     *
     * @param json
     *         json数据
     *
     * @return 可供操作的列表
     */
    protected abstract List<ITEM> onParse(String json);

    /**
     * 当列表加载完成时触发
     *
     * @param success
     *         加载是否成功
     */
    protected void onPostAction(boolean success) {
        if (mPullableLayout != null) {
            if (mAction == ACTION_REFRESH) {
                if (success) {
                    if (mLastItemAddedCount > 0) {
                        scrollToTop();
                        mPullableLayout.setRefreshComplete();
                    } else {
                        mPullableLayout.setRefreshComplete("没有内容");
                    }
                } else {
                    mPullableLayout.setRefreshComplete("刷新失败");
                }
            } else if (mAction == ACTION_LOAD) {
                if (success) {
                    if (mLastItemAddedCount > 0) {
                        scrollToLoaded();
                        mPullableLayout.setLoadComplete();
                    } else {
                        mPullableLayout.setLoadComplete("没有更多了");
                        previousPage();
                    }
                } else {
                    mPullableLayout.setLoadComplete("加载失败");
                    previousPage();
                }
            }
            mAction = ACTION_NONE;
        }
        if (mEmptyHelper != null) {
            if (isEmpty()) {
                mEmptyHelper.showIdle();
            } else {
                mEmptyHelper.hide();
            }
        }
    }

    /**
     * 当列表加载开始时触发
     */
    protected void onPreAction() {
        mLastItemAddedCount = 0;
        if (mEmptyHelper != null && isEmpty()) {
            mEmptyHelper.showLoading();
        }
    }

    @Override
    public void onPullToLoad() {
        mAction = ACTION_LOAD;
        nextPage();
        onRequestAction();
    }

    @Override
    public void onPullToRefresh() {
        mAction = ACTION_REFRESH;
        onRequestAction();
    }

    protected void onRequestAction() {
        mFramework.startAction();
    }

    /**
     * 从背景线程发送一条消息至主线程
     *
     * @param message
     *         消息
     */
    protected void postMessage(Message message) {
        mHandler.sendMessage(message);
    }

    /**
     * 从背景线程发送一条消息至主线程
     *
     * @param obj
     *         对象消息
     */
    protected void postMessage(Object obj) {
        Message message = mHandler.obtainMessage();

        message.obj = obj;
        message.sendToTarget();
    }

    /**
     * 从背景线程发送一条空消息至主线程
     *
     * @param what
     *         消息类型
     */
    protected void postMessage(int what) {
        Message message = mHandler.obtainMessage();

        message.what = what;
        message.sendToTarget();
    }

    /**
     * 列表翻至前一页
     */
    public void previousPage() {
        if (mCurrentPage > 1) {
            mCurrentPage--;
        }
    }

    /**
     * 移除指定条目，并通知视图刷新
     *
     * @param start
     *         条目起始位置
     * @param end
     *         条目终止位置
     *
     * @return 是否移除成功
     */
    public boolean removeItem(int start, int end) {
        return mAdapter.removeItem(start, end);
    }

    /**
     * 移除一个条目，并通知视图刷新
     *
     * @param item
     *         条目
     *
     * @return 是否移除成功
     */
    public boolean removeItem(ITEM item) {
        return mAdapter.removeItem(item);
    }

    /**
     * 移除一个条目，并通知视图刷新
     *
     * @param position
     *         条目位置
     *
     * @return 是否移除成功
     */
    public boolean removeItem(int position) {
        return mAdapter.removeItem(position);
    }

    public void removeItemDecoration(RecyclerView.ItemDecoration decoration) {
        mRecyclerView.removeItemDecoration(decoration);
    }

    private void scrollToLoaded() {
        RecyclerView.LayoutManager manager = mRecyclerView.getLayoutManager();
        View child = manager.getChildAt(manager.getChildCount() - 1);

        mRecyclerView.smoothScrollBy(0, child.getBottom());
    }

    public void scrollToTop() {
        mRecyclerView.smoothScrollToPosition(0);
    }

    public void setCanLoad(boolean enabled) {
        if (mPullableLayout != null) {
            mPullableLayout.setCanLoad(enabled);
        }
    }

    public void setCanRefresh(boolean enabled) {
        if (mPullableLayout != null) {
            mPullableLayout.setCanRefresh(enabled);
        }
    }

    public void setClickableWhenEmptyIdle(boolean clickableWhenIdle) {
        if (mEmptyHelper != null) {
            mEmptyHelper.setClickableWhenIdle(clickableWhenIdle);
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

    public void setHasFixedSize(boolean hasFixedSize) {
        mRecyclerView.setHasFixedSize(hasFixedSize);
    }

    protected void setItem(RecyclerHolder<ITEM> holder, ITEM item) {
        holder.setItem(item);
    }

    public void setItemAnimator(RecyclerView.ItemAnimator animator) {
        mRecyclerView.setItemAnimator(animator);
    }

    protected void setItemDecoration() {
        addItemDecoration(new DividerItemDecoration(mFramework.getContext(), null));
    }

    public void setLayoutManager(RecyclerView.LayoutManager manager) {
        mRecyclerView.setLayoutManager(manager);
    }

    public void showEmptyIdle() {
        if (mEmptyHelper != null) {
            mEmptyHelper.showIdle();
        }
    }

    public void showEmptyLoading() {
        if (mEmptyHelper != null) {
            mEmptyHelper.showLoading();
        }
    }

    public void setShowRetryWhenEmptyIdle(boolean showRetryWhenIdle) {
        if (mEmptyHelper != null) {
            mEmptyHelper.setShowRetryWhenIdle(showRetryWhenIdle);
        }
    }

    public void sort(Comparator<ITEM> comparator) {
        mAdapter.sort(comparator);
    }

    private class Adapter extends RecyclerAdapter<ITEM> {
        @Override
        public long getItemId(int position) {
            ITEM item = getItem(position);

            return item == null ? RecyclerView.NO_ID : item.hashCode();
        }

        @Override
        public int getItemViewType(int position) {
            return getViewType(position);
        }

        @Override
        public void onBindViewHolder(RecyclerHolder<ITEM> holder, int position) {
            setItem(holder, getItem(position));
        }

        @Override
        public RecyclerHolder<ITEM> onCreateViewHolder(ViewGroup parent, int viewType) {
            View convertView = createView(LayoutInflater.from(parent.getContext()), parent, viewType);
            RecyclerHolder<ITEM> holder = createHolder(convertView, viewType);

            holder.setOnClickListener(mOnItemClickListener);

            return holder;
        }
    }

    private class ParseTask extends AsyncTask<String, Void, List<ITEM>> {
        @Override
        protected List<ITEM> doInBackground(String... params) {
            return onParse(params[0]);
        }

        @Override
        protected void onCancelled() {
            mParseTask = null;
        }

        @Override
        protected void onPostExecute(List<ITEM> itemList) {
            mParseTask = null;
            if (itemList != null) {
                if (mAction == ACTION_REFRESH) {
                    clear();
                }
                from(itemList, mSortReverse, false);
                mSortReverse = false;
            } else {
                onPostAction(false);
            }
        }
    }
}