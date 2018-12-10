package com.edusoho.yunketang.edu.factory.json;

import java.lang.reflect.Type;

/**
 * Created by su on 2016/1/4.
 */
public interface Parser {

    <T> T fromJson(String json, Class<T> tClass);

    <T> T fromJson(String json, Type type);

    String jsonToString(Object jsonObj);
}
