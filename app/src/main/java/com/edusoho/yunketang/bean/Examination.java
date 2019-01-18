package com.edusoho.yunketang.bean;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.edusoho.yunketang.R;
import com.edusoho.yunketang.utils.DateUtils;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class Examination implements Serializable {
    public boolean isShowDetail;  // 自定义字段：表示是否显示题型详情
    public String classId;        // 班级id
    public String examinationId;  // 试卷主键id
    public String homeworkId;     // 作业id
    public String homeworkType;   // 1、模块作业 2、班级作业
    public int moduleId;          // 模块id
    public int businessType;      // 行业id
    public int levelId;           // 职业/等级id
    public int courseId;          // 课程id
    public String examinationName;// 试卷名称
    public int finishState;       // 0：未开始，1：已开始未完成，2：已完成
    public String sum;            // 题目总数
    public String count;          // 题目总数
    public String questionSums;   // 题目总数
    @SerializedName("finishCountSum")
    public String finishedSum;    // 完成题目数量
    public int androidIndex;      // 最后停留页面
    public String trueSum;        // 正确数量
    public String falseSum;       // 错误数量
    public String completeSum;    // 试卷已完成人数
    public String completeDate;   // 试卷完成时间
    public String sumMinute;      // 试卷规定时长
    public int chargeMode;        // 收费模式（0：免费，1：收费）
    public String price;          // 价格(元）
    public boolean isPay;         // 是否购买了
    public List<ExaminationInfo> examinationInfo; // 题型信息

    public class ExaminationInfo {
        public int questionSum;        // 题目数量
        public int questionType;       // 题目类型：1、单选题 2、多选题 3、阅读选择题 4、听力选择题 5、判断选择题 6、简答题 7、综合题
        public String questionTypeName;// 题目类型名称
        public float questionPoint;    // 题目分值

        /**
         * 获取题型信息
         */
        public String getQuestionInfo() {
            return "共" + questionSum + "题，共" + String.valueOf(questionPoint).replace(".0", "") + "分";
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
                return "共" + (TextUtils.isEmpty(questionSums) ? sum : questionSums) + "题";
            case 1:
                return (TextUtils.isEmpty(finishedSum) ? "0" : finishedSum) + "/" + sum;
            case 2:
                return "对:" + trueSum + "      错:" + falseSum;
        }
        return "";
    }

    /**
     * 获取试卷状态信息
     */
    public String getTotalCount() {
        return "共" + count + "题";
    }

    /**
     * 已完成人数
     */
    public String getCompletePerson() {
        if (TextUtils.isEmpty(completeSum) || "0".equals(completeSum)) {
            return "还没有人完成";
        }
        return completeSum + "人已完成";
    }

    /**
     * 试卷时长
     */
    public String getExaminationTime() {
        if (TextUtils.isEmpty(sumMinute)) {
            return "";
        }
        return DateUtils.second2Min2(Integer.valueOf(sumMinute) * 60);
    }

    /**
     * 开始按钮文字
     */
    public String getStartText() {
        switch (finishState) {
            case 0:
                return chargeMode == 0 || isPay ? "开始" : "¥" + price;
            case 1:
                return "开始";
            case 2:
                return "完成";
        }
        return "";
    }

    /**
     * 获取试卷完成进度总值
     */
    public int getProgressMax() {
        switch (finishState) {
            case 0:
                return 100;
            case 1:
                return Integer.valueOf(sum);
            case 2:
                return 100;
        }
        return 1;
    }

    /**
     * 获取试卷当前完成进度
     */
    public int getCurrentProgress() {
        switch (finishState) {
            case 0:
                return 0;
            case 1:
                return Integer.valueOf(finishedSum);
            case 2:
                return 100;
        }
        return 0;
    }
}
