package com.edusoho.yunketang.edu.api;


import com.google.gson.JsonObject;
import com.edusoho.yunketang.bean.course.DiscussDetail;
import com.edusoho.yunketang.edu.bean.CourseItem;
import com.edusoho.yunketang.edu.bean.CourseMember;
import com.edusoho.yunketang.edu.bean.CourseProject;
import com.edusoho.yunketang.edu.bean.CourseTask;
import com.edusoho.yunketang.edu.bean.DataPageResult;
import com.edusoho.yunketang.edu.bean.Member;
import com.edusoho.yunketang.edu.bean.Review;
import com.edusoho.yunketang.edu.bean.TaskEvent;

import java.util.List;
import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by JesseHuang on 2017/4/18.
 */

public interface CourseApi {

    @GET("courses/{courseId}/items")
    Observable<List<CourseItem>> getCourseItems(@Path("courseId") int courseId,
                                                @Query("onlyPublished") int status);

    @GET("courses/{courseId}/tasks/{taskId}")
    Observable<CourseTask> getCourseTask(@Path("courseId") int courseId,
                                         @Path("taskId") int taskId);

    @GET("courses/{courseId}")
    Observable<CourseProject> getCourseProject(@Path("courseId") int courseId);

    @GET("courses/{courseId}/members")
    Observable<DataPageResult<Member>> getCourseMembers(@Path("courseId") int courseId,
                                                        @Query("role") String role,
                                                        @Query("offset") int offset,
                                                        @Query("limit") int limit);

    @GET("courses/{courseId}/trial_tasks/first")
    Observable<CourseTask> getTrialFirstTask(@Path("courseId") int id);

    @GET("courses/{courseId}/reviews")
    Observable<DataPageResult<Review>> getCourseProjectReviews(@Path("courseId") int courseId,
                                                               @Query("offset") int offset,
                                                               @Query("limit") int limit);

    @PATCH("courses/{courseId}/tasks/{taskId}/events/finish")
    Observable<TaskEvent> setCourseTaskFinish(@Path("courseId") int courseId,
                                              @Path("taskId") int taskId);

    @FormUrlEncoded
    @PATCH("courses/{courseId}/tasks/{taskId}/events/doing")
    Observable<TaskEvent> setCourseTaskDoing(@Path("courseId") int courseId,
                                             @Path("taskId") int taskId,
                                             @FieldMap Map<String, String> fieldMaps);

    @POST("courses/{courseId}/members")
    Observable<CourseMember> joinFreeOrVipCourse(@Path("courseId") int courseId);

    @PATCH("courses/{courseId}/events/course_view")
    Observable<JsonObject> setCourseView(@Path("courseId") int courseId);

    @GET("courses/{courseId}/threads?limit=15&simplify=0&sort=posted")
    Observable<DiscussDetail> getCourseDiscuss(@Path("courseId") int courseId,
                                               @Query("start") int start);
}
