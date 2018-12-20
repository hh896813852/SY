package com.edusoho.yunketang.bean;

import java.io.Serializable;
import java.util.List;

public class FaultRecord implements Serializable {
    public String examinationName; // 试卷名称
    public String count;           // 错误题数
    public String errorId;
    public String homeworkId;
    public List<FaultQuestion> questions; // 错题列表

    public class FaultQuestion implements Serializable {
        public String id;
        public String errorId;
        public String homeworkId;
        public String examinationId; // 试卷id，用于添加收藏
        public String type;     // 题目类型(1：单项选择题，2：多项选择题，3：阅读选择题，4：听力选择题，5：判断选择题，6：简答题，7：综合题）
        public String alias;    // 题目别名
        public String selectId; // 错题（选择题）id
        public String count;    // 错误练习次数
        public String state;    // 状态（0：删除，1：未删除）
    }
}
