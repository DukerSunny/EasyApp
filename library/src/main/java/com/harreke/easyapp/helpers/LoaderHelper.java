package com.harreke.easyapp.helpers;

import android.content.Context;
import android.support.annotation.NonNull;

import com.harreke.easyapp.frameworks.base.IFramework;
import com.harreke.easyapp.requests.IRequestCallback;
import com.harreke.easyapp.requests.androidasynchttp.AndroidAsyncHttpBinaryExecutor;
import com.harreke.easyapp.requests.androidasynchttp.AndroidAsyncHttpClient;
import com.harreke.easyapp.requests.androidasynchttp.AndroidAsyncHttpFileExecutor;
import com.harreke.easyapp.requests.androidasynchttp.AndroidAsyncHttpStringExecutor;
import com.harreke.easyapp.requests.executors.BinaryExecutor;
import com.harreke.easyapp.requests.executors.FileExecutor;
import com.harreke.easyapp.requests.executors.ImageExecutor;
import com.harreke.easyapp.requests.executors.StringExecutor;
import com.harreke.easyapp.requests.universalimageloader.UniversalImageLoaderImageExecutor;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/04/13
 */
public class LoaderHelper {
    /**
     * 取消指定框架中挂载的所有Http请求
     *
     * @param framework 框架
     */
    public static void cancelAll(@NonNull IFramework framework) {
        AndroidAsyncHttpClient.getInstance().cancelAll();
        ImageLoader.getInstance().stop();
    }

    public static void init(@NonNull Context context) {
        UniversalImageLoaderImageExecutor.init(context);
    }

    /**
     * 创建一个网络二进制数据加载器
     * <p>
     * 文件加载器可以从网络上下载二进制数据，支持异步加载
     * <p>
     * 异步加载使用{@link IRequestCallback}回调
     *
     * @return 文件加载器
     */
    public static BinaryExecutor makeBinaryExector() {
        return new AndroidAsyncHttpBinaryExecutor();
    }

    /**
     * 创建一个网络文件加载器
     * <p>
     * 文件加载器可以从网络上下载内容并写入指定文件，支持异步加载
     * <p>
     * 异步加载使用{@link IRequestCallback}回调
     *
     * @return 文件加载器
     */
    public static FileExecutor makeFileExecutor() {
        return new AndroidAsyncHttpFileExecutor();
    }

    /**
     * 创建一个网络图片加载器
     * <p>
     * 图片加载器可以从网络上下载内容并载入指定图片视图，支持异步加载
     * <p>
     * 异步加载使用{@link IRequestCallback}回调
     *
     * @return 图片加载器
     */
    public static ImageExecutor makeImageExecutor() {
        return new UniversalImageLoaderImageExecutor();
    }

    /**
     * 创建一个网络文本加载器
     * <p>
     * 文本加载器可以从网络上下载内容并转化为字符串，支持异步加载，支持上传文件
     * <p>
     * 异步加载使用{@link IRequestCallback}回调
     *
     * @return 文本加载器
     */
    public static StringExecutor makeStringExecutor() {
        return new AndroidAsyncHttpStringExecutor();
    }
}