package com.edusoho.yunketang.edu.api;


import com.edusoho.yunketang.edu.bean.CourseMember;
import com.edusoho.yunketang.edu.bean.CourseProject;
import com.edusoho.yunketang.edu.bean.CourseReview;
import com.edusoho.yunketang.edu.bean.CourseSet;
import com.edusoho.yunketang.edu.bean.DataPageResult;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by JesseHuang on 2017/4/18.
 */

public interface CourseSetApi {

    @GET("course_sets/{courseSetId}")
    Observable<CourseSet> getCourseSet(@Path("courseSetId") int courseSetId);

    @GET("course_sets/{courseSetId}/reviews")
    Observable<DataPageResult<CourseReview>> getCourseReviews(@Path("courseSetId") int courseSetId,
                                                              @Query("limit") int limit,
                                                              @Query("offset") int offset);

    @GET("course_sets/{courseSetId}/courses")
    Observable<List<CourseProject>> getCourseProjects(@Path("courseSetId") int courseSetId);

    @GET("course_sets/{courseSetId}/latest_members")
    Observable<List<CourseMember>> getCourseSetMembers(@Path("courseSetId") int courseSetId,
                                                       @Query("offset") int offset,
                                                       @Query("limit") int limit);
}
