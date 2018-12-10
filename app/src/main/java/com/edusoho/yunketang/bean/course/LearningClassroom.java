package com.edusoho.yunketang.bean.course;

import com.edusoho.yunketang.edu.bean.Classroom;

import java.io.Serializable;
import java.util.List;

/**
 * Created by remilia on 2017/1/9.
 */
public class LearningClassroom implements Serializable {
    private int start;
    private int limit;
    private String total;
    private List<Classroom> data;

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<Classroom> getData() {
        return data;
    }

    public void setData(List<Classroom> data) {
        this.data = data;
    }
}
