package com.edusoho.yunketang.edu.bean;

import com.edusoho.yunketang.edu.bean.innerbean.TaskResult;

import java.io.Serializable;

/**
 * Created by JesseHuang on 2017/3/23.
 * 计划任务
 */

public class CourseTask implements Serializable {
    public boolean isSelected;
    public int        id;
    public int        courseId;
    public int        seq;
    public String     categoryId;
    public String     activityId;
    public String     title;
    public int        isFree;
    public String     isOptional;
    public long       startTime;
    public long       endTime;
    public String     mode;
    public String     status;
    public String     number;
    public String     type;
    public String     mediaSource;
    public String     maxOnlineNum;
    public String     fromCourseSetId;
    public int        length;
    public boolean    lock;
    public String     copyId;
    public String     createdUserId;
    public String     createdTime;
    public String     updatedTime;
    public Activity   activity;
    public TaskResult result;
    public int        migrateLessonId;

    public static class Activity implements Serializable {
        public String        id;
        public String        title;
        public Object        remark;
        public int           mediaId;
        public String        mediaType;
        public String        content;
        public String        length;
        public String        fromCourseId;
        public String        fromCourseSetId;
        public String        fromUserId;
        public String        copyId;
        public long          startTime;
        public String        endTime;
        public String        createdTime;
        public String        updatedTime;
        public String        replayStatus;
        public String        mediaStorage;
        /**
         * finish, end
         */
        public String        finishType;
        public String        finishDetail;
        public TestpaperInfo testpaperInfo;
    }

    public static class TestpaperInfo implements Serializable {

        private String testMode;
        /**
         * 单位：分钟，考试时长
         */
        private long   limitTime;
        private int    redoInterval;
        private int    doTimes;
        private Long   startTime;

        public String getTestMode() {
            return testMode;
        }

        public void setTestMode(String testMode) {
            this.testMode = testMode;
        }

        public long getLimitTime() {
            return limitTime;
        }

        public void setLimitTime(long limitTime) {
            this.limitTime = limitTime;
        }

        public int getRedoInterval() {
            return redoInterval;
        }

        public void setRedoInterval(int redoInterval) {
            this.redoInterval = redoInterval;
        }

        public int getDoTimes() {
            return doTimes;
        }

        public void setDoTimes(int doTimes) {
            this.doTimes = doTimes;
        }

        public Long getStartTime() {
            return startTime != null ? startTime : 0;
        }

        public void setStartTime(Long startTime) {
            this.startTime = startTime;
        }
    }

    public boolean isFinish() {
        return result != null && TaskResultEnum.FINISH.toString().equals(result.status);
    }
}
