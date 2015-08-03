package com.harreke.easyapp.requests;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.harreke.easyapp.utils.LogUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 * <p/>
 * Http请求构造器，支持GET和POST请求
 */
public class RequestBuilder {
    private final String TAG = "RequestBuilder";
    private Map<String, String> mBodyMap = new TreeMap<>();
    private Map<String, String> mHeaderMap = new TreeMap<>();
    private String mHost;
    private RequestMethod mMethod;
    private Map<String, String> mMultiPartMap = new TreeMap<>();
    private String mPath;
    private Map<String, String> mQueryMap = new TreeMap<>();
    private String mTag;

    public RequestBuilder(@NonNull String url) {
        mMethod = RequestMethod.GET;
        mTag = "load_" + url.hashCode();
        mHost = url;
        mPath = "";
    }

    /**
     * 构造Http请求
     *
     * @param method 请求方式
     * @see RequestMethod
     */
    public RequestBuilder(@NonNull RequestMethod method, @NonNull String tag) {
        mMethod = method;
        mTag = tag;
    }

    /**
     * 添加Body
     *
     * @param key   Body名称
     * @param value Body内容
     * @return 自身
     */
    public final RequestBuilder addBody(@NonNull String key, @NonNull Object value) {
        //        String body = String.valueOf(value);
        //
        //        try {
        //            mBodyMap.put(key, URLEncoder.encode(body, "utf-8"));
        //        } catch (UnsupportedEncodingException e) {
        //            mBodyMap.put(key, body);
        //        }
        mBodyMap.put(key, String.valueOf(value));

        return this;
    }

    /**
     * 添加Header
     *
     * @param key   Header名称
     * @param value Header内容
     * @return 自身
     */
    public final RequestBuilder addHeader(@NonNull String key, @NonNull Object value) {
        //        String header = String.valueOf(value);
        //
        //        try {
        //            mHeaderMap.put(key, URLEncoder.encode(header, "utf-8"));
        //        } catch (UnsupportedEncodingException e) {
        //            mHeaderMap.put(key, header);
        //        }
        mHeaderMap.put(key, String.valueOf(value));

        return this;
    }

    public final RequestBuilder addMultiPart(@NonNull String key, @NonNull Object value) {
        String multiPart = String.valueOf(value);

        try {
            mMultiPartMap.put(key, URLEncoder.encode(multiPart, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            mMultiPartMap.put(key, multiPart);
        }

        return this;
    }

    /**
     * 添加Query
     *
     * @param key   Query名称
     * @param value Query内容
     * @return 自身
     */
    public final RequestBuilder addQuery(@NonNull String key, @NonNull Object value) {
        String query = String.valueOf(value);

        try {
            mQueryMap.put(key, URLEncoder.encode(query, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            mQueryMap.put(key, query);
        }

        return this;
    }

    private String buildString(@NonNull Map<String, String> map) {
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
        return mHost + (TextUtils.isEmpty(mPath) ? "" : "/" + mPath);
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

    public final Map<String, String> getMultiPart() {
        return mMultiPartMap;
    }

    public final String getMultiPartString() {
        return buildString(mMultiPartMap);
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

    public String getTag() {
        return mTag;
    }

    public final String getUrl() {
        String queryString = getQueryString();

        return getBaseUrl() + (TextUtils.isEmpty(queryString) ? "" : "?" + queryString);
    }

    public final void print() {
        String print = "";

        switch (mMethod) {
            case GET:
                print = "GET " + getUrl();
                break;
            case POST:
                print = "POST " + getUrl() + "\nHeaders:\n" + getHeaderString() + "\nBodies:\n" +
                        getBodyString() + "\nMultiParts:\n" + getMultiPartString();
                break;
        }
        LogUtil.e(TAG + "/" + mTag, print);
    }

    public final RequestBuilder setBaseUrl(String host, String path) {
        mHost = host;
        mPath = path;

        return this;
    }
}