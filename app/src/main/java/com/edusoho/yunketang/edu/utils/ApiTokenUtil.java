package com.edusoho.yunketang.edu.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.edusoho.yunketang.bean.School;
import com.edusoho.yunketang.edu.bean.RequestUrl;
import com.edusoho.yunketang.edu.bean.UserResult;

import java.util.Map;

/**
 * Created by howzhi on 15/11/2.
 */
public class ApiTokenUtil {

    public static RequestUrl bindNewUrl(Context context, String url, boolean hasToken) {
        School school = SchoolUtil.getDefaultSchool(context);
        String token = ApiTokenUtil.getToken(context).get("token").toString();
        StringBuffer sb = new StringBuffer(school.host);
        sb.append(url);
        RequestUrl requestUrl = new RequestUrl(sb.toString());

        if (hasToken) {
            requestUrl.heads.put("Auth-Token", token);
        }
        return requestUrl;
    }

    public static Map<String,String> getToken(Context context) {
        SharedPreferences sp = context.getSharedPreferences("token", Context.MODE_APPEND);
        return (Map<String,String>) sp.getAll();
    }

    public static String getTokenString(Context context) {
        SharedPreferences sp = context.getSharedPreferences("token", Context.MODE_APPEND);
        Map<String,String> map = (Map<String,String>) sp.getAll();
        return map.containsKey("token") ? map.get("token") : "";
    }

    public static String getApiToken(Context context) {
        SharedPreferences sp = context.getSharedPreferences("token", Context.MODE_APPEND);
        Map tokenMap = sp.getAll();
        return tokenMap.containsKey("apiToken") ? tokenMap.get("apiToken").toString() : "";
    }

    public static void saveApiToken(Context context, String apiToken) {
        SharedPreferences sp = context.getSharedPreferences("token", Context.MODE_APPEND);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString("apiToken", apiToken);
        edit.apply();
    }

    public static void saveToken(Context context, UserResult userResult) {
        SharedPreferences sp = context.getSharedPreferences("token", Context.MODE_APPEND);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString("token", userResult.token);
        edit.putString("userInfo", AppUtils.encode2(new Gson().toJson(userResult.user)));
        edit.apply();
    }

    public static void removeToken(Context context) {
        SharedPreferences sp = context.getSharedPreferences("token", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString("token", "");
        edit.putString("userInfo", "");
        edit.apply();
    }
}
