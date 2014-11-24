package com.harreke.easyapp.frameworks.bases.application;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;

import com.harreke.easyapp.configs.ImageExecutorConfig;
import com.harreke.easyapp.tools.FileUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/30
 *
 * Application框架
 */
public class ApplicationFramework extends Application {
    /**
     * 附件目录
     */
    public static final String DIR_ASSETS = "assets";
    /**
     * 数据库目录
     */
    public static final String DIR_DATABASES = "databases";
    /**
     * 杂项目录
     */
    public static final String DIR_MISCS = "miscs";
    /**
     * 缓存目录
     */
    public static final String DIR_TEMPS = "temps";
    /**
     * app临时目录
     */
    public static String CacheDir;
    public static float Density;
    public static String StorageDir;
    private static final String TAG = "ApplicationFramework";
    private boolean mAssetsEnabled = false;
    private boolean mMiscsEnabled = false;
    private boolean mTempsEnabled = false;

    /**
     * 将指定附件拷贝至设备内存
     *
     * @param assetName
     *         附件文件名
     *
     *         附件文件名需要使用相对路径，比如”logo.png“，”pic/home.png“
     * @param targetPath
     *         目标路径
     *
     *         目标路径需要使用相对路径（相对于app临时目录下的assets文件夹），该路径必须存在
     *
     * @return 是否拷贝成功
     */
    public final boolean copyAsset(String assetName, String targetPath) {
        File file;
        boolean success = false;
        if (mAssetsEnabled && assetName != null && assetName.length() > 0 && targetPath != null) {
            file = new File(CacheDir + "/" + DIR_ASSETS + "/" + targetPath + (targetPath.length() == 0 ? "" : "/") + assetName);
            try {
                if (!file.exists() || file.createNewFile() || file.canWrite()) {
                    FileUtil.copyFile(getAssets().open(assetName), new FileOutputStream(file));
                    success = true;
                }
            } catch (IOException e) {
                Log.e(TAG, "Copy assets " + file.getAbsolutePath() + "  error!");
            }
        }

        return success;
    }

    /**
     * 在设备内存中创建指定目录
     *
     * @param dir
     *         目录名称
     *
     *         目录名称需要使用相对路径，比如”pic“
     *
     * @return 是否创建成功
     */
    public final boolean createCacheDir(String dir) {
        File file;
        boolean success = true;

        file = new File(CacheDir + "/" + dir);
        if (!file.exists()) {
            if (!file.mkdir()) {
                success = false;
                Log.e(TAG, "Cannot create cache/" + dir + " directory!");
            }
        } else if (!file.isDirectory()) {
            success = false;
            Log.e(TAG, "Cannot access cache/" + dir + " as directory!");
        }

        return success;
    }

    public final boolean createStorageDir(String dir) {
        File file;
        boolean success = true;

        file = new File(StorageDir + "/" + dir);
        if (!file.exists()) {
            if (!file.mkdir()) {
                success = false;
                Log.e(TAG, "Cannot create storage/" + dir + " directory!");
            }
        } else if (!file.isDirectory()) {
            success = false;
            Log.e(TAG, "Cannot access storage/" + dir + " as directory!");
        }

        return success;
    }

    private SharedPreferences getPreference() {
        return PreferenceManager.getDefaultSharedPreferences(this);
    }

    /**
     * 判断附件目录是否可用
     *
     * @return 附件目录是否可用
     */
    public boolean isAssetsEnabled() {
        return mAssetsEnabled;
    }

    /**
     * 判断杂项目录是否可用
     *
     * @return 杂项目录是否可用
     */
    public boolean isMiscsEnabled() {
        return mMiscsEnabled;
    }

    /**
     * 判断缓存目录是否可用
     *
     * @return 缓存目录是否可用
     */
    public boolean isTempsEnabled() {
        return mTempsEnabled;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Density = getResources().getDisplayMetrics().density;
        CacheDir = getCacheDir().getAbsolutePath();
        StorageDir = Environment.getExternalStorageDirectory().getAbsolutePath();

        mAssetsEnabled = createCacheDir(DIR_ASSETS);
        mTempsEnabled = createCacheDir(DIR_TEMPS);
        mMiscsEnabled = createCacheDir(DIR_MISCS);

        ImageExecutorConfig.config(this);
    }

    public final boolean readBoolean(String key, boolean defaultVaule) {
        return getPreference().getBoolean(key, defaultVaule);
    }

    /**
     * 从文档中读取一个整型数据
     *
     * @param key
     *         索引名
     * @param defalutValue
     *         缺省索引值
     *
     * @return 索引值
     */
    public final int readInt(String key, int defalutValue) {
        return getPreference().getInt(key, defalutValue);
    }

    /**
     * 从文档中读取一个字符串
     *
     * @param key
     *         索引名
     * @param defaultValue
     *         缺省索引值
     *
     * @return 索引值
     */
    public final String readString(String key, String defaultValue) {
        return getPreference().getString(key, defaultValue);
    }

    public final void writeBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = getPreference().edit();

        editor.putBoolean(key, value);

        editor.apply();
    }

    /**
     * 将一个整型数据写入文档
     *
     * @param key
     *         索引名
     * @param value
     *         索引值
     */
    public final void writeInt(String key, int value) {
        SharedPreferences.Editor editor = getPreference().edit();

        editor.putInt(key, value);
        editor.apply();
    }

    /**
     * 将一个字符串写入文档
     *
     * @param key
     *         索引名
     * @param value
     *         索引值
     */
    public final void writeString(String key, String value) {
        SharedPreferences.Editor editor = getPreference().edit();

        editor.putString(key, value);
        editor.apply();
    }
}