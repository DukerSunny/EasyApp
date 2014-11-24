package com.harreke.easyapp.requests;

import android.util.Log;

import com.harreke.easyapp.tools.GsonUtil;

import java.util.HashMap;
import java.util.Iterator;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 *
 * Http请求构造器，支持GET和POST请求
 */
public class RequestBuilder {
    private final String TAG = "RequestBuilder";
    private String mBaseUrl;
    private HashMap<String, String> mBodyMap = new HashMap<String, String>();
    private HashMap<String, String> mHeaderMap = new HashMap<String, String>();
    private Method mMethod;
    private HashMap<String, String> mQueryMap = new HashMap<String, String>();

    /**
     * 构造Http请求
     *
     * @param method
     *         请求方式
     *         {@link com.harreke.easyapp.requests.RequestBuilder.Method}
     * @param baseUrl
     *         目标Url
     */
    public RequestBuilder(Method method, String baseUrl) {
        mMethod = method;
        mBaseUrl = baseUrl;
    }

    /**
     * 添加Body
     *
     * @param key
     *         Body名称
     * @param value
     *         Body内容
     *
     * @return 自身
     */
    public final RequestBuilder addBody(String key, Object value) {
        mBodyMap.put(key, String.valueOf(value));

        return this;
    }

    /**
     * 添加Header
     *
     * @param key
     *         Header名称
     * @param value
     *         Header内容
     *
     * @return 自身
     */
    public final RequestBuilder addHeader(String key, Object value) {
        mHeaderMap.put(key, String.valueOf(value));

        return this;
    }

    /**
     * 添加Query
     *
     * @param key
     *         Query名称
     * @param value
     *         Query内容
     *
     * @return 自身
     */
    public final RequestBuilder addQuery(String key, Object value) {
        mQueryMap.put(key, String.valueOf(value));

        return this;
    }

    public final HashMap<String, String> getBody() {
        return mBodyMap;
    }

    public final HashMap<String, String> getHeader() {
        return mHeaderMap;
    }

    public final Method getMethod() {
        return mMethod;
    }

    public final HashMap<String, String> getQuery() {
        return mQueryMap;
    }

    public final String getUrl() {
        Iterator<String> iterator = mQueryMap.keySet().iterator();
        String url = mBaseUrl;
        String key;

        if (iterator.hasNext()) {
            key = iterator.next();
            url += "?" + key + "=" + mQueryMap.get(key);
            while (iterator.hasNext()) {
                key = iterator.next();
                url += "&" + key + "=" + mQueryMap.get(key);
            }
        }

        return url;
    }

    public final void print() {
        String print = "";

        switch (mMethod) {
            case GET:
                print = "GET " + getUrl();
                break;
            case POST:
                print = "POST " + getUrl() + "\nHeaders:\n" + GsonUtil.toString(mHeaderMap) + "\nBodies:\n" + GsonUtil.toString(mBodyMap);
        }
        Log.e(TAG, print);
    }

    public enum Method {
        GET,
        POST
    }
}