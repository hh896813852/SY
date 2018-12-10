package com.edusoho.yunketang.edu.utils;

import android.content.Context;
import android.provider.Settings;

import java.util.UUID;

/**
 * Created by JesseHuang on 2017/6/22.
 */

public class DeviceUtils {

    public static String getAndroidId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static String getUUID(Context context) {
        String androidId = getAndroidId(context);
        return new UUID(androidId.hashCode(), ((long) androidId.hashCode() << 32) | androidId.hashCode()).toString();
    }
}
