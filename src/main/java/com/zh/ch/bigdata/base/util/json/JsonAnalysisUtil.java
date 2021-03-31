/*
 * JSON解析工具
 */
package com.zh.ch.bigdata.base.util.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zh.ch.bigdata.base.util.exception.JsonKeyNotFoundException;
import com.zh.ch.bigdata.base.util.exception.ProjectException;

/**
 * Json数据解析工具
 *
 * @author hadoop
 */
public class JsonAnalysisUtil {

    /**
     * 字符串解析为JSONObject
     *
     * @param jsonData json字符串数据
     * @return JSONObject
     */
    public static JSONObject parseJsonObject(String jsonData) {
        return JSON.parseObject(jsonData);
    }

    public static JSONArray parseJsonArray(String jsonData) {
        return JSON.parseArray(jsonData);
    }

    /**
     * 获取单层json数据中的key值
     *
     * @param jsonData 带解析的json字符串数据
     * @param key 需要获取值对应的key
     * @return 获取单层json数据中的key值
     * @throws JsonKeyNotFoundException 异常
     */
    public static String getString(String jsonData, String key) throws ProjectException {
        JSONObject jsonObject = parseJsonObject(jsonData);
        if (jsonObject.getString(key) == null) {
            throw new JsonKeyNotFoundException(key);
        } else {
            return jsonObject.getString(key);
        }
    }

    /**
     * 获取单层json数据中的key值
     *
     * @param jsonObject 待解析的JSONObject数据
     * @param key 需要获取值对应的key
     * @return 获取单层json数据中的key值
     * @throws JsonKeyNotFoundException 异常
     */
    public static String getString(JSONObject jsonObject, String key) throws ProjectException {
        if (jsonObject.getString(key) == null) {
            throw new JsonKeyNotFoundException(key);
        } else {
            return jsonObject.getString(key);
        }
    }

    /**
     * 获取单层json数据中的key值
     *
     * @param jsonData 待解析的JSONObject数据
     * @param key 需要获取值对应的key
     * @return 获取单层json数据中的key值
     * @throws JsonKeyNotFoundException 异常
     */
    public static Integer getInteger(String jsonData, String key) throws ProjectException {
        JSONObject jsonObject = parseJsonObject(jsonData);
        if (jsonObject.getString(key) == null) {
            throw new JsonKeyNotFoundException(key);
        } else {
            return jsonObject.getInteger(key);
        }
    }

    /**
     * 获取单层json数据中的key值
     *
     * @param jsonObject 待解析的JSONObject数据
     * @param key 需要获取值对应的key
     * @return 获取单层json数据中的key值
     * @throws JsonKeyNotFoundException 异常
     */
    public static Integer getInteger(JSONObject jsonObject, String key) throws ProjectException {
        if (jsonObject.getString(key) == null) {
            throw new JsonKeyNotFoundException(key);
        } else {
            return jsonObject.getInteger(key);
        }
    }

    /**
     * 获取单层json数据中的key值
     *
     * @param jsonData 待解析的JSONObject数据
     * @param key 需要获取值对应的key
     * @return 获取单层json数据中的key值
     * @throws JsonKeyNotFoundException 异常
     */
    public static Long getLong(String jsonData, String key) throws ProjectException {
        JSONObject jsonObject = parseJsonObject(jsonData);
        if (jsonObject.getString(key) == null) {
            throw new JsonKeyNotFoundException(key);
        } else {
            return jsonObject.getLong(key);
        }
    }

    /**
     * 获取单层json数据中的key值
     *
     * @param jsonObject 待解析的JSONObject数据
     * @param key 需要获取值对应的key
     * @return 获取单层json数据中的key值
     * @throws JsonKeyNotFoundException 异常
     */
    public static Long getLong(JSONObject jsonObject, String key) throws ProjectException {
        if (jsonObject.getString(key) == null) {
            throw new JsonKeyNotFoundException(key);
        } else {
            return jsonObject.getLong(key);
        }
    }

    /**
     * 获取单层json数据中的key值
     *
     * @param jsonData 待解析的JSONObject数据
     * @param key 需要获取值对应的key
     * @return 获取单层json数据中的key值
     * @throws JsonKeyNotFoundException 异常
     */
    public static Float getFloat(String jsonData, String key) throws ProjectException {
        JSONObject jsonObject = parseJsonObject(jsonData);
        if (jsonObject.getString(key) == null) {
            throw new JsonKeyNotFoundException(key);
        } else {
            return jsonObject.getFloat(key);
        }
    }

    /**
     * 获取单层json数据中的key值
     *
     * @param jsonObject 待解析的JSONObject数据
     * @param key 需要获取值对应的key
     * @return 获取单层json数据中的key值
     * @throws JsonKeyNotFoundException 异常
     */
    public static Float getFloat(JSONObject jsonObject, String key) throws ProjectException {
        if (jsonObject.getString(key) == null) {
            throw new JsonKeyNotFoundException(key);
        } else {
            return jsonObject.getFloat(key);
        }
    }

    /**
     * 获取单层json数据中的key值对应的JSONObject
     *
     * @param jsonData 待解析的JSON String数据
     * @param key 需要获取值对应的key
     * @return 获取单层json数据中的key值对应的JSONObject
     * @throws JsonKeyNotFoundException 异常
     */
    public static JSONObject getJsonObject(String jsonData, String key) throws ProjectException {
        JSONObject jsonObject = parseJsonObject(jsonData);
        if (jsonObject.getJSONObject(key) == null) {
            throw new JsonKeyNotFoundException(key);
        } else {
            return jsonObject.getJSONObject(key);
        }
    }

    /**
     * 获取单层json数据中的key值对应的JSONObject
     *
     * @param jsonObject 待解析的JSONObject数据
     * @param key 需要获取值对应的key
     * @return 获取单层json数据中的key值对应的JSONObject
     * @throws JsonKeyNotFoundException 异常
     */
    public static JSONObject getJsonObject(JSONObject jsonObject, String key)
            throws ProjectException {
        if (jsonObject.getJSONObject(key) == null) {
            throw new JsonKeyNotFoundException(key);
        } else {
            return jsonObject.getJSONObject(key);
        }
    }

    /**
     * 获取单层json数据中的key值对应的JSONArray
     *
     * @param jsonObject 待解析的JSONObject数据
     * @param key 需要获取值对应的key
     * @return 获取单层json数据中的key值对应的JSONArray
     * @throws JsonKeyNotFoundException 异常
     */
    public static JSONArray getJsonArray(JSONObject jsonObject, String key)
            throws ProjectException {
        if (jsonObject.getJSONArray(key) == null) {
            throw new JsonKeyNotFoundException(key);
        } else {
            return jsonObject.getJSONArray(key);
        }
    }

    /**
     * 获取单层json数据中的key值对应的JSONArray
     *
     * @param jsonData 待解析的JSONObject数据
     * @param key 需要获取值对应的key
     * @return 获取单层json数据中的key值对应的JSONArray
     * @throws JsonKeyNotFoundException 异常
     */
    public static JSONArray getJsonArray(String jsonData, String key) throws ProjectException {
        JSONObject jsonObject = parseJsonObject(jsonData);
        if (jsonObject.getJSONArray(key) == null) {
            throw new JsonKeyNotFoundException(key);
        } else {
            return jsonObject.getJSONArray(key);
        }
    }
}
