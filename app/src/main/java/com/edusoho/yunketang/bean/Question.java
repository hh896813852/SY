package com.edusoho.yunketang.bean;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Question implements Serializable {
    public int questionType;       // 题型类型：1、单选题 2、多选题 3、阅读选择题 4、听力选择题 5、判断选择题 6、简答题 7、综合题 （0、题干（自定义））
    public String questionTypeName;// 题型类型名称
    public int questionSort;       // 题目在该题型中的顺序
    public String questionId;      // 题目id
    public boolean isStar;         // 题目是否收藏
    public String starId;          // 收藏id
    public String topic;           // 题目说明（文字）
    public String topicVoiceUrl;   // 题目说明（语音）
    public String topicPictureUrl; // 题目说明（图片）
    public String userResult;      // 用户答案
    public List<QuestionDetails> details; // 题目（可包含多个）

    public String subclassification;     // 题型子分类id
    public String subclassificationName; // 题型子分类名称
    public String errorId;               // 错误题目id（我的错题里面含有,用于删除错题）

    public String score;           // 得分
    public Postil homeworkMistake; // 我的消息->老师批注使用

    public class Postil implements Serializable {
        public String id;
        public String type;
        public String score;
        public String postil;
        public String postilUrl;
        public String userResult;
    }

    public class QuestionDetails implements Serializable {
        public int childQuestionType;  // 子题类型：1、单选题 2、简答题（自定义）
        public String correctResult;   // 正确答案
        public String correctResultUrl;// 正确答案图片url
        public int choiceType;         // 选项类型（0:文字，1：图片，2：语音）
        public int choiceMode;         // 选项模式（0：ABCD模式，1:1234模式）
        public String postil;          // 老师批注
        public String postilUrl;       // 老师批注url
        public String resultResolve;   // 答案解析
        public String resultResolveUrl;// 答案图片url
        public int childQuestionSort;  // 子题题目序号
        public String topicSubsidiary; // 子题题目
        public String topicSubsidiaryUrl;// 子题题目图片
        public String choices_a; // 选项A
        public String choices_b; // 选项B
        public String choices_c; // 选项C
        public String choices_d; // 选项D
        public String choices_e; // 选项E
        public String choices_f; // 选项F
        public String choices_g; // 选项G
        public List<Option> options; // 选项集合（自定义）
        public String myAnswerContent;// 我的作答文字内容（自定义）
        public String myAnswerPicUrl; // 我的作答图片url（自定义）

        public class Option implements Serializable {
            public int choiceType;      // 选项类型（0:文字，1：图片，2：语音）
            public String optionType;   // 选项类型
            public String optionContent;// 选项内容
            public String optionPicUrl; // 选项图片url
            public boolean isPicked;    // 是否被选中
            public boolean isRight;     // 是否正确
        }

        /**
         * 获取正确答案
         */
        public String getCorrectResult() {
            String correctStr = correctResult;
            if (choiceMode == 0) {
                correctStr = correctStr.replace("1", "A")
                        .replace("2", "B")
                        .replace("3", "C")
                        .replace("4", "D")
                        .replace("5", "E")
                        .replace("6", "F")
                        .replace("7", "G");
            }
            return correctStr;
        }

        /**
         * 获取用户答案
         */
        public String getUserResult(String userResult) {
            String userResultStr = userResult;
            if (choiceMode == 0) {
                userResultStr = userResultStr.replace("1", "A")
                        .replace("2", "B")
                        .replace("3", "C")
                        .replace("4", "D")
                        .replace("5", "E")
                        .replace("6", "F")
                        .replace("7", "G");
            }
            return userResultStr;
        }

        /**
         * 获取想选列表
         */
        public List<Option> getChoiceList() {
            List<Option> list = new ArrayList<>();
            if (!TextUtils.isEmpty(choices_a)) {
                Option option = new Option();
                option.choiceType = choiceType;
                option.optionType = choiceMode == 0 ? "A" : "1";
                if (choiceType == 0) { // 文字
                    option.optionContent = choices_a;
                }
                if (choiceType == 1) { // 图片
                    option.optionPicUrl = choices_a;
                }
                list.add(option);
            }
            if (!TextUtils.isEmpty(choices_b)) {
                Option option = new Option();
                option.choiceType = choiceType;
                option.optionType = choiceMode == 0 ? "B" : "2";
                if (choiceType == 0) { // 文字
                    option.optionContent = choices_b;
                }
                if (choiceType == 1) { // 图片
                    option.optionPicUrl = choices_b;
                }
                list.add(option);
            }
            if (!TextUtils.isEmpty(choices_c)) {
                Option option = new Option();
                option.choiceType = choiceType;
                option.optionType = choiceMode == 0 ? "C" : "3";
                if (choiceType == 0) { // 文字
                    option.optionContent = choices_c;
                }
                if (choiceType == 1) { // 图片
                    option.optionPicUrl = choices_c;
                }
                list.add(option);
            }
            if (!TextUtils.isEmpty(choices_d)) {
                Option option = new Option();
                option.choiceType = choiceType;
                option.optionType = choiceMode == 0 ? "D" : "4";
                if (choiceType == 0) { // 文字
                    option.optionContent = choices_d;
                }
                if (choiceType == 1) { // 图片
                    option.optionPicUrl = choices_d;
                }
                list.add(option);
            }
            if (!TextUtils.isEmpty(choices_e)) {
                Option option = new Option();
                option.choiceType = choiceType;
                option.optionType = choiceMode == 0 ? "E" : "5";
                if (choiceType == 0) { // 文字
                    option.optionContent = choices_e;
                }
                if (choiceType == 1) { // 图片
                    option.optionPicUrl = choices_e;
                }
                list.add(option);
            }
            if (!TextUtils.isEmpty(choices_f)) {
                Option option = new Option();
                option.choiceType = choiceType;
                option.optionType = choiceMode == 0 ? "F" : "6";
                if (choiceType == 0) { // 文字
                    option.optionContent = choices_f;
                }
                if (choiceType == 1) { // 图片
                    option.optionPicUrl = choices_f;
                }
                list.add(option);
            }
            if (!TextUtils.isEmpty(choices_g)) {
                Option option = new Option();
                option.choiceType = choiceType;
                option.optionType = choiceMode == 0 ? "G" : "7";
                if (choiceType == 0) { // 文字
                    option.optionContent = choices_g;
                }
                if (choiceType == 1) { // 图片
                    option.optionPicUrl = choices_g;
                }
                list.add(option);
            }
            return list;
        }
    }

    //--------------------------  题干参数  ------------------------------------//
    public int type;              // 题干下题目类型
    public String alias;          // 题型别名
    public String id;             // 题型id
    public String examinationId;  // 试卷id
    public String templateId;     // 模板id
    public String explain;        // 题干说明
    public String classification; // 子题型id
    public String questionSum;    // 该题型下题目总数
    public String point;          // 题目分数
    public String knowledgeIds;   // 知识点ids
    public String sids;           // 题目ids（英文逗号隔开）
    public int sort;              // 顺序
    public int sumPoint;          // 该题型下所有题目分值
    public String faultSort;      // 错题序号（自定义，用于错题解析）

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
