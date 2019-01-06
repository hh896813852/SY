package com.edusoho.yunketang.edu.bean;

import com.edusoho.yunketang.edu.bean.innerbean.Study;

import java.io.Serializable;

/**
 * Created by DF on 2017/5/11.
 */

public class StudyCourse implements Serializable {
    public int id;
    public int courseType; // 1、上元在线 2、上元会计
    public String title;
    public String courseSetTitle;
    public int publishedTaskNum;
    public int learnedNum;
    public Study courseSet;
    public int studentNum;
}
