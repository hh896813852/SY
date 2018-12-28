package com.edusoho.yunketang.bean;

import com.edusoho.yunketang.utils.DateUtils;

import java.io.Serializable;

public class MsgInfo implements Serializable {
    public String id;         // 消息id
    public String postil;     // 老师批注
    public String unReadCount;// 未读消息数量
    public long createTime;   // 创建时间
    public long time;         // 最新消息时间
    public int readStatus;    // 消息是否已读（0：未读；1：已读）
    public String teacherName;// 老师姓名
    public String homeworkMistakeId;

    public String getTime() {
        return DateUtils.formatDate(createTime,"yyyy/MM/dd");
    }
}
