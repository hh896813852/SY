package com.edusoho.yunketang.helper;

import com.edusoho.yunketang.R;

/**
 * Created by JesseHuang on 2017/5/4.
 */
public class CourseAccessHelper {

    public static final int SUCCESS = 1;
    public static final int ERROR   = -1;

    public static final String USER_LOCKED        = "user.locked";
    public static final String USER_NOT_LOGIN     = "user.not_login";
    //接口过滤，无需考虑此情况，后期可能会增加
    public static final String COURSE_CLOSED      = "course.closed";
    //接口过滤，无需考虑此情况
    public static final String COURSE_UNPUBLISHED = "course.unpublished";

    public static final String COURSE_REACH_MAX_STUDENT_NUM = "course.reach_max_student_num";
    public static final String COURSE_NOT_BUYABLE           = "course.not_buyable";
    public static final String COURSE_EXPIRED               = "course.expired";
    public static final String COURSE_BUY_EXPIRED           = "course.buy_expired";
    public static final String ONLY_VIP_JOIN_WAY            = "course.only_vip_join_way";
    public static final String COURSE_SUCCESS               = "success";

    public static final String MEMBER_EXIST         = "member.member_exist";
    public static final String MEMBER_EXPIRED       = "member.expired";
    public static final String MEMBER_VIP_EXPIRED   = "vip.member_expired";
    public static final String MEMBER_VIP_NOT_EXIST = "vip.level_not_exist";
    public static final String MEMBER_VIP_LOW       = "vip.level_low";
    public static final String COURSE_NOT_ARRIVE    = "course.not_arrive";
    public static final String VIP_NOT_MEMBER       = "vip.not_member";
    public static final String MEMBER_SUCCESS       = "success";


    public static int getErrorResId(String code) {
        int result;
        switch (code) {
            case USER_LOCKED:
                result = R.string.course_user_locked;
                break;
            case COURSE_REACH_MAX_STUDENT_NUM:
                result = R.string.course_reach_max_student_num;
                break;
            case COURSE_UNPUBLISHED:
                result = R.string.course_unpublish;
                break;
            case COURSE_NOT_BUYABLE:
                result = R.string.course_not_buy;
                break;
            case COURSE_CLOSED:
                result = R.string.course_limit_join;
                break;
            case COURSE_EXPIRED:
                result = R.string.course_date_limit;
                break;
            case COURSE_BUY_EXPIRED:
                result = R.string.course_project_expire_hint;
                break;
            case ONLY_VIP_JOIN_WAY:
                result = R.string.only_vip_learn;
                break;
            case MEMBER_EXPIRED:
                result = R.string.course_member_expired_dialog;
                break;
            case MEMBER_VIP_EXPIRED:
                result = R.string.member_vip_expired;
                break;
            case MEMBER_VIP_NOT_EXIST:
                result = R.string.member_vip_not_exist;
                break;
            case MEMBER_VIP_LOW:
                result = R.string.member_level_low;
                break;
            case COURSE_NOT_ARRIVE:
                result = R.string.course_not_start;
                break;
            default:
                result = R.string.unknown_error;
        }
        return result;
    }
}