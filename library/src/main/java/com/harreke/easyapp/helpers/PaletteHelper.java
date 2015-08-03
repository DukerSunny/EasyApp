package com.harreke.easyapp.helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.graphics.Palette;

import com.harreke.easyapp.utils.ResourceUtil;

import java.util.List;

/**
 * Created by 启圣 on 2015/6/25.
 */
public class PaletteHelper {
    private int mBackgroundColor;
    private int mBodyTextColor;
    private int mColorPrimary;
    private int mTitleTextColor;

    public PaletteHelper(Context context) {
        mColorPrimary = ResourceUtil.obtainThemeColor(context)[0];
    }

    private void generateDefault() {
        mBackgroundColor = mColorPrimary;
        mTitleTextColor = Color.WHITE;
        mBodyTextColor = Color.WHITE;
    }

    public int getBackgroundColor() {
        return mBackgroundColor;
    }

    public int getBodyTextColor() {
        return mBodyTextColor;
    }

    public int getTitleTextColor() {
        return mTitleTextColor;
    }

    public void parse(@Nullable Bitmap bitmap) {
        Palette palette;
        List<Palette.Swatch> swatchList;
        Palette.Swatch swatch;
        int i;

        if (bitmap == null) {
            generateDefault();
        } else {
            palette = new Palette.Builder(bitmap).generate();
            swatchList = palette.getSwatches();
            if (swatchList.size() > 0) {
                swatch = swatchList.get(0);
                mBackgroundColor = swatch.getRgb();
                mTitleTextColor = swatch.getTitleTextColor();
                mBodyTextColor = swatch.getBodyTextColor();
            } else {
                generateDefault();
            }
        }
    }
}