package com.edusoho.yunketang.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class JsonUtil {

	public static String toJson(Object obj){
		return new Gson().toJson(obj);
	}

	public static <T> T fromJson(String json, TypeToken<T> token){
		return new Gson().fromJson(json,token.getType());
	}

	public static <T> T fromJson(String json, Class<T> clazz){
		return new Gson().fromJson(json, clazz);
	}
}