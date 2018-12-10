package com.edusoho.yunketang.edu.bean;

import java.io.Serializable;

/**
 * Created by JesseHuang on 2017/5/29.
 */

public class ErrorResult implements Serializable {
    public Error error;

    public static class Error implements Serializable {
        public String message;
        public int code;
    }
}
