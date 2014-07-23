package com.harreke.utils.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.harreke.utils.R;
import com.harreke.utils.listeners.OnCheckSettingChangeListener;

/**
 复选设置视图

 带有一个可滑动状态框的设置视图，可以设置选中、非选中两种状态
 */
public class CheckSettingView extends LinearLayout implements View.OnTouchListener {
    private int buttonWidth;
    private ImageView checksetting_button_off;
    private ImageView checksetting_button_on;
    private TextView checksetting_summary;
    private ImageView checksetting_thumb;
    private boolean mAnimate = false;
    private float mBounceStep;
    private int mButtonHeight;
    private OnCheckSettingChangeListener mCheckSettingListener = null;
    private boolean mChecked = false;
    private float mDensity;
    private float mLastX;
    private float mLastY;
    private int mOffset;
    private int mScrollWidth;
    private boolean mSingleTap;
    private String mSummaryOff;
    private String mSummaryOn;
    private float mThumbPosition;
    private Runnable mBounceRunnable = new Runnable() {
        @Override
        public void run() {
            if (mAnimate) {
                mThumbPosition += mBounceStep;
                if (mThumbPosition < 0) {
                    mThumbPosition = 0;
                    invalidateButton();
                    mAnimate = false;
                    setChecked(false, false, true);
                } else if (mThumbPosition > mScrollWidth) {
                    mThumbPosition = mScrollWidth;
                    invalidateButton();
                    mAnimate = false;
                    setChecked(true, false, true);
                } else {
                    invalidateButton();
                    postDelayed(this, 10);
                }
            }
        }
    };
    private int mThumbSize;

    public CheckSettingView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.checkSettingViewStyle);
    }

    public CheckSettingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray style;
        LayoutParams params;
        LinearLayout layout;
        FrameLayout buttonView;
        TextView textView;
        int buttonGravity;
        Drawable buttonOff;
        Drawable buttonOn;
        int buttonPadding;
        int summaryColor;
        int summarySize;
        String text;
        int textColor;
        int textSize;
        Drawable thumb;

        style = context.obtainStyledAttributes(attrs, R.styleable.CheckSettingView, defStyle, 0);
        buttonGravity = style.getInt(R.styleable.CheckSettingView_buttonGravity, 0);
        mButtonHeight = (int) style.getDimension(R.styleable.CheckSettingView_buttonHeight, 0);
        buttonOff = style.getDrawable(R.styleable.CheckSettingView_buttonOff);
        buttonOn = style.getDrawable(R.styleable.CheckSettingView_buttonOn);
        buttonPadding = (int) style.getDimension(R.styleable.CheckSettingView_buttonPadding, 0);
        buttonWidth = (int) style.getDimension(R.styleable.CheckSettingView_buttonWidth, 0);
        mChecked = style.getBoolean(R.styleable.CheckSettingView_checked, false);
        summaryColor = style.getColor(R.styleable.CheckSettingView_summaryColor, 0);
        mSummaryOn = style.getString(R.styleable.CheckSettingView_summaryOn);
        mSummaryOff = style.getString(R.styleable.CheckSettingView_summaryOff);
        summarySize = (int) style.getDimension(R.styleable.CheckSettingView_summarySize, 0);
        text = style.getString(R.styleable.CheckSettingView_text);
        textColor = style.getColor(R.styleable.CheckSettingView_textColor, 0);
        textSize = (int) style.getDimension(R.styleable.CheckSettingView_textSize, 0);
        thumb = style.getDrawable(R.styleable.CheckSettingView_thumb);
        mThumbSize = (int) style.getDimension(R.styleable.CheckSettingView_thumbSize, 0);
        style.recycle();

        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);

        layout = new LinearLayout(context);
        layout.setOrientation(VERTICAL);
        layout.setLayoutParams(new LayoutParams(-1, -2, 1));

        textView = new TextView(context);
        textView.setLayoutParams(new LayoutParams(-1, -2));
        textView.setText(text);
        textView.setTextColor(textColor);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        layout.addView(textView);

        checksetting_summary = new TextView(context);
        checksetting_summary.setLayoutParams(new LayoutParams(-1, -2));
        checksetting_summary.setTextSize(TypedValue.COMPLEX_UNIT_PX, summarySize);
        checksetting_summary.setTextColor(summaryColor);
        layout.addView(checksetting_summary);

        buttonView = new FrameLayout(context);
        buttonView.setLayoutParams(new LayoutParams(buttonWidth, mButtonHeight));

        checksetting_button_off = new ImageView(context);
        checksetting_button_off.setLayoutParams(new FrameLayout.LayoutParams(buttonWidth, mButtonHeight));
        if (buttonOff != null) {
            checksetting_button_off.setImageDrawable(buttonOff);
        }
        buttonView.addView(checksetting_button_off);

        checksetting_button_on = new ImageView(context);
        checksetting_button_on.setLayoutParams(new FrameLayout.LayoutParams(buttonWidth, mButtonHeight));
        if (buttonOn != null) {
            checksetting_button_on.setImageDrawable(buttonOn);
        }
        buttonView.addView(checksetting_button_on);

        if (mThumbSize > buttonWidth) {
            mThumbSize = buttonWidth;
        }
        if (mThumbSize > mButtonHeight) {
            mThumbSize = mButtonHeight;
        }
        checksetting_thumb = new ImageView(context);
        checksetting_thumb.setLayoutParams(new FrameLayout.LayoutParams(mThumbSize, mThumbSize));
        if (thumb != null) {
            checksetting_thumb.setImageDrawable(thumb);
        }
        buttonView.addView(checksetting_thumb);

        if (buttonGravity == 0) {
            addView(buttonView);
            params = (LayoutParams) layout.getLayoutParams();
            params.leftMargin = buttonPadding;
            layout.setLayoutParams(params);
            addView(layout);
        } else {
            addView(layout);
            params = (LayoutParams) buttonView.getLayoutParams();
            params.leftMargin = buttonPadding;
            buttonView.setLayoutParams(params);
            addView(buttonView);
        }

        mDensity = getResources().getDisplayMetrics().density;
        mOffset = (mButtonHeight - mThumbSize) / 2;
        mScrollWidth = buttonWidth - mThumbSize - mOffset * 2;
        mThumbPosition = 0;

        invalidateButton();

        buttonView.setOnTouchListener(this);

        setSummary(mChecked);
    }

    private void invalidateButton() {
        FrameLayout.LayoutParams params;
        float alpha;

        alpha = mThumbPosition / mScrollWidth;
        checksetting_button_off.setAlpha(1 - alpha);
        checksetting_button_on.setAlpha(alpha);
        params = (FrameLayout.LayoutParams) checksetting_thumb.getLayoutParams();
        params.leftMargin = (int) (mThumbPosition + mOffset);
        params.topMargin = mOffset;
        checksetting_thumb.setLayoutParams(params);
        invalidate();
    }

    private void setSummary(boolean checked) {
        if (checked) {
            if (mSummaryOn == null) {
                checksetting_summary.setVisibility(GONE);
            } else {
                checksetting_summary.setVisibility(VISIBLE);
                checksetting_summary.setText(mSummaryOn);
            }
        } else {
            if (mSummaryOff == null) {
                checksetting_summary.setVisibility(GONE);
            } else {
                checksetting_summary.setVisibility(VISIBLE);
                checksetting_summary.setText(mSummaryOff);
            }
        }
    }

    /**
     获得状态

     @return 状态（选中、非选中）
     */
    public final boolean isChecked() {
        return mChecked;
    }

    /**
     设置状态

     @param checked
     状态（选中、非选中）
     */
    public final void setChecked(boolean checked) {
        setChecked(checked, false, false);
    }

    private void setChecked(boolean checked, boolean withAnim, boolean shouldTrigger) {
        if (mChecked != checked) {
            mChecked = checked;
            setSummary(checked);
            if (withAnim) {
                if (checked) {
                    scrollbackRight();
                } else {
                    scrollbackLeft();
                }
            } else {
                if (checked) {
                    mThumbPosition = mScrollWidth;
                } else {
                    mThumbPosition = 0;
                }
                invalidateButton();
            }
            if (shouldTrigger && mCheckSettingListener != null) {
                mCheckSettingListener.onCheckSettingChange(this, checked);
            }
        }
    }

    private void scrollbackRight() {
        mBounceStep = mScrollWidth / 10f;
        startAnimate();
    }

    private void scrollbackLeft() {
        mBounceStep = -mScrollWidth / 10f;
        startAnimate();
    }

    private void startAnimate() {
        mAnimate = true;
        postDelayed(mBounceRunnable, 10);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float eventX;
        float eventY;

        eventX = event.getX();
        eventY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastX = eventX;
                mLastY = eventY;
                mAnimate = false;
                mSingleTap = true;
                break;
            case MotionEvent.ACTION_UP:
                if (mSingleTap =
                        Math.abs(eventX - mLastX) < mDensity && Math.abs(eventY - mLastY) < mDensity && event.getEventTime() - event.getDownTime() < 200) {
                    setChecked(!mChecked, true, true);
                } else {
                    if (mThumbPosition < (buttonWidth - mThumbSize) / 2) {
                        scrollbackLeft();
                    } else {
                        scrollbackRight();
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mSingleTap && (Math.abs(eventX - mLastX) >= mDensity || Math.abs(eventY - mLastY) >= mDensity)) {
                    mSingleTap = false;
                }
                if (eventX >= mOffset && eventX <= buttonWidth - mThumbSize - mOffset && eventY >= 0 && eventY <= mButtonHeight) {
                    mThumbPosition = eventX - mOffset;
                    invalidateButton();
                }
        }

        return true;
    }

    /**
     设置状态监听器

     @param checkSettingListener
     状态监听器
     */
    public final void setOnCheckSettingListener(OnCheckSettingChangeListener checkSettingListener) {
        mCheckSettingListener = checkSettingListener;
    }

    /**
     设置非选中状态的描述

     @param summaryOff
     非选中状态的描述
     */
    public final void setSummaryOff(String summaryOff) {
        mSummaryOff = summaryOff;
        setSummary(mChecked);
    }

    /**
     设置选中状态的描述

     @param summaryOn
     选中状态的描述
     */
    public final void setSummaryOn(String summaryOn) {
        mSummaryOn = summaryOn;
        setSummary(mChecked);
    }
}