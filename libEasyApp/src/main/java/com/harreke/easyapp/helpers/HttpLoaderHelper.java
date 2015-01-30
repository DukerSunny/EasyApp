package com.harreke.easyapp.helpers;

import android.content.Context;

import com.harreke.easyapp.requests.IRequestCallback;
import com.harreke.easyapp.requests.IRequestExecutor;
import com.harreke.easyapp.requests.RequestBuilder;
import com.harreke.easyapp.requests.ion.IonHttpExecutor;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/08/04
 */
public class HttpLoaderHelper {
    /**
     * 创建一个Http请求执行器
     *
     * 默认使用Ion的Http库
     * 若需要使用其他Http库，请重写该函数
     *
     * @param context
     *         Context
     * @param builder
     *         请求构造器
     * @param callback
     *         请求回调
     *
     * @return Ion Http请求执行器
     */
    public static IRequestExecutor loadString(Context context, RequestBuilder builder, IRequestCallback<String> callback) {
        return new IonHttpExecutor(context, builder, callback);
    }
}