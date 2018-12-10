package com.edusoho.yunketang.edu.api;

import com.edusoho.yunketang.bean.Course;

import java.util.List;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by JesseHuang on 2017/5/22.
 */

public interface AppApi {

    @GET("app/channels")
    Observable<List<Course>> getAppChannels();

}
