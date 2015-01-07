package com.harreke.easyapp.requests;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.harreke.easyapp.enums.RequestMethod;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 *
 * Http请求构造器，支持GET和POST请求
 */
public class RequestBuilder {
    private final String TAG = "RequestBuilder";
    private String mBaseUrl;
    private Map<String, String> mBodyMap = new LinkedHashMap<String, String>();
    private Map<String, String> mHeaderMap = new LinkedHashMap<String, String>();
    private RequestMethod mMethod;
    private Map<String, String> mQueryMap = new LinkedHashMap<String, String>();

    /**
     * 构造Http请求
     *
     * @param method
     *         请求方式
     * @param baseUrl
     *         目标Url
     *
     * @see com.harreke.easyapp.enums.RequestMethod
     */
    public RequestBuilder(RequestMethod method, String baseUrl) {
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

    public final Map<String, String> getBody() {
        return mBodyMap;
    }

    public final Map<String, String> getHeader() {
        return mHeaderMap;
    }

    public final RequestMethod getMethod() {
        return mMethod;
    }

    public final Map<String, String> getQuery() {
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
                print = "POST " + getUrl() + "\nHeaders:\n" + JSON.toJSONString(mHeaderMap) + "\nBodies:\n" +
                        JSON.toJSONString(mBodyMap);
        }
        Log.e(TAG, print);
    }
}