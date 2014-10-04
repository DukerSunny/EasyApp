package com.harreke.easyapp.frameworks.bases.application;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.harreke.easyapp.configs.ImageExecutorConfig;
import com.harreke.easyapp.tools.FileUtil;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

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
     * 缓存目录
     */
    public static final String DIR_CACHES = "caches";
    /**
     * 数据库目录
     */
    public static final String DIR_DATABASES = "databases";
    /**
     * 杂项目录
     */
    public static final String DIR_MISCS = "miscs";
    /**
     * app临时目录
     */
    public static String CacheDir;
    public static float Density;
    public static ApplicationFramework mInstance = null;
    private static final String TAG = "ApplicationFramework";
    private boolean mAssetsEnabled = false;
    private boolean mCachesEnabled = false;
    private boolean mMiscsEnabled = false;

    public static ApplicationFramework getInstance() {
        return mInstance;
    }

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
            file = new File(CacheDir + "/" + DIR_ASSETS + "/" + targetPath + "/" + assetName);
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
    public final boolean createDir(String dir) {
        File file;
        boolean success = true;

        file = new File(CacheDir + "/" + dir);
        if (!file.exists()) {
            if (!file.mkdir()) {
                success = false;
                Log.e(TAG, "Cannot create \"" + dir + "\" directory!");
            }
        } else if (!file.isDirectory()) {
            success = false;
            Log.e(TAG, "Cannot access \"" + dir + "\" as directory!");
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
     * 判断缓存目录是否可用
     *
     * @return 缓存目录是否可用
     */
    public boolean isCachesEnabled() {
        return mCachesEnabled;
    }

    /**
     * 判断杂项目录是否可用
     *
     * @return 杂项目录是否可用
     */
    public boolean isMiscsEnabled() {
        return mMiscsEnabled;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;

        Density = getResources().getDisplayMetrics().density;
        CacheDir = getCacheDir().getAbsolutePath();

        mAssetsEnabled = createDir(DIR_ASSETS);
        mCachesEnabled = createDir(DIR_CACHES);
        mMiscsEnabled = createDir(DIR_MISCS);

        ImageExecutorConfig.config(this);
    }

    /**
     * 从文档中读取一个整型数据
     *
     * @param key
     *         索引名
     *
     * @return 索引值
     */
    public final int readInt(String key, int defalutValue) {
        return getPreference().getInt(key, defalutValue);
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
        editor.commit();
    }
}