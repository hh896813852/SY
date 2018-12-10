package com.edusoho.yunketang.bean;


import com.edusoho.yunketang.bean.lesson.LessonItem;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by JesseHuang on 15/6/15.
 */
public class LessonsResult {
    public ArrayList<LessonItem> lessons;
    public HashMap<Integer, LearnStatus> learnStatuses;
}
