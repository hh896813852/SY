package com.edusoho.yunketang.edu.factory.json;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by su on 2016/1/4.
 */
public class ParserType<T> {

    public ParserType() {
    }

    private class InnerParseType extends ParserType<String> {
    }

    public Class<T> getGenericType(int index) {
        Type genType = new InnerParseType().getClass().getGenericSuperclass();
        if (!(genType instanceof ParameterizedType)) {
            throw new RuntimeException("type error");
        }

        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        if (index >= params.length || index < 0) {
            throw new RuntimeException("Index outof bounds");
        }

        Class tClass = (Class) params[index];
        if (!(tClass instanceof Class)) {
            throw new RuntimeException("type error");
        }
        return tClass;
    }
}
