package air.tv.douyu.android.parsers;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/01/07
 */
public class Parser {
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

    public static <T> List<T> parseList(String json, Class<T> classOfT) {
        JSONObject object = parseJSONObject(json);
        List<T> data;

        if (object != null) {
            data = JSON.parseArray(object.getJSONArray("data").toString(), classOfT);
            if (data != null) {
                return data;
            }
        }

        return null;
    }

    public static <T> T parseObject(String json, Class<T> classOfT) {
        JSONObject object = parseJSONObject(json);
        T data;

        if (object != null) {
            data = object.getObject("data", classOfT);
            if (data != null) {
                return data;
            }
        }

        return null;
    }
}