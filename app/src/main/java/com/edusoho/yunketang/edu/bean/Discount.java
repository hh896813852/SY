package com.edusoho.yunketang.edu.bean;

import java.io.Serializable;

/**
 * Created by DF on 2017/4/10.
 */

public class Discount implements Serializable {
    public static final String STATUS_RUNNING = "running";

    public int id;
    public String name;
    public String type;
    public String startTime;
    public String endTime;
    public String status;
}
