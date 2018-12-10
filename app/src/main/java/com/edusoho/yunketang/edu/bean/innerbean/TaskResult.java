package com.edusoho.yunketang.edu.bean.innerbean;

import java.io.Serializable;

/**
 * Created by JesseHuang on 2017/4/27.
 */

public class TaskResult implements Serializable {
    public int id;
    public int activityId;
    public int courseId;
    public int courseTaskId;
    public int userId;
    public String finishedTime;
    public String createdTime;
    public String updatedTime;
    public String status;
}
