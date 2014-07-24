package com.harreke.easyappframework.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.TextView;

import com.harreke.easyappframework.R;
import com.harreke.easyappframework.listeners.OnListSettingChangeListener;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 *
 * 列表设置视图
 *
 * 列表设置视图包含有一个ListView，可以通过点击ListView里的项目来改变列表设置视图的选择状态
 */
public class ListSettingView extends LinearLayout implements View.OnClickListener, AdapterView.OnItemClickListener {
    private TextView listsetting_list;
    private TextView listsetting_summary;
    private Adapter mAdapter;
    private int mCheckedPosition;
    private CharSequence[] mEntry;
    private OnListSettingChangeListener mListSettingChangeListener = null;
    private int mListTextColor;
    private int mListTextPadding;
    private int mListTextSize;
    private ListPopupWindow mPopupList;

    public ListSettingView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.listSettingViewStyle);
    }

    public ListSettingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray style;
        DisplayMetrics metrics;
        LayoutParams params;
        LinearLayout layout;
        LinearLayout buttonLayout;
        TextView textView;
        ImageView buttonView;
        int popupListHeight;
        int popupListWidth;
        int buttonGravity;
        int buttonPadding;
        String summary;
        int summaryColor;
        int summarySize;
        String text;
        int textColor;
        int textSize;
        Drawable thumb;
        int thumbSize;

        style = context.obtainStyledAttributes(attrs, R.styleable.ListSettingView, defStyle, 0);
        buttonGravity = style.getInt(R.styleable.ListSettingView_buttonGravity, 0);
        buttonPadding = (int) style.getDimension(R.styleable.ListSettingView_buttonPadding, 0);
        mEntry = style.getTextArray(R.styleable.ListSettingView_entry);
        mListTextColor = style.getColor(R.styleable.ListSettingView_listTextColor, 0);
        mListTextSize = (int) style.getDimension(R.styleable.ListSettingView_listTextSize, 0);
        mListTextPadding = (int) style.getDimension(R.styleable.ListSettingView_listTextPadding, 0);
        summary = style.getString(R.styleable.ListSettingView_summary);
        summaryColor = style.getColor(R.styleable.ListSettingView_summaryColor, 0);
        summarySize = (int) style.getDimension(R.styleable.ListSettingView_summarySize, 0);
        text = style.getString(R.styleable.ListSettingView_text);
        textColor = style.getColor(R.styleable.ListSettingView_textColor, 0);
        textSize = (int) style.getDimension(R.styleable.ListSettingView_textSize, 0);
        thumb = style.getDrawable(R.styleable.ListSettingView_thumb);
        thumbSize = (int) style.getDimension(R.styleable.ListSettingView_thumbSize, 0);
        style.recycle();

        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);

        if (mEntry == null) {
            mEntry = new CharSequence[]{};
        }

        layout = new LinearLayout(context);
        layout.setOrientation(VERTICAL);
        layout.setLayoutParams(new LayoutParams(-1, -2, 1));

        textView = new TextView(context);
        textView.setLayoutParams(new LayoutParams(-1, -2));
        textView.setText(text);
        textView.setTextColor(textColor);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        layout.addView(textView);

        listsetting_summary = new TextView(context);
        listsetting_summary.setLayoutParams(new LayoutParams(-1, -2));
        setSummary(summary);
        listsetting_summary.setTextColor(summaryColor);
        listsetting_summary.setTextSize(TypedValue.COMPLEX_UNIT_PX, summarySize);
        layout.addView(listsetting_summary);

        buttonLayout = new LinearLayout(context);
        buttonLayout.setOrientation(HORIZONTAL);
        buttonLayout.setGravity(Gravity.CENTER);
        buttonLayout.setLayoutParams(new LayoutParams(-2, -2));

        listsetting_list = new TextView(context);
        listsetting_list.setLayoutParams(new LayoutParams(-1, -2));
        listsetting_list.setLines(1);
        listsetting_list.setEllipsize(TextUtils.TruncateAt.END);
        listsetting_list.setTextColor(mListTextColor);
        listsetting_list.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        buttonLayout.addView(listsetting_list);

        buttonView = new ImageView(context);
        buttonView.setLayoutParams(new LayoutParams(thumbSize, thumbSize));
        buttonView.setImageDrawable(thumb);
        buttonLayout.addView(buttonView);

        if (buttonGravity == 0) {
            addView(buttonLayout);
            params = (LayoutParams) layout.getLayoutParams();
            params.leftMargin = buttonPadding;
            layout.setLayoutParams(params);
            addView(layout);
        } else {
            addView(layout);
            params = (LayoutParams) buttonLayout.getLayoutParams();
            params.leftMargin = buttonPadding;
            buttonLayout.setLayoutParams(params);
            addView(buttonLayout);
        }

        buttonLayout.setOnClickListener(this);

        mPopupList = new ListPopupWindow(context);
        metrics = context.getResources().getDisplayMetrics();
        popupListWidth = metrics.widthPixels;
        popupListHeight = metrics.heightPixels;
        if (popupListWidth > popupListHeight) {
            mPopupList.setWidth(popupListHeight / 2);
        } else {
            mPopupList.setWidth(popupListWidth / 2);
        }
        mPopupList.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupList.setAnchorView(buttonLayout);
        mPopupList.setModal(true);
        mAdapter = new Adapter();
        mPopupList.setAdapter(mAdapter);
        mPopupList.setOnItemClickListener(this);

        mCheckedPosition = 0;
        update(mCheckedPosition);
    }

    /**
     * 选中指定项目
     *
     * @param position
     *         指定项目的位置
     */
    public final void check(int position) {
        mCheckedPosition = position;
        update(position);
    }

    /**
     * 获得被选中项目的位置
     *
     * @return 被选中项目的位置
     */
    public final int getCheckedPosition() {
        return mCheckedPosition;
    }

    /**
     * 隐藏ListView
     */
    public final void hideList() {
        mPopupList.dismiss();
    }

    /**
     * 判断是否正在显示ListView
     *
     * @return 是否正在显示ListView
     */
    public final boolean isShowingList() {
        return mPopupList.isShowing();
    }

    @Override
    public void onClick(View v) {
        if (isShowingList()) {
            hideList();
        } else {
            showList();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mPopupList.dismiss();
        update(position);
        mCheckedPosition = position;
        if (mListSettingChangeListener != null) {
            mListSettingChangeListener.onListSettingChange(this, view, position);
        }
    }

    /**
     * 设置列表状态监听器
     *
     * @param listSettingChangeListener
     *         列表状态监听器
     */
    public void setOnListSettingChangeListener(OnListSettingChangeListener listSettingChangeListener) {
        mListSettingChangeListener = listSettingChangeListener;
    }

    /**
     * 设置描述
     *
     * @param summary
     *         描述
     */
    public final void setSummary(String summary) {
        if (summary != null) {
            listsetting_summary.setVisibility(VISIBLE);
            listsetting_summary.setText(summary);
        } else {
            listsetting_summary.setVisibility(GONE);
        }
    }

    /**
     * 显示ListView
     */
    public final void showList() {
        mPopupList.show();
    }

    private void update(int position) {
        listsetting_list.setText(mAdapter.getItem(position));
    }

    private class Adapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mEntry.length;
        }

        @Override
        public CharSequence getItem(int position) {
            if (position >= 0 && position < mEntry.length) {
                return mEntry[position];
            } else {
                return "";
            }
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @SuppressWarnings("ConstantConditions")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView textView;

            if (convertView != null) {
                textView = (TextView) convertView;
            } else {
                textView = new TextView(getContext());
                textView.setLayoutParams(new AbsListView.LayoutParams(-1, -2));
                textView.setPadding(mListTextPadding, mListTextPadding, mListTextPadding, mListTextPadding);
                textView.setGravity(Gravity.CENTER);
                textView.setTextColor(mListTextColor);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mListTextSize);
                convertView = textView;
            }
            textView.setText(getItem(position));

            return convertView;
        }
    }
}