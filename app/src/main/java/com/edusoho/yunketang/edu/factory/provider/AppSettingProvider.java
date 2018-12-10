package com.edusoho.yunketang.edu.factory.provider;

import android.content.Context;
import android.content.SharedPreferences;

import com.edusoho.yunketang.bean.School;
import com.edusoho.yunketang.bean.User;
import com.edusoho.yunketang.edu.bean.AppConfig;
import com.edusoho.yunketang.edu.factory.FactoryManager;
import com.edusoho.yunketang.edu.factory.UtilFactory;
import com.edusoho.yunketang.edu.utils.ApiTokenUtil;
import com.edusoho.yunketang.edu.utils.AppUtils;
import com.edusoho.yunketang.edu.utils.SchoolUtil;

/**
 * Created by su on 2016/2/25.
 */
public class AppSettingProvider extends AbstractProvider {

    private static final String USER_SP = "token";

    private AppConfig mAppConfig;
    private User mCurrentUser;
    private School mCurrentSchool;

    public AppSettingProvider(Context context) {
        super(context);
        init();
    }

    private void init() {
        SharedPreferences sp = getSharedPreferences();
        mCurrentUser = getUtilFactory().getJsonParser().fromJson(AppUtils.encode2(sp.getString("userInfo", "")), User.class);
        mCurrentSchool = SchoolUtil.getDefaultSchool(mContext);
        mAppConfig = getAppconfigFromSp();
    }

    private AppConfig getAppconfigFromSp() {
        SharedPreferences sp = mContext.getSharedPreferences("config", Context.MODE_PRIVATE);
        AppConfig config = new AppConfig();
        config.showSplash = sp.getBoolean("showSplash", true);
        config.isEnableIMChat = sp.getBoolean("isEnableIMChat", true);
        config.isPublicRegistDevice = sp.getBoolean("registPublicDevice", false);
        config.startWithSchool = sp.getBoolean("startWithSchool", true);
        config.offlineType = sp.getInt("offlineType", 0);
        config.newVerifiedNotify = sp.getBoolean("newVerifiedNotify", false);
        config.msgSound = sp.getInt("msgSound", 1);
        config.msgVibrate = sp.getInt("msgVibrate", 2);
        return config;
    }

    public AppConfig getAppConfig() {
        return mAppConfig;
    }

    public void saveConfig(AppConfig config) {
        SharedPreferences sp = mContext.getSharedPreferences("config", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putBoolean("showSplash", config.showSplash);
        edit.putBoolean("isEnableIMChat", config.isEnableIMChat);
        edit.putBoolean("registPublicDevice", config.isPublicRegistDevice);
        edit.putBoolean("startWithSchool", config.startWithSchool);
        edit.putInt("offlineType", config.offlineType);
        edit.putInt("msgSound", config.msgSound);
        edit.putInt("msgVibrate", config.msgVibrate);
        edit.putBoolean("newVerifiedNotify", config.newVerifiedNotify);
        edit.apply();
    }

    public User getCurrentUser() {
        return mCurrentUser;
    }

    public void setCurrentSchool(School school) {
        this.mCurrentSchool = school;
    }

    public School getCurrentSchool() {
        return mCurrentSchool;
    }

    public void setUser(User user) {
        this.mCurrentUser = user;
        saveUser(user);
    }

    public void removeToken() {
        this.mCurrentUser = null;
        ApiTokenUtil.removeToken(mContext);
    }

    private void saveUser(User user) {
        SharedPreferences sp = getSharedPreferences();
        sp.edit().putString("userInfo", AppUtils.encode2(getUtilFactory().getJsonParser().jsonToString(user))).commit();
    }

    protected SharedPreferences getSharedPreferences() {
        return mContext.getSharedPreferences(USER_SP, Context.MODE_PRIVATE);
    }

    protected UtilFactory getUtilFactory() {
        return FactoryManager.getInstance().create(UtilFactory.class);
    }
}
