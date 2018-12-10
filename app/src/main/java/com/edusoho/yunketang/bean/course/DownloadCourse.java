package com.edusoho.yunketang.bean.course;

import android.text.TextUtils;

import com.edusoho.yunketang.bean.Course;

/**
 * Created by suju on 17/1/10.
 */

public class DownloadCourse extends Course {

    private int cachedLessonNum;

    private long cachedSize;

    private boolean expird;

    public boolean isExpird() {
        return expird;
    }

    public void setExpird(boolean expird) {
        this.expird = expird;
    }

    public int getCachedLessonNum() {
        return cachedLessonNum;
    }

    public void setCachedLessonNum(int cachedLessonNum) {
        this.cachedLessonNum = cachedLessonNum;
    }

    public long getCachedSize() {
        return cachedSize;
    }

    public void setCachedSize(long cachedSize) {
        this.cachedSize = cachedSize;
    }

    public String getPicture() {
        if (TextUtils.isEmpty(middlePicture)) {
            return "";
        }

        if (middlePicture.startsWith("//")) {
            return "http:" + middlePicture;
        }

        return middlePicture;
    }
}
