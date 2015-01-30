package com.harreke.easyapp.requests;

import android.util.Log;

import com.harreke.easyapp.enums.RequestMethod;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 *
 * Http请求构造器，支持GET和POST请求
 */
public class RequestBuilder {
    private final String TAG = "RequestBuilder";
    private Map<String, String> mBodyMap = new TreeMap<String, String>();
    private Map<String, String> mHeaderMap = new TreeMap<String, String>();
    private String mHost;
    private RequestMethod mMethod;
    private String mPath;
    private Map<String, String> mQueryMap = new TreeMap<String, String>();

    /**
     * 构造Http请求
     *
     * @param method
     *         请求方式
     *
     * @see com.harreke.easyapp.enums.RequestMethod
     */
    public RequestBuilder(RequestMethod method) {
        mMethod = method;
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

    private String buildString(Map<String, String> map) {
        Iterator<String> iterator = map.keySet().iterator();
        StringBuilder result = new StringBuilder();
        String key;

        if (iterator.hasNext()) {
            key = iterator.next();
            result.append(key).append("=").append(map.get(key));
            while (iterator.hasNext()) {
                key = iterator.next();
                result.append("&").append(key).append("=").append(map.get(key));
            }
        }

        return result.toString();
    }

    public void clear() {
        clearHeader();
        clearQuery();
        clearBody();
    }

    public void clearBody() {
        mBodyMap.clear();
    }

    public void clearHeader() {
        mHeaderMap.clear();
    }

    public void clearQuery() {
        mQueryMap.clear();
    }

    public String getBaseUrl() {
        return mHost + "/" + mPath;
    }

    public final Map<String, String> getBody() {
        return mBodyMap;
    }

    public final String getBodyString() {
        return buildString(mBodyMap);
    }

    public final Map<String, String> getHeader() {
        return mHeaderMap;
    }

    public final String getHeaderString() {
        return buildString(mHeaderMap);
    }

    public String getHost() {
        return mHost;
    }

    public final RequestMethod getMethod() {
        return mMethod;
    }

    public String getPath() {
        return mPath;
    }

    public final Map<String, String> getQuery() {
        return mQueryMap;
    }

    public final String getQueryString() {
        return buildString(mQueryMap);
    }

    public final String getUrl() {
        return getBaseUrl() + "?" + getQueryString();
    }

    public final void print() {
        String print = "";

        switch (mMethod) {
            case GET:
                print = "GET " + getBaseUrl() + "?" + getQueryString();
                break;
            case POST:
                print = "POST " + getBaseUrl() + "?" + getQueryString() + "\nHeaders:\n" + getHeaderString() + "\nBodies:\n" +
                        getBodyString();
                break;
        }
        Log.e(TAG, print);
    }

    public final RequestBuilder setBaseUrl(String host, String path) {
        mHost = host;
        mPath = path;

        return this;
    }
}