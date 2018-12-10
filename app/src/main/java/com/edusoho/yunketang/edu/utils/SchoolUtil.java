package com.edusoho.yunketang.edu.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

import com.edusoho.yunketang.SYApplication;
import com.edusoho.yunketang.bean.School;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by howzhi on 15/11/2.
 */
public class SchoolUtil {
    private static final String EnterSchool = "enter_school";

    public static School getDefaultSchool(Context context) {
        School item = null;
        if (context == null) {
            return item;
        }
        item = new School();
        item.name = SYApplication.getInstance().courseType == 1 ? "上元在线" : "上元会计学院";
        item.host = SYApplication.getInstance().courseType == 1 ? "http://www.233863.com" : "http://www.sykjxy.com";
        item.url = SYApplication.getInstance().courseType == 1 ? "http://www.233863.com/mapi_v2" : "http://www.sykjxy.com/mapi_v2";
        item.logo = "";
        item.version = SYApplication.getInstance().courseType == 1 ? "8.3.13" : "8.3.11";
        item.url = checkSchoolUrl(item.url);
        return item;
    }

    public static void saveSchool(Context context, School school) {
        SharedPreferences sp = context.getSharedPreferences("defaultSchool", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString("name", school.name);
        edit.putString("url", school.url);
        edit.putString("host", school.host);
        edit.putString("logo", school.logo);
        edit.putString("version", school.version);
        edit.commit();
    }

    private static String checkSchoolUrl(String url) {
        if (url.endsWith("mapi_v1")) {
            String newUrl = url.substring(0, url.length() - 1);
            return newUrl + "2";
        }
        return url;
    }

    public static List<Map<String, Object>> loadEnterSchool(Context context) {
        List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
        SharedPreferences sp = context.getSharedPreferences("EnterSchool", Context.MODE_PRIVATE);
        String result = sp.getString(EnterSchool, "");
        try {
            JSONArray array = new JSONArray(result);
            for (int i = 0; i < array.length(); i++) {
                JSONObject itemObject = array.getJSONObject(i);
                Map<String, Object> itemMap = new HashMap<String, Object>();
                JSONArray names = itemObject.names();
                if (names != null) {
                    for (int j = 0; j < names.length(); j++) {
                        String name = names.getString(j);
                        String value = itemObject.getString(name);
                        itemMap.put(name, value);
                    }
                }
                datas.add(itemMap);
            }
        } catch (JSONException e) {
        }

        return datas;
    }

    public static void saveEnterSchool(Context context, String schoolname, String entertime, String loginname, String schoolhost, String version) {
        Map map = new HashMap();
        String lable = new String();
        lable = schoolname.substring(0, 2);
        map.put("lable", lable);
        map.put("schoolname", schoolname);
        map.put("entertime", entertime);
        map.put("loginname", loginname);
        map.put("schoolhost", schoolhost);
        map.put("version", version);
        List<Map<String, Object>> list = SchoolUtil.loadEnterSchool(context);
        if (list == null) {
            list = new ArrayList<Map<String, Object>>();
        }
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).get("schoolhost").toString().equals(map.get("schoolhost"))) {
                list.remove(i);
                i--;
            }
        }
        list.add(map);
        if (list.size() > 4) {
            list.remove(0);
        }
        JSONArray mJsonArray;
        mJsonArray = new JSONArray();
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> itemMap = list.get(i);
            Iterator<Map.Entry<String, Object>> iterator = itemMap.entrySet().iterator();

            JSONObject object = new JSONObject();

            while (iterator.hasNext()) {
                Map.Entry<String, Object> entry = iterator.next();
                try {
                    object.put(entry.getKey(), entry.getValue());
                } catch (JSONException e) {

                }
            }
            mJsonArray.put(object);
        }

        SharedPreferences sp = context.getSharedPreferences("EnterSchool", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(EnterSchool, mJsonArray.toString());
        editor.apply();
    }

    public static void saveSchoolHistory(School site) {
        SimpleDateFormat nowfmt = new SimpleDateFormat("登录时间：yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        String loginTime = nowfmt.format(date);
        Uri uri = Uri.parse(site.url);
        String domain = uri.getPort() == -1 ?
                uri.getHost() :
                uri.getHost() + ":" + uri.getPort();
        SchoolUtil.saveEnterSchool(SYApplication.getInstance()
                , site.name, loginTime, "登录账号：未登录", domain, site.version);
    }
}
