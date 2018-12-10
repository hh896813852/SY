package com.edusoho.yunketang.edu.bean;

/**
 * Created by JesseHuang on 15/4/23.
 */
public class MessageType {

    public static final int UI_THREAD = 0001;
    public static final int BG_THREAD = 0010;

    public int code;
    public String type;
    public int runType;

    public static int NONE = -1;

    @Override
    public String toString() {
        if (code == NONE) {
            return type;
        }
        return type + "_" + code;
    }

    public MessageType(int runType) {
        this.runType = runType;
    }

    public MessageType(String type) {
        this(BG_THREAD);
        this.code = NONE;
        this.type = type;
    }

    public MessageType(String type, int runType) {
        this(runType);
        this.code = NONE;
        this.type = type;
    }

    public MessageType(int code, String type) {
        this(BG_THREAD);
        this.code = code;
        this.type = type;
    }

    public MessageType(int code, String type, int runType) {
        this(runType);
        this.code = code;
        this.type = type;
    }
}
