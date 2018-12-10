package com.edusoho.yunketang.edu.api;

import com.edusoho.yunketang.bean.VipLevel;
import com.edusoho.yunketang.edu.bean.Classroom;
import com.edusoho.yunketang.edu.bean.CourseProject;
import com.edusoho.yunketang.edu.bean.CourseSet;
import com.edusoho.yunketang.edu.bean.DataPageResult;
import com.edusoho.yunketang.edu.bean.Discount;
import com.edusoho.yunketang.edu.bean.SimpleVip;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by JesseHuang on 2017/4/18.
 */

public interface PluginsApi {
    @GET("plugins/vip/vip_levels")
    Observable<List<VipLevel>> getVIPLevels();

    @GET("plugins/vip/vip_levels/{levelId}")
    Observable<VipLevel> getVipLevel(@Path("levelId") int id);

    @GET("plugins/discount/discounts/{discountId}")
    Observable<Discount> getDiscountInfo(@Path("discountId") int discountId);

    @GET("plugins/vip/vip_courses")
    Observable<DataPageResult<CourseProject>> getCourseProjectByLevelId(@Query("levelId") int levelId,
                                                                        @Query("offset") int offset,
                                                                        @Query("limit") int limit,
                                                                        @Query("parentId") int parendId,
                                                                        @Query("sort") String sort);

    @GET("plugins/vip/vip_classrooms")
    Observable<DataPageResult<Classroom>> getClassroomByLevelId(@Query("levelId") int levelId,
                                                                @Query("offset") int offset,
                                                                @Query("limit") int limit,
                                                                @Query("sort") String sort);

    @GET("plugins/vip/vip_course_sets")
    Observable<List<CourseSet>> getCourseSetsByLevelId(@Query("levelId") int levelId);

    @GET("plugins/vip/vip_users/{userId}")
    Observable<SimpleVip> getSimpleVipByUserId(@Path("userId") int userId);
}
