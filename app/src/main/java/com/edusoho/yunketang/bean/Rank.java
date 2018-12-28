package com.edusoho.yunketang.bean;

import java.io.Serializable;

public class Rank implements Serializable {
    public String userSumCorrectCount; // 用户模块正确总数
    public String userFalseCount;      // 用户模块错误总数
    public String beatPercent; // 击败率
    public String moduleRank;  // 做题排名
    public String classRank;   // 班级排名
    public int percent;        // 模块正确率
}
