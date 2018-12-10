package com.edusoho.yunketang.edu.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by JesseHuang on 2017/4/6.
 */

public class CourseItem implements Serializable {
    public String     type;
    public int        number;
    public int        seq;
    public String     title;
    public CourseTask task;
    public List<CourseItem> childList;

    public String toTaskItemSequence() {
        if (seq != 0 && number != 0) {
            return number + " - " + seq;
        } else {
            return number + "";
        }
    }
}
