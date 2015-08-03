package com.harreke.easyapp.frameworks.recyclerview;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.harreke.easyapp.R;
import com.harreke.easyapp.frameworks.base.ApplicationFramework;
import com.harreke.easyapp.frameworks.base.IFramework;
import com.harreke.easyapp.helpers.EmptyHelper;
import com.harreke.easyapp.helpers.LoaderHelper;
import com.harreke.easyapp.parsers.IListParser;
import com.harreke.easyapp.parsers.IObjectParser;
import com.harreke.easyapp.parsers.ListResult;
import com.harreke.easyapp.parsers.ObjectResult;
import com.harreke.easyapp.requests.IRequestCallback;
import com.harreke.easyapp.requests.IRequestExecutor;
import com.harreke.easyapp.requests.RequestBuilder;
import com.harreke.easyapp.requests.executors.StringExecutor;
import com.harreke.easyapp.utils.LogUtil;
import com.harreke.easyapp.utils.MathUtil;
import com.harreke.easyapp.widgets.pullablelayout.OnPullableListener;
import com.harreke.easyapp.widgets.pullablelayout.PullableLayout;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/12/01
 */
public abstract class RecyclerFramework<ITEM> implements OnPullableListener {
    private int mAction = PullableLayout.ACTION_IDLE;
    private RecyclerAdapter<ITEM> mAdapter = null;
    private boolean mAutoLoadMore = false;
    private Comparator<ITEM> mComparator = null;
    private int mCurrentPage = 1;
    private EmptyHelper mEmptyHelper = null;
    private WeakReference<IFramework> mFrameworkRef = null;
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            onHandleMessage(message);
            return false;
        }
    });
    private boolean mInterruptWhenFailure = false;
    private int mLastItemAddedCount = 0;
    private int mLastScrollState = RecyclerView.SCROLL_STATE_IDLE;
    private IListParser<ITEM> mListParser = null;
    private View.OnClickListener mOnEmptyClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onRequestAction();
        }
    };
    private PairParseTask mPairParseTask = null;
    private ParseTask mParseTask = null;
    private View mPressedChild = null;
    private float mPressedX = 0f;
    private float mPressedY = 0f;
    private PullableLayout mPullableLayout = null;
    private RecyclerView mRecyclerView;
    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            RecyclerHolder<ITEM> holder;

            if (mAutoLoadMore && mAdapter != null && getItemCount() > 0 && newState == RecyclerView.SCROLL_STATE_IDLE &&
                    mLastScrollState != newState) {
                holder = getItemHolder(getItemCount() - 1);
                if (holder != null && holder.itemView != null && holder.itemView.getBottom() >= mRecyclerView.getBottom()) {
                    mAction = PullableLayout.ACTION_LOAD;
                    setLoadStart();
                }
            }
            mLastScrollState = newState;
        }
    };
    private StringExecutor mRequestExecutor = null;
    private IRequestCallback<String> mRequestCallback = new IRequestCallback<String>() {
        @Override
        public void onFailure(IRequestExecutor executor, String requestUrl) {
            onPostAction(null, false);
        }

        @Override
        public void onSuccess(IRequestExecutor executor, String requestUrl, String result) {
            mParseTask = new ParseTask();
            mParseTask.execute(result);
        }
    };
    private int mTouchThreshold = (int) ApplicationFramework.TouchThreshold;
    private RecyclerView.OnItemTouchListener mOnItemTouchListener = new RecyclerView.OnItemTouchListener() {
        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            int position;

            if (mAction == PullableLayout.ACTION_REFRESH) {
                return true;
            } else {
                switch (e.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mPressedX = e.getX();
                        mPressedY = e.getY();
                        mPressedChild = findChildView(rv, mPressedX, mPressedY);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (MathUtil.getDistance(e.getX(), e.getY(), mPressedX, mPressedY) > mTouchThreshold) {
                            mPressedChild = null;
                        }
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        mPressedChild = null;
                        break;
                    case MotionEvent.ACTION_UP:
                        if (mPressedChild != null) {
                            position = getChildPosition(rv, mPressedChild);
                            mPressedChild = null;
                            onItemClick(position, getItem(position));
                        }
                        break;
                }

                return false;
            }
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean b) {
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }
    };

    public RecyclerFramework(IFramework framework) {
        View empty_root;

        mFrameworkRef = new WeakReference<>(framework);
        mRecyclerView = (RecyclerView) framework.findViewById(getRecyclerViewId());
        mRecyclerView.addOnItemTouchListener(mOnItemTouchListener);
        mRecyclerView.setOnScrollListener(mOnScrollListener);
        setLayoutManager(makeDefaultLayoutManager(framework));
        setItemAnimator(new DefaultItemAnimator());
        setHasFixedSize(true);
        setCanLoad(true);
        setCanRefresh(true);
        mPullableLayout = (PullableLayout) framework.findViewById(getPullableLayoutId());
        if (mPullableLayout != null) {
            mPullableLayout.setOnPullableListener(this);
        }
        empty_root = framework.findViewById(getEmptyLayoutId());
        if (empty_root != null) {
            mEmptyHelper = new EmptyHelper(framework, getEmptyLayoutId());
            mEmptyHelper.showEmptyIdle();
            mEmptyHelper.setOnClickListener(mOnEmptyClickListener);
        }
    }

    public void addItem(ITEM item) {
        if (mAdapter != null) {
            mAdapter.addItem(item);
        }
    }

    /**
     * 添加一组条目，并通知视图刷新
     *
     * @param itemList 条目列表
     */
    public void addItem(List<ITEM> itemList) {
        if (mAdapter != null) {
            mAdapter.addItem(itemList);
        }
    }

    /**
     * 添加一组条目，并通知视图刷新
     *
     * @param items 条目数组
     */
    public void addItem(ITEM[] items) {
        if (mAdapter != null) {
            mAdapter.addItem(items);
        }
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
        cancelPairParseTask();
        cancelParseTask();
        cancelExecutor();
    }

    private void cancelExecutor() {
        if (mRequestExecutor != null && mRequestExecutor.isExecuting()) {
            mRequestExecutor.cancel();
            mRequestExecutor = null;
        }
    }

    private void cancelPairParseTask() {
        if (mPairParseTask != null) {
            mPairParseTask.cancel();
            mPairParseTask = null;
        }
    }

    private void cancelParseTask() {
        if (mParseTask != null) {
            mParseTask.cancel(true);
            mParseTask = null;
        }
    }

    /**
     * 清空列表
     */
    public void clear() {
        cancel();
        mCurrentPage = 1;
        if (mAdapter != null) {
            mAdapter.clear();
        }
    }

    protected abstract RecyclerHolder<ITEM> createHolder(View itemView, int viewType);

    protected abstract View createView(LayoutInflater inflater, ViewGroup parent, int viewType);

    protected View findChildView(RecyclerView rv, float x, float y) {
        return rv.findChildViewUnder(x, y);
    }

    /**
     * 查询条目位置
     *
     * @param hashCode 条目哈希码
     * @return 条目位置
     */
    public int findItem(int hashCode) {
        return mAdapter == null ? -1 : mAdapter.findItem(hashCode);
    }

    /**
     * 填充列表视图
     *
     * @param itemList 条目列表
     */
    public final void from(@Nullable List<ITEM> itemList) {
        if (itemList != null) {
            //                Collections.sort(itemList, mComparator);
            addItem(itemList);
            sortAll(mComparator);
            mLastItemAddedCount = itemList.size();
            onPostAction(null, true);
        } else {
            onPostAction(null, false);
        }
    }

    /**
     * 填充列表视图
     *
     * @param items 条目数组
     */
    @SafeVarargs
    public final void from(@Nullable ITEM... items) {
        if (items != null) {
            from(Arrays.asList(items));
        } else {
            onPostAction(null, false);
        }
    }

    /**
     * 填充列表视图，从网络加载内容
     * <p>
     * 执行新的网络请求会取消之前未完成的请求
     *
     * @param requestBuilder Http请求构造器
     */
    public final void from(@NonNull RequestBuilder requestBuilder) {
        onPreAction();
        cancel();
        mRequestExecutor = LoaderHelper.makeStringExecutor();
        mRequestExecutor.request(requestBuilder).execute(getFramework(), mRequestCallback);
    }

    @SafeVarargs
    public final void from(@NonNull RequestPair<ITEM>... requestPairs) {
        from(false, requestPairs);
    }

    @SafeVarargs
    public final void from(boolean interruptWhenFailure, @NonNull RequestPair<ITEM>... requestPairs) {
        onPreAction();
        cancel();
        mInterruptWhenFailure = interruptWhenFailure;
        mPairParseTask = new PairParseTask();
        mPairParseTask.execute(requestPairs);
    }

    public RecyclerAdapter<ITEM> getAdapter() {
        return mAdapter;
    }

    protected int getChildPosition(RecyclerView rv, View child) {
        return rv.getChildPosition(child);
    }

    /**
     * 获得列表当前页面序号
     *
     * @return int
     */
    public final int getCurrentPage() {
        return mAction == PullableLayout.ACTION_REFRESH ? 1 : mCurrentPage;
    }

    protected int getEmptyLayoutId() {
        return R.id.empty_root;
    }

    public IFramework getFramework() {
        return mFrameworkRef == null ? null : mFrameworkRef.get();
    }

    /**
     * 获得条目
     *
     * @param position 条目位置
     * @return 条目对象
     */
    public final ITEM getItem(int position) {
        return mAdapter == null ? null : mAdapter.getItem(position);
    }

    /**
     * 获得条目总数
     *
     * @return 条目总数
     */
    public int getItemCount() {
        return mAdapter == null ? 0 : mAdapter.getItemCount();
    }

    @SuppressWarnings("unchecked")
    public final RecyclerHolder<ITEM> getItemHolder(int position) {
        return (RecyclerHolder<ITEM>) mRecyclerView.findViewHolderForPosition(position);
    }

    protected long getItemId(int position) {
        return mAdapter == null ? 0 : mAdapter.getItemId(position);
    }

    public List<ITEM> getItemList() {
        return mAdapter == null ? null : mAdapter.getItemList();
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
            if (mPullableLayout != null) {
                mPullableLayout.setPullableEnabled(true);
            }
        }
    }

    public boolean isEmpty() {
        return mAdapter == null || mAdapter.getItemCount() == 0;
    }

    /**
     * 判断当前网络页面是否为第一页
     *
     * @return boolean
     */
    public final boolean isFirstPage() {
        return mCurrentPage <= 1;
    }

    protected void loadImage(IFramework framework, RecyclerHolder<ITEM> holder, ITEM item) {
        holder.loadImage(framework, item);
    }

    private RecyclerView.LayoutManager makeDefaultLayoutManager(IFramework framework) {
        LinearLayoutManager manager = new LinearLayoutManager(framework.getContext());

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
     * 处理从后台线程发送至主线程的消息
     *
     * @param message 消息
     */
    protected void onHandleMessage(Message message) {
    }

    public abstract void onItemClick(int position, ITEM item);

    /**
     * 解析从网络加载的json数据，转换成可供操作的列表
     * <p>
     * 注：
     * 该函数运行于后台线程，进行解析时不会阻塞主线程
     * 在加载网络数据之前，必须设置一个解析器
     *
     * @param json json数据
     * @return 可供操作的列表
     * @see com.harreke.easyapp.parsers.ListResult
     */
    protected ListResult<ITEM> onParse(String json) {
        if (mListParser == null) {
            throw new IllegalStateException("Cannot find list parser for network data!");
        } else {
            return mListParser.parse(json);
        }
    }

    /**
     * 当列表加载完成时触发
     *
     * @param message 加载结果信息
     * @param success 加载是否成功
     */
    protected void onPostAction(String message, boolean success) {
        mRequestExecutor = null;
        if (mPullableLayout != null) {
            if (mAction == PullableLayout.ACTION_REFRESH) {
                if (success && mLastItemAddedCount > 0) {
                    scrollToTop();
                }
                mPullableLayout.setRefreshComplete();
            } else if (mAction == PullableLayout.ACTION_LOAD) {
                if (success && mLastItemAddedCount > 0) {
                    scrollToLoaded();
                } else {
                    previousPage();
                }
                mPullableLayout.setLoadComplete();
            }
            mAction = PullableLayout.ACTION_IDLE;
        }
        if (mEmptyHelper != null) {
            if (isEmpty()) {
                if (success) {
                    showEmptyIdle();
                } else {
                    showEmptyFailureIdle(message);
                }
            } else {
                hideEmpty();
            }
        }
    }

    /**
     * 当列表加载开始时触发
     */
    protected void onPreAction() {
        mLastItemAddedCount = 0;
        if (mEmptyHelper != null && isEmpty()) {
            showEmptyLoading();
        }
    }

    @Override
    public void onPullToLoad() {
        mAction = PullableLayout.ACTION_LOAD;
        nextPage();
        onRequestAction();
    }

    @Override
    public void onPullToRefresh() {
        mAction = PullableLayout.ACTION_REFRESH;
        onRequestAction();
    }

    protected void onRequestAction() {
        IFramework framework = getFramework();

        if (framework != null) {
            framework.startAction();
        }
    }

    /**
     * 从后台线程发送一条消息至主线程
     *
     * @param message 消息
     */
    protected void postMessage(Message message) {
        mHandler.sendMessage(message);
    }

    /**
     * 从后台线程发送一条消息至主线程
     *
     * @param obj 对象消息
     */
    protected void postMessage(Object obj) {
        Message message = mHandler.obtainMessage();

        message.obj = obj;
        message.sendToTarget();
    }

    /**
     * 从线程发送一条空消息至主线程
     *
     * @param what 消息类型
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

    public void refresh(int position) {
        if (position >= 0 && position < getItemCount() && mAdapter != null) {
            mAdapter.notifyItemChanged(position);
        }
    }

    public void refreshAll() {
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 销毁框架
     */
    public final void release() {
        clear();
        if (mFrameworkRef != null) {
            mFrameworkRef.clear();
        }
    }

    /**
     * 移除指定条目，并通知视图刷新
     *
     * @param start 条目起始位置
     * @param end   条目终止位置
     */
    public void removeItem(int start, int end) {
        if (mAdapter != null) {
            mAdapter.removeItem(start, end);
        }
    }

    /**
     * 移除一个条目，并通知视图刷新
     *
     * @param item 条目
     */
    public void removeItem(ITEM item) {
        if (mAdapter != null) {
            mAdapter.removeItem(item);
        }
    }

    /**
     * 移除一个条目，并通知视图刷新
     *
     * @param position 条目位置
     */
    public void removeItem(int position) {
        if (mAdapter != null) {
            mAdapter.removeItem(position);
        }
    }

    public void removeItemDecoration(RecyclerView.ItemDecoration decoration) {
        mRecyclerView.removeItemDecoration(decoration);
    }

    /**
     * 替换条目
     *
     * @param position 条目位置
     * @param item     新条目对象
     */
    public final void replaceItem(int position, ITEM item) {
        if (mAdapter != null) {
            mAdapter.replaceItem(position, item);
        }
    }

    public void scrollToBottom() {
        mRecyclerView.smoothScrollToPosition(getItemCount() - 1);
    }

    private void scrollToLoaded() {
        mRecyclerView.smoothScrollBy(0, (int) (ApplicationFramework.Density * 32));
    }

    public void scrollToTop() {
        mRecyclerView.smoothScrollToPosition(0);
    }

    public void setAutoLoadMore(boolean autoLoadMore) {
        mAutoLoadMore = autoLoadMore;
        setCanLoad(!mAutoLoadMore);
    }

    public void setCanLoad(boolean enabled) {
        if (mPullableLayout != null) {
            mPullableLayout.setLoadable(enabled);
        }
    }

    public void setCanRefresh(boolean enabled) {
        if (mPullableLayout != null) {
            mPullableLayout.setRefreshable(enabled);
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
     * @param comparator 比较器
     */
    public final void setComparator(Comparator<ITEM> comparator) {
        mComparator = comparator;
    }

    public void setHasFixedSize(boolean hasFixedSize) {
        mRecyclerView.setHasFixedSize(hasFixedSize);
    }

    protected void setItem(RecyclerHolder<ITEM> holder, ITEM item) {
        holder.setItem(item);
        holder.itemView.setTag(item.hashCode());
    }

    public void setItemAnimator(RecyclerView.ItemAnimator animator) {
        mRecyclerView.setItemAnimator(animator);
    }

    public void setItemDecoration(@NonNull RecyclerView.ItemDecoration... decorations) {
        int i;

        for (i = 0; i < decorations.length; i++) {
            addItemDecoration(decorations[i]);
        }
    }

    public void setLayoutManager(RecyclerView.LayoutManager manager) {
        mRecyclerView.setLayoutManager(manager);
    }

    /**
     * 设置网络列表解析器
     *
     * @param listParser 列表解析器
     * @see com.harreke.easyapp.parsers.ListResult
     */
    public void setListParser(IListParser<ITEM> listParser) {
        mListParser = listParser;
    }

    public void setLoadStart() {
        if (mPullableLayout != null) {
            mPullableLayout.setLoadStart();
        }
    }

    public void setRefreshStart() {
        if (mPullableLayout != null) {
            mPullableLayout.setRefreshStart();
        }
    }

    public void setShowRetryWhenEmptyIdle(boolean showRetryWhenIdle) {
        if (mEmptyHelper != null) {
            mEmptyHelper.setShowRetryWhenIdle(showRetryWhenIdle);
        }
    }

    public void showEmptyFailureIdle(String message) {
        if (mEmptyHelper != null) {
            mEmptyHelper.showEmptyFailureIdle(message);
            if (mPullableLayout != null) {
                mPullableLayout.setPullableEnabled(false);
            }
        }
    }

    public void showEmptyFailureIdle() {
        showEmptyFailureIdle(null);
    }

    public void showEmptyIdle() {
        if (mEmptyHelper != null) {
            mEmptyHelper.showEmptyIdle();
            if (mPullableLayout != null) {
                mPullableLayout.setPullableEnabled(false);
            }
        }
    }

    public void showEmptyLoading() {
        if (mEmptyHelper != null) {
            mEmptyHelper.showLoading();
            if (mPullableLayout != null) {
                mPullableLayout.setPullableEnabled(false);
            }
        }
    }

    public void sortAll(Comparator<ITEM> comparator) {
        if (mAdapter != null) {
            mAdapter.sort(comparator);
        }
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
            IFramework framework = getFramework();
            ITEM item;

            if (framework != null) {
                item = getItem(position);
                loadImage(framework, holder, item);
                setItem(holder, item);
            }
        }

        @Override
        public RecyclerHolder<ITEM> onCreateViewHolder(ViewGroup parent, int viewType) {
            View convertView = createView(LayoutInflater.from(parent.getContext()), parent, viewType);
            convertView.setClickable(true);

            return createHolder(convertView, viewType);
        }
    }

    private class PairParseTask implements IRequestCallback<String> {
        private ListResult<ITEM> mFinalResult;
        private int mIndex;
        private RequestPair<ITEM>[] mRequestPairs;
        private StringExecutor mStringExecutor;

        public void cancel() {
            if (mStringExecutor != null) {
                mStringExecutor.cancel();
                mStringExecutor = null;
            }
            mFinalResult = null;
            mRequestPairs = null;
        }

        private void execute() {
            RequestPair<ITEM> requestPair = mRequestPairs[mIndex];
            IFramework framework = getFramework();

            if (requestPair != null && framework != null) {
                mStringExecutor = LoaderHelper.makeStringExecutor();
                mStringExecutor.request(requestPair.requestBuilder).execute(framework, this);
            } else {
                setFailure(null);
            }
        }

        @SafeVarargs
        public final void execute(@NonNull RequestPair<ITEM>... requestPairs) {
            cancel();
            mRequestPairs = requestPairs;
            mIndex = 0;
            mFinalResult = new ListResult<>();
            mFinalResult.setList(new ArrayList<ITEM>());
            execute();
        }

        private void next() {
            if (mIndex < mRequestPairs.length - 1) {
                mIndex++;
                execute();
            } else {
                onPostExecute();
            }
        }

        @Override
        public void onFailure(IRequestExecutor executor, String requestUrl) {
            setFailure(null);
        }

        private void onPostExecute() {
            List<ITEM> itemList;

            mPairParseTask = null;
            if (mAction == PullableLayout.ACTION_REFRESH) {
                clear();
            }
            itemList = mFinalResult.getList();
            if (itemList != null) {
                from(itemList);
                onPostAction(null, true);
            } else {
                onPostAction(mFinalResult.getMessage(), false);
            }
            mFinalResult = null;
        }

        @Override
        public void onSuccess(IRequestExecutor executor, String requestUrl, String result) {
            RequestPair<ITEM> requestPair = mRequestPairs[mIndex];
            IListParser<ITEM> listParser = requestPair.listParser;
            IObjectParser<ITEM> objectParser = requestPair.objectParser;
            ListResult<ITEM> listResult;
            ObjectResult<ITEM> objectResult;
            List<ITEM> itemList = mFinalResult.getList();

            if (listParser != null) {
                listResult = listParser.parse(result);
                if (listResult.getList() != null) {
                    itemList.addAll(listResult.getList());
                    next();
                } else {
                    setFailure(listResult.getMessage());
                }
            } else {
                objectResult = objectParser.parse(result);
                if (objectResult.getObject() != null) {
                    itemList.add(objectResult.getObject());
                    next();
                } else {
                    setFailure(objectResult.getMessage());
                }
            }
        }

        private void setFailure(String message) {
            if (mInterruptWhenFailure) {
                mFinalResult.setList(null);
                mFinalResult.setMessage(message);
                onPostExecute();
            } else {
                next();
            }
        }
    }

    private class ParseTask extends AsyncTask<String, Void, ListResult<ITEM>> {
        @Override
        protected ListResult<ITEM> doInBackground(String... params) {
            return onParse(params[0]);
        }

        @Override
        protected void onCancelled() {
            mParseTask = null;
        }

        @Override
        protected void onPostExecute(ListResult<ITEM> listResult) {
            List<ITEM> itemList;

            mParseTask = null;
            if (mAction == PullableLayout.ACTION_REFRESH) {
                clear();
            }
            itemList = listResult.getList();
            if (itemList != null) {
                from(itemList);
                onPostAction(null, true);
            } else {
                onPostAction(listResult.getMessage(), false);
            }
        }
    }
}