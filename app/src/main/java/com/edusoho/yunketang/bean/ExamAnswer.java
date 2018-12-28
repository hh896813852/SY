package com.edusoho.yunketang.bean;

import java.io.Serializable;
import java.util.List;

public class ExamAnswer implements Serializable {
    public String questionId;
    public int index;
    public List<MyAnswer> correctResults;
    public List<MyAnswer> userResults;
}
