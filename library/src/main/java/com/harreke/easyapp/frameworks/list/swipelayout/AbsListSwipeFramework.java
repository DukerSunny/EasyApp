package com.harreke.easyapp.frameworks.list.swipelayout;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.implments.SwipeItemMangerImpl;
import com.harreke.easyapp.adapters.swipelayout.AbsListSwipeAdapter;
import com.harreke.easyapp.frameworks.bases.IFramework;
import com.harreke.easyapp.frameworks.list.ListFramework;
import com.harreke.easyapp.frameworks.list.abslistview.IAbsListItemClickListener;
import com.harreke.easyapp.holders.abslistview.IAbsListHolder;

import java.util.Comparator;
import java.util.List;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/10/27
 *
 * AbsListView框架
 *
 * @param <ITEM>
 *         条目类型
 * @param <HOLDER>
 *         条目容器类型
 */
public abstract class AbsListSwipeFramework<ITEM, HOLDER extends IAbsListHolder<ITEM>> extends ListFramework<ITEM>
        implements IAbsListSwipe<ITEM, HOLDER>, IAbsListItemClickListener<ITEM>, AdapterView.OnClickListener,
        AdapterView.OnItemClickListener, SwipeLayout.SwipeListener {
    private Adapter mAdapter;
    private int mHeaderCount = 0;
    private AbsListView mListView;
    private SwipeLayout.ShowMode mShowMode = SwipeLayout.ShowMode.PullOut;
    private int mSwipeLayoutId;

    public AbsListSwipeFramework(IFramework framework, int listId, int swipeLayoutId) {
        super(framework, listId);
        mSwipeLayoutId = swipeLayoutId;
    }

    public final void addFooterView(View view) {
        if (mListView instanceof ListView) {
            ((ListView) mListView).addFooterView(view);
        }
    }

    public final void addHeaderView(View view) {
        if (mListView instanceof ListView) {
            ((ListView) mListView).addHeaderView(view);
            mHeaderCount++;
        }
    }

    /**
     * 添加一个条目
     *
     * @param itemId
     *         条目Id，大于等于0，用于检测是否有重复条目
     *         若为-1，则不检测重复条目
     * @param item
     *         条目对象
     *
     * @return 如果添加成功，返回true，否则返回false
     */
    @Override
    public final boolean addItem(int itemId, ITEM item) {
        return mAdapter.addItem(itemId, item);
    }

    /**
     * 设置数据适配器
     */
    @Override
    public void bindAdapter() {
        mAdapter = new Adapter();
        //                mAdapter.setMode(SwipeItemMangerImpl.Mode.Single);
        mListView.setAdapter(mAdapter);
    }

    /**
     * 清空列表
     */
    @Override
    public void clear() {
        super.clear();
        mAdapter.clear();
    }

    public final void close(int position) {
        if (position >= 0 && position < mAdapter.getCount()) {
            mAdapter.closeItem(position);
        }
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
    @Override
    public int getItemCount() {
        return mAdapter.getCount();
    }

    @Override
    public List<ITEM> getItemList() {
        return mAdapter.getItemList();
    }

    @Override
    public AbsListView getListView() {
        return mListView;
    }

    /**
     * 判断列表是否为空
     *
     * @return boolean
     */
    @Override
    public boolean isEmpty() {
        return mAdapter.isEmpty();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position < mHeaderCount) {
            onItemClick(position, null);
        } else {
            onItemClick(position, mAdapter.getItem(position - mHeaderCount));
        }
    }

    /**
     * 刷新列表
     */
    @Override
    public void refresh() {
        mAdapter.refresh();
    }

    @Override
    public boolean removeItem(int position) {
        ITEM item = getItem(position);

        return item != null && mAdapter.removeItem(parseItemId(item), item);
    }

    @Override
    public boolean removeItem(ITEM item) {
        return item != null && mAdapter.removeItem(parseItemId(item), item);
    }

    @Override
    public void scrollToTop() {
        mListView.smoothScrollToPositionFromTop(0, 0, 0);
    }

    public void setEnabled(boolean enabled) {
        mAdapter.setEnabled(enabled);
    }

    /**
     * 设置列表视图
     *
     * @param listView
     *         列表视图
     */
    @Override
    public void setListView(AbsListView listView) {
        mListView = listView;
        mListView.setOnItemClickListener(this);
    }

    public void setMode(SwipeItemMangerImpl.Mode mode) {
        mAdapter.setMode(mode);
    }

    /**
     * 设置选中条目
     *
     * @param position
     *         条目位置
     */
    public final void setSelection(int position) {
        if (position >= 0 && position < mAdapter.getCount()) {
            mListView.setSelection(position);
        }
    }

    public final void setShowMode(SwipeLayout.ShowMode showMode) {
        mShowMode = showMode;
    }

    /**
     * 排序列表条目
     *
     * @param comparator
     *         比较器
     */
    @Override
    public void sort(Comparator<ITEM> comparator) {
        mAdapter.sort(comparator);
    }

    private class Adapter extends AbsListSwipeAdapter<ITEM> {
        @SuppressWarnings("unchecked")
        @Override
        public void fillValues(int position, View convertView) {
            HOLDER holder = (HOLDER) convertView.getTag();

            holder.setItem(position, getItem(position));
        }

        @Override
        public View generateView(int position, ViewGroup parent) {
            SwipeLayout convertView = (SwipeLayout) createView();
            HOLDER holder = createHolder(convertView);

            convertView.addSwipeListener(AbsListSwipeFramework.this);
            convertView.setShowMode(mShowMode);
            convertView.setTag(holder);

            return convertView;
        }

        @Override
        public int getSwipeLayoutResourceId(int i) {
            return mSwipeLayoutId;
        }
    }
}