package com.harreke.easyapp.frameworks.bases.application;

import android.app.Application;

import com.harreke.easyapp.tools.FileUtil;
import com.harreke.easyapp.tools.DevUtil;

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
    public static ApplicationFramework mInstance = null;
    private boolean mAssetsEnabled = false;
    private String mCacheDir;
    private boolean mCachesEnabled = false;
    private boolean mMiscsEnabled = false;

    public static ApplicationFramework getInstance() {
        return mInstance;
    }

    /**
     * 将指定附件拷贝至设备内存
     *
     * @param asset
     *         附件文件名
     *
     *         附件文件名需要使用相对路径，比如”logo.png“，”pic/home.png“
     *
     * @return 是否拷贝成功
     */
    public final boolean copyAsset(String asset) {
        File file;
        boolean success = false;

        if (mAssetsEnabled && asset != null && asset.length() > 0) {
            file = new File(mCacheDir + "/" + DIR_ASSETS + "/" + asset);
            try {
                if (!file.exists() || file.createNewFile() || file.canWrite()) {
                    FileUtil.copyFile(getAssets().open(asset), new FileOutputStream(file));
                    success = true;
                }
            } catch (IOException e) {
                DevUtil.e("Copy assets " + file.getAbsolutePath() + "  error!");
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

        file = new File(mCacheDir + "/" + dir);
        if (!file.exists()) {
            if (!file.mkdir()) {
                success = false;
                DevUtil.e("Cannot create \"" + dir + "\" directory!");
            }
        } else if (!file.isDirectory()) {
            success = false;
            DevUtil.e("Cannot access \"" + dir + "\" as directory!");
        }

        return success;
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

        mCacheDir = getCacheDir().getAbsolutePath();

        mAssetsEnabled = createDir(DIR_ASSETS);
        mCachesEnabled = createDir(DIR_CACHES);
        mMiscsEnabled = createDir(DIR_MISCS);
    }
}