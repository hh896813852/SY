package com.edusoho.yunketang.bean;

import com.edusoho.yunketang.utils.DateUtils;

import java.io.Serializable;

public class ClassCourse implements Serializable {
    public String sign;
    public String id;
    public long courseDate;
    public long startTime;
    public long endTime;
    public String lengthTime;
    public String classCourseId;
    public String teacherId;
    public String classId;
    public String courseName;
    public String className;
    public String dayOfWeek;

    /**
     * 获取课程日期
     */
    public String getCourseDate() {
        return DateUtils.formatDate(courseDate, "MM-dd");
    }

    /**
     * 获取开始和结束时间
     */
    public String getStartAndEndTime() {
        return DateUtils.formatDate(startTime, "HH:mm") + "-" + DateUtils.formatDate(endTime, "HH:mm");
    }
}
