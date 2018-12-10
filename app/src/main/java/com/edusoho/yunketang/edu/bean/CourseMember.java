package com.edusoho.yunketang.edu.bean;

import com.edusoho.yunketang.edu.bean.innerbean.Access;
import com.edusoho.yunketang.edu.bean.innerbean.Avatar;

import java.io.Serializable;

/**
 * Created by JesseHuang on 2017/4/13.
 */

public class CourseMember implements Serializable {
    public static final int NONE = 0;
    public static final int MEMBER = 1;
    public static final int EXPIRE = 3;

    public int id;
    public int courseId;
    public String deadline;
    public int levelId;
    public int learnedNum;
    public int noteNum;
    public String noteLastUpdateTime;
    public String isLearned;
    public String finishedTime;
    public String role;
    public String locked;
    public String createdTime;
    public String lastLearnTime;
    public String lastViewTime;
    public int courseSetId;
    public User user;
    public Access access;

    public static class User implements Serializable {
        public String id;
        public String nickname;
        public String title;
        public Avatar avatar;
    }
}
