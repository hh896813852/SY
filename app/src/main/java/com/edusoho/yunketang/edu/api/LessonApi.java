package com.edusoho.yunketang.edu.api;

import com.edusoho.yunketang.bean.lesson.LessonItem;
import com.google.gson.JsonObject;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface LessonApi {

    @GET("lessons/{id}?hls_encryption=1")
    Observable<LessonItem> getLesson(@Path("id") int lessonId);

    @FormUrlEncoded
    @POST("lessons/{lessonId}/live_tickets")
    Observable<JsonObject> getLiveTicket(@Path("lessonId") int taskId, @Field("device") String device);

    @GET("lessons/{lessonId}/live_tickets/{ticket}")
    Observable<JsonObject> getLiveTicketInfo(@Path("lessonId") int taskId, @Path("ticket") String ticket);

    @GET("lessons/{lessonId}/replay")
    Observable<JsonObject> getLiveReplay(@Path("lessonId") int taskId, @Query("device") String device);
}