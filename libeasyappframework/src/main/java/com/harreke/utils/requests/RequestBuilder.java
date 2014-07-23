package com.harreke.utils.requests;

import com.harreke.utils.tools.Debug;
import com.harreke.utils.tools.GsonUtil;

import java.util.HashMap;
import java.util.Iterator;

/**
 Http请求，支持GET和POST请求
 */
public abstract class RequestBuilder {
    private String mBaseUrl;
    private HashMap<String, String> mBodyMap = new HashMap<String, String>();
    private HashMap<String, String> mHeaderMap = new HashMap<String, String>();
    private RequestMethod mMethod;
    private HashMap<String, String> mQueryMap = new HashMap<String, String>();

    /**
     构造Http请求

     @param method
     请求方式（GET、POST）
     @param baseUrl
     目标Url
     */
    public RequestBuilder(RequestMethod method, String baseUrl) {
        mMethod = method;
        mBaseUrl = baseUrl;
    }

    /**
     添加Body

     @param key
     Body名称
     @param value
     Body内容

     @return 自身
     */
    public final RequestBuilder addBody(String key, Object value) {
        mBodyMap.put(key, String.valueOf(value));

        return this;
    }

    /**
     添加Header

     @param key
     Header名称
     @param value
     Header内容

     @return 自身
     */
    public final RequestBuilder addHeader(String key, Object value) {
        mHeaderMap.put(key, String.valueOf(value));

        return this;
    }

    /**
     添加Query

     @param key
     Query名称
     @param value
     Query内容

     @return 自身
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

    public final RequestMethod getMethod() {
        return mMethod;
    }

    public final HashMap<String, String> getQuery() {
        return mQueryMap;
    }

    public final void print() {
        String method = "";

        switch (mMethod) {
            case METHOD_GET:
                method = "GET";
                break;
            case METHOD_POST:
                method = "POST";
        }
        Debug.e("\nMethod:" + method + "\nUrl:" + getUrl() + "\nHeaders:\n" + GsonUtil.toString(mHeaderMap) + "Bodys:\n" + GsonUtil.toString(mBodyMap));
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
}