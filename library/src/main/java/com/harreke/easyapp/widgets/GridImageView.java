package com.harreke.easyapp.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.harreke.easyapp.R;
import com.harreke.easyapp.frameworks.base.ApplicationFramework;
import com.harreke.easyapp.utils.MathUtil;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/04/15
 */
public class GridImageView extends AutoFitImageView {
    private boolean mClick = false;
    private float mDownX;
    private float mDownY;
    private int mHeight;
    private int mHeightGap;
    private Drawable mLineDivider;
    private int mLineDividerHeight;
    private int mLineDividerHeightThreshold;
    private int mLineMax;
    private OnGridClickListener mOnGridClickListener = null;
    private Drawable mRowDivider;
    private int mRowDividerWidth;
    private int mRowDividerWidthThreshold;
    private int mRowMax;
    private float mThreshold = ApplicationFramework.TouchThreshold;
    private int mWidth;
    private int mWidthGap;

    public GridImageView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.gridImageViewStyle);
    }

    public GridImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray style;

        style = context.obtainStyledAttributes(attrs, R.styleable.GridImageView, defStyleAttr, 0);
        mRowMax = style.getInt(R.styleable.GridImageView_grid_row_max, 2);
        if (mRowMax < 1) {
            mRowMax = 1;
        }
        mRowDivider = style.getDrawable(R.styleable.GridImageView_grid_row_divider);
        mRowDividerWidth =
                (int) style.getDimension(R.styleable.GridImageView_grid_row_divider_width, ApplicationFramework.Density);
        mRowDividerWidthThreshold = mRowDividerWidth / 2;
        mLineMax = style.getInt(R.styleable.GridImageView_grid_line_max, 2);
        if (mLineMax < 1) {
            mLineMax = 1;
        }
        mLineDivider = style.getDrawable(R.styleable.GridImageView_grid_line_divider);
        mLineDividerHeight =
                (int) style.getDimension(R.styleable.GridImageView_grid_line_divider_height, ApplicationFramework.Density);
        mLineDividerHeightThreshold = mLineDividerHeight / 2;
        style.recycle();

        setClickable(true);
    }

    public Bitmap getBitmap(int row, int line) {
        BitmapDrawable drawable = (BitmapDrawable) getDrawable();
        Bitmap bitmap;
        int widthGap;
        int heightGap;

        if (drawable != null) {
            bitmap = drawable.getBitmap();
            widthGap = bitmap.getWidth() / mRowMax;
            heightGap = bitmap.getHeight() / mLineMax;
            if (widthGap > 0 && heightGap > 0) {
                return Bitmap.createBitmap(bitmap, row * widthGap, line * heightGap, widthGap, heightGap);
            }
        }

        return null;
    }

    //    public Drawable getDrawable(int row, int line) {
    //        Drawable drawable = getDrawable();
    //        Drawable result = null;
    //        Bitmap bitmap;
    //        Canvas canvas;
    //
    //        if (drawable != null) {
    //            drawable.setBounds(0, 0, mWidth, mHeight);
    //            bitmap = Bitmap.createBitmap(mWidth, mHeight,
    //                    drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
    //            canvas = new Canvas(bitmap);
    //            drawable.draw(canvas);
    //            result = new BitmapDrawable(getResources(),
    //                    Bitmap.createBitmap(bitmap, row * mWidthGap, line * mHeightGap, mWidthGap, mHeightGap));
    //        }
    //
    //        return result;
    //    }

    public int getLineMax() {
        return mLineMax;
    }

    public int getRowMax() {
        return mRowMax;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int i;
        int x;
        int y;

        if (mRowDivider != null) {
            for (i = 1; i < mRowMax; i++) {
                x = mWidthGap * i;
                mRowDivider.setBounds(x - mRowDividerWidthThreshold, 0, x + mRowDividerWidthThreshold, mHeight);
                mRowDivider.draw(canvas);
            }
        }
        if (mLineDivider != null) {
            for (i = 1; i < mLineMax; i++) {
                y = mHeightGap * i;
                mLineDivider.setBounds(0, y - mLineDividerHeightThreshold, mWidth, y + mLineDividerHeightThreshold);
                mLineDivider.draw(canvas);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mWidth = getMeasuredWidth();
        mWidthGap = mWidth / mRowMax;
        mHeight = getMeasuredHeight();
        mHeightGap = mHeight / mLineMax;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float nowX = event.getX();
        float nowY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = nowX;
                mDownY = nowY;
                mClick = true;
                break;
            case MotionEvent.ACTION_MOVE:
                if (MathUtil.getDistance(nowX, nowY, mDownX, mDownY) > mThreshold) {
                    mClick = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mClick) {
                    if (mOnGridClickListener != null) {
                        mOnGridClickListener.onGridClick((int) (mDownX / mWidthGap), (int) (mDownY / mHeightGap));

                        return true;
                    }
                }
        }

        return super.onTouchEvent(event);
    }

    public void setLineMax(int lineMax) {
        mLineMax = lineMax;
    }

    public void setOnGridClickListener(OnGridClickListener onGridClickListener) {
        mOnGridClickListener = onGridClickListener;
    }

    public void setRowMax(int rowMax) {
        mRowMax = rowMax;
    }

    public interface OnGridClickListener {
        public void onGridClick(int row, int line);
    }
}