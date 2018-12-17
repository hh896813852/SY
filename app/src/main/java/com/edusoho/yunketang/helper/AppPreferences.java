package com.edusoho.yunketang.helper;


import android.text.TextUtils;

import com.edusoho.yunketang.BuildConfig;
import com.edusoho.yunketang.bean.EducationCourse;
import com.edusoho.yunketang.bean.Setting;
import com.edusoho.yunketang.utils.JsonUtil;


/**
 * @author any
 */
public class AppPreferences extends BasePreferences {
    /**
     * 是否展示引导页
     */
    public static final String IS_FIRST_OPEN_APP = "sy.is_first_open_app_" + BuildConfig.VERSION_CODE;

    public static final String USER_INFO = "sy.user_info";

    public static final String APP_UPDATE_ID = "sy.app_update_id";//app更新下载id（DownloadManager）

    public static final String APP_FORCE_UPDATE = "sy.app_force_update";//是否强制更新app

    public static final String SELECTED_COURSE = "sy.selected_course";  // 选择的课程

    public static final String SETTING_CONFIG = "sy.setting_config"; // 设置配置

    public static boolean isFirstOpenApp() {
        return getBoolean(IS_FIRST_OPEN_APP, true);
    }

    public static void finishFirstOpenApp() {
        setBoolean(IS_FIRST_OPEN_APP, false);
    }

    public static String getUserInfo() {
        return getString(USER_INFO, "");
    }

    public static void setUserInfo(String userInfo) {
        setString(USER_INFO, userInfo);
    }

    public static void clearGestures(String userId) {
        clear(userId);
    }

    public static void clearUserInfo() {
        clear(USER_INFO);
    }

    public static Setting getSettings() {
        String json = getString(SETTING_CONFIG, "");
        if (TextUtils.isEmpty(json)) {
            Setting setting = new Setting();
            setting.isAllow4GPlay = 0;
            setting.soundSwitchState = 1;
            setting.vibrationSwitchState = 1;
            return setting;
        }
        return JsonUtil.fromJson(json, Setting.class);
    }

    public static void saveSettings(Setting setting) {
        setString(SETTING_CONFIG, JsonUtil.toJson(setting));
    }

    public static void setAppUpdateId(long id) {
        setLong(APP_UPDATE_ID, id);
    }

    public static long getAppUpdateId() {
        return getLong(APP_UPDATE_ID, -1l);
    }

    public static boolean isForceUpdateAPP() {
        return getBoolean(APP_FORCE_UPDATE, false);
    }

    public static void setAppForceUpdate(boolean isForce) {
        setBoolean(APP_FORCE_UPDATE, isForce);
    }

    public static EducationCourse getSelectedCourse() {
        String json = getString(SELECTED_COURSE, "");
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        return JsonUtil.fromJson(json, EducationCourse.class);
    }

    public static void setSelectedCourse(EducationCourse course) {
        setString(SELECTED_COURSE, JsonUtil.toJson(course));
    }
}
