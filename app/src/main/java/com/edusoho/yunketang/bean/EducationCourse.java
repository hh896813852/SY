package com.edusoho.yunketang.bean;

import java.io.Serializable;

public class EducationCourse implements Serializable {
    public int id;
    public int businessId;
    public String businessName;
    public int levelId;
    public String levelName;  // 职业/等级 名称
    public int courseId;
    public String courseName; // 课程名称
    public int state;
}
