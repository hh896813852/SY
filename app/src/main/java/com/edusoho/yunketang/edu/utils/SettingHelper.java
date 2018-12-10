package com.edusoho.yunketang.edu.utils;

import android.content.Context;

import com.edusoho.yunketang.bean.seting.CloudVideoSetting;
import com.edusoho.yunketang.bean.seting.CourseSetting;
import com.edusoho.yunketang.bean.seting.PaymentSetting;
import com.edusoho.yunketang.bean.seting.UserSetting;
import com.edusoho.yunketang.bean.seting.VIPSetting;
import com.edusoho.yunketang.edu.api.SettingApi;
import com.edusoho.yunketang.edu.http.HttpUtils;
import com.edusoho.yunketang.edu.http.SubscriberProcessor;

import java.lang.reflect.Type;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by JesseHuang on 2017/5/10.
 */

public class SettingHelper {

    public static void sync(final Context context) {
//        if (StringUtils.isEmpty(EdusohoApp.app.host)) {
//            return;
//        }
        getCourseSetting().subscribe(new SubscriberProcessor<CourseSetting>() {
            @Override
            public void onNext(CourseSetting courseSetting) {
                context.getApplicationContext()
                        .getSharedPreferences(SharedPreferencesHelper.CourseSetting.XML_NAME, 0)
                        .edit()
                        .putString(SharedPreferencesHelper.CourseSetting.SHOW_STUDENT_NUM_ENABLED_KEY, courseSetting.showStudentNumEnabled)
                        .putString(SharedPreferencesHelper.CourseSetting.CHAPTER_NAME_KEY, courseSetting.chapterName)
                        .putString(SharedPreferencesHelper.CourseSetting.PART_NAME_KEY, courseSetting.partName)
                        .apply();
            }
        });
        getUserSetting().subscribe(new SubscriberProcessor<UserSetting>() {
            @Override
            public void onNext(UserSetting userSetting) {
                context.getApplicationContext()
                        .getSharedPreferences(SharedPreferencesHelper.SchoolSetting.XML_NAME, 0)
                        .edit()
                        .putString(SharedPreferencesHelper.SchoolSetting.USER_SETTING_KEY, GsonUtils.parseString(userSetting))
                        .apply();
            }

            @Override
            public void onError(String message) {
                UserSetting userSetting = new UserSetting();
                userSetting.init();
                context.getSharedPreferences(SharedPreferencesHelper.SchoolSetting.XML_NAME, 0)
                        .edit()
                        .putString(SharedPreferencesHelper.SchoolSetting.USER_SETTING_KEY, GsonUtils.parseString(userSetting))
                        .apply();
            }
        });
        getCloudVideoSetting().subscribe(new SubscriberProcessor<CloudVideoSetting>() {
            @Override
            public void onNext(CloudVideoSetting cloudVideoSetting) {
                context.getApplicationContext()
                        .getSharedPreferences(SharedPreferencesHelper.SchoolSetting.XML_NAME, 0)
                        .edit()
                        .putString(SharedPreferencesHelper.SchoolSetting.CLOUD_VIDEO_SETTING, GsonUtils.parseString(cloudVideoSetting))
                        .apply();
            }
        });
        getPaymentSetting().subscribe(new SubscriberProcessor<PaymentSetting>() {
            @Override
            public void onNext(PaymentSetting paymentSetting) {
                context.getApplicationContext()
                        .getSharedPreferences(SharedPreferencesHelper.SchoolSetting.XML_NAME, 0)
                        .edit()
                        .putString(SharedPreferencesHelper.SchoolSetting.PAYMENT_SETTING, GsonUtils.parseString(paymentSetting))
                        .apply();
            }

            @Override
            public void onError(String message) {
                context.getApplicationContext().getSharedPreferences(SharedPreferencesHelper.SchoolSetting.XML_NAME, 0)
                        .edit()
                        .remove(SharedPreferencesHelper.SchoolSetting.PAYMENT_SETTING)
                        .apply();
            }
        });

        getVIPSetting().subscribe(new SubscriberProcessor<VIPSetting>() {
            @Override
            public void onNext(VIPSetting vipSetting) {
                context.getApplicationContext()
                        .getSharedPreferences(SharedPreferencesHelper.SchoolSetting.XML_NAME, 0)
                        .edit()
                        .putString(SharedPreferencesHelper.SchoolSetting.VIP_SETTING, GsonUtils.parseString(vipSetting))
                        .apply();
            }
        });
    }

    public static <T> T getSetting(Type type, Context context, String key) {
        String setting = SharedPreferencesUtils
                .getInstance(context)
                .open(SharedPreferencesHelper.SchoolSetting.XML_NAME)
                .getString(key);
        return GsonUtils.parseJson(setting, type);
    }

    public static Observable<CourseSetting> getCourseSetting() {
        return HttpUtils.getOldInstance()
                .baseOnApi()
                .createApi(SettingApi.class)
                .getCourseSet()
                .filter(new Func1<CourseSetting, Boolean>() {
                    @Override
                    public Boolean call(CourseSetting courseSetting) {
                        return courseSetting != null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<UserSetting> getUserSetting() {
        return HttpUtils.getOldInstance()
                .baseOnApi()
                .createApi(SettingApi.class)
                .getUserSetting()
                .filter(new Func1<UserSetting, Boolean>() {
                    @Override
                    public Boolean call(UserSetting userSetting) {
                        return userSetting != null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<CloudVideoSetting> getCloudVideoSetting() {
        return HttpUtils.getOldInstance()
                .baseOnApi()
                .createApi(SettingApi.class)
                .getCloudVideoSetting()
                .filter(new Func1<CloudVideoSetting, Boolean>() {
                    @Override
                    public Boolean call(CloudVideoSetting cloudVideoSetting) {
                        return cloudVideoSetting != null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<PaymentSetting> getPaymentSetting() {
        return HttpUtils.getInstance()
                .createApi(SettingApi.class)
                .getPaymentSetting()
                .filter(new Func1<PaymentSetting, Boolean>() {
                    @Override
                    public Boolean call(PaymentSetting paymentSetting) {
                        return paymentSetting != null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<VIPSetting> getVIPSetting() {
        return HttpUtils.getInstance()
                .createApi(SettingApi.class)
                .getVIPSetting()
                .filter(new Func1<VIPSetting, Boolean>() {
                    @Override
                    public Boolean call(VIPSetting vipSetting) {
                        return vipSetting != null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
