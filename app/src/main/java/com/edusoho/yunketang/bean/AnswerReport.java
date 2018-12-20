package com.edusoho.yunketang.bean;

import java.io.Serializable;
import java.util.List;

public class AnswerReport implements Serializable {
    public int correctSum; // 总正确数量
    public int falseSum;   // 总错误数量
    public String percent; // 正确率
    public List<AnswerDetails> returnList; // 各题型作答情况

    public class AnswerDetails implements Serializable {
        public int questionType; // 该题型类型
        public int correctCount; // 该题型正确数量
        public int falseCount;   // 该题型错误数量
        public String alias;     // 题目别名
        public List<Mistake> homeworkMistakes;

        public class Mistake {
            public String id;
            public String homeworkId;   // 作业id
            public int idex;            // 题目序号
            public String mistakeId;    // 错误题目id
            public int type;            // 错误类型（0：错误，1：未答）
            public int questionType;    // 题目类型（0：客观选择题，1：主观综合题）
            public String userResult;   // 用户答案(可以为空，因为用户可能未作答）
            public String score;
            public String homeworkDetailId;
        }
    }
}
