package com.xmx.androidframeworkbase.common.json;

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

    public static final String RESPONSE_STATUS = "status"; // 状态键
    public static final String STATUS_ERROR = "0"; // 失败状态
    public static final String STATUS_EXECUTE_SUCCESS = "1"; // 成功状态（无数据）
    public static final String STATUS_QUERY_SUCCESS = "2"; // 成功状态（有数据）
    public static final String RESPONSE_PROMPT = "prompt"; // 提示消息键
    public static final String RESPONSE_ENTITIES = "entities"; // 数据列表键
    public static final String RESPONSE_ENTITY = "entity"; // 单数据键

    /**
     * 将JSON字符串解析为Map
     *
     * @param json 最外层为对象的JSON字符串
     * @return 解析出的Map对象
     * @throws Exception 解析异常
     */
    static public Map<String, Object> parseObject(String json) throws Exception {
        return parseObject(new JSONObject(json));
    }

    /**
     * 将JSONObject解析为Map
     *
     * @param object JSONObject对象
     * @return 解析出的Map对象
     * @throws Exception 解析异常
     */
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

    /**
     * 将JSON字符串解析为List
     *
     * @param json 最外层为数组的JSON字符串
     * @return 解析出的List数组
     * @throws Exception 解析异常
     */
    static public List<Object> parseArray(String json) throws Exception {
        return parseArray(new JSONArray(json));
    }

    /**
     * 将JSONArray解析为List
     *
     * @param array JSONArray数组
     * @return 解析出的List数组
     * @throws Exception 解析异常
     */
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

    /**
     * 格式化Map对象
     *
     * @param map 由JSON解析出的Map对象
     * @param sep 键值分隔符
     * @param tab 层次推进符
     * @return 格式化后的字符串
     */
    static public String formatJSONObject(Map<String, Object> map, String sep, String tab) {
        StringBuilder sb = new StringBuilder();
        sb = JSONUtil.appendJSONObject(map, sb, 0, sep, tab);
        return sb.toString();
    }

    /**
     * 格式化List数组
     *
     * @param list 由JSON解析出的List数组
     * @param sep 键值分隔符
     * @param tab 层次推进符
     * @return 格式化后的字符串
     */
    static public String formatJSONArray(List<Object> list, String sep, String tab) {
        StringBuilder sb = new StringBuilder();
        sb = JSONUtil.appendJSONArray(list, sb, 0, sep, tab);
        return sb.toString();
    }

    /**
     * 向字符串中追加Map对象
     *
     * @param map 由JSON解析出的Map对象
     * @param source 原字符串
     * @param level 所在层次
     * @param sep 键值分隔符
     * @param tab 层次推进符
     * @return 追加后的字符串
     */
    static private StringBuilder appendJSONObject(Map<String, Object> map,
                                                  StringBuilder source,
                                                  int level, String sep, String tab) {
        StringBuilder t = new StringBuilder();
        for (int i = 0; i < level; ++i) {
            t.append(tab);
        }
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            source.append(t).append(key).append(sep);
            Object value = entry.getValue();
            if (value instanceof Map) {
                source.append("\n");
                source = appendJSONObject((Map<String, Object>) value, source, level + 1, sep, tab);
            } else if (value instanceof List) {
                source.append("\n");
                source = appendJSONArray((List<Object>) value, source, level + 1, sep, tab);
            } else {
                source.append(value.toString());
                source.append("\n");
            }
        }
        return source;
    }

    /**
     * 向字符串中追加List数组
     *
     * @param list 由JSON解析出的List数组
     * @param source 原字符串
     * @param level 所在层次
     * @param sep 键值分隔符
     * @param tab 层次推进符
     * @return 追加后的字符串
     */
    static private StringBuilder appendJSONArray(List<Object> list,
                                                 StringBuilder source,
                                                 int level, String sep, String tab) {
        StringBuilder t = new StringBuilder();
        for (int i = 0; i < level; ++i) {
            t.append(tab);
        }
        int i = 0;
        for (Object item : list) {
            source.append(t).append("[").append(i).append("]").append(sep);
            if (item instanceof Map) {
                source.append("\n");
                source = appendJSONObject((Map<String, Object>) item, source, level + 1, sep, tab);
            } else if (item instanceof List) {
                source.append("\n");
                source = appendJSONArray((List<Object>) item, source, level + 1, sep, tab);
            } else {
                source.append(item.toString());
                source.append("\n");
            }
            i++;
        }
        return source;
    }

    static public<T extends IJsonEntity> ListJsonObject<T> parseListObject(String json, Class<T> c) throws Exception {
        Map<String, Object> map = parseObject(json);
        ListJsonObject<T> object = new ListJsonObject<>();

        object.status = (String) map.get(RESPONSE_STATUS);
        object.prompt = (String) map.get(RESPONSE_PROMPT);
        if (STATUS_QUERY_SUCCESS.equals(object.status)) {
            List<Object> list = (List<Object>) map.get(RESPONSE_ENTITIES);
            List<T> entities = new ArrayList<>();
            for (Object item : list) {
                T t = c.newInstance();
                t.initWithJson((Map<String, Object>)item);
                entities.add(t);
            }
            object.entities = entities;
        } else {
            object.entities = null;
        }

        return object;
    }
}
