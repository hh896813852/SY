package com.edusoho.yunketang.edu.bean;

import com.edusoho.yunketang.edu.bean.innerbean.TaskResult;

import java.io.Serializable;

/**
 * Created by JesseHuang on 2017/4/27.
 */

public class TaskEvent implements Serializable {
    public TaskResult result;
    public String event;
    public CourseTask nextTask;
    public String lastTime;
    public String completionRate;
}
