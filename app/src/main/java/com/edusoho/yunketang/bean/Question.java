package com.edusoho.yunketang.bean;

import java.io.Serializable;
import java.util.List;

public class Question implements Serializable {
    public int questionType;       // 题型类型：1、单选题 2、多选题 3、阅读选择题 4、听力选择题 5、判断选择题 6、简答题 7、综合题
    public String questionTypeName;// 题型类型名称
    public String topic;           // 题目说明（文字）
    public String topicVoiceUrl;   // 题目说明（语音）
    public String topicPictureUrl; // 题目说明（图片）
    public List<QuestionDetails> details; // 题目（可包含多个）

    public class QuestionDetails {
        public String correctResult;   // 正确答案
        public String choiceType;      // 选项类型 （0:文字，1：图片，2：语音）
        public String choiceMode;      // 选项模式 （0：ABCD模式，1:1234模式）
        public String resultResolveUrl;// 答案url
        public String resultResolve;   // 答案解析
        public String choices_a; // 选项A
        public String choices_b; // 选项B
        public String choices_c; // 选项C
        public String choices_d; // 选项D
        public String choices_e; // 选项E
        public String choices_f; // 选项F
        public String choices_g; // 选项G
    }
}
