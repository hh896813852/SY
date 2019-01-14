package com.edusoho.yunketang.bean;

import android.text.TextUtils;

import com.edusoho.yunketang.utils.DateUtils;

import java.io.Serializable;

public class ClassInfo implements Serializable {
    public int itemHeight;
    public String sign;
    public String id;
    public String classCode;
    public String className;
    public int classType;
    public String subjectId;
    public String schoolId;
    public String productId;
    public int lectureType;
    public long startTime;
    public long endTime;
    public int homeworkStatus;
    public String classPersons;
    public String studyModality;
    public String batch;
    public String alreadyCourses;
    public String alreadyCoursesMins;
    public String dayOfWeek;
    public String majorName;
    public String classStatus;
    public String totalClassCount;
    public float totalClassHour;
    public String classCourseEntities;
    public String subjectName;
    public String schoolName;
    public String paperIds;
    public String businessType;
    public String gradateMajor;

    public String getTitleName() {
        if (!TextUtils.isEmpty(className)) {
            return className;
        }
        return batch + gradateMajor;
    }

    /**
     * 获取学习进度
     */
    public int getProgress() {
        if (TextUtils.isEmpty(alreadyCourses)) {
            return 0;
        }
        return Integer.valueOf(alreadyCourses);
    }

    /**
     * 获取学习总进度
     */
    public int getProgressMax() {
        if (TextUtils.isEmpty(totalClassCount)) {
            return 0;
        }
        return Integer.valueOf(totalClassCount);
    }

    /**
     * 获取总课数（个位数前面加0）
     */
    public String getTotalClassCount() {
        if (TextUtils.isEmpty(totalClassCount)) {
            return "0";
        }
        return totalClassCount.length() == 1 ? "0" + totalClassCount : totalClassCount;
    }

    /**
     * 获取总分钟
     */
    public String getTotalClassMin() {
        return String.valueOf((int) totalClassHour * 60);
    }

    /**
     * 获取开始和结束时间
     */
    public String getClassTime() {
        return DateUtils.formatDate(startTime, "yyyy/MM") + "-" + DateUtils.formatDate(endTime, "yyyy/MM");
    }
}
