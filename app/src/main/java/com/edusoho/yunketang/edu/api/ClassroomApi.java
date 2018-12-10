package com.edusoho.yunketang.edu.api;

import com.edusoho.yunketang.bean.course.ClassroomMember;
import com.edusoho.yunketang.bean.course.DiscussDetail;
import com.edusoho.yunketang.edu.bean.Classroom;
import com.edusoho.yunketang.edu.bean.CourseProject;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface ClassroomApi {

    @GET("classrooms/{classroomId}")
    Observable<Classroom> getClassroom(@Path("classroomId") int classroomId);

    @GET("classrooms/{classroomId}/courses")
    Observable<List<CourseProject>> getCourseProjects(@Path("classroomId") int classroomId);

    @POST("classrooms/{classroomId}/members")
    Observable<ClassroomMember> joinClassroom(@Path("classroomId") int id);

    //----以下是老接口不需要Accept----
    @GET("classrooms/{classroomId}/threads")
    Observable<DiscussDetail> getClassroomDiscuss(@Path("classroomId") int id, @Query("start") int start
            , @Query("limit") int limit, @Query("sort") String sort);

}
