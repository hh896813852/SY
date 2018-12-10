package com.edusoho.yunketang.edu.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by JesseHuang on 2017/5/2.
 */

public enum TaskFinishType {
    TIME("time"), END("end"), SCORE("score"), SUBMIT("submit");

    private String mName;

    TaskFinishType(String name) {
        this.mName = name;
    }

    private static final Map<String, TaskFinishType> mTaskFinishTypeMaps = new HashMap<>();

    static {
        for (TaskFinishType taskTypeEnum : values()) {
            mTaskFinishTypeMaps.put(taskTypeEnum.toString(), taskTypeEnum);
        }
    }

    public static TaskFinishType fromString(String type) {
        return mTaskFinishTypeMaps.get(type);
    }

    @Override
    public String toString() {
        return this.mName;
    }
}
