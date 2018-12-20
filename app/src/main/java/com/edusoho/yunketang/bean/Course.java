package com.edusoho.yunketang.bean;

import android.util.Log;

import com.edusoho.yunketang.edu.bean.CourseProject;
import com.edusoho.yunketang.edu.bean.innerbean.Teacher;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zy on 2018/11/8 0008.
 */

public class Course implements Serializable {

    public int             id;
    public int             courseType;// 自定义 1、上元在线 2、上元会计
    public Boolean         isSelected;
    public String          title;
    public int             categoryId;
    public String          orderType;
    public String          showCount;
    public int             actualCount;
    public List<Discovery> data;

    public static class Discovery implements Serializable {
        public int    id;
        public String type;
        public String title;
        public String subtitle;
        public String summary;
        public Cover  cover;
        public int    studentNum;
        public float  discount;
        public float  price;
        public float  maxCoursePrice;
        public float  minCoursePrice;
        public Price  minCoursePrice2;
        public Price  maxCoursePrice2;
        public Price  price2;
    }

    public String summary;
    public Cover  cover;
    public float  discount;
    public float  maxCoursePrice;
    public float  minCoursePrice;
    public Price  minCoursePrice2;
    public Price  maxCoursePrice2;
    public Price  price2;
    public int       studentNum;
    public double    rating;
    public String    smallPicture;
    public double    price;
    public double    originPrice;
    public String    coinPrice;
    public String    originCoinPrice;
    public int       courseSetId;
    public int       parentId;
    public String    subtitle;
    public String    expiryDay;
    public String    showStudentNumType;
    public String    income;
    public String    status;
    public int       lessonNum;
    public int       giveCredit;
    public int       maxStudentNum;
    public String    ratingNum;
    public String    serializeMode;
    public String    middlePicture;
    public String    largePicture;
    public String    about;
    public String[]  goals;
    public String[]  audiences;
    public String    address;
    public String    hitNum;
    public String    userId;
    public int       vipLevelId;
    public String    createdTime;
    public Teacher[] teachers;
    public Teacher   creator;
    public String    type;
    public String    buyable;
    public String    convNo;
    public int       learnedNum;
    public int       totalLesson;
    public long      courseDeadline;
    public int       liveState;
    public String    courseSetTitle;

    private String sourceName;
    public  String source;

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getLargePicture() {
        int schemIndex = largePicture.lastIndexOf("http://");
        if (schemIndex != -1) {
            return largePicture.substring(schemIndex);
        }
        if (largePicture.startsWith("//")) {
            return "http:" + largePicture;
        }
        return largePicture;
    }

    public Course parse(CourseProject courseProject) {
        try {
            if (courseProject != null && courseProject.courseSet != null
                    && courseProject.courseSet.cover != null) {
                this.id = courseProject.id;
                this.title = courseProject.getTitle();
                this.largePicture = courseProject.courseSet.cover.large;
                this.middlePicture = courseProject.courseSet.cover.middle;
                this.courseSetTitle = courseProject.courseSet.title;
            }
        } catch (Exception ex) {
            Log.d("flag--", "parse: " + ex.getMessage());
        }
        return this;
    }
}
