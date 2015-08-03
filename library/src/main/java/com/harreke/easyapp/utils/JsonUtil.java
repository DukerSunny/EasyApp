package com.harreke.easyapp.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import java.util.List;


/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/01/08
 */
public class JsonUtil {
    public static JSONArray toJSONArray(String json) {
        JSONArray jsonArray;

        try {
            jsonArray = JSON.parseArray(json);
        } catch (JSONException e) {
            jsonArray = null;
        }

        return jsonArray;
    }

    public static JSONObject toJSONObject(String json) {
        JSONObject jsonObject;

        try {
            jsonObject = JSON.parseObject(json);
        } catch (JSONException e) {
            jsonObject = null;
        }

        return jsonObject;
    }

    public static <T> List<T> toList(String json, Class<T> classOfT) {
        List<T> list;

        try {
            list = JSON.parseArray(json, classOfT);
        } catch (JSONException e) {
            list = null;
        }

        return list;
    }

    public static <T> T toObject(String json, TypeReference<T> typeReference) {
        T object;

        try {
            object = JSON.parseObject(json, typeReference);
        } catch (JSONException e) {
            object = null;
        }

        return object;
    }

    public static <T> T toObject(String json, Class<T> classOfT) {
        T object;

        try {
            object = JSON.parseObject(json, classOfT);
        } catch (JSONException e) {
            object = null;
        }

        return object;
    }

    public static String toString(Object object) {
        String string;

        try {
            string = JSON.toJSONString(object);
        } catch (JSONException e) {
            string = null;
        }

        return string;
    }
}