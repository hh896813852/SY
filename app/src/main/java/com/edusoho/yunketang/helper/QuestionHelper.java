package com.edusoho.yunketang.helper;

public class QuestionHelper {

    public static String getSort(int i) {
        switch (i) {
            case 0:
                return "第一题";
            case 1:
                return "第二题";
            case 2:
                return "第三题";
            case 3:
                return "第四题";
            case 4:
                return "第五题";
            case 5:
                return "第六题";
            case 6:
                return "第七题";
            case 7:
                return "第八题";
            case 8:
                return "第九题";
            case 9:
                return "第十题";
        }
        return "第" + (i + 1) + "题";
    }
}
