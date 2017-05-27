package com.xmx.androidframeworkbase.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by The_onE on 2017/5/27.
 * 处理JSON数据，将对象转化为Map，将数组转化为List
 */

public class JSONUtil {
    static public Map<String, Object> parseObject(String json) throws Exception {
        return parseObject(new JSONObject(json));
    }

    static private Map<String, Object> parseObject(JSONObject object) throws Exception {
        Map<String, Object> map = new HashMap<>();
        Iterator<String> it = object.keys();
        // 遍历JSON对象
        while (it.hasNext()) {
            String key = it.next();
            try {
                // 若为数组
                JSONArray array = object.getJSONArray(key);
                List<Object> res = parseArray(array);
                map.put(key, res);
            } catch (JSONException e) {
                try {
                    // 若为对象
                    JSONObject obj = object.getJSONObject(key);
                    Map<String, Object> res = parseObject(obj);
                    map.put(key, res);
                } catch (JSONException ex) {
                    // 若为值
                    String value = object.getString(key);
                    map.put(key, value);
                }
            }
        }
        return map;
    }

    static public List<Object> parseArray(String json) throws Exception {
        return parseArray(new JSONArray(json));
    }

    static private List<Object> parseArray(JSONArray array) throws Exception {
        List<Object> list = new ArrayList<>();
        int length = array.length();
        for (int i = 0; i < length; ++i) {
            try {
                // 若为数组
                JSONArray arr = array.getJSONArray(i);
                List<Object> res = parseArray(array);
                list.add(res);
            } catch (JSONException e) {
                try {
                    // 若为对象
                    JSONObject obj = array.getJSONObject(i);
                    Map<String, Object> res = parseObject(obj);
                    list.add(res);
                } catch (JSONException ex) {
                    // 若为值
                    String value = array.getString(i);
                    list.add(value);
                }
            }
        }
        return list;
    }
}
