package com.edusoho.yunketang.edu.factory.json;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.edusoho.yunketang.edu.factory.converter.StringConverter;

import java.lang.reflect.Type;

/**
 * Created by su on 2016/1/4.
 */
public class ParserImpl implements Parser {

    private Gson mGson;

    public ParserImpl() {
        mGson = createGson();
    }

    public <T> T fromJson(String json, Class<T> tClass) {
        try {
            return mGson.fromJson(json, tClass);
        } catch (Exception e) {
            Log.d("Parser", "parse error:" + tClass);
        }

        return null;
    }

    public <T> T fromJson(String json, Type type) {
        try {
            return mGson.fromJson(json, type);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public String jsonToString(Object jsonObj) {
        return mGson.toJson(jsonObj);
    }

    private Gson createGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(String.class, new StringConverter());
        return gsonBuilder.create();
    }
}
