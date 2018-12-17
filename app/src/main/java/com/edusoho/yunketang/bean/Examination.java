package com.edusoho.yunketang.bean;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import com.edusoho.yunketang.R;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Examination implements Serializable {
    public boolean isShowDetail;  // 自定义字段：表示是否显示题型详情
    public String examinationId;  // 试卷主键id
    public String homeworkId;     // 作业id
    public String examinationName;// 试卷名称
    public int finishState;       // 0：未开始，1：已开始未完成，2：已完成
    @SerializedName("sum")
    public String questionSums;   // 题目总数
    @SerializedName("finishCountSum")
    public String finishedSum;    // 完成题目数量
    public int lastPageIndex;     // 最后停留页面
    public String trueSum;        // 正确数量
    public String falseSum;       // 错误数量
    public String completeSum;    // 试卷已完成人数
    public String completeDate;   // 试卷完成时间
    public int chargeMode;        // 收费模式（0：免费，1：收费）
    public String price;          // 价格(元）
    public List<ExaminationInfo> examinationInfo; // 题型信息

    public class ExaminationInfo {
        public int questionSum;        // 题目数量
        public int questionType;       // 题目类型：1、单选题 2、多选题 3、阅读选择题 4、听力选择题 5、判断选择题 6、简答题 7、综合题
        public String questionTypeName;// 题目类型名称
        public int questionPoint;      // 题目分值

        /**
         * 获取题型信息
         */
        public String getQuestionInfo() {
            return "共" + questionSum + "题，每题" + questionPoint + "分";
        }

        /**
         * 获取题型背景
         */
        public Drawable getQuestionTypeDrawable(Context context) {
            switch (questionType) {
                case 1: // 单选题
                    return ContextCompat.getDrawable(context, R.drawable.bg_single_choice);
                case 2: // 多选题
                    return ContextCompat.getDrawable(context, R.drawable.bg_multiple_choice);
                case 3: // 阅读选择题
                    return ContextCompat.getDrawable(context, R.drawable.bg_read_choice);
                case 4: // 听力选择题
                    return ContextCompat.getDrawable(context, R.drawable.bg_listen_choice);
                case 5: // 判断选择题
                    return ContextCompat.getDrawable(context, R.drawable.bg_true_or_false);
                case 6: // 简答题
                    return ContextCompat.getDrawable(context, R.drawable.bg_simple_answer);
                case 7: // 综合题
                    return ContextCompat.getDrawable(context, R.drawable.bg_content_analyze);
            }
            return ContextCompat.getDrawable(context, R.drawable.bg_content_analyze);
        }
    }

    /**
     * 获取试卷状态信息
     */
    public String getExamStatus() {
        switch (finishState) {
            case 0:
                return "共" + questionSums + "题";
            case 1:
                return finishedSum + "/" + questionSums;
            case 2:
                return "对:" + trueSum + "      错:" + falseSum;
        }
        return "";
    }
}
