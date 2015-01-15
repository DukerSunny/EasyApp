package air.tv.douyu.android.parsers;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/01/07
 */
public class Parser {
    public static boolean parseBoolean(String json) {
        return parseObject(json, Boolean.class);
    }

    public static double parseDouble(String json) {
        return parseObject(json, Double.class);
    }

    public static float parseFloat(String json) {
        return parseObject(json, Float.class);
    }

    public static int parseInt(String json) {
        return parseObject(json, Integer.class);
    }

    private static JSONObject parseJSONObject(String json) {
        JSONObject object;
        int error;

        try {
            object = JSON.parseObject(json);
            if (object != null) {
                error = object.getInteger("error");
                if (error == 0) {
                    return object;
                }
            }
        } catch (JSONException e) {
            return null;
        }

        return null;
    }

    public static <ITEM> List<ITEM> parseList(String json, Class<ITEM> clazz) {
        JSONObject object = parseJSONObject(json);
        List<ITEM> data;

        if (object != null) {
            data = JSON.parseArray(object.getJSONArray("data").toString(), clazz);
            if (data != null) {
                return data;
            }
        }

        return null;
    }

    public static <ITEM> ITEM parseObject(String json, Class<ITEM> clazz) {
        JSONObject object = parseJSONObject(json);
        ITEM data;

        if (object != null) {
            data = object.getObject("data", clazz);
            if (data != null) {
                return data;
            }
        }

        return null;
    }

    public static String parseString(String json) {
        return parseObject(json, String.class);
    }
}