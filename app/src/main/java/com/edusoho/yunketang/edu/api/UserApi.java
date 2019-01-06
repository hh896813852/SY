package com.edusoho.yunketang.edu.api;

import com.edusoho.yunketang.bean.User;
import com.edusoho.yunketang.bean.course.Study;
import com.edusoho.yunketang.bean.thread.MyThreadEntity;
import com.edusoho.yunketang.edu.bean.ChatRoomResult;
import com.edusoho.yunketang.edu.bean.Classroom;
import com.edusoho.yunketang.edu.bean.CourseLearningProgress;
import com.edusoho.yunketang.edu.bean.CourseMember;
import com.edusoho.yunketang.edu.bean.DataPageResult;
import com.edusoho.yunketang.edu.bean.StudyCourse;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.http.DELETE;
import retrofit2.http.Field;
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

public interface UserApi {

    @GET("me")
    Observable<User> getUser();

    /**
     * 查询用户
     *
     * @param value 标示类型 {nickname,id,email,mobile}
     * @return
     */
    @GET("user/{value}")
    Observable<User> getUser(@Path("type") String value);

    @GET("me/courses")
    Observable<DataPageResult<StudyCourse>> getMyStudyCourse(@Query("offset") int offset, @Query("limit") int limit);

    @GET("me/live_course_sets")
    Observable<List<Study>> getMyStudyLiveCourseSet(@Query("offset") int offset, @Query("limit") int limit);

    @GET("me/classrooms")
    Observable<List<Classroom>> getMyClassrooms(@Query("offset") int offset, @Query("limit") int limit);

    @GET("me/course_members/{courseId}")
    Observable<CourseMember> getCourseMember(@Path("courseId") int courseId);

    @GET("me/course_learning_progress/{courseId}")
    Observable<CourseLearningProgress> getMyCourseLearningProgress(@Path("courseId") int courseId);

    @GET("me/favorite_course_sets/{courseSetId}")
    Observable<JsonObject> getFavorite(@Path("courseSetId") int courseSetId);

    @DELETE("me/favorite_course_sets/{courseSetId}")
    Observable<JsonObject> cancelFavoriteCourseSet(@Path("courseSetId") int courseSetId);

    @FormUrlEncoded
    @POST("me/favorite_course_sets")
    Observable<JsonObject> favoriteCourseSet(@Field("courseSetId") int courseSetId);

    @DELETE("me/course_members/{courseId}")
    Observable<JsonObject> exitCourse(@Path("courseId") int courseId);

    @GET("me/vip_levels/{levelId}")
    Observable<JsonObject> isVip(@Path("levelId") int levelId);

    @GET("me/favorite_course_sets")
    Observable<DataPageResult<Study>> getMyFavoriteCourseSet(@Query("offset") int offset, @Query("limit") int limit);

    @GET("me/classroom_members/{classroomId}")
    Observable<JsonObject> getClassroomStatus(@Path("classroomId") int classroomId);

    @GET("me/course_sets/{courseSetId}/course_members")
    Observable<List<CourseMember>> getMeCourseSetProject(@Path("courseSetId") int courseSetId);

    @FormUrlEncoded
    @POST("user")
    Observable<User> register(@Field("mobile") String mobile, @Field("smsToken") String smsToken,
                              @Field("smsCode") String smsCode, @Field("encrypt_password") String password);

    @FormUrlEncoded
    @PATCH("user/{mobile}/password/mobile")
    Observable<User> resetPasswordBySMS(@Path("mobile") String mobile, @Field("smsToken") String smsToken,
                                        @Field("smsCode") String smsCode, @Field("encrypt_password") String password);

    @FormUrlEncoded
    @PATCH("user/{email}/password/email")
    Observable<User> resetPasswordByMail(@Path("email") String mail, @Field("dragCaptchaToken") String dragCaptchaToken);


    //----以下是老接口不需要Accept----

    @GET("me/chatrooms")
    Observable<ChatRoomResult> getChatRooms();

    @GET("chaos_threads/getThreads")
    Observable<MyThreadEntity[]> getMyAskThread(@Query("offset") int offset, @Query("limit") int limit);

    @GET("chaos_threads_posts/getThreadPosts")
    Observable<MyThreadEntity[]> getMyAnswerThread(@Query("offser") int offset, @Query("limit") int limit);
}
