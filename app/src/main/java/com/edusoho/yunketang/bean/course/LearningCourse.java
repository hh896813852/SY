package com.edusoho.yunketang.bean.course;

import com.edusoho.yunketang.bean.Course;

import java.io.Serializable;
import java.util.List;

/**
 * Created by remilia on 2017/1/9.
 */
public class LearningCourse implements Serializable {
    public int start;
    public int limit;
    public int total;
    public List<Course> data;
    public Error error;
}
