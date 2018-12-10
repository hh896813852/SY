package com.edusoho.yunketang.bean.course;

import java.io.Serializable;
import java.util.List;

/**
 * Created by remilia on 2017/1/9.
 */
public class CourseProgress implements Serializable {

    public String total;
    public List<Progress> resources;

    public static class Progress {
        public int courseId;
        public int totalLesson;
        public int learnedNum;
    }
}
