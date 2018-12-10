package com.edusoho.yunketang.edu.api;

import com.edusoho.yunketang.bean.seting.CloudVideoSetting;
import com.edusoho.yunketang.bean.seting.CourseSetting;
import com.edusoho.yunketang.bean.seting.PaymentSetting;
import com.edusoho.yunketang.bean.seting.UserSetting;
import com.edusoho.yunketang.bean.seting.VIPSetting;

import retrofit2.http.GET;
import rx.Observable;

public interface SettingApi {

    @GET("setting/payment")
    Observable<PaymentSetting> getPaymentSetting();

    @GET("settings/vip")
    Observable<VIPSetting> getVIPSetting();

    //---以下不需要Accept----
    @GET("setting/course")
    Observable<CourseSetting> getCourseSet();

    @GET("setting/user")
    Observable<UserSetting> getUserSetting();

    @GET("setting/cloud_video")
    Observable<CloudVideoSetting> getCloudVideoSetting();
}
