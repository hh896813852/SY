package com.edusoho.yunketang.bean.json;

/**
 * Created by suju on 16/11/30.
 */

public interface GsonEnum<E> {

    String serialize();

    E deserialize(String jsonEnum);
}
