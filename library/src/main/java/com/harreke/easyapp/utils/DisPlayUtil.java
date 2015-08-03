package com.harreke.easyapp.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by neavo on 14-3-15.
 */

public class DisPlayUtil {

	public static final  float getDensity(Context ctx) {
		return ctx.getResources().getDisplayMetrics().density;
	}

    /**
     * 设置弹出框的默认大小
     */
    public static void setDefaultDialogSize(Activity activity){
        int height = (int)(DisPlayUtil.getScreenHeight(activity)*0.7);
        int width = (int)(DisPlayUtil.getScreenWidth(activity)*0.5);
        setDialogSize(activity,height,width);
    }

    /**
     * 设置默认弹出框的大小
     */
    public static void setDialogSize(Activity activity,int height,int width){
        WindowManager.LayoutParams p = activity.getWindow().getAttributes();  //获取对话框当前的参数值
        p.height = height;
        p.width = width;
        activity.getWindow().setAttributes(p);
    }

	public static final  int toPx(Context ctx, float dp) {
		return Math.round(dp * ctx.getResources().getDisplayMetrics().density);
	}

	public static final  int toDp(Context ctx, float px) {
		return Math.round((px / ctx.getResources().getDisplayMetrics().density));
	}

    public static  int getScreenRealWidth(Activity activity) {
        int widthPixels;
        WindowManager w = activity.getWindowManager();
        Display d = w.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        d.getMetrics(metrics);
        // since SDK_INT = 1;
        widthPixels = metrics.widthPixels;
        if(Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 17) {
            try {
                widthPixels = (Integer) Display.class.getMethod("getRawWidth").invoke(d);
            }
            catch (Exception ignored) {

            }
        } else if(Build.VERSION.SDK_INT >= 17) {
            try {
                DisplayMetrics realMetrics = new DisplayMetrics();
                d.getRealMetrics(realMetrics);
                widthPixels = realMetrics.widthPixels;
            }
            catch (Exception ignored) {

            }
        } else {
            try {
                widthPixels = (Integer) Display.class.getMethod("getRealWidth").invoke(d);
            }
            catch (Exception ignored) {

            }
        }
        return widthPixels;
    }

    public static int getNavigationBarHeight(Activity mActivity) {
        Resources resources = mActivity.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height","dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        LogUtil.v("dbw", "Navi height:" + height);
        return height;
    }



    /**
     * 得到屏幕真实高度包含NavigationBar
     * @param activity
     * @return
     */
    public static int getScreenRealHeight(Activity activity) {
        int heightPixels;
        WindowManager w = activity.getWindowManager();
        Display d = w.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        d.getMetrics(metrics);
        // since SDK_INT = 1;
        heightPixels = metrics.heightPixels;
        // includes window decorations (statusbar bar/navigation bar)
        if (Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 17)
            try {
                heightPixels = (Integer) Display.class
                        .getMethod("getRawHeight").invoke(d);
            } catch (Exception ignored) {
            }
            // includes window decorations (statusbar bar/navigation bar)
        else if (Build.VERSION.SDK_INT >= 17)
            try {
                android.graphics.Point realSize = new android.graphics.Point();
                Display.class.getMethod("getRealSize",
                        android.graphics.Point.class).invoke(d, realSize);
                heightPixels = realSize.y;
            } catch (Exception ignored) {
            }
        return heightPixels;
    }

    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 获取屏幕高度，不包括虚拟条
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 判断屏幕是否可以旋转
     * @param activity
     * @return
     */
    public static boolean enableScreenRotation(Activity activity) {
        int value = Settings.System.getInt(activity.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION,1);
        return value==1;
    }




    public static float getScreenBrightness(Activity activity) {
        int value = 0;
        ContentResolver cr = activity.getContentResolver();
        try {
            value = Settings.System.getInt(cr, Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {

        }
        return value/255f;
    }

    public static void setScreenBrightness(Activity activity,float brightness) {
        ContentResolver resolver = activity.getContentResolver();
        try
        {
            if(Settings.System.getInt(resolver,Settings.System.SCREEN_BRIGHTNESS_MODE) ==
                    Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC)
            {
                Settings.System.putInt(resolver,
                        Settings.System.SCREEN_BRIGHTNESS_MODE,
                        Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
            }
        }
        catch (Settings.SettingNotFoundException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Settings.System.putInt(resolver, Settings.System.SCREEN_BRIGHTNESS, (int)(brightness*255));
    }
}
