package com.edusoho.yunketang.bean;

import java.io.Serializable;
import java.util.List;

public class Question implements Serializable {
    public int questionType;       // 题型类型：1、单选题 2、多选题 3、阅读选择题 4、听力选择题 5、判断选择题 6、简答题 7、综合题 （0、题干（自定义））
    public String questionTypeName;// 题型类型名称
    public int questionSort;       // 题目在该题型中的顺序
    public String questionId;      // 题目id
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

    //--------------------------  题干参数  ------------------------------------//
    public int type;              // 题型类型
    public String alias;          // 题型别名
    public String id;             // 题型id
    public String examinationId;  // 试卷id
    public String templateId;     // 模板id
    public String explain;        // 题干说明
    public String sum;            // 题目总数
    public String point;          // 题目分数
    public String knowledgeIds;   // 知识点ids
    public String sids;           // 题目ids（英文逗号隔开）
    public int sort;              // 顺序

    /**
     * 获取题型类型名称
     */
    public String getQuestionTypeName() {
        int typeId = questionType == 0 ? type : questionType;
        switch (typeId) {
            case 1:
                return "单选题";
            case 2:
                return "多选题";
            case 3:
                return "阅读选择题";
            case 4:
                return "听力选择题";
            case 5:
                return "判断选择题";
            case 6:
                return "简答题";
            case 7:
                return "综合题";
        }
        return "";
    }
}
