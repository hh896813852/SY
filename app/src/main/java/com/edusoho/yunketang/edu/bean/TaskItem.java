package com.edusoho.yunketang.edu.bean;

import java.io.Serializable;

/**
 * Created by JesseHuang on 2017/4/6.
 * 用于显示教育计划任务列表
 */

public class TaskItem implements Serializable {
    public int id;
    public String courseId;
    public String type;
    public String parentId;
    public int isFree;
    public int number;
    public int secondNumber;
    public int seq;
    public String title;
    public String length;
    public String createdTime;

    public String toTaskSequence() {
        if (number == 0) {
            return "";
        } else if (secondNumber == 0) {
            return number + "";
        } else {
            return String.format("%d-%d：", number, secondNumber);
        }
    }
}
