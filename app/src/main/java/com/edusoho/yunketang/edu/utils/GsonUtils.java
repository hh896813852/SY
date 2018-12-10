package com.edusoho.yunketang.edu.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by JesseHuang on 2017/5/30.
 */

public class GsonUtils {
    public static <T> T parseJson(String json, TypeToken<T> type) {
        Gson gson = new Gson();
        T value = null;
        try {
            value = gson.fromJson(
                    json, type.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    public static <T> T parseJson(String json, Type type) {
        Gson gson = new Gson();
        T value = null;
        try {
            value = gson.fromJson(
                    json, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    public static <T> String parseJsonArray(List<T> list) {
        String json = null;
        try {
            json = new Gson().toJson(list);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return json;
    }

    public static <T> String parseString(T model) {
        Gson gson = new Gson();
        String json = "";
        try {
            json = gson.toJson(model);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return json;
    }
}
