package com.edusoho.yunketang.edu.bean;

import java.io.Serializable;

/**
 * Created by JesseHuang on 2017/4/14.
 */

public class CourseLearningProgress implements Serializable {

    public int taskCount;
    public float progress;
    public int taskResultCount;
    public int taskPerDay;
    public int planStudyTaskCount;
    public int planProgressProgress;

    public CourseMember member;
    public CourseTask nextTask;
}
