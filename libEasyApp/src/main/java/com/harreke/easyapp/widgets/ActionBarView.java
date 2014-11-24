package com.harreke.easyapp.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.harreke.easyapp.R;
import com.harreke.easyapp.frameworks.bases.IActionBar;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/11/18
 */
public class ActionBarView extends LinearLayout implements IActionBar {
    private int itemBackground;
    private int mHeight;
    private ImageView mHome;
    private LinearLayout mItemLayout;
    private OnActionBarClickListener mOnActionBarClickListener = null;
    private OnClickListener mOnHomeClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mOnActionBarClickListener != null) {
                mOnActionBarClickListener.onActionBarHomeClick();
            }
        }
    };
    private OnClickListener mOnItemClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mOnActionBarClickListener != null) {
                mOnActionBarClickListener.onActionBarItemClick((Integer) v.getTag(), v);
            }
        }
    };
    private TextView mTitle;

    public ActionBarView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.actionBarViewStyle);
    }

    public ActionBarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);

        TypedArray style;
        int background;
        int homeIcon;
        float textSize;
        int textColor;

        style = context.obtainStyledAttributes(attrs, R.styleable.ActionBarView, defStyle, 0);
        background = style.getResourceId(R.styleable.ActionBarView_background, 0);
        homeIcon = style.getResourceId(R.styleable.ActionBarView_homeIcon, 0);
        itemBackground = style.getResourceId(R.styleable.ActionBarView_itemBackground, 0);
        textSize = style.getDimension(R.styleable.ActionBarView_textSize, 0);
        textColor = style.getColor(R.styleable.ActionBarView_textColor, 0);
        style.recycle();

        mHeight = (int) getResources().getDimension(R.dimen.actionBarHeight);

        setOrientation(HORIZONTAL);
        setBackgroundResource(background);

        mHome = createImageItem(homeIcon);
        mHome.setOnClickListener(mOnHomeClickListener);
        addView(mHome);

        mTitle = new TextView(context);
        mTitle.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, mHeight));
        mTitle.setGravity(Gravity.CENTER_VERTICAL);
        mTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        mTitle.setTextColor(textColor);
        addView(mTitle);

        mItemLayout = new LinearLayout(context);
        mItemLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, mHeight));
        mItemLayout.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
        addView(mItemLayout);
    }

    @Override
    public void addActionBarImageItem(int id, int imageId) {
        ImageView item = createImageItem(imageId);

        item.setTag(id);
        item.setOnClickListener(mOnItemClickListener);
        mItemLayout.addView(item);
    }

    @Override
    public void addActionBarViewItem(int id, int layoutId, boolean clickable) {
        View item = LayoutInflater.from(getContext()).inflate(layoutId, mItemLayout, false);

        addActionBarViewItem(id, item, clickable);
    }

    @Override
    public void addActionBarViewItem(int id, View item, boolean clickable) {
        if (clickable) {
            item.setTag(id);
            item.setOnClickListener(mOnItemClickListener);
        }
        mItemLayout.addView(item);
    }

    private ImageView createImageItem(int imageId) {
        ImageView imageView = new ImageView(getContext());

        imageView.setLayoutParams(new LayoutParams(mHeight, mHeight));
        imageView.setImageResource(imageId);
        imageView.setBackgroundResource(itemBackground);

        return imageView;
    }

    @Override
    public void hideActionbarItem(int id) {
        View item;
        int i;

        for (i = 0; i < mItemLayout.getChildCount(); i++) {
            item = mItemLayout.getChildAt(i);
            if (item.getTag() instanceof Integer && (Integer) item.getTag() == id) {
                item.setVisibility(GONE);
                break;
            }
        }
    }

    @Override
    public void setActionBarTitle(int textId) {
        mTitle.setText(textId);
    }

    @Override
    public void setActionBarTitle(String text) {
        mTitle.setText(text);
    }

    public void setOnActionBarClickListener(OnActionBarClickListener onActionBarClickListener) {
        mOnActionBarClickListener = onActionBarClickListener;
    }

    @Override
    public void showActionBarHome(boolean show) {
        if (show) {
            mHome.setVisibility(VISIBLE);
        } else {
            mHome.setVisibility(GONE);
        }
    }

    @Override
    public void showActionBarItem(int id) {
        View item;
        int i;

        for (i = 0; i < mItemLayout.getChildCount(); i++) {
            item = mItemLayout.getChildAt(i);
            if (item.getTag() instanceof Integer && (Integer) item.getTag() == id) {
                item.setVisibility(VISIBLE);
                break;
            }
        }
    }

    @Override
    public void showActionBarTitle(boolean show) {
        if (show) {
            mTitle.setVisibility(VISIBLE);
        } else {
            mTitle.setVisibility(GONE);
        }
    }

    public interface OnActionBarClickListener {
        public void onActionBarHomeClick();

        public void onActionBarItemClick(int id, View item);
    }
}
