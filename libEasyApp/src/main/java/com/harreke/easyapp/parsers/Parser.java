package com.harreke.easyapp.parsers;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/01/07
 */
public class Parser {
    public static ObjectResult<Boolean> parseBoolean(String json, String flagKey, String dataKey, String messageKey) {
        return parseObject(json, Boolean.class, flagKey, dataKey, messageKey);
    }

    public static ObjectResult<Double> parseDouble(String json, String flagKey, String dataKey, String messageKey) {
        return parseObject(json, Double.class, flagKey, dataKey, messageKey);
    }

    public static ObjectResult<Float> parseFloat(String json, String flagKey, String dataKey, String messageKey) {
        return parseObject(json, Float.class, flagKey, dataKey, messageKey);
    }

    public static ObjectResult<Integer> parseInt(String json, String flagKey, String dataKey, String messageKey) {
        return parseObject(json, Integer.class, flagKey, dataKey, messageKey);
    }

    private static JSONObject parseJSONObject(String json) {
        JSONObject object;

        try {
            object = JSON.parseObject(json);
        } catch (JSONException e) {
            object = null;
        }

        return object;
    }

    public static <ITEM> ListResult<ITEM> parseList(String json, Class<ITEM> clazz, String flagKey, String dataKey,
            String messageKey) {
        JSONObject object = parseJSONObject(json);
        ListResult<ITEM> listResult;

        if (object != null) {
            listResult = new ListResult<ITEM>();
            if (!TextUtils.isEmpty(flagKey)) {
                listResult.setFlag(object.getIntValue(flagKey));
            }
            if (!TextUtils.isEmpty(dataKey)) {
                try {
                    listResult.setList(JSON.parseArray(object.getJSONArray(dataKey).toString(), clazz));
                } catch (JSONException ignored) {
                }
            }
            if (!TextUtils.isEmpty(messageKey)) {
                listResult.setMessage(object.getString(messageKey));
            }
        } else {
            listResult = null;
        }

        return listResult;
    }

    public static <ITEM> ObjectResult<ITEM> parseObject(String json, Class<ITEM> clazz, String flagKey, String dataKey,
            String messageKey) {
        JSONObject object = parseJSONObject(json);
        ObjectResult<ITEM> objectResult;

        if (object != null) {
            objectResult = new ObjectResult<>();
            if (!TextUtils.isEmpty(flagKey)) {
                objectResult.setFlag(object.getIntValue(flagKey));
            }
            if (!TextUtils.isEmpty(dataKey)) {
                try {
                    objectResult.setObject(JSON.parseObject(object.getString(dataKey), clazz));
                } catch (JSONException ignored) {
                }
            }
            if (!TextUtils.isEmpty(messageKey)) {
                objectResult.setMessage(object.getString(messageKey));
            }
        } else {
            objectResult = null;
        }

        return objectResult;
    }

    public static ObjectResult<String> parseString(String json, String flagKey, String dataKey, String messageKey) {
        //        return parseObject(json, String.class, flagKey, dataKey, messageKey);
        JSONObject object = parseJSONObject(json);
        ObjectResult<String> objectResult;

        if (object != null) {
            objectResult = new ObjectResult<>();
            if (!TextUtils.isEmpty(flagKey)) {
                objectResult.setFlag(object.getIntValue(flagKey));
            }
            if (!TextUtils.isEmpty(dataKey)) {
                objectResult.setObject(object.getString(dataKey));
            }
            if (!TextUtils.isEmpty(messageKey)) {
                objectResult.setMessage(object.getString(messageKey));
            }
        } else {
            objectResult = null;
        }

        return objectResult;
    }
}