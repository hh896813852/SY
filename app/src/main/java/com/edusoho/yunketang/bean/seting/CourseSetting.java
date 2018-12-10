package com.edusoho.yunketang.bean.seting;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by JesseHuang on 2017/5/7.
 */

public class CourseSetting implements Serializable {
    @SerializedName("show_student_num_enabled")
    public String showStudentNumEnabled;
    @SerializedName("chapter_name")
    public String chapterName;
    @SerializedName("part_name")
    public String partName;
}
