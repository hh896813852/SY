package com.edusoho.yunketang.helper;

import android.content.Context;
import android.content.SharedPreferences;


import com.edusoho.yunketang.SYApplication;

import java.util.Map;

/**
 * @author any
 */
public class BasePreferences {

    private static SharedPreferences preferences = SYApplication.getInstance().getSharedPreferences("sy_syedu", Context.MODE_PRIVATE);

    public static boolean setString(final String entry, String value) {
        return preferences.edit().putString(entry, value).commit();
    }

    public static boolean setBoolean(final String entry, boolean value) {
        return preferences.edit().putBoolean(entry, value).commit();
    }

    public static boolean setInt(final String entry, int value) {
        return preferences.edit().putInt(entry, value).commit();
    }

    public static boolean setLong(final String entry, long value) {
        return preferences.edit().putLong(entry, value).commit();
    }

    public static boolean setFloat(final String entry, float value) {
        return preferences.edit().putFloat(entry, value).commit();
    }

    public Map<String, ?> getAll() {
        return preferences.getAll();
    }

    public static String getString(final String entry, String defaultValue) {
        return preferences.getString(entry, defaultValue);
    }

    public static int getInt(final String entry, int defaultValue) {
        return preferences.getInt(entry, defaultValue);
    }

    public static float getFloat(final String entry, float defaultValue) {
        return preferences.getFloat(entry, defaultValue);
    }

    public static long getLong(final String entry, long defaultValue) {
        return preferences.getLong(entry, defaultValue);
    }

    public static boolean getBoolean(final String entry, boolean defaultValue) {
        return preferences.getBoolean(entry, defaultValue);
    }

    public static boolean contains(String entry) {
        return preferences.contains(entry);
    }

    public static boolean clear(String entry) {
        return preferences.edit().remove(entry).commit();
    }

    public boolean clearAll() {
        return preferences.edit().clear().commit();
    }

}
