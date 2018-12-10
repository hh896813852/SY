package com.edusoho.yunketang.edu.bean;

import android.text.TextUtils;

import com.edusoho.yunketang.bean.Cover;
import com.edusoho.yunketang.bean.Price;
import com.edusoho.yunketang.edu.bean.innerbean.Access;
import com.edusoho.yunketang.edu.bean.innerbean.Teacher;

import java.io.Serializable;

/**
 * Created by JesseHuang on 2017/3/26.
 * 教学计划
 */

public class CourseProject implements Serializable {

    public  int                id;
    public  String             title;
    private String             subtitle;
    public  String             status;
    public  String             summary;
    public  int                studentNum;
    public  int                parentId;
    public  String[]           audiences;
    public  float              rating;
    public  float              price;
    public  float              originPrice;
    public  String             learnMode;
    public  int                isFree;
    public  int                taskNum;
    public  String             buyable;
    public  int                tryLookable;
    public  int                tryLookLength;
    public  int                vipLevelId;
    public  int                publishedTaskNum;
    public  int                enableFinish;
    public  int                watchLimit;
    public  Service[]          services;
    public  Teacher[]          teachers;
    public  SimpleCourseSet    courseSet;
    public  LearningExpiryDate learningExpiryDate;
    public Price price2;
    public  Price              originPrice2;
    public Access access;
    public  String             isAudioOn;

    public String getTitle() {
        if (this.courseSet != null && "默认教学计划".equals(this.title)) {
            return this.courseSet.title;
        } else {
            return title;
        }
    }

    public String getSubtitle() {
        if (TextUtils.isEmpty(subtitle)) {
            return "";
        } else {
            return subtitle;
        }
    }

    public static class Service implements Serializable {
        public String shortName;
        public String fullName;
        public String summary;
    }

    public static class SimpleCourseSet implements Serializable {
        public int    id;
        public String type;
        public String title;
        public String subtitle;
        public Cover cover;
        public int    studentNum;
        public String discount;
        public String summary;

        /**
         * 课程中价格最大计划的价格
         */
        public String maxCoursePrice;
        /**
         * 课程中价格最小计划的价格
         */
        public String minCoursePrice;
    }

    public static class LearningExpiryDate implements Serializable {
        public String  expiryMode;
        public String  expiryStartDate;
        public String  expiryEndDate;
        public String  expiryDays;
        public boolean expired;
    }

    public enum LearnMode {
        FREEMODE("freeMode"), LOCKMODE("lockMode");

        private String mName;

        LearnMode(String name) {
            this.mName = name;
        }

        public String getName() {
            return mName;
        }

        public static LearnMode getMode(String name) {
            for (LearnMode mode : values()) {
                if (mode.getName().equals(name))
                    return mode;
            }
            return null;
        }
    }

    public enum ExpiryMode {
        FOREVER("forever"), END_DATE("end_date"), DAYS("days"), DATE("date");

        private String mName;

        ExpiryMode(String name) {
            this.mName = name;
        }

        @Override
        public String toString() {
            return mName;
        }
    }
}
