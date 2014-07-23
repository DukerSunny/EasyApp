package com.harreke.utils.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.harreke.utils.R;
import com.harreke.utils.listeners.OnChildSettingChangeListener;

/**
 单选设置视图

 具有跟RadioButton类似的运作效果，可设置选中、非选中两种状态
 */
public class ChildSettingView extends LinearLayout implements View.OnClickListener {
    private ImageView childsetting_button;
    private Drawable mButtonOff;
    private Drawable mButtonOn;
    private boolean mChecked;
    private OnChildSettingChangeListener mChildSettingChangeListener = null;

    public ChildSettingView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.childSettingViewStyle);
    }

    public ChildSettingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray style;
        LayoutParams params;
        TextView textView;
        int buttonGravity;
        int buttonHeight;
        int buttonPadding;
        int buttonWidth;
        String text;
        int textColor;
        int textSize;

        style = context.obtainStyledAttributes(attrs, R.styleable.ChildSettingView, defStyle, 0);
        buttonGravity = style.getInt(R.styleable.ChildSettingView_buttonGravity, 0);
        buttonHeight = (int) style.getDimension(R.styleable.ChildSettingView_buttonHeight, 0);
        mButtonOff = style.getDrawable(R.styleable.ChildSettingView_buttonOff);
        mButtonOn = style.getDrawable(R.styleable.ChildSettingView_buttonOn);
        buttonPadding = (int) style.getDimension(R.styleable.ChildSettingView_buttonPadding, 0);
        buttonWidth = (int) style.getDimension(R.styleable.ChildSettingView_buttonWidth, 0);
        mChecked = style.getBoolean(R.styleable.ChildSettingView_checked, false);
        text = style.getString(R.styleable.ChildSettingView_text);
        textColor = style.getColor(R.styleable.ChildSettingView_textColor, 0);
        textSize = (int) style.getDimension(R.styleable.ChildSettingView_textSize, 0);
        style.recycle();

        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);

        textView = new TextView(context);
        textView.setLayoutParams(new LayoutParams(-1, -2, 1));
        textView.setText(text);
        textView.setTextColor(textColor);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);

        childsetting_button = new ImageView(context);
        childsetting_button.setLayoutParams(new LayoutParams(buttonWidth, buttonHeight));
        if (mChecked) {
            childsetting_button.setImageDrawable(mButtonOn);
        } else {
            childsetting_button.setImageDrawable(mButtonOff);
        }

        if (buttonGravity == 0) {
            addView(childsetting_button);
            params = (LayoutParams) textView.getLayoutParams();
            params.leftMargin = buttonPadding;
            textView.setLayoutParams(params);
            addView(textView);
        } else {
            addView(textView);
            params = (LayoutParams) childsetting_button.getLayoutParams();
            params.leftMargin = buttonPadding;
            childsetting_button.setLayoutParams(params);
            addView(childsetting_button);
        }

        setOnClickListener(this);
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
        setChecked(checked, false);
    }

    private void setChecked(boolean checked, boolean shouldTrigger) {
        if (mChecked != checked) {
            mChecked = checked;
            if (checked) {
                childsetting_button.setImageDrawable(mButtonOn);
            } else {
                childsetting_button.setImageDrawable(mButtonOff);
            }
            if (shouldTrigger && mChildSettingChangeListener != null) {
                mChildSettingChangeListener.onChildSettingChange(this, checked);
            }
        }
    }

    @Override
    public void onClick(View v) {
        setChecked(!mChecked, true);
    }

    /**
     设置状态监听器

     @param childSettingChangeListener
     状态监听器
     */
    public final void setOnChildSettingListener(OnChildSettingChangeListener childSettingChangeListener) {
        mChildSettingChangeListener = childSettingChangeListener;
    }
}