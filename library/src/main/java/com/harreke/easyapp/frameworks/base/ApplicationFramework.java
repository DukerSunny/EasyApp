package com.harreke.easyapp.frameworks.base;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;

import com.harreke.easyapp.helpers.LoaderHelper;
import com.harreke.easyapp.utils.FileUtil;
import com.harreke.easyapp.widgets.RandomGUID;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

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
    public static float TouchThreshold;
    private static final String TAG = "ApplicationFramework";
    private static ApplicationFramework mInstance = null;
    private boolean mAssetsEnabled = false;
    private boolean mMiscsEnabled = false;
    private boolean mTempsEnabled = false;

    public static ApplicationFramework getInstance() {
        return mInstance;
    }

    public static String getMd5UUID(@NonNull IFramework framework) {
        ApplicationFramework application = framework.getApplicationFramework();
        String uuid = application.readString("device_id_md5", null);

        if (uuid == null) {
            uuid = new RandomGUID().valueAfterMD5;
            application.writeString("device_id_md5", uuid);
        }

        return uuid;
    }

    public static String getUUID(@NonNull IFramework framework) {
        ApplicationFramework application = framework.getApplicationFramework();
        String uuid = application.readString("device_id", null);

        if (uuid == null) {
            uuid = UUID.randomUUID().toString();
            application.writeString("device_id", uuid);
        }

        return uuid;
    }

    public static String getVersion(@NonNull Context context) {
        PackageManager manager = context.getPackageManager();
        PackageInfo info;
        String version;

        try {
            info = manager.getPackageInfo(context.getPackageName(), 0);
            version = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            version = "";
        }

        return version;
    }

    private static Bundle readMetaData(@NonNull Context context) {
        ApplicationInfo info;
        Bundle bundle;

        try {
            info = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            bundle = info.metaData;
        } catch (PackageManager.NameNotFoundException e) {
            bundle = null;
        }

        return bundle;
    }

    public static int readMetaDataInt(@NonNull Context context, @NonNull String key) {
        Bundle metaData = readMetaData(context);

        if (metaData != null) {
            return metaData.getInt(key, 0);
        } else {
            return 0;
        }
    }

    public static String readMetaDataString(@NonNull Context context, @NonNull String key) {
        Bundle metaData = readMetaData(context);

        if (metaData != null) {
            return metaData.getString(key);
        } else {
            return null;
        }
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
    public final boolean copyAsset(@NonNull String assetName, @NonNull String targetPath) {
        File file;
        boolean success = false;
        if (mAssetsEnabled && assetName.length() > 0) {
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
        DisplayMetrics metrics = getResources().getDisplayMetrics();

        Density = metrics.density;
        TouchThreshold = Density * 8f;
        CacheDir = getCacheDir().getAbsolutePath();
        StorageDir = Environment.getExternalStorageDirectory().getAbsolutePath();
        LoaderHelper.init(this);

        mAssetsEnabled = createCacheDir(DIR_ASSETS);
        mTempsEnabled = createCacheDir(DIR_TEMPS);
        mMiscsEnabled = createCacheDir(DIR_MISCS);
        mInstance = this;
    }

    public final boolean readBoolean(@NonNull String key, boolean defaultValue) {
        return getPreference().getBoolean(key, defaultValue);
    }

    /**
     * 从文档中读取一个整型数据
     *
     * @param key
     *         索引名
     * @param defaultValue
     *         缺省索引值
     *
     * @return 索引值
     */
    public final int readInt(@NonNull String key, int defaultValue) {
        return getPreference().getInt(key, defaultValue);
    }

    /**
     * 从文档中读取一个长整型数据
     *
     * @param key
     *         索引名
     * @param defaultValue
     *         缺省索引值
     *
     * @return 索引值
     */
    public final long readLong(@NonNull String key, int defaultValue) {
        return getPreference().getLong(key, defaultValue);
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
    public final String readString(@NonNull String key, @Nullable String defaultValue) {
        return getPreference().getString(key, defaultValue);
    }

    public final void writeBoolean(@NonNull String key, boolean value) {
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
    public final void writeInt(@NonNull String key, int value) {
        SharedPreferences.Editor editor = getPreference().edit();

        editor.putInt(key, value);
        editor.apply();
    }

    /**
     * 将一个长整型数据写入文档
     *
     * @param key
     *         索引名
     * @param value
     *         索引值
     */
    public final void writeLong(@NonNull String key, long value) {
        SharedPreferences.Editor editor = getPreference().edit();

        editor.putLong(key, value);
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
    public final void writeString(@NonNull String key, @Nullable String value) {
        SharedPreferences.Editor editor = getPreference().edit();

        editor.putString(key, value);
        editor.commit();
    }
}