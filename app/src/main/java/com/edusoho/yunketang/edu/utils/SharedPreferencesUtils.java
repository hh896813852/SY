package com.edusoho.yunketang.edu.utils;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * Created by JesseHuang on 2017/5/9.
 */

public class SharedPreferencesUtils {
    private        Context                mContext;
    private        SharedPreferences      preference;
    private static SharedPreferencesUtils instance;

    private SharedPreferencesUtils(Context context) {
        mContext = context.getApplicationContext();
    }

    public static SharedPreferencesUtils getInstance(Context context) {
        if (instance == null) {
            synchronized (SharedPreferencesUtils.class) {
                if (instance == null) {
                    instance = new SharedPreferencesUtils(context);
                }
            }
        }
        return instance;
    }

    public SharedPreferencesUtils open(String name) {
        this.preference = mContext.getSharedPreferences(name, 0);
        return instance;
    }

    public String getString(String key) {
        return this.preference.getString(key, "");
    }

    public void putString(String key, String value) {
        this.preference.edit().putString(key, value).apply();
    }

    public int getInt(String key) {
        return this.preference.getInt(key, 0);
    }

    public void putInt(String key, int value) {
        this.preference.edit().putInt(key, value).apply();
    }
}
