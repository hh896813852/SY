package com.edusoho.yunketang.edu.bean;

import java.io.Serializable;

/**
 * Created by DF on 2017/3/31.
 */

public class CourseReview implements Serializable {

        public String            content;
        public String            rating;
        public String            updatedTime;
        public CourseMember.User user;
        public CourseProject     course;

}
