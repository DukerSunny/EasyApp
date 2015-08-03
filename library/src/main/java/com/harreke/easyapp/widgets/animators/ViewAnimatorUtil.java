package com.harreke.easyapp.widgets.animators;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nineoldandroids.animation.PropertyValuesHolder;
import com.nineoldandroids.animation.TypeEvaluator;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.ViewHelper;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/04/28
 */
public abstract class ViewAnimatorUtil {
    public final static long DURATION = 300l;
    private final static String Key_Alpha = "alpha";
    private final static String Key_BackgroundColor = "backgroundColor";
    private final static String Key_Coordinate = "coordinate";
    private final static String Key_Height = "height";
    private final static String Key_Pivot = "pivot";
    private final static String Key_PivotX = "pivotX";
    private final static String Key_PivotY = "pivotY";
    private final static String Key_Rotation = "rotation";
    private final static String Key_RotationX = "rotationX";
    private final static String Key_RotationY = "rotationY";
    private final static String Key_Scale = "scale";
    private final static String Key_ScaleX = "scaleX";
    private final static String Key_ScaleY = "scaleY";
    private final static String Key_Size = "size";
    private final static String Key_TextColor = "textColor";
    private final static String Key_Translation = "translation";
    private final static String Key_TranslationX = "translationX";
    private final static String Key_TranslationY = "translationY";
    private final static String Key_Visibility = "visibilityOff";
    private final static String Key_VisibilityReverse = "visibilityOn";
    private final static String Key_Width = "width";
    private final static String Key_X = "x";
    private final static String Key_Y = "y";
    private static TypeEvaluator<float[]> floatArrEvaluator = new TypeEvaluator<float[]>() {
        @Override
        public float[] evaluate(float fraction, float[] startValue, float[] endValue) {
            float start0 = startValue[0];
            float start1 = startValue[1];

            float end0 = endValue[0];
            float end1 = endValue[1];

            return new float[]{start0 + fraction * (end0 - start0), start1 + fraction * (end1 - start1)};
        }
    };
    private static TypeEvaluator<int[]> intArrEvaluator = new TypeEvaluator<int[]>() {
        @Override
        public int[] evaluate(float fraction, int[] startValue, int[] endValue) {
            int start0 = startValue[0];
            int start1 = startValue[1];

            int end0 = endValue[0];
            int end1 = endValue[1];

            return new int[]{start0 + (int) (fraction * (end0 - start0)), start1 + (int) (fraction * (end1 - start1))};
        }
    };
    private static TypeEvaluator<Integer> rgbEvaluator = new TypeEvaluator<Integer>() {
        @Override
        public Integer evaluate(float fraction, Integer startValue, Integer endValue) {
            int startInt = startValue;
            int startA = (startInt >> 24) & 0xff;
            int startR = (startInt >> 16) & 0xff;
            int startG = (startInt >> 8) & 0xff;
            int startB = startInt & 0xff;

            int endInt = endValue;
            int endA = (endInt >> 24) & 0xff;
            int endR = (endInt >> 16) & 0xff;
            int endG = (endInt >> 8) & 0xff;
            int endB = endInt & 0xff;

            return (startA + (int) (fraction * (endA - startA))) << 24 |
                    (startR + (int) (fraction * (endR - startR))) << 16 |
                    (startG + (int) (fraction * (endG - startG))) << 8 |
                    (startB + (int) (fraction * (endB - startB)));
        }
    };

    public static boolean containsAlpha(@NonNull Map<String, Object> map) {
        return map.containsKey(Key_Alpha);
    }

    public static boolean containsBackgroundColor(@NonNull Map<String, Object> map) {
        return map.containsKey(Key_BackgroundColor);
    }

    public static boolean containsCoordinate(@NonNull Map<String, Object> map) {
        return map.containsKey(Key_Coordinate);
    }

    public static boolean containsHeight(@NonNull Map<String, Object> map) {
        return map.containsKey(Key_Height);
    }

    public static boolean containsPivot(@NonNull Map<String, Object> map) {
        return map.containsKey(Key_Pivot);
    }

    public static boolean containsPivotX(@NonNull Map<String, Object> map) {
        return map.containsKey(Key_PivotX);
    }

    public static boolean containsPivotY(@NonNull Map<String, Object> map) {
        return map.containsKey(Key_PivotY);
    }

    public static boolean containsRotation(@NonNull Map<String, Object> map) {
        return map.containsKey(Key_Rotation);
    }

    public static boolean containsRotationX(@NonNull Map<String, Object> map) {
        return map.containsKey(Key_RotationX);
    }

    public static boolean containsRotationY(@NonNull Map<String, Object> map) {
        return map.containsKey(Key_RotationY);
    }

    public static boolean containsScale(@NonNull Map<String, Object> map) {
        return map.containsKey(Key_Scale);
    }

    public static boolean containsScaleX(@NonNull Map<String, Object> map) {
        return map.containsKey(Key_ScaleX);
    }

    public static boolean containsScaleY(@NonNull Map<String, Object> map) {
        return map.containsKey(Key_ScaleY);
    }

    public static boolean containsSize(@NonNull Map<String, Object> map) {
        return map.containsKey(Key_Size);
    }

    public static boolean containsTextColor(@NonNull Map<String, Object> map) {
        return map.containsKey(Key_TextColor);
    }

    public static boolean containsTranslation(@NonNull Map<String, Object> map) {
        return map.containsKey(Key_Translation);
    }

    public static boolean containsTranslationX(@NonNull Map<String, Object> map) {
        return map.containsKey(Key_TranslationX);
    }

    public static boolean containsTranslationY(@NonNull Map<String, Object> map) {
        return map.containsKey(Key_TranslationY);
    }

    public static boolean containsVisibility(@NonNull Map<String, Object> map) {
        return map.containsKey(Key_Visibility);
    }

    public static boolean containsVisibilityReverse(@NonNull Map<String, Object> map) {
        return map.containsKey(Key_VisibilityReverse);
    }

    public static boolean containsWidth(@NonNull Map<String, Object> map) {
        return map.containsKey(Key_Width);
    }

    public static boolean containsX(@NonNull Map<String, Object> map) {
        return map.containsKey(Key_X);
    }

    public static boolean containsY(@NonNull Map<String, Object> map) {
        return map.containsKey(Key_Y);
    }

    public static float getAlpha(@NonNull View view) {
        return ViewHelper.getAlpha(view);
    }

    public static float getAlpha(@NonNull Map<String, Object> map) {
        return (float) map.get(Key_Alpha);
    }

    public static int getBackgroundColor(@NonNull View view) {
        Drawable drawable = view.getBackground();

        if (drawable != null && drawable instanceof ColorDrawable) {
            return ((ColorDrawable) drawable).getColor();
        }

        return Color.TRANSPARENT;
    }

    public static int getBackgroundColor(@NonNull Map<String, Object> map) {
        return (int) map.get(Key_BackgroundColor);
    }

    public static float[] getCoordinate(@NonNull Map<String, Object> map) {
        return (float[]) map.get(Key_Coordinate);
    }

    public static float[] getCoordinate(@NonNull View view) {
        return new float[]{ViewHelper.getX(view), ViewHelper.getY(view)};
    }

    public static int getHeight(@NonNull Map<String, Object> map) {
        return (int) map.get(Key_Height);
    }

    public static int getHeight(@NonNull View view) {
        return view.getMeasuredHeight();
    }

    public static float[] getPivot(@NonNull Map<String, Object> map) {
        return (float[]) map.get(Key_Pivot);
    }

    public static float[] getPivot(@NonNull View view) {
        return new float[]{ViewHelper.getPivotX(view), ViewHelper.getPivotY(view)};
    }

    public static float getPivotX(@NonNull Map<String, Object> map) {
        return (float) map.get(Key_PivotX);
    }

    public static float getPivotX(@NonNull View view) {
        return ViewHelper.getPivotX(view);
    }

    public static float getPivotY(@NonNull Map<String, Object> map) {
        return (float) map.get(Key_PivotY);
    }

    public static float getPivotY(@NonNull View view) {
        return ViewHelper.getPivotY(view);
    }

    public static float getRotation(@NonNull Map<String, Object> map) {
        return (float) map.get(Key_Rotation);
    }

    public static float getRotation(@NonNull View view) {
        return ViewHelper.getRotation(view);
    }

    public static float getRotationX(@NonNull Map<String, Object> map) {
        return (float) map.get(Key_RotationX);
    }

    public static float getRotationX(@NonNull View view) {
        return ViewHelper.getRotationX(view);
    }

    public static float getRotationY(@NonNull Map<String, Object> map) {
        return (float) map.get(Key_RotationY);
    }

    public static float getRotationY(@NonNull View view) {
        return ViewHelper.getRotationY(view);
    }

    public static float[] getScale(@NonNull Map<String, Object> map) {
        return (float[]) map.get(Key_Scale);
    }

    public static float[] getScale(@NonNull View view) {
        return new float[]{ViewHelper.getScaleX(view), ViewHelper.getScaleY(view)};
    }

    public static float getScaleX(@NonNull Map<String, Object> map) {
        return (float) map.get(Key_ScaleX);
    }

    public static float getScaleX(@NonNull View view) {
        return ViewHelper.getScaleX(view);
    }

    public static float getScaleY(@NonNull Map<String, Object> map) {
        return (float) map.get(Key_ScaleY);
    }

    public static float getScaleY(@NonNull View view) {
        return ViewHelper.getScaleY(view);
    }

    public static int[] getSize(@NonNull Map<String, Object> map) {
        return (int[]) map.get(Key_Size);
    }

    public static int[] getSize(@NonNull View view) {
        return new int[]{view.getMeasuredWidth(), view.getMeasuredHeight()};
    }

    public static int getTextColor(@NonNull View view) {
        if (view instanceof TextView) {
            return ((TextView) view).getCurrentTextColor();
        } else {
            return Color.TRANSPARENT;
        }
    }

    public static int getTextColor(@NonNull Map<String, Object> map) {
        return (int) map.get(Key_TextColor);
    }

    public static float[] getTranslation(@NonNull Map<String, Object> map) {
        return (float[]) map.get(Key_TranslationX);
    }

    public static float[] getTranslation(@NonNull View view) {
        return new float[]{ViewHelper.getTranslationX(view), ViewHelper.getTranslationY(view)};
    }

    public static float getTranslationX(@NonNull Map<String, Object> map) {
        return (float) map.get(Key_TranslationX);
    }

    public static float getTranslationX(@NonNull View view) {
        return ViewHelper.getTranslationX(view);
    }

    public static float getTranslationY(@NonNull View view) {
        return ViewHelper.getTranslationY(view);
    }

    public static float getTranslationY(@NonNull Map<String, Object> map) {
        return (float) map.get(Key_TranslationY);
    }

    public static float getVisibility(@NonNull View view) {
        return view.getVisibility();
    }

    public static int getVisibility(@NonNull Map<String, Object> map) {
        return (int) map.get(Key_Visibility);
    }

    public static int getVisibilityReverse(@NonNull Map<String, Object> map) {
        return (int) map.get(Key_VisibilityReverse);
    }

    public static int getWidth(@NonNull Map<String, Object> map) {
        return (int) map.get(Key_Width);
    }

    public static int getWidth(@NonNull View view) {
        return view.getMeasuredWidth();
    }

    public static float getX(@NonNull Map<String, Object> map) {
        return (float) map.get(Key_X);
    }

    public static float getX(@NonNull View view) {
        return ViewHelper.getX(view);
    }

    public static float getY(@NonNull Map<String, Object> map) {
        return (float) map.get(Key_Y);
    }

    public static float getY(@NonNull View view) {
        return ViewHelper.getY(view);
    }

    public static ValueAnimator make(@NonNull View view, @NonNull Map<String, Object> map, boolean debug) {
        List<PropertyValuesHolder> holderList = new LinkedList<>();

        if (containsAlpha(map)) {
            holderList.add(PropertyValuesHolder.ofFloat(Key_Alpha, getAlpha(view), getAlpha(map)));
            if (debug) {
                Log.e(null, "Alpha from " + getAlpha(view) + " to " + getAlpha(map));
            }
        }
        if (containsSize(map)) {
            holderList.add(PropertyValuesHolder.ofObject(Key_Size, intArrEvaluator, getSize(view), getSize(map)));
            if (debug) {
                Log.e(null, "Size from " + Arrays.toString(getSize(view)) + " to " + Arrays.toString(getSize(map)));
            }
        }
        if (containsWidth(map)) {
            holderList.add(PropertyValuesHolder.ofInt(Key_Width, getWidth(view), getWidth(map)));
            if (debug) {
                Log.e(null, "Width from " + getWidth(view) + " to " + getWidth(map));
            }
        }
        if (containsHeight(map)) {
            holderList.add(PropertyValuesHolder.ofInt(Key_Height, getHeight(view), getHeight(map)));
            if (debug) {
                Log.e(null, "Height from " + getHeight(view) + " to " + getHeight(map));
            }
        }
        if (containsPivot(map)) {
            holderList.add(PropertyValuesHolder.ofObject(Key_Pivot, floatArrEvaluator, getPivot(view), getPivot(map)));
            if (debug) {
                Log.e(null, "Pivot from " + Arrays.toString(getPivot(view)) + " to " + Arrays.toString(getPivot(map)));
            }
        }
        if (containsPivotX(map)) {
            holderList.add(PropertyValuesHolder.ofFloat(Key_PivotX, getPivotX(view), getPivotX(map)));
            if (debug) {
                Log.e(null, "PivotX from " + getPivotX(view) + " to " + getPivotX(map));
            }
        }
        if (containsPivotY(map)) {
            holderList.add(PropertyValuesHolder.ofFloat(Key_PivotY, getPivotY(view), getPivotY(map)));
            if (debug) {
                Log.e(null, "PivotY from " + getPivotY(view) + " to " + getPivotY(map));
            }
        }
        if (containsRotation(map)) {
            holderList.add(PropertyValuesHolder.ofFloat(Key_Rotation, getRotation(view), getRotation(map)));
            if (debug) {
                Log.e(null, "Rotation from " + getRotation(view) + " to " + getRotation(map));
            }
        }
        if (containsRotationX(map)) {
            holderList.add(PropertyValuesHolder.ofFloat(Key_RotationX, getRotationX(view), getRotationX(map)));
            if (debug) {
                Log.e(null, "RotationX from " + getRotationX(view) + " to " + getRotationX(map));
            }
        }
        if (containsRotationY(map)) {
            holderList.add(PropertyValuesHolder.ofFloat(Key_RotationY, getRotationY(view), getRotationY(map)));
            if (debug) {
                Log.e(null, "RotationY from " + getRotationY(view) + " to " + getRotationY(map));
            }
        }
        if (containsScale(map)) {
            holderList.add(PropertyValuesHolder.ofObject(Key_Scale, floatArrEvaluator, getScale(view), getScale(map)));
            if (debug) {
                Log.e(null, "Scale from " + Arrays.toString(getScale(view)) + " to " + Arrays.toString(getScale(map)));
            }
        }
        if (containsScaleX(map)) {
            holderList.add(PropertyValuesHolder.ofFloat(Key_ScaleX, getScaleX(view), getScaleX(map)));
            if (debug) {
                Log.e(null, "ScaleX from " + getScaleX(view) + " to " + getScaleX(map));
            }
        }
        if (containsScaleY(map)) {
            holderList.add(PropertyValuesHolder.ofFloat(Key_ScaleY, getScaleY(view), getScaleY(map)));
            if (debug) {
                Log.e(null, "ScaleY from " + getScaleY(view) + " to " + getScaleY(map));
            }
        }
        if (containsTranslation(map)) {
            holderList.add(PropertyValuesHolder.ofObject(Key_Translation, floatArrEvaluator, getTranslation(view), getTranslation(map)));
            if (debug) {
                Log.e(null, "Translation from " + Arrays.toString(getTranslation(view)) + " to " + Arrays.toString(getTranslation(map)));
            }
        }
        if (containsTranslationX(map)) {
            holderList.add(PropertyValuesHolder.ofFloat(Key_TranslationX, getTranslationX(view), getTranslationX(map)));
            if (debug) {
                Log.e(null, "TranslationX from " + getTranslationX(view) + " to " + getTranslationX(map));
            }
        }
        if (containsTranslationY(map)) {
            holderList.add(PropertyValuesHolder.ofFloat(Key_TranslationY, getTranslationY(view), getTranslationY(map)));
            if (debug) {
                Log.e(null, "TranslationY from " + getTranslationY(view) + " to " + getTranslationY(map));
            }
        }
        if (containsCoordinate(map)) {
            holderList.add(PropertyValuesHolder.ofObject(Key_Coordinate, floatArrEvaluator, getCoordinate(view), getCoordinate(map)));
            if (debug) {
                Log.e(null, "Coordinate from " + Arrays.toString(getCoordinate(view)) + " to " + Arrays.toString(getCoordinate(map)));
            }
        }
        if (containsX(map)) {
            holderList.add(PropertyValuesHolder.ofFloat(Key_X, getX(view), getX(map)));
            if (debug) {
                Log.e(null, "X from " + getX(view) + " to " + getX(map));
            }
        }
        if (containsY(map)) {
            holderList.add(PropertyValuesHolder.ofFloat(Key_Y, getY(view), getY(map)));
            if (debug) {
                Log.e(null, "Y from " + getY(view) + " to " + getY(map));
            }
        }
        if (containsBackgroundColor(map)) {
            holderList.add(PropertyValuesHolder.ofObject(Key_BackgroundColor, rgbEvaluator, getBackgroundColor(view), getBackgroundColor(map)));
            if (debug) {
                Log.e(null, "BackgroundColor from " + Integer.toHexString(getBackgroundColor(view)) + " to " +
                        Integer.toHexString(getBackgroundColor(map)));
            }
        }
        if (containsTextColor(map)) {
            holderList.add(PropertyValuesHolder.ofObject(Key_TextColor, rgbEvaluator, getTextColor(view), getTextColor(map)));
            if (debug) {
                Log.e(null, "TextColor from " + Integer.toHexString(getTextColor(view)) + " to " +
                        Integer.toHexString(getTextColor(map)));
            }
        }

        return ValueAnimator.ofPropertyValuesHolder(holderList.toArray(new PropertyValuesHolder[holderList.size()]));
    }

    public static ValueAnimator make(@NonNull View view, @NonNull Map<String, Object> fromMap, @NonNull Map<String, Object> toMap, boolean debug) {
        List<PropertyValuesHolder> holderList = new LinkedList<>();

        if (containsAlpha(toMap)) {
            holderList.add(PropertyValuesHolder.ofFloat(Key_Alpha, containsAlpha(fromMap) ? getAlpha(fromMap) : getAlpha(view), getAlpha(toMap)));
            if (debug) {
                Log.e(null, "Alpha from " + (containsAlpha(fromMap) ? getAlpha(fromMap) : getAlpha(view)) + " to " +
                        getAlpha(toMap));
            }
        }
        if (containsSize(toMap)) {
            holderList.add(PropertyValuesHolder.ofObject(Key_Size, intArrEvaluator, containsSize(fromMap) ? getSize(fromMap) : getSize(view), getSize(toMap)));
            if (debug) {
                Log.e(null, "Size from " + Arrays.toString((containsSize(fromMap) ? getSize(fromMap) : getSize(view))) + " to " +
                        Arrays.toString(getSize(toMap)));
            }
        }
        if (containsWidth(toMap)) {
            holderList.add(PropertyValuesHolder.ofInt(Key_Width, containsWidth(fromMap) ? getWidth(fromMap) : getWidth(view), getWidth(toMap)));
            if (debug) {
                Log.e(null, "Width from " + (containsWidth(fromMap) ? getWidth(fromMap) : getWidth(view)) + " to " +
                        getWidth(toMap));
            }
        }
        if (containsHeight(toMap)) {
            holderList.add(PropertyValuesHolder.ofInt(Key_Height, containsHeight(fromMap) ? getHeight(fromMap) : getHeight(view), getHeight(toMap)));
            if (debug) {
                Log.e(null, "Height from " + (containsHeight(fromMap) ? getHeight(fromMap) : getHeight(view)) + " to " +
                        getHeight(toMap));
            }
        }
        if (containsPivot(toMap)) {
            holderList.add(PropertyValuesHolder
                    .ofObject(Key_Pivot, floatArrEvaluator, containsPivot(fromMap) ? getPivot(fromMap) : getPivot(view), getPivot(toMap)));
            if (debug) {
                Log.e(null, "Pivot from " + Arrays.toString(containsPivot(fromMap) ? getPivot(fromMap) : getPivot(view)) + " to " +
                        Arrays.toString(getPivot(toMap)));
            }
        }
        if (containsPivotX(toMap)) {
            holderList.add(PropertyValuesHolder.ofFloat(Key_PivotX, containsPivotX(fromMap) ? getPivotX(fromMap) : getPivotX(view), getPivotX(toMap)));
            if (debug) {
                Log.e(null, "PivotX from " + (containsPivotX(fromMap) ? getPivotX(fromMap) : getPivotX(view)) + " to " +
                        getPivotX(toMap));
            }
        }
        if (containsPivotY(toMap)) {
            holderList.add(PropertyValuesHolder.ofFloat(Key_PivotY, containsPivotY(fromMap) ? getPivotY(fromMap) : getPivotY(view), getPivotY(toMap)));
            if (debug) {
                Log.e(null, "PivotY from " + (containsPivotY(fromMap) ? getPivotY(fromMap) : getPivotY(view)) + " to " +
                        getPivotY(toMap));
            }
        }
        if (containsRotation(toMap)) {
            holderList
                    .add(PropertyValuesHolder.ofFloat(Key_Rotation, containsRotation(fromMap) ? getRotation(fromMap) : getRotation(view), getRotation(toMap)));
            if (debug) {
                Log.e(null, "Rotation from " + (containsRotation(fromMap) ? getRotation(fromMap) : getRotation(view)) + " to " +
                        getRotation(toMap));
            }
        }
        if (containsRotationX(toMap)) {
            holderList.add(PropertyValuesHolder
                    .ofFloat(Key_RotationX, containsRotationX(fromMap) ? getRotationX(fromMap) : getRotationX(view), getRotationX(toMap)));
            if (debug) {
                Log.e(null, "RotationX from " + (containsRotationX(fromMap) ? getRotationX(fromMap) : getRotationX(view)) + " to " +
                        getRotationX(toMap));
            }
        }
        if (containsRotationY(toMap)) {
            holderList.add(PropertyValuesHolder
                    .ofFloat(Key_RotationY, containsRotationY(fromMap) ? getRotationY(fromMap) : getRotationY(view), getRotationY(toMap)));
            if (debug) {
                Log.e(null, "RotationY from " + (containsRotationY(fromMap) ? getRotationY(fromMap) : getRotationY(view)) + " to " +
                        getRotationY(toMap));
            }
        }
        if (containsScale(toMap)) {
            holderList.add(PropertyValuesHolder
                    .ofObject(Key_Scale, floatArrEvaluator, containsScale(fromMap) ? getScale(fromMap) : getScale(view), getScale(toMap)));
            if (debug) {
                Log.e(null, "Scale from " + Arrays.toString(containsScale(fromMap) ? getScale(fromMap) : getScale(view)) + " to " +
                        Arrays.toString(getScale(toMap)));
            }
        }
        if (containsScaleX(toMap)) {
            holderList.add(PropertyValuesHolder.ofFloat(Key_ScaleX, containsScaleX(fromMap) ? getScaleX(fromMap) : getScaleX(view), getScaleX(toMap)));
            if (debug) {
                Log.e(null, "ScaleX from " + (containsScaleX(fromMap) ? getScaleX(fromMap) : getScaleX(view)) + " to " +
                        getScaleX(toMap));
            }
        }
        if (containsScaleY(toMap)) {
            holderList.add(PropertyValuesHolder.ofFloat(Key_ScaleY, containsScaleY(fromMap) ? getScaleY(fromMap) : getScaleY(view), getScaleY(toMap)));
            if (debug) {
                Log.e(null, "ScaleY from " + (containsScaleY(fromMap) ? getScaleY(fromMap) : getScaleY(view)) + " to " +
                        getScaleY(toMap));
            }
        }
        if (containsTranslation(toMap)) {
            holderList.add(PropertyValuesHolder
                    .ofObject(Key_Translation, floatArrEvaluator, containsTranslation(fromMap) ? getTranslation(fromMap) : getTranslation(view),
                            getTranslation(toMap)));
            if (debug) {
                Log.e(null, "Translation from " + Arrays.toString(containsTranslation(fromMap) ? getTranslation(fromMap) : getTranslation(view)) + " to " +
                        Arrays.toString(getTranslation(toMap)));
            }
        }
        if (containsTranslationX(toMap)) {
            holderList.add(PropertyValuesHolder
                    .ofFloat(Key_TranslationX, containsTranslationX(fromMap) ? getTranslationX(fromMap) : getTranslationX(view), getTranslationX(toMap)));
            if (debug) {
                Log.e(null, "TranslationX from " +
                        (containsTranslationX(fromMap) ? getTranslationX(fromMap) : getTranslationX(view)) + " to " +
                        getTranslationX(toMap));
            }
        }
        if (containsTranslationY(toMap)) {
            holderList.add(PropertyValuesHolder
                    .ofFloat(Key_TranslationY, containsTranslationY(fromMap) ? getTranslationY(fromMap) : getTranslationY(view), getTranslationY(toMap)));
            if (debug) {
                Log.e(null, "TranslationY from " +
                        (containsTranslationY(fromMap) ? getTranslationY(fromMap) : getTranslationY(view)) + " to " +
                        getTranslationY(toMap));
            }
        }
        if (containsCoordinate(toMap)) {
            holderList.add(PropertyValuesHolder
                    .ofObject(Key_Coordinate, floatArrEvaluator, containsCoordinate(fromMap) ? getCoordinate(fromMap) : getCoordinate(view),
                            getCoordinate(toMap)));
            if (debug) {
                Log.e(null, "Coordinate from " + Arrays.toString((containsCoordinate(fromMap) ? getCoordinate(fromMap) : getCoordinate(view))) + " to " +
                        Arrays.toString(getCoordinate(toMap)));
            }
        }
        if (containsX(toMap)) {
            holderList.add(PropertyValuesHolder.ofFloat(Key_X, containsX(fromMap) ? getX(fromMap) : getX(view), getX(toMap)));
            if (debug) {
                Log.e(null, "X from " + (containsX(fromMap) ? getX(fromMap) : getX(view)) + " to " +
                        getX(toMap));
            }
        }
        if (containsY(toMap)) {
            holderList.add(PropertyValuesHolder.ofFloat(Key_Y, containsY(fromMap) ? getY(fromMap) : getY(view), getY(toMap)));
            if (debug) {
                Log.e(null, "Y from " + (containsY(fromMap) ? getY(fromMap) : getY(view)) + " to " +
                        getY(toMap));
            }
        }
        if (containsBackgroundColor(toMap)) {
            holderList.add(PropertyValuesHolder
                    .ofObject(Key_BackgroundColor, rgbEvaluator, containsBackgroundColor(fromMap) ? getBackgroundColor(fromMap) : getBackgroundColor(view),
                            getBackgroundColor(toMap)));
            if (debug) {
                Log.e(null, "BackgroundColor from " +
                        Integer.toHexString(containsBackgroundColor(fromMap) ? getBackgroundColor(fromMap) : getBackgroundColor(view)) + " to " +
                        Integer.toHexString(getBackgroundColor(toMap)));
            }
        }
        if (containsTextColor(toMap)) {
            holderList.add(PropertyValuesHolder
                    .ofObject(Key_TextColor, rgbEvaluator, containsTextColor(fromMap) ? getTextColor(fromMap) : getTextColor(view), getTextColor(toMap)));
            if (debug) {
                Log.e(null, "TextColor from " +
                        Integer.toHexString(containsTextColor(fromMap) ? getTextColor(fromMap) : getTextColor(view)) + " to " +
                        Integer.toHexString(getTextColor(toMap)));
            }
        }

        return ValueAnimator.ofPropertyValuesHolder(holderList.toArray(new PropertyValuesHolder[holderList.size()]));
    }

    public static void set(@NonNull View view, @NonNull Map<String, Object> map) {
        if (containsAlpha(map)) {
            setAlpha(view, getAlpha(map));
        }
        if (containsSize(map)) {
            setSize(view, getSize(map));
        }
        if (containsWidth(map)) {
            setWidth(view, getWidth(map));
        }
        if (containsHeight(map)) {
            setHeight(view, getHeight(map));
        }
        if (containsPivot(map)) {
            setPivot(view, getPivot(map));
        }
        if (containsPivotX(map)) {
            setPivotX(view, getPivotX(map));
        }
        if (containsPivotY(map)) {
            setPivotY(view, getPivotX(map));
        }
        if (containsRotation(map)) {
            setRotation(view, getRotation(map));
        }
        if (containsRotationX(map)) {
            setRotation(view, getRotationX(map));
        }
        if (containsRotationY(map)) {
            setRotation(view, getRotationY(map));
        }
        if (containsScale(map)) {
            setScale(view, getScale(map));
        }
        if (containsScaleX(map)) {
            setScaleX(view, getScaleX(map));
        }
        if (containsScaleY(map)) {
            setScaleY(view, getScaleY(map));
        }
        if (containsTranslation(map)) {
            setTranslation(view, getTranslation(map));
        }
        if (containsTranslationX(map)) {
            setTranslationX(view, getTranslationX(map));
        }
        if (containsTranslationY(map)) {
            setTranslationY(view, getTranslationY(map));
        }
        if (containsCoordinate(map)) {
            setCoordinate(view, getCoordinate(map));
        }
        if (containsX(map)) {
            setX(view, getX(map));
        }
        if (containsY(map)) {
            setY(view, getY(map));
        }
        if (containsBackgroundColor(map)) {
            setBackgroundColor(view, getBackgroundColor(map));
        }
        if (containsTextColor(map)) {
            setTextColor(view, getTextColor(map));
        }
    }

    public static void setAlpha(@NonNull Map<String, Object> map, float alpha) {
        map.put(Key_Alpha, alpha);
    }

    public static void setAlpha(@NonNull View view, float alpha) {
        ViewHelper.setAlpha(view, alpha);
    }

    public static void setBackgroundColor(@NonNull Map<String, Object> map, int backgroundColor) {
        map.put(Key_BackgroundColor, backgroundColor);
    }

    public static void setBackgroundColor(@NonNull View view, int backgroundColor) {
        view.setBackgroundColor(backgroundColor);
    }

    public static void setCoordinate(@NonNull View view, float[] coordinate) {
        setCoordinate(view, coordinate[0], coordinate[1]);
    }

    public static void setCoordinate(@NonNull View view, float x, float y) {
        ViewHelper.setX(view, x);
        ViewHelper.setY(view, y);
    }

    public static void setCoordinate(@NonNull Map<String, Object> map, float x, float y) {
        map.put(Key_Coordinate, new float[]{x, y});
    }

    public static void setHeight(@NonNull Map<String, Object> map, int height) {
        map.put(Key_Height, height);
    }

    public static void setHeight(@NonNull View view, int height) {
        ViewGroup.LayoutParams params = view.getLayoutParams();

        params.height = height;
        view.setLayoutParams(params);
    }

    public static void setPivot(@NonNull Map<String, Object> map, float[] pivot) {
        map.put(Key_Pivot, pivot);
    }

    public static void setPivot(@NonNull Map<String, Object> map, float pivotX, float pivotY) {
        map.put(Key_Pivot, new float[]{pivotX, pivotY});
    }

    public static void setPivot(@NonNull View view, float[] pivot) {
        ViewHelper.setPivotX(view, pivot[0]);
        ViewHelper.setPivotX(view, pivot[1]);
    }

    public static void setPivotX(@NonNull Map<String, Object> map, float pivotX) {
        map.put(Key_PivotX, pivotX);
    }

    public static void setPivotX(@NonNull View view, float pivotX) {
        ViewHelper.setPivotX(view, pivotX);
    }

    public static void setPivotY(@NonNull Map<String, Object> map, float pivotY) {
        map.put(Key_PivotY, pivotY);
    }

    public static void setPivotY(@NonNull View view, float pivotY) {
        ViewHelper.setPivotY(view, pivotY);
    }

    public static void setRotation(@NonNull Map<String, Object> map, float rotation) {
        map.put(Key_Rotation, rotation);
    }

    public static void setRotation(@NonNull View view, float rotation) {
        ViewHelper.setRotation(view, rotation);
    }

    public static void setRotationX(@NonNull Map<String, Object> map, float rotationX) {
        map.put(Key_RotationX, rotationX);
    }

    public static void setRotationX(@NonNull View view, float rotationX) {
        ViewHelper.setRotationX(view, rotationX);
    }

    public static void setRotationY(@NonNull Map<String, Object> map, float rotationY) {
        map.put(Key_RotationY, rotationY);
    }

    public static void setRotationY(@NonNull View view, float rotationY) {
        ViewHelper.setRotationY(view, rotationY);
    }

    public static void setScale(@NonNull Map<String, Object> map, float scaleX, float scaleY) {
        map.put(Key_Scale, new float[]{scaleX, scaleY});
    }

    public static void setScale(@NonNull View view, float scaleX, float scaleY) {
        ViewHelper.setScaleX(view, scaleX);
        ViewHelper.setScaleY(view, scaleY);
    }

    public static void setScale(@NonNull View view, float[] scale) {
        setScale(view, scale[0], scale[1]);
    }

    public static void setScaleX(@NonNull Map<String, Object> map, float scaleX) {
        map.put(Key_ScaleX, scaleX);
    }

    public static void setScaleX(@NonNull View view, float scaleX) {
        ViewHelper.setScaleX(view, scaleX);
    }

    public static void setScaleY(@NonNull Map<String, Object> map, float scaleY) {
        map.put(Key_ScaleY, scaleY);
    }

    public static void setScaleY(@NonNull View view, float scaleY) {
        ViewHelper.setScaleY(view, scaleY);
    }

    public static void setSize(@NonNull Map<String, Object> map, int width, int height) {
        map.put(Key_Size, new int[]{width, height});
    }

    public static void setSize(@NonNull View view, int width, int height) {
        ViewGroup.LayoutParams params;

        params = view.getLayoutParams();
        params.width = width;
        params.height = height;
        view.setLayoutParams(params);
    }

    public static void setSize(@NonNull View view, @NonNull int[] size) {
        setSize(view, size[0], size[1]);
    }

    public static void setTextColor(@NonNull Map<String, Object> map, int backgroundColor) {
        map.put(Key_TextColor, backgroundColor);
    }

    public static void setTextColor(@NonNull View view, int backgroundColor) {
        if (view instanceof TextView) {
            ((TextView) view).setTextColor(backgroundColor);
        }
    }

    public static void setTranslation(@NonNull Map<String, Object> map, float translationX, float translationY) {
        map.put(Key_Translation, new float[]{translationX, translationY});
    }

    public static void setTranslation(@NonNull View view, float[] translation) {
        ViewHelper.setTranslationX(view, translation[0]);
        ViewHelper.setTranslationX(view, translation[1]);
    }

    public static void setTranslationX(@NonNull Map<String, Object> map, float translationX) {
        map.put(Key_BackgroundColor, translationX);
    }

    public static void setTranslationX(@NonNull View view, float translationX) {
        ViewHelper.setTranslationX(view, translationX);
    }

    public static void setTranslationY(@NonNull Map<String, Object> map, float translationY) {
        map.put(Key_TranslationY, translationY);
    }

    public static void setTranslationY(@NonNull View view, float translationY) {
        ViewHelper.setTranslationY(view, translationY);
    }

    public static void setVisibility(@NonNull View view, int visibility) {
        view.setVisibility(visibility);
    }

    public static void setVisibility(@NonNull Map<String, Object> map, int visibility) {
        map.put(Key_Visibility, visibility);
    }

    public static void setVisibilityReverse(@NonNull Map<String, Object> map, int visibilityReverse) {
        map.put(Key_VisibilityReverse, visibilityReverse);
    }

    public static void setWidth(@NonNull Map<String, Object> map, int width) {
        map.put(Key_Width, width);
    }

    public static void setWidth(@NonNull View view, int width) {
        ViewGroup.LayoutParams params = view.getLayoutParams();

        params.width = width;
        view.setLayoutParams(params);
    }

    public static void setX(@NonNull Map<String, Object> map, float x) {
        map.put(Key_X, x);
    }

    public static void setX(@NonNull View view, float x) {
        ViewHelper.setX(view, x);
    }

    public static void setY(@NonNull Map<String, Object> map, float y) {
        map.put(Key_Y, y);
    }

    public static void setY(@NonNull View view, float y) {
        ViewHelper.setY(view, y);
    }

    public static void update(@NonNull View view, @NonNull ValueAnimator animation) {
        Object value;

        value = animation.getAnimatedValue(Key_Alpha);
        if (value != null) {
            setAlpha(view, (float) value);
        }
        value = animation.getAnimatedValue(Key_Size);
        if (value != null) {
            setSize(view, (int[]) value);
        }
        value = animation.getAnimatedValue(Key_Width);
        if (value != null) {
            setWidth(view, (int) value);
        }
        value = animation.getAnimatedValue(Key_Height);
        if (value != null) {
            setHeight(view, (int) value);
        }
        value = animation.getAnimatedValue(Key_Pivot);
        if (value != null) {
            setPivot(view, (float[]) value);
        }
        value = animation.getAnimatedValue(Key_PivotX);
        if (value != null) {
            setPivotX(view, (float) value);
        }
        value = animation.getAnimatedValue(Key_PivotY);
        if (value != null) {
            setPivotY(view, (float) value);
        }
        value = animation.getAnimatedValue(Key_Rotation);
        if (value != null) {
            setRotation(view, (float) value);
        }
        value = animation.getAnimatedValue(Key_RotationX);
        if (value != null) {
            setRotationX(view, (float) value);
        }
        value = animation.getAnimatedValue(Key_RotationY);
        if (value != null) {
            setRotationY(view, (float) value);
        }
        value = animation.getAnimatedValue(Key_Scale);
        if (value != null) {
            setScale(view, (float[]) value);
        }
        value = animation.getAnimatedValue(Key_ScaleX);
        if (value != null) {
            setScaleX(view, (float) value);
        }
        value = animation.getAnimatedValue(Key_ScaleY);
        if (value != null) {
            setScaleY(view, (float) value);
        }
        value = animation.getAnimatedValue(Key_Translation);
        if (value != null) {
            setTranslation(view, (float[]) value);
        }
        value = animation.getAnimatedValue(Key_TranslationX);
        if (value != null) {
            setTranslationX(view, (float) value);
        }
        value = animation.getAnimatedValue(Key_TranslationY);
        if (value != null) {
            setTranslationY(view, (float) value);
        }
        value = animation.getAnimatedValue(Key_Coordinate);
        if (value != null) {
            setCoordinate(view, (float[]) value);
        }
        value = animation.getAnimatedValue(Key_X);
        if (value != null) {
            setX(view, (float) value);
        }
        value = animation.getAnimatedValue(Key_Y);
        if (value != null) {
            setY(view, (float) value);
        }
        value = animation.getAnimatedValue(Key_BackgroundColor);
        if (value != null) {
            setBackgroundColor(view, (int) value);
        }
        value = animation.getAnimatedValue(Key_TextColor);
        if (value != null) {
            setTextColor(view, (int) value);
        }
    }
}